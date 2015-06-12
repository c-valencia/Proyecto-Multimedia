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
import Logica.Pregunta;
import Logica.Opcion;
import Logica.Examen;
import Logica.RespuestaXExamen;
import Persistencia.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author cristian
 */
public class DaoRespuestaXExamen implements Serializable {

    public DaoRespuestaXExamen(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RespuestaXExamen respuestaXExamen) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pregunta idPreguntaReferenciada = respuestaXExamen.getIdPreguntaReferenciada();
            if (idPreguntaReferenciada != null) {
                idPreguntaReferenciada = em.getReference(idPreguntaReferenciada.getClass(), idPreguntaReferenciada.getIdPregunta());
                respuestaXExamen.setIdPreguntaReferenciada(idPreguntaReferenciada);
            }
            Opcion idOpcionSeleccionada = respuestaXExamen.getIdOpcionSeleccionada();
            if (idOpcionSeleccionada != null) {
                idOpcionSeleccionada = em.getReference(idOpcionSeleccionada.getClass(), idOpcionSeleccionada.getIdOpcion());
                respuestaXExamen.setIdOpcionSeleccionada(idOpcionSeleccionada);
            }
            Examen idExamenReferenciado = respuestaXExamen.getIdExamenReferenciado();
            if (idExamenReferenciado != null) {
                idExamenReferenciado = em.getReference(idExamenReferenciado.getClass(), idExamenReferenciado.getIdExamen());
                respuestaXExamen.setIdExamenReferenciado(idExamenReferenciado);
            }
            em.persist(respuestaXExamen);
            if (idPreguntaReferenciada != null) {
                idPreguntaReferenciada.getRespuestaXExamenList().add(respuestaXExamen);
                idPreguntaReferenciada = em.merge(idPreguntaReferenciada);
            }
            if (idOpcionSeleccionada != null) {
                idOpcionSeleccionada.getRespuestaXExamenList().add(respuestaXExamen);
                idOpcionSeleccionada = em.merge(idOpcionSeleccionada);
            }
            if (idExamenReferenciado != null) {
                idExamenReferenciado.getRespuestaXExamenList().add(respuestaXExamen);
                idExamenReferenciado = em.merge(idExamenReferenciado);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RespuestaXExamen respuestaXExamen) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RespuestaXExamen persistentRespuestaXExamen = em.find(RespuestaXExamen.class, respuestaXExamen.getIdRespuestaXExamen());
            Pregunta idPreguntaReferenciadaOld = persistentRespuestaXExamen.getIdPreguntaReferenciada();
            Pregunta idPreguntaReferenciadaNew = respuestaXExamen.getIdPreguntaReferenciada();
            Opcion idOpcionSeleccionadaOld = persistentRespuestaXExamen.getIdOpcionSeleccionada();
            Opcion idOpcionSeleccionadaNew = respuestaXExamen.getIdOpcionSeleccionada();
            Examen idExamenReferenciadoOld = persistentRespuestaXExamen.getIdExamenReferenciado();
            Examen idExamenReferenciadoNew = respuestaXExamen.getIdExamenReferenciado();
            if (idPreguntaReferenciadaNew != null) {
                idPreguntaReferenciadaNew = em.getReference(idPreguntaReferenciadaNew.getClass(), idPreguntaReferenciadaNew.getIdPregunta());
                respuestaXExamen.setIdPreguntaReferenciada(idPreguntaReferenciadaNew);
            }
            if (idOpcionSeleccionadaNew != null) {
                idOpcionSeleccionadaNew = em.getReference(idOpcionSeleccionadaNew.getClass(), idOpcionSeleccionadaNew.getIdOpcion());
                respuestaXExamen.setIdOpcionSeleccionada(idOpcionSeleccionadaNew);
            }
            if (idExamenReferenciadoNew != null) {
                idExamenReferenciadoNew = em.getReference(idExamenReferenciadoNew.getClass(), idExamenReferenciadoNew.getIdExamen());
                respuestaXExamen.setIdExamenReferenciado(idExamenReferenciadoNew);
            }
            respuestaXExamen = em.merge(respuestaXExamen);
            if (idPreguntaReferenciadaOld != null && !idPreguntaReferenciadaOld.equals(idPreguntaReferenciadaNew)) {
                idPreguntaReferenciadaOld.getRespuestaXExamenList().remove(respuestaXExamen);
                idPreguntaReferenciadaOld = em.merge(idPreguntaReferenciadaOld);
            }
            if (idPreguntaReferenciadaNew != null && !idPreguntaReferenciadaNew.equals(idPreguntaReferenciadaOld)) {
                idPreguntaReferenciadaNew.getRespuestaXExamenList().add(respuestaXExamen);
                idPreguntaReferenciadaNew = em.merge(idPreguntaReferenciadaNew);
            }
            if (idOpcionSeleccionadaOld != null && !idOpcionSeleccionadaOld.equals(idOpcionSeleccionadaNew)) {
                idOpcionSeleccionadaOld.getRespuestaXExamenList().remove(respuestaXExamen);
                idOpcionSeleccionadaOld = em.merge(idOpcionSeleccionadaOld);
            }
            if (idOpcionSeleccionadaNew != null && !idOpcionSeleccionadaNew.equals(idOpcionSeleccionadaOld)) {
                idOpcionSeleccionadaNew.getRespuestaXExamenList().add(respuestaXExamen);
                idOpcionSeleccionadaNew = em.merge(idOpcionSeleccionadaNew);
            }
            if (idExamenReferenciadoOld != null && !idExamenReferenciadoOld.equals(idExamenReferenciadoNew)) {
                idExamenReferenciadoOld.getRespuestaXExamenList().remove(respuestaXExamen);
                idExamenReferenciadoOld = em.merge(idExamenReferenciadoOld);
            }
            if (idExamenReferenciadoNew != null && !idExamenReferenciadoNew.equals(idExamenReferenciadoOld)) {
                idExamenReferenciadoNew.getRespuestaXExamenList().add(respuestaXExamen);
                idExamenReferenciadoNew = em.merge(idExamenReferenciadoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = respuestaXExamen.getIdRespuestaXExamen();
                if (findRespuestaXExamen(id) == null) {
                    throw new NonexistentEntityException("The respuestaXExamen with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RespuestaXExamen respuestaXExamen;
            try {
                respuestaXExamen = em.getReference(RespuestaXExamen.class, id);
                respuestaXExamen.getIdRespuestaXExamen();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The respuestaXExamen with id " + id + " no longer exists.", enfe);
            }
            Pregunta idPreguntaReferenciada = respuestaXExamen.getIdPreguntaReferenciada();
            if (idPreguntaReferenciada != null) {
                idPreguntaReferenciada.getRespuestaXExamenList().remove(respuestaXExamen);
                idPreguntaReferenciada = em.merge(idPreguntaReferenciada);
            }
            Opcion idOpcionSeleccionada = respuestaXExamen.getIdOpcionSeleccionada();
            if (idOpcionSeleccionada != null) {
                idOpcionSeleccionada.getRespuestaXExamenList().remove(respuestaXExamen);
                idOpcionSeleccionada = em.merge(idOpcionSeleccionada);
            }
            Examen idExamenReferenciado = respuestaXExamen.getIdExamenReferenciado();
            if (idExamenReferenciado != null) {
                idExamenReferenciado.getRespuestaXExamenList().remove(respuestaXExamen);
                idExamenReferenciado = em.merge(idExamenReferenciado);
            }
            em.remove(respuestaXExamen);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RespuestaXExamen> findRespuestaXExamenEntities() {
        return findRespuestaXExamenEntities(true, -1, -1);
    }

    public List<RespuestaXExamen> findRespuestaXExamenEntities(int maxResults, int firstResult) {
        return findRespuestaXExamenEntities(false, maxResults, firstResult);
    }

    private List<RespuestaXExamen> findRespuestaXExamenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RespuestaXExamen.class));
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

    public RespuestaXExamen findRespuestaXExamen(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RespuestaXExamen.class, id);
        } finally {
            em.close();
        }
    }

    public int getRespuestaXExamenCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RespuestaXExamen> rt = cq.from(RespuestaXExamen.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
