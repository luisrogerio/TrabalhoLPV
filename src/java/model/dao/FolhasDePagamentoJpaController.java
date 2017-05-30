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
import model.FolhasDePagamento;
import model.Funcionarios;
import model.dao.exceptions.NonexistentEntityException;

/**
 *
 * @author luisr
 */
public class FolhasDePagamentoJpaController implements Serializable {

       private static FolhasDePagamentoJpaController instance = new FolhasDePagamentoJpaController();
    private EntityManagerFactory emf = null;

    public static FolhasDePagamentoJpaController getInstance() {
        return instance;
    }

    private FolhasDePagamentoJpaController() {
    }

    public EntityManager getEntityManager() {
        return PersistenceUtil.getEntityManager();
    }

    public void create(FolhasDePagamento folhasDePagamento) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Funcionarios funcionariosId = folhasDePagamento.getFuncionariosId();
            if (funcionariosId != null) {
                funcionariosId = em.getReference(funcionariosId.getClass(), funcionariosId.getId());
                folhasDePagamento.setFuncionariosId(funcionariosId);
            }
            em.persist(folhasDePagamento);
            if (funcionariosId != null) {
                funcionariosId.getFolhasDePagamentoCollection().add(folhasDePagamento);
                funcionariosId = em.merge(funcionariosId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FolhasDePagamento folhasDePagamento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FolhasDePagamento persistentFolhasDePagamento = em.find(FolhasDePagamento.class, folhasDePagamento.getId());
            Funcionarios funcionariosIdOld = persistentFolhasDePagamento.getFuncionariosId();
            Funcionarios funcionariosIdNew = folhasDePagamento.getFuncionariosId();
            if (funcionariosIdNew != null) {
                funcionariosIdNew = em.getReference(funcionariosIdNew.getClass(), funcionariosIdNew.getId());
                folhasDePagamento.setFuncionariosId(funcionariosIdNew);
            }
            folhasDePagamento = em.merge(folhasDePagamento);
            if (funcionariosIdOld != null && !funcionariosIdOld.equals(funcionariosIdNew)) {
                funcionariosIdOld.getFolhasDePagamentoCollection().remove(folhasDePagamento);
                funcionariosIdOld = em.merge(funcionariosIdOld);
            }
            if (funcionariosIdNew != null && !funcionariosIdNew.equals(funcionariosIdOld)) {
                funcionariosIdNew.getFolhasDePagamentoCollection().add(folhasDePagamento);
                funcionariosIdNew = em.merge(funcionariosIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = folhasDePagamento.getId();
                if (findFolhasDePagamento(id) == null) {
                    throw new NonexistentEntityException("The folhasDePagamento with id " + id + " no longer exists.");
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
            FolhasDePagamento folhasDePagamento;
            try {
                folhasDePagamento = em.getReference(FolhasDePagamento.class, id);
                folhasDePagamento.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The folhasDePagamento with id " + id + " no longer exists.", enfe);
            }
            Funcionarios funcionariosId = folhasDePagamento.getFuncionariosId();
            if (funcionariosId != null) {
                funcionariosId.getFolhasDePagamentoCollection().remove(folhasDePagamento);
                funcionariosId = em.merge(funcionariosId);
            }
            em.remove(folhasDePagamento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FolhasDePagamento> findFolhasDePagamentoEntities() {
        return findFolhasDePagamentoEntities(true, -1, -1);
    }

    public List<FolhasDePagamento> findFolhasDePagamentoEntities(int maxResults, int firstResult) {
        return findFolhasDePagamentoEntities(false, maxResults, firstResult);
    }

    private List<FolhasDePagamento> findFolhasDePagamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FolhasDePagamento.class));
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

    public FolhasDePagamento findFolhasDePagamento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FolhasDePagamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getFolhasDePagamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FolhasDePagamento> rt = cq.from(FolhasDePagamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
