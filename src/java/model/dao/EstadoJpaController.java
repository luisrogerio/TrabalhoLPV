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

/**
 *
 * @author luisr
 */
public class EstadoJpaController implements Serializable {

    private static EstadoJpaController instance = new EstadoJpaController();
    private EntityManagerFactory emf = null;

    public static EstadoJpaController getInstance() {
        return instance;
    }

    private EstadoJpaController() {
    }

    public EntityManager getEntityManager() {
        return PersistenceUtil.getEntityManager();
    }

    public void create(Estado estado) {
        if (estado.getFuncionariosCollection() == null) {
            estado.setFuncionariosCollection(new ArrayList<Funcionarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Funcionarios> attachedFuncionariosCollection = new ArrayList<Funcionarios>();
            for (Funcionarios funcionariosCollectionFuncionariosToAttach : estado.getFuncionariosCollection()) {
                funcionariosCollectionFuncionariosToAttach = em.getReference(funcionariosCollectionFuncionariosToAttach.getClass(), funcionariosCollectionFuncionariosToAttach.getId());
                attachedFuncionariosCollection.add(funcionariosCollectionFuncionariosToAttach);
            }
            estado.setFuncionariosCollection(attachedFuncionariosCollection);
            em.persist(estado);
            for (Funcionarios funcionariosCollectionFuncionarios : estado.getFuncionariosCollection()) {
                Estado oldEstadoIdOfFuncionariosCollectionFuncionarios = funcionariosCollectionFuncionarios.getEstadoId();
                funcionariosCollectionFuncionarios.setEstadoId(estado);
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

    public void edit(Estado estado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado persistentEstado = em.find(Estado.class, estado.getId());
            Collection<Funcionarios> funcionariosCollectionOld = persistentEstado.getFuncionariosCollection();
            Collection<Funcionarios> funcionariosCollectionNew = estado.getFuncionariosCollection();
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
            estado.setFuncionariosCollection(funcionariosCollectionNew);
            estado = em.merge(estado);
            for (Funcionarios funcionariosCollectionNewFuncionarios : funcionariosCollectionNew) {
                if (!funcionariosCollectionOld.contains(funcionariosCollectionNewFuncionarios)) {
                    Estado oldEstadoIdOfFuncionariosCollectionNewFuncionarios = funcionariosCollectionNewFuncionarios.getEstadoId();
                    funcionariosCollectionNewFuncionarios.setEstadoId(estado);
                    funcionariosCollectionNewFuncionarios = em.merge(funcionariosCollectionNewFuncionarios);
                    if (oldEstadoIdOfFuncionariosCollectionNewFuncionarios != null && !oldEstadoIdOfFuncionariosCollectionNewFuncionarios.equals(estado)) {
                        oldEstadoIdOfFuncionariosCollectionNewFuncionarios.getFuncionariosCollection().remove(funcionariosCollectionNewFuncionarios);
                        oldEstadoIdOfFuncionariosCollectionNewFuncionarios = em.merge(oldEstadoIdOfFuncionariosCollectionNewFuncionarios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estado.getId();
                if (findEstado(id) == null) {
                    throw new NonexistentEntityException("The estado with id " + id + " no longer exists.");
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
            Estado estado;
            try {
                estado = em.getReference(Estado.class, id);
                estado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Funcionarios> funcionariosCollectionOrphanCheck = estado.getFuncionariosCollection();
            for (Funcionarios funcionariosCollectionOrphanCheckFuncionarios : funcionariosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estado (" + estado + ") cannot be destroyed since the Funcionarios " + funcionariosCollectionOrphanCheckFuncionarios + " in its funcionariosCollection field has a non-nullable estadoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estado> findEstadoEntities() {
        return findEstadoEntities(true, -1, -1);
    }

    public List<Estado> findEstadoEntities(int maxResults, int firstResult) {
        return findEstadoEntities(false, maxResults, firstResult);
    }

    private List<Estado> findEstadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estado.class));
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

    public Estado findEstado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estado> rt = cq.from(Estado.class);
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
