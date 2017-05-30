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
import model.Estado;
import model.dao.exceptions.IllegalOrphanException;
import model.dao.exceptions.NonexistentEntityException;
import model.state.EstadoFerias;

/**
 *
 * @author luisr
 */
public class EstadoFeriasJpaController implements Serializable {

    private static EstadoFeriasJpaController instance = new EstadoFeriasJpaController();
    private EntityManagerFactory emf = null;

    public static EstadoFeriasJpaController getInstance() {
        return instance;
    }

    private EstadoFeriasJpaController() {
    }

    public EntityManager getEntityManager() {
        return PersistenceUtil.getEntityManager();
    }

    public void create(EstadoFerias estadoFerias) {
        if (estadoFerias.getFuncionariosCollection() == null) {
            estadoFerias.setFuncionariosCollection(new ArrayList<Funcionarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Funcionarios> attachedFuncionariosCollection = new ArrayList<Funcionarios>();
            for (Funcionarios funcionariosCollectionFuncionariosToAttach : estadoFerias.getFuncionariosCollection()) {
                funcionariosCollectionFuncionariosToAttach = em.getReference(funcionariosCollectionFuncionariosToAttach.getClass(), funcionariosCollectionFuncionariosToAttach.getId());
                attachedFuncionariosCollection.add(funcionariosCollectionFuncionariosToAttach);
            }
            estadoFerias.setFuncionariosCollection(attachedFuncionariosCollection);
            em.persist(estadoFerias);
            for (Funcionarios funcionariosCollectionFuncionarios : estadoFerias.getFuncionariosCollection()) {
                model.Estado oldEstadoIdOfFuncionariosCollectionFuncionarios = funcionariosCollectionFuncionarios.getEstadoId();
                funcionariosCollectionFuncionarios.setEstadoId(estadoFerias);
                funcionariosCollectionFuncionarios = em.merge(funcionariosCollectionFuncionarios);
                if (oldEstadoIdOfFuncionariosCollectionFuncionarios != null) {
                    oldEstadoIdOfFuncionariosCollectionFuncionarios.getFuncionariosCollection().remove(funcionariosCollectionFuncionarios);
                    oldEstadoIdOfFuncionariosCollectionFuncionarios = em.merge(oldEstadoIdOfFuncionariosCollectionFuncionarios);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EstadoFerias estadoFerias) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstadoFerias persistentEstadoFerias = em.find(EstadoFerias.class, estadoFerias.getId());
            Collection<Funcionarios> funcionariosCollectionOld = persistentEstadoFerias.getFuncionariosCollection();
            Collection<Funcionarios> funcionariosCollectionNew = estadoFerias.getFuncionariosCollection();
            List<String> illegalOrphanMessages = null;
            for (Funcionarios funcionariosCollectionOldFuncionarios : funcionariosCollectionOld) {
                if (!funcionariosCollectionNew.contains(funcionariosCollectionOldFuncionarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Funcionarios " + funcionariosCollectionOldFuncionarios + " since its estadoId field is not nullable.");
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
            estadoFerias.setFuncionariosCollection(funcionariosCollectionNew);
            estadoFerias = em.merge(estadoFerias);
            for (Funcionarios funcionariosCollectionNewFuncionarios : funcionariosCollectionNew) {
                if (!funcionariosCollectionOld.contains(funcionariosCollectionNewFuncionarios)) {
                    EstadoFerias oldEstadoIdOfFuncionariosCollectionNewFuncionarios = (EstadoFerias) funcionariosCollectionNewFuncionarios.getEstadoId();
                    funcionariosCollectionNewFuncionarios.setEstadoId(estadoFerias);
                    funcionariosCollectionNewFuncionarios = em.merge(funcionariosCollectionNewFuncionarios);
                    if (oldEstadoIdOfFuncionariosCollectionNewFuncionarios != null && !oldEstadoIdOfFuncionariosCollectionNewFuncionarios.equals(estadoFerias)) {
                        oldEstadoIdOfFuncionariosCollectionNewFuncionarios.getFuncionariosCollection().remove(funcionariosCollectionNewFuncionarios);
                        oldEstadoIdOfFuncionariosCollectionNewFuncionarios = em.merge(oldEstadoIdOfFuncionariosCollectionNewFuncionarios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estadoFerias.getId();
                if (findEstadoFerias(id) == null) {
                    throw new NonexistentEntityException("The estadoFerias with id " + id + " no longer exists.");
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
            EstadoFerias estadoFerias;
            try {
                estadoFerias = em.getReference(EstadoFerias.class, id);
                estadoFerias.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadoFerias with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Funcionarios> funcionariosCollectionOrphanCheck = estadoFerias.getFuncionariosCollection();
            for (Funcionarios funcionariosCollectionOrphanCheckFuncionarios : funcionariosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EstadoFerias (" + estadoFerias + ") cannot be destroyed since the Funcionarios " + funcionariosCollectionOrphanCheckFuncionarios + " in its funcionariosCollection field has a non-nullable estadoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estadoFerias);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EstadoFerias> findEstadoFeriasEntities() {
        return findEstadoFeriasEntities(true, -1, -1);
    }

    public List<EstadoFerias> findEstadoFeriasEntities(int maxResults, int firstResult) {
        return findEstadoFeriasEntities(false, maxResults, firstResult);
    }

    private List<EstadoFerias> findEstadoFeriasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstadoFerias.class));
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

    public EstadoFerias findEstadoFerias(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EstadoFerias.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoFeriasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstadoFerias> rt = cq.from(EstadoFerias.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Estado findByEstado(String férias) {
        return this.getEntityManager().createNamedQuery("findByEstado", Estado.class)
                .setParameter("estado", férias).getSingleResult();
    }

}
