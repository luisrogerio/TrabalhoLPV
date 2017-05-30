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
import model.state.EstadoLicenca;

/**
 *
 * @author luisr
 */
public class EstadoLicencaJpaController implements Serializable {

    private static EstadoLicencaJpaController instance = new EstadoLicencaJpaController();
    private EntityManagerFactory emf = null;

    public static EstadoLicencaJpaController getInstance() {
        return instance;
    }

    private EstadoLicencaJpaController() {
    }

    public EntityManager getEntityManager() {
        return PersistenceUtil.getEntityManager();
    }

    public void create(EstadoLicenca estadoLicenca) {
        if (estadoLicenca.getFuncionariosCollection() == null) {
            estadoLicenca.setFuncionariosCollection(new ArrayList<Funcionarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Funcionarios> attachedFuncionariosCollection = new ArrayList<Funcionarios>();
            for (Funcionarios funcionariosCollectionFuncionariosToAttach : estadoLicenca.getFuncionariosCollection()) {
                funcionariosCollectionFuncionariosToAttach = em.getReference(funcionariosCollectionFuncionariosToAttach.getClass(), funcionariosCollectionFuncionariosToAttach.getId());
                attachedFuncionariosCollection.add(funcionariosCollectionFuncionariosToAttach);
            }
            estadoLicenca.setFuncionariosCollection(attachedFuncionariosCollection);
            em.persist(estadoLicenca);
            for (Funcionarios funcionariosCollectionFuncionarios : estadoLicenca.getFuncionariosCollection()) {
                model.Estado oldEstadoIdOfFuncionariosCollectionFuncionarios = funcionariosCollectionFuncionarios.getEstadoId();
                funcionariosCollectionFuncionarios.setEstadoId(estadoLicenca);
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

    public void edit(EstadoLicenca estadoLicenca) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstadoLicenca persistentEstadoLicenca = em.find(EstadoLicenca.class, estadoLicenca.getId());
            Collection<Funcionarios> funcionariosCollectionOld = persistentEstadoLicenca.getFuncionariosCollection();
            Collection<Funcionarios> funcionariosCollectionNew = estadoLicenca.getFuncionariosCollection();
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
            estadoLicenca.setFuncionariosCollection(funcionariosCollectionNew);
            estadoLicenca = em.merge(estadoLicenca);
            for (Funcionarios funcionariosCollectionNewFuncionarios : funcionariosCollectionNew) {
                if (!funcionariosCollectionOld.contains(funcionariosCollectionNewFuncionarios)) {
                    EstadoLicenca oldEstadoIdOfFuncionariosCollectionNewFuncionarios = (EstadoLicenca) funcionariosCollectionNewFuncionarios.getEstadoId();
                    funcionariosCollectionNewFuncionarios.setEstadoId(estadoLicenca);
                    funcionariosCollectionNewFuncionarios = em.merge(funcionariosCollectionNewFuncionarios);
                    if (oldEstadoIdOfFuncionariosCollectionNewFuncionarios != null && !oldEstadoIdOfFuncionariosCollectionNewFuncionarios.equals(estadoLicenca)) {
                        oldEstadoIdOfFuncionariosCollectionNewFuncionarios.getFuncionariosCollection().remove(funcionariosCollectionNewFuncionarios);
                        oldEstadoIdOfFuncionariosCollectionNewFuncionarios = em.merge(oldEstadoIdOfFuncionariosCollectionNewFuncionarios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estadoLicenca.getId();
                if (findEstadoLicenca(id) == null) {
                    throw new NonexistentEntityException("The estadoLicenca with id " + id + " no longer exists.");
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
            EstadoLicenca estadoLicenca;
            try {
                estadoLicenca = em.getReference(EstadoLicenca.class, id);
                estadoLicenca.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadoLicenca with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Funcionarios> funcionariosCollectionOrphanCheck = estadoLicenca.getFuncionariosCollection();
            for (Funcionarios funcionariosCollectionOrphanCheckFuncionarios : funcionariosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EstadoLicenca (" + estadoLicenca + ") cannot be destroyed since the Funcionarios " + funcionariosCollectionOrphanCheckFuncionarios + " in its funcionariosCollection field has a non-nullable estadoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estadoLicenca);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EstadoLicenca> findEstadoLicencaEntities() {
        return findEstadoLicencaEntities(true, -1, -1);
    }

    public List<EstadoLicenca> findEstadoLicencaEntities(int maxResults, int firstResult) {
        return findEstadoLicencaEntities(false, maxResults, firstResult);
    }

    private List<EstadoLicenca> findEstadoLicencaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstadoLicenca.class));
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

    public EstadoLicenca findEstadoLicenca(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EstadoLicenca.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoLicencaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstadoLicenca> rt = cq.from(EstadoLicenca.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Estado findByEstado(String licença) {
        return this.getEntityManager().createNamedQuery("findByEstado", Estado.class)
                .setParameter("estado", licença).getSingleResult();
    }

}
