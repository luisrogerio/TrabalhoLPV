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
import model.state.EstadoDesligado;

/**
 *
 * @author luisr
 */
public class EstadoDesligadoJpaController implements Serializable {

    private static EstadoDesligadoJpaController instance = new EstadoDesligadoJpaController();
    private EntityManagerFactory emf = null;

    public static EstadoDesligadoJpaController getInstance() {
        return instance;
    }

    private EstadoDesligadoJpaController() {
    }

    public EntityManager getEntityManager() {
        return PersistenceUtil.getEntityManager();
    }

    public void create(EstadoDesligado estadoDesligado) {
        if (estadoDesligado.getFuncionariosCollection() == null) {
            estadoDesligado.setFuncionariosCollection(new ArrayList<Funcionarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Funcionarios> attachedFuncionariosCollection = new ArrayList<Funcionarios>();
            for (Funcionarios funcionariosCollectionFuncionariosToAttach : estadoDesligado.getFuncionariosCollection()) {
                funcionariosCollectionFuncionariosToAttach = em.getReference(funcionariosCollectionFuncionariosToAttach.getClass(), funcionariosCollectionFuncionariosToAttach.getId());
                attachedFuncionariosCollection.add(funcionariosCollectionFuncionariosToAttach);
            }
            estadoDesligado.setFuncionariosCollection(attachedFuncionariosCollection);
            em.persist(estadoDesligado);
            for (Funcionarios funcionariosCollectionFuncionarios : estadoDesligado.getFuncionariosCollection()) {
                model.Estado oldEstadoIdOfFuncionariosCollectionFuncionarios = funcionariosCollectionFuncionarios.getEstadoId();
                funcionariosCollectionFuncionarios.setEstadoId(estadoDesligado);
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

    public void edit(EstadoDesligado estadoDesligado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstadoDesligado persistentEstadoDesligado = em.find(EstadoDesligado.class, estadoDesligado.getId());
            Collection<Funcionarios> funcionariosCollectionOld = persistentEstadoDesligado.getFuncionariosCollection();
            Collection<Funcionarios> funcionariosCollectionNew = estadoDesligado.getFuncionariosCollection();
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
            estadoDesligado.setFuncionariosCollection(funcionariosCollectionNew);
            estadoDesligado = em.merge(estadoDesligado);
            for (Funcionarios funcionariosCollectionNewFuncionarios : funcionariosCollectionNew) {
                if (!funcionariosCollectionOld.contains(funcionariosCollectionNewFuncionarios)) {
                    EstadoDesligado oldEstadoIdOfFuncionariosCollectionNewFuncionarios = (EstadoDesligado) funcionariosCollectionNewFuncionarios.getEstadoId();
                    funcionariosCollectionNewFuncionarios.setEstadoId(estadoDesligado);
                    funcionariosCollectionNewFuncionarios = em.merge(funcionariosCollectionNewFuncionarios);
                    if (oldEstadoIdOfFuncionariosCollectionNewFuncionarios != null && !oldEstadoIdOfFuncionariosCollectionNewFuncionarios.equals(estadoDesligado)) {
                        oldEstadoIdOfFuncionariosCollectionNewFuncionarios.getFuncionariosCollection().remove(funcionariosCollectionNewFuncionarios);
                        oldEstadoIdOfFuncionariosCollectionNewFuncionarios = em.merge(oldEstadoIdOfFuncionariosCollectionNewFuncionarios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estadoDesligado.getId();
                if (findEstadoDesligado(id) == null) {
                    throw new NonexistentEntityException("The estadoDesligado with id " + id + " no longer exists.");
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
            EstadoDesligado estadoDesligado;
            try {
                estadoDesligado = em.getReference(EstadoDesligado.class, id);
                estadoDesligado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadoDesligado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Funcionarios> funcionariosCollectionOrphanCheck = estadoDesligado.getFuncionariosCollection();
            for (Funcionarios funcionariosCollectionOrphanCheckFuncionarios : funcionariosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EstadoDesligado (" + estadoDesligado + ") cannot be destroyed since the Funcionarios " + funcionariosCollectionOrphanCheckFuncionarios + " in its funcionariosCollection field has a non-nullable estadoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estadoDesligado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EstadoDesligado> findEstadoDesligadoEntities() {
        return findEstadoDesligadoEntities(true, -1, -1);
    }

    public List<EstadoDesligado> findEstadoDesligadoEntities(int maxResults, int firstResult) {
        return findEstadoDesligadoEntities(false, maxResults, firstResult);
    }

    private List<EstadoDesligado> findEstadoDesligadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstadoDesligado.class));
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

    public EstadoDesligado findEstadoDesligado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EstadoDesligado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoDesligadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstadoDesligado> rt = cq.from(EstadoDesligado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Estado findByEstado(String desligado) {
        return this.getEntityManager().createNamedQuery("findByEstado", Estado.class)
                .setParameter("estado", desligado).getSingleResult();
    }

}
