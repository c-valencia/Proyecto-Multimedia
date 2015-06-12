/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Logica.Examen;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Logica.Persona;
import Logica.RespuestaXExamen;
import java.util.ArrayList;
import java.util.List;
import Logica.ExamenXPregunta;
import Persistencia.exceptions.IllegalOrphanException;
import Persistencia.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author cristian
 */
public class DaoExamen implements Serializable {

    public DaoExamen(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Examen examen) {
        if (examen.getRespuestaXExamenList() == null) {
            examen.setRespuestaXExamenList(new ArrayList<RespuestaXExamen>());
        }
        if (examen.getExamenXPreguntaList() == null) {
            examen.setExamenXPreguntaList(new ArrayList<ExamenXPregunta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona idEstudiante = examen.getIdEstudiante();
            if (idEstudiante != null) {
                idEstudiante = em.getReference(idEstudiante.getClass(), idEstudiante.getCedula());
                examen.setIdEstudiante(idEstudiante);
            }
            List<RespuestaXExamen> attachedRespuestaXExamenList = new ArrayList<RespuestaXExamen>();
            for (RespuestaXExamen respuestaXExamenListRespuestaXExamenToAttach : examen.getRespuestaXExamenList()) {
                respuestaXExamenListRespuestaXExamenToAttach = em.getReference(respuestaXExamenListRespuestaXExamenToAttach.getClass(), respuestaXExamenListRespuestaXExamenToAttach.getIdRespuestaXExamen());
                attachedRespuestaXExamenList.add(respuestaXExamenListRespuestaXExamenToAttach);
            }
            examen.setRespuestaXExamenList(attachedRespuestaXExamenList);
            List<ExamenXPregunta> attachedExamenXPreguntaList = new ArrayList<ExamenXPregunta>();
            for (ExamenXPregunta examenXPreguntaListExamenXPreguntaToAttach : examen.getExamenXPreguntaList()) {
                examenXPreguntaListExamenXPreguntaToAttach = em.getReference(examenXPreguntaListExamenXPreguntaToAttach.getClass(), examenXPreguntaListExamenXPreguntaToAttach.getIdExamenXPregunta());
                attachedExamenXPreguntaList.add(examenXPreguntaListExamenXPreguntaToAttach);
            }
            examen.setExamenXPreguntaList(attachedExamenXPreguntaList);
            em.persist(examen);
            if (idEstudiante != null) {
                idEstudiante.getExamenList().add(examen);
                idEstudiante = em.merge(idEstudiante);
            }
            for (RespuestaXExamen respuestaXExamenListRespuestaXExamen : examen.getRespuestaXExamenList()) {
                Examen oldIdExamenReferenciadoOfRespuestaXExamenListRespuestaXExamen = respuestaXExamenListRespuestaXExamen.getIdExamenReferenciado();
                respuestaXExamenListRespuestaXExamen.setIdExamenReferenciado(examen);
                respuestaXExamenListRespuestaXExamen = em.merge(respuestaXExamenListRespuestaXExamen);
                if (oldIdExamenReferenciadoOfRespuestaXExamenListRespuestaXExamen != null) {
                    oldIdExamenReferenciadoOfRespuestaXExamenListRespuestaXExamen.getRespuestaXExamenList().remove(respuestaXExamenListRespuestaXExamen);
                    oldIdExamenReferenciadoOfRespuestaXExamenListRespuestaXExamen = em.merge(oldIdExamenReferenciadoOfRespuestaXExamenListRespuestaXExamen);
                }
            }
            for (ExamenXPregunta examenXPreguntaListExamenXPregunta : examen.getExamenXPreguntaList()) {
                Examen oldIdExamenReferenciadoOfExamenXPreguntaListExamenXPregunta = examenXPreguntaListExamenXPregunta.getIdExamenReferenciado();
                examenXPreguntaListExamenXPregunta.setIdExamenReferenciado(examen);
                examenXPreguntaListExamenXPregunta = em.merge(examenXPreguntaListExamenXPregunta);
                if (oldIdExamenReferenciadoOfExamenXPreguntaListExamenXPregunta != null) {
                    oldIdExamenReferenciadoOfExamenXPreguntaListExamenXPregunta.getExamenXPreguntaList().remove(examenXPreguntaListExamenXPregunta);
                    oldIdExamenReferenciadoOfExamenXPreguntaListExamenXPregunta = em.merge(oldIdExamenReferenciadoOfExamenXPreguntaListExamenXPregunta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Examen examen) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Examen persistentExamen = em.find(Examen.class, examen.getIdExamen());
            Persona idEstudianteOld = persistentExamen.getIdEstudiante();
            Persona idEstudianteNew = examen.getIdEstudiante();
            List<RespuestaXExamen> respuestaXExamenListOld = persistentExamen.getRespuestaXExamenList();
            List<RespuestaXExamen> respuestaXExamenListNew = examen.getRespuestaXExamenList();
            List<ExamenXPregunta> examenXPreguntaListOld = persistentExamen.getExamenXPreguntaList();
            List<ExamenXPregunta> examenXPreguntaListNew = examen.getExamenXPreguntaList();
            List<String> illegalOrphanMessages = null;
            for (RespuestaXExamen respuestaXExamenListOldRespuestaXExamen : respuestaXExamenListOld) {
                if (!respuestaXExamenListNew.contains(respuestaXExamenListOldRespuestaXExamen)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RespuestaXExamen " + respuestaXExamenListOldRespuestaXExamen + " since its idExamenReferenciado field is not nullable.");
                }
            }
            for (ExamenXPregunta examenXPreguntaListOldExamenXPregunta : examenXPreguntaListOld) {
                if (!examenXPreguntaListNew.contains(examenXPreguntaListOldExamenXPregunta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ExamenXPregunta " + examenXPreguntaListOldExamenXPregunta + " since its idExamenReferenciado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idEstudianteNew != null) {
                idEstudianteNew = em.getReference(idEstudianteNew.getClass(), idEstudianteNew.getCedula());
                examen.setIdEstudiante(idEstudianteNew);
            }
            List<RespuestaXExamen> attachedRespuestaXExamenListNew = new ArrayList<RespuestaXExamen>();
            for (RespuestaXExamen respuestaXExamenListNewRespuestaXExamenToAttach : respuestaXExamenListNew) {
                respuestaXExamenListNewRespuestaXExamenToAttach = em.getReference(respuestaXExamenListNewRespuestaXExamenToAttach.getClass(), respuestaXExamenListNewRespuestaXExamenToAttach.getIdRespuestaXExamen());
                attachedRespuestaXExamenListNew.add(respuestaXExamenListNewRespuestaXExamenToAttach);
            }
            respuestaXExamenListNew = attachedRespuestaXExamenListNew;
            examen.setRespuestaXExamenList(respuestaXExamenListNew);
            List<ExamenXPregunta> attachedExamenXPreguntaListNew = new ArrayList<ExamenXPregunta>();
            for (ExamenXPregunta examenXPreguntaListNewExamenXPreguntaToAttach : examenXPreguntaListNew) {
                examenXPreguntaListNewExamenXPreguntaToAttach = em.getReference(examenXPreguntaListNewExamenXPreguntaToAttach.getClass(), examenXPreguntaListNewExamenXPreguntaToAttach.getIdExamenXPregunta());
                attachedExamenXPreguntaListNew.add(examenXPreguntaListNewExamenXPreguntaToAttach);
            }
            examenXPreguntaListNew = attachedExamenXPreguntaListNew;
            examen.setExamenXPreguntaList(examenXPreguntaListNew);
            examen = em.merge(examen);
            if (idEstudianteOld != null && !idEstudianteOld.equals(idEstudianteNew)) {
                idEstudianteOld.getExamenList().remove(examen);
                idEstudianteOld = em.merge(idEstudianteOld);
            }
            if (idEstudianteNew != null && !idEstudianteNew.equals(idEstudianteOld)) {
                idEstudianteNew.getExamenList().add(examen);
                idEstudianteNew = em.merge(idEstudianteNew);
            }
            for (RespuestaXExamen respuestaXExamenListNewRespuestaXExamen : respuestaXExamenListNew) {
                if (!respuestaXExamenListOld.contains(respuestaXExamenListNewRespuestaXExamen)) {
                    Examen oldIdExamenReferenciadoOfRespuestaXExamenListNewRespuestaXExamen = respuestaXExamenListNewRespuestaXExamen.getIdExamenReferenciado();
                    respuestaXExamenListNewRespuestaXExamen.setIdExamenReferenciado(examen);
                    respuestaXExamenListNewRespuestaXExamen = em.merge(respuestaXExamenListNewRespuestaXExamen);
                    if (oldIdExamenReferenciadoOfRespuestaXExamenListNewRespuestaXExamen != null && !oldIdExamenReferenciadoOfRespuestaXExamenListNewRespuestaXExamen.equals(examen)) {
                        oldIdExamenReferenciadoOfRespuestaXExamenListNewRespuestaXExamen.getRespuestaXExamenList().remove(respuestaXExamenListNewRespuestaXExamen);
                        oldIdExamenReferenciadoOfRespuestaXExamenListNewRespuestaXExamen = em.merge(oldIdExamenReferenciadoOfRespuestaXExamenListNewRespuestaXExamen);
                    }
                }
            }
            for (ExamenXPregunta examenXPreguntaListNewExamenXPregunta : examenXPreguntaListNew) {
                if (!examenXPreguntaListOld.contains(examenXPreguntaListNewExamenXPregunta)) {
                    Examen oldIdExamenReferenciadoOfExamenXPreguntaListNewExamenXPregunta = examenXPreguntaListNewExamenXPregunta.getIdExamenReferenciado();
                    examenXPreguntaListNewExamenXPregunta.setIdExamenReferenciado(examen);
                    examenXPreguntaListNewExamenXPregunta = em.merge(examenXPreguntaListNewExamenXPregunta);
                    if (oldIdExamenReferenciadoOfExamenXPreguntaListNewExamenXPregunta != null && !oldIdExamenReferenciadoOfExamenXPreguntaListNewExamenXPregunta.equals(examen)) {
                        oldIdExamenReferenciadoOfExamenXPreguntaListNewExamenXPregunta.getExamenXPreguntaList().remove(examenXPreguntaListNewExamenXPregunta);
                        oldIdExamenReferenciadoOfExamenXPreguntaListNewExamenXPregunta = em.merge(oldIdExamenReferenciadoOfExamenXPreguntaListNewExamenXPregunta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = examen.getIdExamen();
                if (findExamen(id) == null) {
                    throw new NonexistentEntityException("The examen with id " + id + " no longer exists.");
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
            Examen examen;
            try {
                examen = em.getReference(Examen.class, id);
                examen.getIdExamen();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The examen with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RespuestaXExamen> respuestaXExamenListOrphanCheck = examen.getRespuestaXExamenList();
            for (RespuestaXExamen respuestaXExamenListOrphanCheckRespuestaXExamen : respuestaXExamenListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Examen (" + examen + ") cannot be destroyed since the RespuestaXExamen " + respuestaXExamenListOrphanCheckRespuestaXExamen + " in its respuestaXExamenList field has a non-nullable idExamenReferenciado field.");
            }
            List<ExamenXPregunta> examenXPreguntaListOrphanCheck = examen.getExamenXPreguntaList();
            for (ExamenXPregunta examenXPreguntaListOrphanCheckExamenXPregunta : examenXPreguntaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Examen (" + examen + ") cannot be destroyed since the ExamenXPregunta " + examenXPreguntaListOrphanCheckExamenXPregunta + " in its examenXPreguntaList field has a non-nullable idExamenReferenciado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Persona idEstudiante = examen.getIdEstudiante();
            if (idEstudiante != null) {
                idEstudiante.getExamenList().remove(examen);
                idEstudiante = em.merge(idEstudiante);
            }
            em.remove(examen);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Examen> findExamenEntities() {
        return findExamenEntities(true, -1, -1);
    }

    public List<Examen> findExamenEntities(int maxResults, int firstResult) {
        return findExamenEntities(false, maxResults, firstResult);
    }

    private List<Examen> findExamenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Examen.class));
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

    public Examen findExamen(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Examen.class, id);
        } finally {
            em.close();
        }
    }

    public int getExamenCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Examen> rt = cq.from(Examen.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
