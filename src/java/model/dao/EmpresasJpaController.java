/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Funcionarios;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Empresas;
import model.dao.exceptions.IllegalOrphanException;
import model.dao.exceptions.NonexistentEntityException;

/**
 *
 * @author luisr
 */
public class EmpresasJpaController implements Serializable {

    private static EmpresasJpaController instance = new EmpresasJpaController();
    private EntityManagerFactory emf = null;

    public static EmpresasJpaController getInstance() {
        return instance;
    }

    private EmpresasJpaController() {
    }

    public EntityManager getEntityManager() {
        return PersistenceUtil.getEntityManager();
    }

    public void create(Empresas empresas) {
        if (empresas.getFuncionariosCollection() == null) {
            empresas.setFuncionariosCollection(new ArrayList<Funcionarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Funcionarios> attachedFuncionariosCollection = new ArrayList<Funcionarios>();
            for (Funcionarios funcionariosCollectionFuncionariosToAttach : empresas.getFuncionariosCollection()) {
                funcionariosCollectionFuncionariosToAttach = em.getReference(funcionariosCollectionFuncionariosToAttach.getClass(), funcionariosCollectionFuncionariosToAttach.getId());
                attachedFuncionariosCollection.add(funcionariosCollectionFuncionariosToAttach);
            }
            empresas.setFuncionariosCollection(attachedFuncionariosCollection);
            em.persist(empresas);
            for (Funcionarios funcionariosCollectionFuncionarios : empresas.getFuncionariosCollection()) {
                Empresas oldEmpresaIdOfFuncionariosCollectionFuncionarios = funcionariosCollectionFuncionarios.getEmpresaId();
                funcionariosCollectionFuncionarios.setEmpresaId(empresas);
                funcionariosCollectionFuncionarios = em.merge(funcionariosCollectionFuncionarios);
                if (oldEmpresaIdOfFuncionariosCollectionFuncionarios != null) {
                    oldEmpresaIdOfFuncionariosCollectionFuncionarios.getFuncionariosCollection().remove(funcionariosCollectionFuncionarios);
                    oldEmpresaIdOfFuncionariosCollectionFuncionarios = em.merge(oldEmpresaIdOfFuncionariosCollectionFuncionarios);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empresas empresas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresas persistentEmpresas = em.find(Empresas.class, empresas.getId());
            Collection<Funcionarios> funcionariosCollectionOld = persistentEmpresas.getFuncionariosCollection();
            Collection<Funcionarios> funcionariosCollectionNew = empresas.getFuncionariosCollection();
            List<String> illegalOrphanMessages = null;
            for (Funcionarios funcionariosCollectionOldFuncionarios : funcionariosCollectionOld) {
                if (!funcionariosCollectionNew.contains(funcionariosCollectionOldFuncionarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Funcionarios " + funcionariosCollectionOldFuncionarios + " since its empresaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Funcionarios> attachedFuncionariosCollectionNew = new ArrayList<Funcionarios>();
            for (Funcionarios funcionariosCollectionNewFuncionariosToAttach : funcionariosCollectionNew) {
                funcionariosCollectionNewFuncionariosToAttach = em.getReference(funcionariosCollectionNewFuncionariosToAttach.getClass(), funcionariosCollectionNewFuncionariosToAttach.getId());
                attachedFuncionariosCollectionNew.add(funcionariosCollectionNewFuncionariosToAttach);
            }
            funcionariosCollectionNew = attachedFuncionariosCollectionNew;
            empresas.setFuncionariosCollection(funcionariosCollectionNew);
            empresas = em.merge(empresas);
            for (Funcionarios funcionariosCollectionNewFuncionarios : funcionariosCollectionNew) {
                if (!funcionariosCollectionOld.contains(funcionariosCollectionNewFuncionarios)) {
                    Empresas oldEmpresaIdOfFuncionariosCollectionNewFuncionarios = funcionariosCollectionNewFuncionarios.getEmpresaId();
                    funcionariosCollectionNewFuncionarios.setEmpresaId(empresas);
                    funcionariosCollectionNewFuncionarios = em.merge(funcionariosCollectionNewFuncionarios);
                    if (oldEmpresaIdOfFuncionariosCollectionNewFuncionarios != null && !oldEmpresaIdOfFuncionariosCollectionNewFuncionarios.equals(empresas)) {
                        oldEmpresaIdOfFuncionariosCollectionNewFuncionarios.getFuncionariosCollection().remove(funcionariosCollectionNewFuncionarios);
                        oldEmpresaIdOfFuncionariosCollectionNewFuncionarios = em.merge(oldEmpresaIdOfFuncionariosCollectionNewFuncionarios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empresas.getId();
                if (findEmpresas(id) == null) {
                    throw new NonexistentEntityException("The empresas with id " + id + " no longer exists.");
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
            Empresas empresas;
            try {
                empresas = em.getReference(Empresas.class, id);
                empresas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empresas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Funcionarios> funcionariosCollectionOrphanCheck = empresas.getFuncionariosCollection();
            for (Funcionarios funcionariosCollectionOrphanCheckFuncionarios : funcionariosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresas (" + empresas + ") cannot be destroyed since the Funcionarios " + funcionariosCollectionOrphanCheckFuncionarios + " in its funcionariosCollection field has a non-nullable empresaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(empresas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empresas> findEmpresasEntities() {
        return findEmpresasEntities(true, -1, -1);
    }

    public List<Empresas> findEmpresasEntities(int maxResults, int firstResult) {
        return findEmpresasEntities(false, maxResults, firstResult);
    }

    private List<Empresas> findEmpresasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empresas.class));
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

    public Empresas findEmpresas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empresas.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpresasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empresas> rt = cq.from(Empresas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
