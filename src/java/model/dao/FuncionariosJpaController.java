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
import model.Empresas;
import model.Estado;
import model.Funcionarios;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.FolhasDePagamento;
import model.dao.exceptions.IllegalOrphanException;
import model.dao.exceptions.NonexistentEntityException;

/**
 *
 * @author luisr
 */
public class FuncionariosJpaController implements Serializable {

    private static FuncionariosJpaController instance = new FuncionariosJpaController();
    private EntityManagerFactory emf = null;

    public static FuncionariosJpaController getInstance() {
        return instance;
    }

    private FuncionariosJpaController() {
    }

    public EntityManager getEntityManager() {
        return PersistenceUtil.getEntityManager();
    }

    public void create(Funcionarios funcionarios) {
        if (funcionarios.getFuncionariosCollection() == null) {
            funcionarios.setFuncionariosCollection(new ArrayList<Funcionarios>());
        }
        if (funcionarios.getFolhasDePagamentoCollection() == null) {
            funcionarios.setFolhasDePagamentoCollection(new ArrayList<FolhasDePagamento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresas empresaId = funcionarios.getEmpresaId();
            if (empresaId != null) {
                empresaId = em.getReference(empresaId.getClass(), empresaId.getId());
                funcionarios.setEmpresaId(empresaId);
            }
            Estado estadoId = funcionarios.getEstadoId();
            if (estadoId != null) {
                estadoId = em.getReference(estadoId.getClass(), estadoId.getId());
                funcionarios.setEstadoId(estadoId);
            }
            Funcionarios gerenteId = funcionarios.getGerenteId();
            if (gerenteId != null) {
                gerenteId = em.getReference(gerenteId.getClass(), gerenteId.getId());
                funcionarios.setGerenteId(gerenteId);
            }
            Collection<Funcionarios> attachedFuncionariosCollection = new ArrayList<Funcionarios>();
            for (Funcionarios funcionariosCollectionFuncionariosToAttach : funcionarios.getFuncionariosCollection()) {
                funcionariosCollectionFuncionariosToAttach = em.getReference(funcionariosCollectionFuncionariosToAttach.getClass(), funcionariosCollectionFuncionariosToAttach.getId());
                attachedFuncionariosCollection.add(funcionariosCollectionFuncionariosToAttach);
            }
            funcionarios.setFuncionariosCollection(attachedFuncionariosCollection);
            Collection<FolhasDePagamento> attachedFolhasDePagamentoCollection = new ArrayList<FolhasDePagamento>();
            for (FolhasDePagamento folhasDePagamentoCollectionFolhasDePagamentoToAttach : funcionarios.getFolhasDePagamentoCollection()) {
                folhasDePagamentoCollectionFolhasDePagamentoToAttach = em.getReference(folhasDePagamentoCollectionFolhasDePagamentoToAttach.getClass(), folhasDePagamentoCollectionFolhasDePagamentoToAttach.getId());
                attachedFolhasDePagamentoCollection.add(folhasDePagamentoCollectionFolhasDePagamentoToAttach);
            }
            funcionarios.setFolhasDePagamentoCollection(attachedFolhasDePagamentoCollection);
            em.persist(funcionarios);
            if (empresaId != null) {
                empresaId.getFuncionariosCollection().add(funcionarios);
                empresaId = em.merge(empresaId);
            }
            if (estadoId != null) {
                estadoId.getFuncionariosCollection().add(funcionarios);
                estadoId = em.merge(estadoId);
            }
            if (gerenteId != null) {
                gerenteId.getFuncionariosCollection().add(funcionarios);
                gerenteId = em.merge(gerenteId);
            }
            for (Funcionarios funcionariosCollectionFuncionarios : funcionarios.getFuncionariosCollection()) {
                Funcionarios oldGerenteIdOfFuncionariosCollectionFuncionarios = funcionariosCollectionFuncionarios.getGerenteId();
                funcionariosCollectionFuncionarios.setGerenteId(funcionarios);
                funcionariosCollectionFuncionarios = em.merge(funcionariosCollectionFuncionarios);
                if (oldGerenteIdOfFuncionariosCollectionFuncionarios != null) {
                    oldGerenteIdOfFuncionariosCollectionFuncionarios.getFuncionariosCollection().remove(funcionariosCollectionFuncionarios);
                    oldGerenteIdOfFuncionariosCollectionFuncionarios = em.merge(oldGerenteIdOfFuncionariosCollectionFuncionarios);
                }
            }
            for (FolhasDePagamento folhasDePagamentoCollectionFolhasDePagamento : funcionarios.getFolhasDePagamentoCollection()) {
                Funcionarios oldFuncionariosIdOfFolhasDePagamentoCollectionFolhasDePagamento = folhasDePagamentoCollectionFolhasDePagamento.getFuncionariosId();
                folhasDePagamentoCollectionFolhasDePagamento.setFuncionariosId(funcionarios);
                folhasDePagamentoCollectionFolhasDePagamento = em.merge(folhasDePagamentoCollectionFolhasDePagamento);
                if (oldFuncionariosIdOfFolhasDePagamentoCollectionFolhasDePagamento != null) {
                    oldFuncionariosIdOfFolhasDePagamentoCollectionFolhasDePagamento.getFolhasDePagamentoCollection().remove(folhasDePagamentoCollectionFolhasDePagamento);
                    oldFuncionariosIdOfFolhasDePagamentoCollectionFolhasDePagamento = em.merge(oldFuncionariosIdOfFolhasDePagamentoCollectionFolhasDePagamento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Funcionarios funcionarios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Funcionarios persistentFuncionarios = em.find(Funcionarios.class, funcionarios.getId());
            Empresas empresaIdOld = persistentFuncionarios.getEmpresaId();
            Empresas empresaIdNew = funcionarios.getEmpresaId();
            Estado estadoIdOld = persistentFuncionarios.getEstadoId();
            Estado estadoIdNew = funcionarios.getEstadoId();
            Funcionarios gerenteIdOld = persistentFuncionarios.getGerenteId();
            Funcionarios gerenteIdNew = funcionarios.getGerenteId();
            Collection<Funcionarios> funcionariosCollectionOld = persistentFuncionarios.getFuncionariosCollection();
            Collection<Funcionarios> funcionariosCollectionNew = funcionarios.getFuncionariosCollection();
            Collection<FolhasDePagamento> folhasDePagamentoCollectionOld = persistentFuncionarios.getFolhasDePagamentoCollection();
            Collection<FolhasDePagamento> folhasDePagamentoCollectionNew = funcionarios.getFolhasDePagamentoCollection();
            List<String> illegalOrphanMessages = null;
            for (FolhasDePagamento folhasDePagamentoCollectionOldFolhasDePagamento : folhasDePagamentoCollectionOld) {
                if (!folhasDePagamentoCollectionNew.contains(folhasDePagamentoCollectionOldFolhasDePagamento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain FolhasDePagamento " + folhasDePagamentoCollectionOldFolhasDePagamento + " since its funcionariosId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (empresaIdNew != null) {
                empresaIdNew = em.getReference(empresaIdNew.getClass(), empresaIdNew.getId());
                funcionarios.setEmpresaId(empresaIdNew);
            }
            if (estadoIdNew != null) {
                estadoIdNew = em.getReference(estadoIdNew.getClass(), estadoIdNew.getId());
                funcionarios.setEstadoId(estadoIdNew);
            }
            if (gerenteIdNew != null) {
                gerenteIdNew = em.getReference(gerenteIdNew.getClass(), gerenteIdNew.getId());
                funcionarios.setGerenteId(gerenteIdNew);
            }
            Collection<Funcionarios> attachedFuncionariosCollectionNew = new ArrayList<Funcionarios>();
            for (Funcionarios funcionariosCollectionNewFuncionariosToAttach : funcionariosCollectionNew) {
                funcionariosCollectionNewFuncionariosToAttach = em.getReference(funcionariosCollectionNewFuncionariosToAttach.getClass(), funcionariosCollectionNewFuncionariosToAttach.getId());
                attachedFuncionariosCollectionNew.add(funcionariosCollectionNewFuncionariosToAttach);
            }
            funcionariosCollectionNew = attachedFuncionariosCollectionNew;
            funcionarios.setFuncionariosCollection(funcionariosCollectionNew);
            Collection<FolhasDePagamento> attachedFolhasDePagamentoCollectionNew = new ArrayList<FolhasDePagamento>();
            for (FolhasDePagamento folhasDePagamentoCollectionNewFolhasDePagamentoToAttach : folhasDePagamentoCollectionNew) {
                folhasDePagamentoCollectionNewFolhasDePagamentoToAttach = em.getReference(folhasDePagamentoCollectionNewFolhasDePagamentoToAttach.getClass(), folhasDePagamentoCollectionNewFolhasDePagamentoToAttach.getId());
                attachedFolhasDePagamentoCollectionNew.add(folhasDePagamentoCollectionNewFolhasDePagamentoToAttach);
            }
            folhasDePagamentoCollectionNew = attachedFolhasDePagamentoCollectionNew;
            funcionarios.setFolhasDePagamentoCollection(folhasDePagamentoCollectionNew);
            funcionarios = em.merge(funcionarios);
            if (empresaIdOld != null && !empresaIdOld.equals(empresaIdNew)) {
                empresaIdOld.getFuncionariosCollection().remove(funcionarios);
                empresaIdOld = em.merge(empresaIdOld);
            }
            if (empresaIdNew != null && !empresaIdNew.equals(empresaIdOld)) {
                empresaIdNew.getFuncionariosCollection().add(funcionarios);
                empresaIdNew = em.merge(empresaIdNew);
            }
            if (estadoIdOld != null && !estadoIdOld.equals(estadoIdNew)) {
                estadoIdOld.getFuncionariosCollection().remove(funcionarios);
                estadoIdOld = em.merge(estadoIdOld);
            }
            if (estadoIdNew != null && !estadoIdNew.equals(estadoIdOld)) {
                estadoIdNew.getFuncionariosCollection().add(funcionarios);
                estadoIdNew = em.merge(estadoIdNew);
            }
            if (gerenteIdOld != null && !gerenteIdOld.equals(gerenteIdNew)) {
                gerenteIdOld.getFuncionariosCollection().remove(funcionarios);
                gerenteIdOld = em.merge(gerenteIdOld);
            }
            if (gerenteIdNew != null && !gerenteIdNew.equals(gerenteIdOld)) {
                gerenteIdNew.getFuncionariosCollection().add(funcionarios);
                gerenteIdNew = em.merge(gerenteIdNew);
            }
            for (Funcionarios funcionariosCollectionOldFuncionarios : funcionariosCollectionOld) {
                if (!funcionariosCollectionNew.contains(funcionariosCollectionOldFuncionarios)) {
                    funcionariosCollectionOldFuncionarios.setGerenteId(null);
                    funcionariosCollectionOldFuncionarios = em.merge(funcionariosCollectionOldFuncionarios);
                }
            }
            for (Funcionarios funcionariosCollectionNewFuncionarios : funcionariosCollectionNew) {
                if (!funcionariosCollectionOld.contains(funcionariosCollectionNewFuncionarios)) {
                    Funcionarios oldGerenteIdOfFuncionariosCollectionNewFuncionarios = funcionariosCollectionNewFuncionarios.getGerenteId();
                    funcionariosCollectionNewFuncionarios.setGerenteId(funcionarios);
                    funcionariosCollectionNewFuncionarios = em.merge(funcionariosCollectionNewFuncionarios);
                    if (oldGerenteIdOfFuncionariosCollectionNewFuncionarios != null && !oldGerenteIdOfFuncionariosCollectionNewFuncionarios.equals(funcionarios)) {
                        oldGerenteIdOfFuncionariosCollectionNewFuncionarios.getFuncionariosCollection().remove(funcionariosCollectionNewFuncionarios);
                        oldGerenteIdOfFuncionariosCollectionNewFuncionarios = em.merge(oldGerenteIdOfFuncionariosCollectionNewFuncionarios);
                    }
                }
            }
            for (FolhasDePagamento folhasDePagamentoCollectionNewFolhasDePagamento : folhasDePagamentoCollectionNew) {
                if (!folhasDePagamentoCollectionOld.contains(folhasDePagamentoCollectionNewFolhasDePagamento)) {
                    Funcionarios oldFuncionariosIdOfFolhasDePagamentoCollectionNewFolhasDePagamento = folhasDePagamentoCollectionNewFolhasDePagamento.getFuncionariosId();
                    folhasDePagamentoCollectionNewFolhasDePagamento.setFuncionariosId(funcionarios);
                    folhasDePagamentoCollectionNewFolhasDePagamento = em.merge(folhasDePagamentoCollectionNewFolhasDePagamento);
                    if (oldFuncionariosIdOfFolhasDePagamentoCollectionNewFolhasDePagamento != null && !oldFuncionariosIdOfFolhasDePagamentoCollectionNewFolhasDePagamento.equals(funcionarios)) {
                        oldFuncionariosIdOfFolhasDePagamentoCollectionNewFolhasDePagamento.getFolhasDePagamentoCollection().remove(folhasDePagamentoCollectionNewFolhasDePagamento);
                        oldFuncionariosIdOfFolhasDePagamentoCollectionNewFolhasDePagamento = em.merge(oldFuncionariosIdOfFolhasDePagamentoCollectionNewFolhasDePagamento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = funcionarios.getId();
                if (findFuncionarios(id) == null) {
                    throw new NonexistentEntityException("The funcionarios with id " + id + " no longer exists.");
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
            Funcionarios funcionarios;
            try {
                funcionarios = em.getReference(Funcionarios.class, id);
                funcionarios.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The funcionarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<FolhasDePagamento> folhasDePagamentoCollectionOrphanCheck = funcionarios.getFolhasDePagamentoCollection();
            for (FolhasDePagamento folhasDePagamentoCollectionOrphanCheckFolhasDePagamento : folhasDePagamentoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Funcionarios (" + funcionarios + ") cannot be destroyed since the FolhasDePagamento " + folhasDePagamentoCollectionOrphanCheckFolhasDePagamento + " in its folhasDePagamentoCollection field has a non-nullable funcionariosId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Empresas empresaId = funcionarios.getEmpresaId();
            if (empresaId != null) {
                empresaId.getFuncionariosCollection().remove(funcionarios);
                empresaId = em.merge(empresaId);
            }
            Estado estadoId = funcionarios.getEstadoId();
            if (estadoId != null) {
                estadoId.getFuncionariosCollection().remove(funcionarios);
                estadoId = em.merge(estadoId);
            }
            Funcionarios gerenteId = funcionarios.getGerenteId();
            if (gerenteId != null) {
                gerenteId.getFuncionariosCollection().remove(funcionarios);
                gerenteId = em.merge(gerenteId);
            }
            Collection<Funcionarios> funcionariosCollection = funcionarios.getFuncionariosCollection();
            for (Funcionarios funcionariosCollectionFuncionarios : funcionariosCollection) {
                funcionariosCollectionFuncionarios.setGerenteId(null);
                funcionariosCollectionFuncionarios = em.merge(funcionariosCollectionFuncionarios);
            }
            em.remove(funcionarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Funcionarios> findFuncionariosEntities() {
        return findFuncionariosEntities(true, -1, -1);
    }

    public List<Funcionarios> findFuncionariosEntities(int maxResults, int firstResult) {
        return findFuncionariosEntities(false, maxResults, firstResult);
    }

    private List<Funcionarios> findFuncionariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Funcionarios.class));
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

    public Funcionarios findFuncionarios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Funcionarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getFuncionariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Funcionarios> rt = cq.from(Funcionarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Funcionarios> findAllFuncionariosNotDesligado() {
        return this.getEntityManager().createNamedQuery("findAllFuncionariosNotDesligado", Funcionarios.class).getResultList();
    }

    public List<Funcionarios> findAllGerentes() {
        return this.getEntityManager().createNamedQuery("findAllGerentes", Funcionarios.class).getResultList();
    }
}
