/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Logica.Opcion;
import Logica.RespuestaXExamen;
import java.util.ArrayList;
import java.util.List;
import Logica.ExamenXPregunta;
import Logica.Pregunta;
import Persistencia.exceptions.IllegalOrphanException;
import Persistencia.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author cristian
 */
public class DaoPregunta implements Serializable {

    public DaoPregunta(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pregunta pregunta) {
        if (pregunta.getRespuestaXExamenList() == null) {
            pregunta.setRespuestaXExamenList(new ArrayList<RespuestaXExamen>());
        }
        if (pregunta.getOpcionList() == null) {
            pregunta.setOpcionList(new ArrayList<Opcion>());
        }
        if (pregunta.getExamenXPreguntaList() == null) {
            pregunta.setExamenXPreguntaList(new ArrayList<ExamenXPregunta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Opcion idOpcionCorrecta = pregunta.getIdOpcionCorrecta();
            if (idOpcionCorrecta != null) {
                idOpcionCorrecta = em.getReference(idOpcionCorrecta.getClass(), idOpcionCorrecta.getIdOpcion());
                pregunta.setIdOpcionCorrecta(idOpcionCorrecta);
            }
            List<RespuestaXExamen> attachedRespuestaXExamenList = new ArrayList<RespuestaXExamen>();
            for (RespuestaXExamen respuestaXExamenListRespuestaXExamenToAttach : pregunta.getRespuestaXExamenList()) {
                respuestaXExamenListRespuestaXExamenToAttach = em.getReference(respuestaXExamenListRespuestaXExamenToAttach.getClass(), respuestaXExamenListRespuestaXExamenToAttach.getIdRespuestaXExamen());
                attachedRespuestaXExamenList.add(respuestaXExamenListRespuestaXExamenToAttach);
            }
            pregunta.setRespuestaXExamenList(attachedRespuestaXExamenList);
            List<Opcion> attachedOpcionList = new ArrayList<Opcion>();
            for (Opcion opcionListOpcionToAttach : pregunta.getOpcionList()) {
                opcionListOpcionToAttach = em.getReference(opcionListOpcionToAttach.getClass(), opcionListOpcionToAttach.getIdOpcion());
                attachedOpcionList.add(opcionListOpcionToAttach);
            }
            pregunta.setOpcionList(attachedOpcionList);
            List<ExamenXPregunta> attachedExamenXPreguntaList = new ArrayList<ExamenXPregunta>();
            for (ExamenXPregunta examenXPreguntaListExamenXPreguntaToAttach : pregunta.getExamenXPreguntaList()) {
                examenXPreguntaListExamenXPreguntaToAttach = em.getReference(examenXPreguntaListExamenXPreguntaToAttach.getClass(), examenXPreguntaListExamenXPreguntaToAttach.getIdExamenXPregunta());
                attachedExamenXPreguntaList.add(examenXPreguntaListExamenXPreguntaToAttach);
            }
            pregunta.setExamenXPreguntaList(attachedExamenXPreguntaList);
            em.persist(pregunta);
            if (idOpcionCorrecta != null) {
                idOpcionCorrecta.getPreguntaList().add(pregunta);
                idOpcionCorrecta = em.merge(idOpcionCorrecta);
            }
            for (RespuestaXExamen respuestaXExamenListRespuestaXExamen : pregunta.getRespuestaXExamenList()) {
                Pregunta oldIdPreguntaReferenciadaOfRespuestaXExamenListRespuestaXExamen = respuestaXExamenListRespuestaXExamen.getIdPreguntaReferenciada();
                respuestaXExamenListRespuestaXExamen.setIdPreguntaReferenciada(pregunta);
                respuestaXExamenListRespuestaXExamen = em.merge(respuestaXExamenListRespuestaXExamen);
                if (oldIdPreguntaReferenciadaOfRespuestaXExamenListRespuestaXExamen != null) {
                    oldIdPreguntaReferenciadaOfRespuestaXExamenListRespuestaXExamen.getRespuestaXExamenList().remove(respuestaXExamenListRespuestaXExamen);
                    oldIdPreguntaReferenciadaOfRespuestaXExamenListRespuestaXExamen = em.merge(oldIdPreguntaReferenciadaOfRespuestaXExamenListRespuestaXExamen);
                }
            }
            for (Opcion opcionListOpcion : pregunta.getOpcionList()) {
                Pregunta oldIdPreguntaAsociadaOfOpcionListOpcion = opcionListOpcion.getIdPreguntaAsociada();
                opcionListOpcion.setIdPreguntaAsociada(pregunta);
                opcionListOpcion = em.merge(opcionListOpcion);
                if (oldIdPreguntaAsociadaOfOpcionListOpcion != null) {
                    oldIdPreguntaAsociadaOfOpcionListOpcion.getOpcionList().remove(opcionListOpcion);
                    oldIdPreguntaAsociadaOfOpcionListOpcion = em.merge(oldIdPreguntaAsociadaOfOpcionListOpcion);
                }
            }
            for (ExamenXPregunta examenXPreguntaListExamenXPregunta : pregunta.getExamenXPreguntaList()) {
                Pregunta oldIdPreguntaReferenciadaOfExamenXPreguntaListExamenXPregunta = examenXPreguntaListExamenXPregunta.getIdPreguntaReferenciada();
                examenXPreguntaListExamenXPregunta.setIdPreguntaReferenciada(pregunta);
                examenXPreguntaListExamenXPregunta = em.merge(examenXPreguntaListExamenXPregunta);
                if (oldIdPreguntaReferenciadaOfExamenXPreguntaListExamenXPregunta != null) {
                    oldIdPreguntaReferenciadaOfExamenXPreguntaListExamenXPregunta.getExamenXPreguntaList().remove(examenXPreguntaListExamenXPregunta);
                    oldIdPreguntaReferenciadaOfExamenXPreguntaListExamenXPregunta = em.merge(oldIdPreguntaReferenciadaOfExamenXPreguntaListExamenXPregunta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pregunta pregunta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pregunta persistentPregunta = em.find(Pregunta.class, pregunta.getIdPregunta());
            Opcion idOpcionCorrectaOld = persistentPregunta.getIdOpcionCorrecta();
            Opcion idOpcionCorrectaNew = pregunta.getIdOpcionCorrecta();
            List<RespuestaXExamen> respuestaXExamenListOld = persistentPregunta.getRespuestaXExamenList();
            List<RespuestaXExamen> respuestaXExamenListNew = pregunta.getRespuestaXExamenList();
            List<Opcion> opcionListOld = persistentPregunta.getOpcionList();
            List<Opcion> opcionListNew = pregunta.getOpcionList();
            List<ExamenXPregunta> examenXPreguntaListOld = persistentPregunta.getExamenXPreguntaList();
            List<ExamenXPregunta> examenXPreguntaListNew = pregunta.getExamenXPreguntaList();
            List<String> illegalOrphanMessages = null;
            for (RespuestaXExamen respuestaXExamenListOldRespuestaXExamen : respuestaXExamenListOld) {
                if (!respuestaXExamenListNew.contains(respuestaXExamenListOldRespuestaXExamen)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RespuestaXExamen " + respuestaXExamenListOldRespuestaXExamen + " since its idPreguntaReferenciada field is not nullable.");
                }
            }
            for (Opcion opcionListOldOpcion : opcionListOld) {
                if (!opcionListNew.contains(opcionListOldOpcion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Opcion " + opcionListOldOpcion + " since its idPreguntaAsociada field is not nullable.");
                }
            }
            for (ExamenXPregunta examenXPreguntaListOldExamenXPregunta : examenXPreguntaListOld) {
                if (!examenXPreguntaListNew.contains(examenXPreguntaListOldExamenXPregunta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExamenXPregunta " + examenXPreguntaListOldExamenXPregunta + " since its idPreguntaReferenciada field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idOpcionCorrectaNew != null) {
                idOpcionCorrectaNew = em.getReference(idOpcionCorrectaNew.getClass(), idOpcionCorrectaNew.getIdOpcion());
                pregunta.setIdOpcionCorrecta(idOpcionCorrectaNew);
            }
            List<RespuestaXExamen> attachedRespuestaXExamenListNew = new ArrayList<RespuestaXExamen>();
            for (RespuestaXExamen respuestaXExamenListNewRespuestaXExamenToAttach : respuestaXExamenListNew) {
                respuestaXExamenListNewRespuestaXExamenToAttach = em.getReference(respuestaXExamenListNewRespuestaXExamenToAttach.getClass(), respuestaXExamenListNewRespuestaXExamenToAttach.getIdRespuestaXExamen());
                attachedRespuestaXExamenListNew.add(respuestaXExamenListNewRespuestaXExamenToAttach);
            }
            respuestaXExamenListNew = attachedRespuestaXExamenListNew;
            pregunta.setRespuestaXExamenList(respuestaXExamenListNew);
            List<Opcion> attachedOpcionListNew = new ArrayList<Opcion>();
            for (Opcion opcionListNewOpcionToAttach : opcionListNew) {
                opcionListNewOpcionToAttach = em.getReference(opcionListNewOpcionToAttach.getClass(), opcionListNewOpcionToAttach.getIdOpcion());
                attachedOpcionListNew.add(opcionListNewOpcionToAttach);
            }
            opcionListNew = attachedOpcionListNew;
            pregunta.setOpcionList(opcionListNew);
            List<ExamenXPregunta> attachedExamenXPreguntaListNew = new ArrayList<ExamenXPregunta>();
            for (ExamenXPregunta examenXPreguntaListNewExamenXPreguntaToAttach : examenXPreguntaListNew) {
                examenXPreguntaListNewExamenXPreguntaToAttach = em.getReference(examenXPreguntaListNewExamenXPreguntaToAttach.getClass(), examenXPreguntaListNewExamenXPreguntaToAttach.getIdExamenXPregunta());
                attachedExamenXPreguntaListNew.add(examenXPreguntaListNewExamenXPreguntaToAttach);
            }
            examenXPreguntaListNew = attachedExamenXPreguntaListNew;
            pregunta.setExamenXPreguntaList(examenXPreguntaListNew);
            pregunta = em.merge(pregunta);
            if (idOpcionCorrectaOld != null && !idOpcionCorrectaOld.equals(idOpcionCorrectaNew)) {
                idOpcionCorrectaOld.getPreguntaList().remove(pregunta);
                idOpcionCorrectaOld = em.merge(idOpcionCorrectaOld);
            }
            if (idOpcionCorrectaNew != null && !idOpcionCorrectaNew.equals(idOpcionCorrectaOld)) {
                idOpcionCorrectaNew.getPreguntaList().add(pregunta);
                idOpcionCorrectaNew = em.merge(idOpcionCorrectaNew);
            }
            for (RespuestaXExamen respuestaXExamenListNewRespuestaXExamen : respuestaXExamenListNew) {
                if (!respuestaXExamenListOld.contains(respuestaXExamenListNewRespuestaXExamen)) {
                    Pregunta oldIdPreguntaReferenciadaOfRespuestaXExamenListNewRespuestaXExamen = respuestaXExamenListNewRespuestaXExamen.getIdPreguntaReferenciada();
                    respuestaXExamenListNewRespuestaXExamen.setIdPreguntaReferenciada(pregunta);
                    respuestaXExamenListNewRespuestaXExamen = em.merge(respuestaXExamenListNewRespuestaXExamen);
                    if (oldIdPreguntaReferenciadaOfRespuestaXExamenListNewRespuestaXExamen != null && !oldIdPreguntaReferenciadaOfRespuestaXExamenListNewRespuestaXExamen.equals(pregunta)) {
                        oldIdPreguntaReferenciadaOfRespuestaXExamenListNewRespuestaXExamen.getRespuestaXExamenList().remove(respuestaXExamenListNewRespuestaXExamen);
                        oldIdPreguntaReferenciadaOfRespuestaXExamenListNewRespuestaXExamen = em.merge(oldIdPreguntaReferenciadaOfRespuestaXExamenListNewRespuestaXExamen);
                    }
                }
            }
            for (Opcion opcionListNewOpcion : opcionListNew) {
                if (!opcionListOld.contains(opcionListNewOpcion)) {
                    Pregunta oldIdPreguntaAsociadaOfOpcionListNewOpcion = opcionListNewOpcion.getIdPreguntaAsociada();
                    opcionListNewOpcion.setIdPreguntaAsociada(pregunta);
                    opcionListNewOpcion = em.merge(opcionListNewOpcion);
                    if (oldIdPreguntaAsociadaOfOpcionListNewOpcion != null && !oldIdPreguntaAsociadaOfOpcionListNewOpcion.equals(pregunta)) {
                        oldIdPreguntaAsociadaOfOpcionListNewOpcion.getOpcionList().remove(opcionListNewOpcion);
                        oldIdPreguntaAsociadaOfOpcionListNewOpcion = em.merge(oldIdPreguntaAsociadaOfOpcionListNewOpcion);
                    }
                }
            }
            for (ExamenXPregunta examenXPreguntaListNewExamenXPregunta : examenXPreguntaListNew) {
                if (!examenXPreguntaListOld.contains(examenXPreguntaListNewExamenXPregunta)) {
                    Pregunta oldIdPreguntaReferenciadaOfExamenXPreguntaListNewExamenXPregunta = examenXPreguntaListNewExamenXPregunta.getIdPreguntaReferenciada();
                    examenXPreguntaListNewExamenXPregunta.setIdPreguntaReferenciada(pregunta);
                    examenXPreguntaListNewExamenXPregunta = em.merge(examenXPreguntaListNewExamenXPregunta);
                    if (oldIdPreguntaReferenciadaOfExamenXPreguntaListNewExamenXPregunta != null && !oldIdPreguntaReferenciadaOfExamenXPreguntaListNewExamenXPregunta.equals(pregunta)) {
                        oldIdPreguntaReferenciadaOfExamenXPreguntaListNewExamenXPregunta.getExamenXPreguntaList().remove(examenXPreguntaListNewExamenXPregunta);
                        oldIdPreguntaReferenciadaOfExamenXPreguntaListNewExamenXPregunta = em.merge(oldIdPreguntaReferenciadaOfExamenXPreguntaListNewExamenXPregunta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pregunta.getIdPregunta();
                if (findPregunta(id) == null) {
                    throw new NonexistentEntityException("The pregunta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pregunta pregunta;
            try {
                pregunta = em.getReference(Pregunta.class, id);
                pregunta.getIdPregunta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pregunta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RespuestaXExamen> respuestaXExamenListOrphanCheck = pregunta.getRespuestaXExamenList();
            for (RespuestaXExamen respuestaXExamenListOrphanCheckRespuestaXExamen : respuestaXExamenListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pregunta (" + pregunta + ") cannot be destroyed since the RespuestaXExamen " + respuestaXExamenListOrphanCheckRespuestaXExamen + " in its respuestaXExamenList field has a non-nullable idPreguntaReferenciada field.");
            }
            List<Opcion> opcionListOrphanCheck = pregunta.getOpcionList();
            for (Opcion opcionListOrphanCheckOpcion : opcionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pregunta (" + pregunta + ") cannot be destroyed since the Opcion " + opcionListOrphanCheckOpcion + " in its opcionList field has a non-nullable idPreguntaAsociada field.");
            }
            List<ExamenXPregunta> examenXPreguntaListOrphanCheck = pregunta.getExamenXPreguntaList();
            for (ExamenXPregunta examenXPreguntaListOrphanCheckExamenXPregunta : examenXPreguntaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pregunta (" + pregunta + ") cannot be destroyed since the ExamenXPregunta " + examenXPreguntaListOrphanCheckExamenXPregunta + " in its examenXPreguntaList field has a non-nullable idPreguntaReferenciada field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Opcion idOpcionCorrecta = pregunta.getIdOpcionCorrecta();
            if (idOpcionCorrecta != null) {
                idOpcionCorrecta.getPreguntaList().remove(pregunta);
                idOpcionCorrecta = em.merge(idOpcionCorrecta);
            }
            em.remove(pregunta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pregunta> findPreguntaEntities() {
        return findPreguntaEntities(true, -1, -1);
    }

    public List<Pregunta> findPreguntaEntities(int maxResults, int firstResult) {
        return findPreguntaEntities(false, maxResults, firstResult);
    }

    private List<Pregunta> findPreguntaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pregunta.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Pregunta findPregunta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pregunta.class, id);
        } finally {
            em.close();
        }
    }

    public int getPreguntaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pregunta> rt = cq.from(Pregunta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
