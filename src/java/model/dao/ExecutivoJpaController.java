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
import model.Executivo;
import model.dao.exceptions.NonexistentEntityException;

/**
 *
 * @author luisr
 */
public class ExecutivoJpaController implements Serializable {

    private static ExecutivoJpaController instance = new ExecutivoJpaController();
    private EntityManagerFactory emf = null;

    public static ExecutivoJpaController getInstance() {
        return instance;
    }

    private ExecutivoJpaController() {
    }

    public EntityManager getEntityManager() {
        return PersistenceUtil.getEntityManager();
    }

    public void create(Executivo executivo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(executivo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Executivo executivo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            executivo = em.merge(executivo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = executivo.getId();
                if (findExecutivo(id) == null) {
                    throw new NonexistentEntityException("The executivo with id " + id + " no longer exists.");
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
            Executivo executivo;
            try {
                executivo = em.getReference(Executivo.class, id);
                executivo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The executivo with id " + id + " no longer exists.", enfe);
            }
            em.remove(executivo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Executivo> findExecutivoEntities() {
        return findExecutivoEntities(true, -1, -1);
    }

    public List<Executivo> findExecutivoEntities(int maxResults, int firstResult) {
        return findExecutivoEntities(false, maxResults, firstResult);
    }

    private List<Executivo> findExecutivoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Executivo.class));
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

    public Executivo findExecutivo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Executivo.class, id);
        } finally {
            em.close();
        }
    }

    public int getExecutivoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Executivo> rt = cq.from(Executivo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
