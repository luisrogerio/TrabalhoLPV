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
import model.FuncionariosMensalista;
import model.dao.exceptions.IllegalOrphanException;
import model.dao.exceptions.NonexistentEntityException;

/**
 *
 * @author luisr
 */
public class FuncionariosMensalistaJpaController implements Serializable {

    private static FuncionariosMensalistaJpaController instance = new FuncionariosMensalistaJpaController();
    private EntityManagerFactory emf = null;

    public static FuncionariosMensalistaJpaController getInstance() {
        return instance;
    }

    private FuncionariosMensalistaJpaController() {
    }

    public EntityManager getEntityManager() {
        return PersistenceUtil.getEntityManager();
    }

    public void create(FuncionariosMensalista funcionariosMensalista) {
        if (funcionariosMensalista.getFuncionariosCollection() == null) {
            funcionariosMensalista.setFuncionariosCollection(new ArrayList<Funcionarios>());
        }
        if (funcionariosMensalista.getFolhasDePagamentoCollection() == null) {
            funcionariosMensalista.setFolhasDePagamentoCollection(new ArrayList<FolhasDePagamento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresas empresaId = funcionariosMensalista.getEmpresaId();
            if (empresaId != null) {
                empresaId = em.getReference(empresaId.getClass(), empresaId.getId());
                funcionariosMensalista.setEmpresaId(empresaId);
            }
            Estado estadoId = funcionariosMensalista.getEstadoId();
            if (estadoId != null) {
                estadoId = em.getReference(estadoId.getClass(), estadoId.getId());
                funcionariosMensalista.setEstadoId(estadoId);
            }
            Funcionarios gerenteId = funcionariosMensalista.getGerenteId();
            if (gerenteId != null) {
                gerenteId = em.getReference(gerenteId.getClass(), gerenteId.getId());
                funcionariosMensalista.setGerenteId(gerenteId);
            }
            Collection<Funcionarios> attachedFuncionariosCollection = new ArrayList<Funcionarios>();
            for (Funcionarios funcionariosCollectionFuncionariosToAttach : funcionariosMensalista.getFuncionariosCollection()) {
                funcionariosCollectionFuncionariosToAttach = em.getReference(funcionariosCollectionFuncionariosToAttach.getClass(), funcionariosCollectionFuncionariosToAttach.getId());
                attachedFuncionariosCollection.add(funcionariosCollectionFuncionariosToAttach);
            }
            funcionariosMensalista.setFuncionariosCollection(attachedFuncionariosCollection);
            Collection<FolhasDePagamento> attachedFolhasDePagamentoCollection = new ArrayList<FolhasDePagamento>();
            for (FolhasDePagamento folhasDePagamentoCollectionFolhasDePagamentoToAttach : funcionariosMensalista.getFolhasDePagamentoCollection()) {
                folhasDePagamentoCollectionFolhasDePagamentoToAttach = em.getReference(folhasDePagamentoCollectionFolhasDePagamentoToAttach.getClass(), folhasDePagamentoCollectionFolhasDePagamentoToAttach.getId());
                attachedFolhasDePagamentoCollection.add(folhasDePagamentoCollectionFolhasDePagamentoToAttach);
            }
            funcionariosMensalista.setFolhasDePagamentoCollection(attachedFolhasDePagamentoCollection);
            em.persist(funcionariosMensalista);
            if (empresaId != null) {
                empresaId.getFuncionariosCollection().add(funcionariosMensalista);
                empresaId = em.merge(empresaId);
            }
            if (estadoId != null) {
                estadoId.getFuncionariosCollection().add(funcionariosMensalista);
                estadoId = em.merge(estadoId);
            }
            if (gerenteId != null) {
                gerenteId.getFuncionariosCollection().add(funcionariosMensalista);
                gerenteId = em.merge(gerenteId);
            }
            for (Funcionarios funcionariosCollectionFuncionarios : funcionariosMensalista.getFuncionariosCollection()) {
                model.Funcionarios oldGerenteIdOfFuncionariosCollectionFuncionarios = funcionariosCollectionFuncionarios.getGerenteId();
                funcionariosCollectionFuncionarios.setGerenteId(funcionariosMensalista);
                funcionariosCollectionFuncionarios = em.merge(funcionariosCollectionFuncionarios);
                if (oldGerenteIdOfFuncionariosCollectionFuncionarios != null) {
                    oldGerenteIdOfFuncionariosCollectionFuncionarios.getFuncionariosCollection().remove(funcionariosCollectionFuncionarios);
                    oldGerenteIdOfFuncionariosCollectionFuncionarios = em.merge(oldGerenteIdOfFuncionariosCollectionFuncionarios);
                }
            }
            for (FolhasDePagamento folhasDePagamentoCollectionFolhasDePagamento : funcionariosMensalista.getFolhasDePagamentoCollection()) {
                model.Funcionarios oldFuncionariosIdOfFolhasDePagamentoCollectionFolhasDePagamento = folhasDePagamentoCollectionFolhasDePagamento.getFuncionariosId();
                folhasDePagamentoCollectionFolhasDePagamento.setFuncionariosId(funcionariosMensalista);
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

    public void edit(FuncionariosMensalista funcionariosMensalista) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FuncionariosMensalista persistentFuncionariosMensalista = em.find(FuncionariosMensalista.class, funcionariosMensalista.getId());
            Empresas empresaIdOld = persistentFuncionariosMensalista.getEmpresaId();
            Empresas empresaIdNew = funcionariosMensalista.getEmpresaId();
            Estado estadoIdOld = persistentFuncionariosMensalista.getEstadoId();
            Estado estadoIdNew = funcionariosMensalista.getEstadoId();
            Funcionarios gerenteIdOld = persistentFuncionariosMensalista.getGerenteId();
            Funcionarios gerenteIdNew = funcionariosMensalista.getGerenteId();
            Collection<Funcionarios> funcionariosCollectionOld = persistentFuncionariosMensalista.getFuncionariosCollection();
            Collection<Funcionarios> funcionariosCollectionNew = funcionariosMensalista.getFuncionariosCollection();
            Collection<FolhasDePagamento> folhasDePagamentoCollectionOld = persistentFuncionariosMensalista.getFolhasDePagamentoCollection();
            Collection<FolhasDePagamento> folhasDePagamentoCollectionNew = funcionariosMensalista.getFolhasDePagamentoCollection();
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
                funcionariosMensalista.setEmpresaId(empresaIdNew);
            }
            if (estadoIdNew != null) {
                estadoIdNew = em.getReference(estadoIdNew.getClass(), estadoIdNew.getId());
                funcionariosMensalista.setEstadoId(estadoIdNew);
            }
            if (gerenteIdNew != null) {
                gerenteIdNew = em.getReference(gerenteIdNew.getClass(), gerenteIdNew.getId());
                funcionariosMensalista.setGerenteId(gerenteIdNew);
            }
            Collection<Funcionarios> attachedFuncionariosCollectionNew = new ArrayList<Funcionarios>();
            for (Funcionarios funcionariosCollectionNewFuncionariosToAttach : funcionariosCollectionNew) {
                funcionariosCollectionNewFuncionariosToAttach = em.getReference(funcionariosCollectionNewFuncionariosToAttach.getClass(), funcionariosCollectionNewFuncionariosToAttach.getId());
                attachedFuncionariosCollectionNew.add(funcionariosCollectionNewFuncionariosToAttach);
            }
            funcionariosCollectionNew = attachedFuncionariosCollectionNew;
            funcionariosMensalista.setFuncionariosCollection(funcionariosCollectionNew);
            Collection<FolhasDePagamento> attachedFolhasDePagamentoCollectionNew = new ArrayList<FolhasDePagamento>();
            for (FolhasDePagamento folhasDePagamentoCollectionNewFolhasDePagamentoToAttach : folhasDePagamentoCollectionNew) {
                folhasDePagamentoCollectionNewFolhasDePagamentoToAttach = em.getReference(folhasDePagamentoCollectionNewFolhasDePagamentoToAttach.getClass(), folhasDePagamentoCollectionNewFolhasDePagamentoToAttach.getId());
                attachedFolhasDePagamentoCollectionNew.add(folhasDePagamentoCollectionNewFolhasDePagamentoToAttach);
            }
            folhasDePagamentoCollectionNew = attachedFolhasDePagamentoCollectionNew;
            funcionariosMensalista.setFolhasDePagamentoCollection(folhasDePagamentoCollectionNew);
            funcionariosMensalista = em.merge(funcionariosMensalista);
            if (empresaIdOld != null && !empresaIdOld.equals(empresaIdNew)) {
                empresaIdOld.getFuncionariosCollection().remove(funcionariosMensalista);
                empresaIdOld = em.merge(empresaIdOld);
            }
            if (empresaIdNew != null && !empresaIdNew.equals(empresaIdOld)) {
                empresaIdNew.getFuncionariosCollection().add(funcionariosMensalista);
                empresaIdNew = em.merge(empresaIdNew);
            }
            if (estadoIdOld != null && !estadoIdOld.equals(estadoIdNew)) {
                estadoIdOld.getFuncionariosCollection().remove(funcionariosMensalista);
                estadoIdOld = em.merge(estadoIdOld);
            }
            if (estadoIdNew != null && !estadoIdNew.equals(estadoIdOld)) {
                estadoIdNew.getFuncionariosCollection().add(funcionariosMensalista);
                estadoIdNew = em.merge(estadoIdNew);
            }
            if (gerenteIdOld != null && !gerenteIdOld.equals(gerenteIdNew)) {
                gerenteIdOld.getFuncionariosCollection().remove(funcionariosMensalista);
                gerenteIdOld = em.merge(gerenteIdOld);
            }
            if (gerenteIdNew != null && !gerenteIdNew.equals(gerenteIdOld)) {
                gerenteIdNew.getFuncionariosCollection().add(funcionariosMensalista);
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
                    FuncionariosMensalista oldGerenteIdOfFuncionariosCollectionNewFuncionarios = (FuncionariosMensalista) funcionariosCollectionNewFuncionarios.getGerenteId();
                    funcionariosCollectionNewFuncionarios.setGerenteId(funcionariosMensalista);
                    funcionariosCollectionNewFuncionarios = em.merge(funcionariosCollectionNewFuncionarios);
                    if (oldGerenteIdOfFuncionariosCollectionNewFuncionarios != null && !oldGerenteIdOfFuncionariosCollectionNewFuncionarios.equals(funcionariosMensalista)) {
                        oldGerenteIdOfFuncionariosCollectionNewFuncionarios.getFuncionariosCollection().remove(funcionariosCollectionNewFuncionarios);
                        oldGerenteIdOfFuncionariosCollectionNewFuncionarios = em.merge(oldGerenteIdOfFuncionariosCollectionNewFuncionarios);
                    }
                }
            }
            for (FolhasDePagamento folhasDePagamentoCollectionNewFolhasDePagamento : folhasDePagamentoCollectionNew) {
                if (!folhasDePagamentoCollectionOld.contains(folhasDePagamentoCollectionNewFolhasDePagamento)) {
                    FuncionariosMensalista oldFuncionariosIdOfFolhasDePagamentoCollectionNewFolhasDePagamento = (FuncionariosMensalista) folhasDePagamentoCollectionNewFolhasDePagamento.getFuncionariosId();
                    folhasDePagamentoCollectionNewFolhasDePagamento.setFuncionariosId(funcionariosMensalista);
                    folhasDePagamentoCollectionNewFolhasDePagamento = em.merge(folhasDePagamentoCollectionNewFolhasDePagamento);
                    if (oldFuncionariosIdOfFolhasDePagamentoCollectionNewFolhasDePagamento != null && !oldFuncionariosIdOfFolhasDePagamentoCollectionNewFolhasDePagamento.equals(funcionariosMensalista)) {
                        oldFuncionariosIdOfFolhasDePagamentoCollectionNewFolhasDePagamento.getFolhasDePagamentoCollection().remove(folhasDePagamentoCollectionNewFolhasDePagamento);
                        oldFuncionariosIdOfFolhasDePagamentoCollectionNewFolhasDePagamento = em.merge(oldFuncionariosIdOfFolhasDePagamentoCollectionNewFolhasDePagamento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = funcionariosMensalista.getId();
                if (findFuncionariosMensalista(id) == null) {
                    throw new NonexistentEntityException("The funcionariosMensalista with id " + id + " no longer exists.");
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
            FuncionariosMensalista funcionariosMensalista;
            try {
                funcionariosMensalista = em.getReference(FuncionariosMensalista.class, id);
                funcionariosMensalista.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The funcionariosMensalista with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<FolhasDePagamento> folhasDePagamentoCollectionOrphanCheck = funcionariosMensalista.getFolhasDePagamentoCollection();
            for (FolhasDePagamento folhasDePagamentoCollectionOrphanCheckFolhasDePagamento : folhasDePagamentoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This FuncionariosMensalista (" + funcionariosMensalista + ") cannot be destroyed since the FolhasDePagamento " + folhasDePagamentoCollectionOrphanCheckFolhasDePagamento + " in its folhasDePagamentoCollection field has a non-nullable funcionariosId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Empresas empresaId = funcionariosMensalista.getEmpresaId();
            if (empresaId != null) {
                empresaId.getFuncionariosCollection().remove(funcionariosMensalista);
                empresaId = em.merge(empresaId);
            }
            Estado estadoId = funcionariosMensalista.getEstadoId();
            if (estadoId != null) {
                estadoId.getFuncionariosCollection().remove(funcionariosMensalista);
                estadoId = em.merge(estadoId);
            }
            Funcionarios gerenteId = funcionariosMensalista.getGerenteId();
            if (gerenteId != null) {
                gerenteId.getFuncionariosCollection().remove(funcionariosMensalista);
                gerenteId = em.merge(gerenteId);
            }
            Collection<Funcionarios> funcionariosCollection = funcionariosMensalista.getFuncionariosCollection();
            for (Funcionarios funcionariosCollectionFuncionarios : funcionariosCollection) {
                funcionariosCollectionFuncionarios.setGerenteId(null);
                funcionariosCollectionFuncionarios = em.merge(funcionariosCollectionFuncionarios);
            }
            em.remove(funcionariosMensalista);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FuncionariosMensalista> findFuncionariosMensalistaEntities() {
        return findFuncionariosMensalistaEntities(true, -1, -1);
    }

    public List<FuncionariosMensalista> findFuncionariosMensalistaEntities(int maxResults, int firstResult) {
        return findFuncionariosMensalistaEntities(false, maxResults, firstResult);
    }

    private List<FuncionariosMensalista> findFuncionariosMensalistaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FuncionariosMensalista.class));
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

    public FuncionariosMensalista findFuncionariosMensalista(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FuncionariosMensalista.class, id);
        } finally {
            em.close();
        }
    }

    public int getFuncionariosMensalistaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FuncionariosMensalista> rt = cq.from(FuncionariosMensalista.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
