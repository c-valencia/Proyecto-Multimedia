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
import Logica.Examen;
import Logica.ExamenXPregunta;
import Persistencia.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author cristian
 */
public class DaoExamenXPregunta implements Serializable {

    public DaoExamenXPregunta(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ExamenXPregunta examenXPregunta) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pregunta idPreguntaReferenciada = examenXPregunta.getIdPreguntaReferenciada();
            if (idPreguntaReferenciada != null) {
                idPreguntaReferenciada = em.getReference(idPreguntaReferenciada.getClass(), idPreguntaReferenciada.getIdPregunta());
                examenXPregunta.setIdPreguntaReferenciada(idPreguntaReferenciada);
            }
            Examen idExamenReferenciado = examenXPregunta.getIdExamenReferenciado();
            if (idExamenReferenciado != null) {
                idExamenReferenciado = em.getReference(idExamenReferenciado.getClass(), idExamenReferenciado.getIdExamen());
                examenXPregunta.setIdExamenReferenciado(idExamenReferenciado);
            }
            em.persist(examenXPregunta);
            if (idPreguntaReferenciada != null) {
                idPreguntaReferenciada.getExamenXPreguntaList().add(examenXPregunta);
                idPreguntaReferenciada = em.merge(idPreguntaReferenciada);
            }
            if (idExamenReferenciado != null) {
                idExamenReferenciado.getExamenXPreguntaList().add(examenXPregunta);
                idExamenReferenciado = em.merge(idExamenReferenciado);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ExamenXPregunta examenXPregunta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ExamenXPregunta persistentExamenXPregunta = em.find(ExamenXPregunta.class, examenXPregunta.getIdExamenXPregunta());
            Pregunta idPreguntaReferenciadaOld = persistentExamenXPregunta.getIdPreguntaReferenciada();
            Pregunta idPreguntaReferenciadaNew = examenXPregunta.getIdPreguntaReferenciada();
            Examen idExamenReferenciadoOld = persistentExamenXPregunta.getIdExamenReferenciado();
            Examen idExamenReferenciadoNew = examenXPregunta.getIdExamenReferenciado();
            if (idPreguntaReferenciadaNew != null) {
                idPreguntaReferenciadaNew = em.getReference(idPreguntaReferenciadaNew.getClass(), idPreguntaReferenciadaNew.getIdPregunta());
                examenXPregunta.setIdPreguntaReferenciada(idPreguntaReferenciadaNew);
            }
            if (idExamenReferenciadoNew != null) {
                idExamenReferenciadoNew = em.getReference(idExamenReferenciadoNew.getClass(), idExamenReferenciadoNew.getIdExamen());
                examenXPregunta.setIdExamenReferenciado(idExamenReferenciadoNew);
            }
            examenXPregunta = em.merge(examenXPregunta);
            if (idPreguntaReferenciadaOld != null && !idPreguntaReferenciadaOld.equals(idPreguntaReferenciadaNew)) {
                idPreguntaReferenciadaOld.getExamenXPreguntaList().remove(examenXPregunta);
                idPreguntaReferenciadaOld = em.merge(idPreguntaReferenciadaOld);
            }
            if (idPreguntaReferenciadaNew != null && !idPreguntaReferenciadaNew.equals(idPreguntaReferenciadaOld)) {
                idPreguntaReferenciadaNew.getExamenXPreguntaList().add(examenXPregunta);
                idPreguntaReferenciadaNew = em.merge(idPreguntaReferenciadaNew);
            }
            if (idExamenReferenciadoOld != null && !idExamenReferenciadoOld.equals(idExamenReferenciadoNew)) {
                idExamenReferenciadoOld.getExamenXPreguntaList().remove(examenXPregunta);
                idExamenReferenciadoOld = em.merge(idExamenReferenciadoOld);
            }
            if (idExamenReferenciadoNew != null && !idExamenReferenciadoNew.equals(idExamenReferenciadoOld)) {
                idExamenReferenciadoNew.getExamenXPreguntaList().add(examenXPregunta);
                idExamenReferenciadoNew = em.merge(idExamenReferenciadoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = examenXPregunta.getIdExamenXPregunta();
                if (findExamenXPregunta(id) == null) {
                    throw new NonexistentEntityException("The examenXPregunta with id " + id + " no longer exists.");
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
            ExamenXPregunta examenXPregunta;
            try {
                examenXPregunta = em.getReference(ExamenXPregunta.class, id);
                examenXPregunta.getIdExamenXPregunta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The examenXPregunta with id " + id + " no longer exists.", enfe);
            }
            Pregunta idPreguntaReferenciada = examenXPregunta.getIdPreguntaReferenciada();
            if (idPreguntaReferenciada != null) {
                idPreguntaReferenciada.getExamenXPreguntaList().remove(examenXPregunta);
                idPreguntaReferenciada = em.merge(idPreguntaReferenciada);
            }
            Examen idExamenReferenciado = examenXPregunta.getIdExamenReferenciado();
            if (idExamenReferenciado != null) {
                idExamenReferenciado.getExamenXPreguntaList().remove(examenXPregunta);
                idExamenReferenciado = em.merge(idExamenReferenciado);
            }
            em.remove(examenXPregunta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ExamenXPregunta> findExamenXPreguntaEntities() {
        return findExamenXPreguntaEntities(true, -1, -1);
    }

    public List<ExamenXPregunta> findExamenXPreguntaEntities(int maxResults, int firstResult) {
        return findExamenXPreguntaEntities(false, maxResults, firstResult);
    }

    private List<ExamenXPregunta> findExamenXPreguntaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ExamenXPregunta.class));
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

    public ExamenXPregunta findExamenXPregunta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ExamenXPregunta.class, id);
        } finally {
            em.close();
        }
    }

    public int getExamenXPreguntaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ExamenXPregunta> rt = cq.from(ExamenXPregunta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
