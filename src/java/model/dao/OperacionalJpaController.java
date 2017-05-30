/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Operacional;
import model.dao.exceptions.NonexistentEntityException;

/**
 *
 * @author luisr
 */
public class OperacionalJpaController implements Serializable {
 private static OperacionalJpaController instance = new OperacionalJpaController();
    private EntityManagerFactory emf = null;

    public static OperacionalJpaController getInstance() {
        return instance;
    }

    private OperacionalJpaController() {
    }

    public EntityManager getEntityManager() {
        return PersistenceUtil.getEntityManager();
    }

    public void create(Operacional operacional) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(operacional);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Operacional operacional) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            operacional = em.merge(operacional);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = operacional.getId();
                if (findOperacional(id) == null) {
                    throw new NonexistentEntityException("The operacional with id " + id + " no longer exists.");
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
            Operacional operacional;
            try {
                operacional = em.getReference(Operacional.class, id);
                operacional.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The operacional with id " + id + " no longer exists.", enfe);
            }
            em.remove(operacional);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Operacional> findOperacionalEntities() {
        return findOperacionalEntities(true, -1, -1);
    }

    public List<Operacional> findOperacionalEntities(int maxResults, int firstResult) {
        return findOperacionalEntities(false, maxResults, firstResult);
    }

    private List<Operacional> findOperacionalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Operacional.class));
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

    public Operacional findOperacional(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Operacional.class, id);
        } finally {
            em.close();
        }
    }

    public int getOperacionalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Operacional> rt = cq.from(Operacional.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
