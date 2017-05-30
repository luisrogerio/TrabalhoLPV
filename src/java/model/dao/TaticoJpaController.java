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
import model.Tatico;
import model.dao.exceptions.NonexistentEntityException;

/**
 *
 * @author luisr
 */
public class TaticoJpaController implements Serializable {

    private static TaticoJpaController instance = new TaticoJpaController();
    private EntityManagerFactory emf = null;

    public static TaticoJpaController getInstance() {
        return instance;
    }

    private TaticoJpaController() {
    }

    public EntityManager getEntityManager() {
        return PersistenceUtil.getEntityManager();
    }

    public void create(Tatico tatico) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tatico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tatico tatico) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tatico = em.merge(tatico);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tatico.getId();
                if (findTatico(id) == null) {
                    throw new NonexistentEntityException("The tatico with id " + id + " no longer exists.");
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
            Tatico tatico;
            try {
                tatico = em.getReference(Tatico.class, id);
                tatico.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tatico with id " + id + " no longer exists.", enfe);
            }
            em.remove(tatico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tatico> findTaticoEntities() {
        return findTaticoEntities(true, -1, -1);
    }

    public List<Tatico> findTaticoEntities(int maxResults, int firstResult) {
        return findTaticoEntities(false, maxResults, firstResult);
    }

    private List<Tatico> findTaticoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tatico.class));
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

    public Tatico findTatico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tatico.class, id);
        } finally {
            em.close();
        }
    }

    public int getTaticoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tatico> rt = cq.from(Tatico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
