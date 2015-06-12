/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Logica.Opcion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Logica.Pregunta;
import java.util.ArrayList;
import java.util.List;
import Logica.RespuestaXExamen;
import Persistencia.exceptions.IllegalOrphanException;
import Persistencia.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author cristian
 */
public class DaoOpcion implements Serializable {

    public DaoOpcion(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Opcion opcion) throws IllegalOrphanException {
        if (opcion.getPreguntaList() == null) {
            opcion.setPreguntaList(new ArrayList<Pregunta>());
        }
        if (opcion.getRespuestaXExamenList() == null) {
            opcion.setRespuestaXExamenList(new ArrayList<RespuestaXExamen>());
        }
        List<String> illegalOrphanMessages = null;
        Pregunta idPreguntaAsociadaOrphanCheck = opcion.getIdPreguntaAsociada();
        if (idPreguntaAsociadaOrphanCheck != null) {
            Opcion oldIdOpcionCorrectaOfIdPreguntaAsociada = idPreguntaAsociadaOrphanCheck.getIdOpcionCorrecta();
            if (oldIdOpcionCorrectaOfIdPreguntaAsociada != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pregunta " + idPreguntaAsociadaOrphanCheck + " already has an item of type Opcion whose idPreguntaAsociada column cannot be null. Please make another selection for the idPreguntaAsociada field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pregunta idPreguntaAsociada = opcion.getIdPreguntaAsociada();
            if (idPreguntaAsociada != null) {
                idPreguntaAsociada = em.getReference(idPreguntaAsociada.getClass(), idPreguntaAsociada.getIdPregunta());
                opcion.setIdPreguntaAsociada(idPreguntaAsociada);
            }
            List<Pregunta> attachedPreguntaList = new ArrayList<Pregunta>();
            for (Pregunta preguntaListPreguntaToAttach : opcion.getPreguntaList()) {
                preguntaListPreguntaToAttach = em.getReference(preguntaListPreguntaToAttach.getClass(), preguntaListPreguntaToAttach.getIdPregunta());
                attachedPreguntaList.add(preguntaListPreguntaToAttach);
            }
            opcion.setPreguntaList(attachedPreguntaList);
            List<RespuestaXExamen> attachedRespuestaXExamenList = new ArrayList<RespuestaXExamen>();
            for (RespuestaXExamen respuestaXExamenListRespuestaXExamenToAttach : opcion.getRespuestaXExamenList()) {
                respuestaXExamenListRespuestaXExamenToAttach = em.getReference(respuestaXExamenListRespuestaXExamenToAttach.getClass(), respuestaXExamenListRespuestaXExamenToAttach.getIdRespuestaXExamen());
                attachedRespuestaXExamenList.add(respuestaXExamenListRespuestaXExamenToAttach);
            }
            opcion.setRespuestaXExamenList(attachedRespuestaXExamenList);
            em.persist(opcion);
            if (idPreguntaAsociada != null) {
                idPreguntaAsociada.setIdOpcionCorrecta(opcion);
                idPreguntaAsociada = em.merge(idPreguntaAsociada);
            }
            for (Pregunta preguntaListPregunta : opcion.getPreguntaList()) {
                Opcion oldIdOpcionCorrectaOfPreguntaListPregunta = preguntaListPregunta.getIdOpcionCorrecta();
                preguntaListPregunta.setIdOpcionCorrecta(opcion);
                preguntaListPregunta = em.merge(preguntaListPregunta);
                if (oldIdOpcionCorrectaOfPreguntaListPregunta != null) {
                    oldIdOpcionCorrectaOfPreguntaListPregunta.getPreguntaList().remove(preguntaListPregunta);
                    oldIdOpcionCorrectaOfPreguntaListPregunta = em.merge(oldIdOpcionCorrectaOfPreguntaListPregunta);
                }
            }
            for (RespuestaXExamen respuestaXExamenListRespuestaXExamen : opcion.getRespuestaXExamenList()) {
                Opcion oldIdOpcionSeleccionadaOfRespuestaXExamenListRespuestaXExamen = respuestaXExamenListRespuestaXExamen.getIdOpcionSeleccionada();
                respuestaXExamenListRespuestaXExamen.setIdOpcionSeleccionada(opcion);
                respuestaXExamenListRespuestaXExamen = em.merge(respuestaXExamenListRespuestaXExamen);
                if (oldIdOpcionSeleccionadaOfRespuestaXExamenListRespuestaXExamen != null) {
                    oldIdOpcionSeleccionadaOfRespuestaXExamenListRespuestaXExamen.getRespuestaXExamenList().remove(respuestaXExamenListRespuestaXExamen);
                    oldIdOpcionSeleccionadaOfRespuestaXExamenListRespuestaXExamen = em.merge(oldIdOpcionSeleccionadaOfRespuestaXExamenListRespuestaXExamen);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Opcion opcion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Opcion persistentOpcion = em.find(Opcion.class, opcion.getIdOpcion());
            Pregunta idPreguntaAsociadaOld = persistentOpcion.getIdPreguntaAsociada();
            Pregunta idPreguntaAsociadaNew = opcion.getIdPreguntaAsociada();
            List<Pregunta> preguntaListOld = persistentOpcion.getPreguntaList();
            List<Pregunta> preguntaListNew = opcion.getPreguntaList();
            List<RespuestaXExamen> respuestaXExamenListOld = persistentOpcion.getRespuestaXExamenList();
            List<RespuestaXExamen> respuestaXExamenListNew = opcion.getRespuestaXExamenList();
            List<String> illegalOrphanMessages = null;
            if (idPreguntaAsociadaOld != null && !idPreguntaAsociadaOld.equals(idPreguntaAsociadaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Pregunta " + idPreguntaAsociadaOld + " since its idOpcionCorrecta field is not nullable.");
            }
            if (idPreguntaAsociadaNew != null && !idPreguntaAsociadaNew.equals(idPreguntaAsociadaOld)) {
                Opcion oldIdOpcionCorrectaOfIdPreguntaAsociada = idPreguntaAsociadaNew.getIdOpcionCorrecta();
                if (oldIdOpcionCorrectaOfIdPreguntaAsociada != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pregunta " + idPreguntaAsociadaNew + " already has an item of type Opcion whose idPreguntaAsociada column cannot be null. Please make another selection for the idPreguntaAsociada field.");
                }
            }
            for (Pregunta preguntaListOldPregunta : preguntaListOld) {
                if (!preguntaListNew.contains(preguntaListOldPregunta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pregunta " + preguntaListOldPregunta + " since its idOpcionCorrecta field is not nullable.");
                }
            }
            for (RespuestaXExamen respuestaXExamenListOldRespuestaXExamen : respuestaXExamenListOld) {
                if (!respuestaXExamenListNew.contains(respuestaXExamenListOldRespuestaXExamen)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RespuestaXExamen " + respuestaXExamenListOldRespuestaXExamen + " since its idOpcionSeleccionada field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPreguntaAsociadaNew != null) {
                idPreguntaAsociadaNew = em.getReference(idPreguntaAsociadaNew.getClass(), idPreguntaAsociadaNew.getIdPregunta());
                opcion.setIdPreguntaAsociada(idPreguntaAsociadaNew);
            }
            List<Pregunta> attachedPreguntaListNew = new ArrayList<Pregunta>();
            for (Pregunta preguntaListNewPreguntaToAttach : preguntaListNew) {
                preguntaListNewPreguntaToAttach = em.getReference(preguntaListNewPreguntaToAttach.getClass(), preguntaListNewPreguntaToAttach.getIdPregunta());
                attachedPreguntaListNew.add(preguntaListNewPreguntaToAttach);
            }
            preguntaListNew = attachedPreguntaListNew;
            opcion.setPreguntaList(preguntaListNew);
            List<RespuestaXExamen> attachedRespuestaXExamenListNew = new ArrayList<RespuestaXExamen>();
            for (RespuestaXExamen respuestaXExamenListNewRespuestaXExamenToAttach : respuestaXExamenListNew) {
                respuestaXExamenListNewRespuestaXExamenToAttach = em.getReference(respuestaXExamenListNewRespuestaXExamenToAttach.getClass(), respuestaXExamenListNewRespuestaXExamenToAttach.getIdRespuestaXExamen());
                attachedRespuestaXExamenListNew.add(respuestaXExamenListNewRespuestaXExamenToAttach);
            }
            respuestaXExamenListNew = attachedRespuestaXExamenListNew;
            opcion.setRespuestaXExamenList(respuestaXExamenListNew);
            opcion = em.merge(opcion);
            if (idPreguntaAsociadaNew != null && !idPreguntaAsociadaNew.equals(idPreguntaAsociadaOld)) {
                idPreguntaAsociadaNew.setIdOpcionCorrecta(opcion);
                idPreguntaAsociadaNew = em.merge(idPreguntaAsociadaNew);
            }
            for (Pregunta preguntaListNewPregunta : preguntaListNew) {
                if (!preguntaListOld.contains(preguntaListNewPregunta)) {
                    Opcion oldIdOpcionCorrectaOfPreguntaListNewPregunta = preguntaListNewPregunta.getIdOpcionCorrecta();
                    preguntaListNewPregunta.setIdOpcionCorrecta(opcion);
                    preguntaListNewPregunta = em.merge(preguntaListNewPregunta);
                    if (oldIdOpcionCorrectaOfPreguntaListNewPregunta != null && !oldIdOpcionCorrectaOfPreguntaListNewPregunta.equals(opcion)) {
                        oldIdOpcionCorrectaOfPreguntaListNewPregunta.getPreguntaList().remove(preguntaListNewPregunta);
                        oldIdOpcionCorrectaOfPreguntaListNewPregunta = em.merge(oldIdOpcionCorrectaOfPreguntaListNewPregunta);
                    }
                }
            }
            for (RespuestaXExamen respuestaXExamenListNewRespuestaXExamen : respuestaXExamenListNew) {
                if (!respuestaXExamenListOld.contains(respuestaXExamenListNewRespuestaXExamen)) {
                    Opcion oldIdOpcionSeleccionadaOfRespuestaXExamenListNewRespuestaXExamen = respuestaXExamenListNewRespuestaXExamen.getIdOpcionSeleccionada();
                    respuestaXExamenListNewRespuestaXExamen.setIdOpcionSeleccionada(opcion);
                    respuestaXExamenListNewRespuestaXExamen = em.merge(respuestaXExamenListNewRespuestaXExamen);
                    if (oldIdOpcionSeleccionadaOfRespuestaXExamenListNewRespuestaXExamen != null && !oldIdOpcionSeleccionadaOfRespuestaXExamenListNewRespuestaXExamen.equals(opcion)) {
                        oldIdOpcionSeleccionadaOfRespuestaXExamenListNewRespuestaXExamen.getRespuestaXExamenList().remove(respuestaXExamenListNewRespuestaXExamen);
                        oldIdOpcionSeleccionadaOfRespuestaXExamenListNewRespuestaXExamen = em.merge(oldIdOpcionSeleccionadaOfRespuestaXExamenListNewRespuestaXExamen);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = opcion.getIdOpcion();
                if (findOpcion(id) == null) {
                    throw new NonexistentEntityException("The opcion with id " + id + " no longer exists.");
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
            Opcion opcion;
            try {
                opcion = em.getReference(Opcion.class, id);
                opcion.getIdOpcion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The opcion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Pregunta idPreguntaAsociadaOrphanCheck = opcion.getIdPreguntaAsociada();
            if (idPreguntaAsociadaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Opcion (" + opcion + ") cannot be destroyed since the Pregunta " + idPreguntaAsociadaOrphanCheck + " in its idPreguntaAsociada field has a non-nullable idOpcionCorrecta field.");
            }
            List<Pregunta> preguntaListOrphanCheck = opcion.getPreguntaList();
            for (Pregunta preguntaListOrphanCheckPregunta : preguntaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Opcion (" + opcion + ") cannot be destroyed since the Pregunta " + preguntaListOrphanCheckPregunta + " in its preguntaList field has a non-nullable idOpcionCorrecta field.");
            }
            List<RespuestaXExamen> respuestaXExamenListOrphanCheck = opcion.getRespuestaXExamenList();
            for (RespuestaXExamen respuestaXExamenListOrphanCheckRespuestaXExamen : respuestaXExamenListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Opcion (" + opcion + ") cannot be destroyed since the RespuestaXExamen " + respuestaXExamenListOrphanCheckRespuestaXExamen + " in its respuestaXExamenList field has a non-nullable idOpcionSeleccionada field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(opcion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Opcion> findOpcionEntities() {
        return findOpcionEntities(true, -1, -1);
    }

    public List<Opcion> findOpcionEntities(int maxResults, int firstResult) {
        return findOpcionEntities(false, maxResults, firstResult);
    }

    private List<Opcion> findOpcionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Opcion.class));
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

    public Opcion findOpcion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Opcion.class, id);
        } finally {
            em.close();
        }
    }

    public int getOpcionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Opcion> rt = cq.from(Opcion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
