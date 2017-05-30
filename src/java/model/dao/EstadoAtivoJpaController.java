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
import model.state.EstadoAtivo;

/**
 *
 * @author luisr
 */
public class EstadoAtivoJpaController implements Serializable {

     private static EstadoAtivoJpaController instance = new EstadoAtivoJpaController();
    private EntityManagerFactory emf = null;

    public static EstadoAtivoJpaController getInstance() {
        return instance;
    }

    private EstadoAtivoJpaController() {
    }

    public EntityManager getEntityManager() {
        return PersistenceUtil.getEntityManager();
    }

    public void create(EstadoAtivo estadoAtivo) {
        if (estadoAtivo.getFuncionariosCollection() == null) {
            estadoAtivo.setFuncionariosCollection(new ArrayList<Funcionarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Funcionarios> attachedFuncionariosCollection = new ArrayList<Funcionarios>();
            for (Funcionarios funcionariosCollectionFuncionariosToAttach : estadoAtivo.getFuncionariosCollection()) {
                funcionariosCollectionFuncionariosToAttach = em.getReference(funcionariosCollectionFuncionariosToAttach.getClass(), funcionariosCollectionFuncionariosToAttach.getId());
                attachedFuncionariosCollection.add(funcionariosCollectionFuncionariosToAttach);
            }
            estadoAtivo.setFuncionariosCollection(attachedFuncionariosCollection);
            em.persist(estadoAtivo);
            for (Funcionarios funcionariosCollectionFuncionarios : estadoAtivo.getFuncionariosCollection()) {
                model.Estado oldEstadoIdOfFuncionariosCollectionFuncionarios = funcionariosCollectionFuncionarios.getEstadoId();
                funcionariosCollectionFuncionarios.setEstadoId(estadoAtivo);
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

    public void edit(EstadoAtivo estadoAtivo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstadoAtivo persistentEstadoAtivo = em.find(EstadoAtivo.class, estadoAtivo.getId());
            Collection<Funcionarios> funcionariosCollectionOld = persistentEstadoAtivo.getFuncionariosCollection();
            Collection<Funcionarios> funcionariosCollectionNew = estadoAtivo.getFuncionariosCollection();
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
            estadoAtivo.setFuncionariosCollection(funcionariosCollectionNew);
            estadoAtivo = em.merge(estadoAtivo);
            for (Funcionarios funcionariosCollectionNewFuncionarios : funcionariosCollectionNew) {
                if (!funcionariosCollectionOld.contains(funcionariosCollectionNewFuncionarios)) {
                    EstadoAtivo oldEstadoIdOfFuncionariosCollectionNewFuncionarios = (EstadoAtivo) funcionariosCollectionNewFuncionarios.getEstadoId();
                    funcionariosCollectionNewFuncionarios.setEstadoId(estadoAtivo);
                    funcionariosCollectionNewFuncionarios = em.merge(funcionariosCollectionNewFuncionarios);
                    if (oldEstadoIdOfFuncionariosCollectionNewFuncionarios != null && !oldEstadoIdOfFuncionariosCollectionNewFuncionarios.equals(estadoAtivo)) {
                        oldEstadoIdOfFuncionariosCollectionNewFuncionarios.getFuncionariosCollection().remove(funcionariosCollectionNewFuncionarios);
                        oldEstadoIdOfFuncionariosCollectionNewFuncionarios = em.merge(oldEstadoIdOfFuncionariosCollectionNewFuncionarios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estadoAtivo.getId();
                if (findEstadoAtivo(id) == null) {
                    throw new NonexistentEntityException("The estadoAtivo with id " + id + " no longer exists.");
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
            EstadoAtivo estadoAtivo;
            try {
                estadoAtivo = em.getReference(EstadoAtivo.class, id);
                estadoAtivo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadoAtivo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Funcionarios> funcionariosCollectionOrphanCheck = estadoAtivo.getFuncionariosCollection();
            for (Funcionarios funcionariosCollectionOrphanCheckFuncionarios : funcionariosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EstadoAtivo (" + estadoAtivo + ") cannot be destroyed since the Funcionarios " + funcionariosCollectionOrphanCheckFuncionarios + " in its funcionariosCollection field has a non-nullable estadoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estadoAtivo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EstadoAtivo> findEstadoAtivoEntities() {
        return findEstadoAtivoEntities(true, -1, -1);
    }

    public List<EstadoAtivo> findEstadoAtivoEntities(int maxResults, int firstResult) {
        return findEstadoAtivoEntities(false, maxResults, firstResult);
    }

    private List<EstadoAtivo> findEstadoAtivoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstadoAtivo.class));
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

    public EstadoAtivo findEstadoAtivo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EstadoAtivo.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoAtivoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstadoAtivo> rt = cq.from(EstadoAtivo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Estado findByEstado(String ativo) {
         return this.getEntityManager().createNamedQuery("findByEstado", Estado.class)
                .setParameter("estado", ativo).getSingleResult();
    }
    
}
