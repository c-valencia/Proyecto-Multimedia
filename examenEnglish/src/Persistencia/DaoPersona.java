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
import Logica.Examen;
import Logica.Persona;
import Persistencia.exceptions.IllegalOrphanException;
import Persistencia.exceptions.NonexistentEntityException;
import Persistencia.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author cristian
 */
public class DaoPersona implements Serializable {

    public DaoPersona(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Persona persona) throws PreexistingEntityException, Exception {
        if (persona.getExamenList() == null) {
            persona.setExamenList(new ArrayList<Examen>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Examen> attachedExamenList = new ArrayList<Examen>();
            for (Examen examenListExamenToAttach : persona.getExamenList()) {
                examenListExamenToAttach = em.getReference(examenListExamenToAttach.getClass(), examenListExamenToAttach.getIdExamen());
                attachedExamenList.add(examenListExamenToAttach);
            }
            persona.setExamenList(attachedExamenList);
            em.persist(persona);
            for (Examen examenListExamen : persona.getExamenList()) {
                Persona oldIdEstudianteOfExamenListExamen = examenListExamen.getIdEstudiante();
                examenListExamen.setIdEstudiante(persona);
                examenListExamen = em.merge(examenListExamen);
                if (oldIdEstudianteOfExamenListExamen != null) {
                    oldIdEstudianteOfExamenListExamen.getExamenList().remove(examenListExamen);
                    oldIdEstudianteOfExamenListExamen = em.merge(oldIdEstudianteOfExamenListExamen);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPersona(persona.getCedula()) != null) {
                throw new PreexistingEntityException("Persona " + persona + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Persona persona) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Persona persistentPersona = em.find(Persona.class, persona.getCedula());
            List<Examen> examenListOld = persistentPersona.getExamenList();
            List<Examen> examenListNew = persona.getExamenList();
            List<String> illegalOrphanMessages = null;
            for (Examen examenListOldExamen : examenListOld) {
                if (!examenListNew.contains(examenListOldExamen)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Examen " + examenListOldExamen + " since its idEstudiante field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Examen> attachedExamenListNew = new ArrayList<Examen>();
            for (Examen examenListNewExamenToAttach : examenListNew) {
                examenListNewExamenToAttach = em.getReference(examenListNewExamenToAttach.getClass(), examenListNewExamenToAttach.getIdExamen());
                attachedExamenListNew.add(examenListNewExamenToAttach);
            }
            examenListNew = attachedExamenListNew;
            persona.setExamenList(examenListNew);
            persona = em.merge(persona);
            for (Examen examenListNewExamen : examenListNew) {
                if (!examenListOld.contains(examenListNewExamen)) {
                    Persona oldIdEstudianteOfExamenListNewExamen = examenListNewExamen.getIdEstudiante();
                    examenListNewExamen.setIdEstudiante(persona);
                    examenListNewExamen = em.merge(examenListNewExamen);
                    if (oldIdEstudianteOfExamenListNewExamen != null && !oldIdEstudianteOfExamenListNewExamen.equals(persona)) {
                        oldIdEstudianteOfExamenListNewExamen.getExamenList().remove(examenListNewExamen);
                        oldIdEstudianteOfExamenListNewExamen = em.merge(oldIdEstudianteOfExamenListNewExamen);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = persona.getCedula();
                if (findPersona(id) == null) {
                    throw new NonexistentEntityException("The persona with id " + id + " no longer exists.");
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
            Persona persona;
            try {
                persona = em.getReference(Persona.class, id);
                persona.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The persona with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Examen> examenListOrphanCheck = persona.getExamenList();
            for (Examen examenListOrphanCheckExamen : examenListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Examen " + examenListOrphanCheckExamen + " in its examenList field has a non-nullable idEstudiante field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(persona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Persona> findPersonaEntities() {
        return findPersonaEntities(true, -1, -1);
    }

    public List<Persona> findPersonaEntities(int maxResults, int firstResult) {
        return findPersonaEntities(false, maxResults, firstResult);
    }

    private List<Persona> findPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Persona.class));
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

    public Persona findPersona(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Persona.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Persona> rt = cq.from(Persona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
