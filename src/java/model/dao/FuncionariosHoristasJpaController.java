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
import model.FuncionariosHoristas;
import model.dao.exceptions.IllegalOrphanException;
import model.dao.exceptions.NonexistentEntityException;

/**
 *
 * @author luisr
 */
public class FuncionariosHoristasJpaController implements Serializable {

     private static FuncionariosHoristasJpaController instance = new FuncionariosHoristasJpaController();
    private EntityManagerFactory emf = null;

    public static FuncionariosHoristasJpaController getInstance() {
        return instance;
    }

    private FuncionariosHoristasJpaController() {
    }

    public EntityManager getEntityManager() {
        return PersistenceUtil.getEntityManager();
    }

    public void create(FuncionariosHoristas funcionariosHoristas) {
        if (funcionariosHoristas.getFuncionariosCollection() == null) {
            funcionariosHoristas.setFuncionariosCollection(new ArrayList<Funcionarios>());
        }
        if (funcionariosHoristas.getFolhasDePagamentoCollection() == null) {
            funcionariosHoristas.setFolhasDePagamentoCollection(new ArrayList<FolhasDePagamento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresas empresaId = funcionariosHoristas.getEmpresaId();
            if (empresaId != null) {
                empresaId = em.getReference(empresaId.getClass(), empresaId.getId());
                funcionariosHoristas.setEmpresaId(empresaId);
            }
            Estado estadoId = funcionariosHoristas.getEstadoId();
            if (estadoId != null) {
                estadoId = em.getReference(estadoId.getClass(), estadoId.getId());
                funcionariosHoristas.setEstadoId(estadoId);
            }
            Funcionarios gerenteId = funcionariosHoristas.getGerenteId();
            if (gerenteId != null) {
                gerenteId = em.getReference(gerenteId.getClass(), gerenteId.getId());
                funcionariosHoristas.setGerenteId(gerenteId);
            }
            Collection<Funcionarios> attachedFuncionariosCollection = new ArrayList<Funcionarios>();
            for (Funcionarios funcionariosCollectionFuncionariosToAttach : funcionariosHoristas.getFuncionariosCollection()) {
                funcionariosCollectionFuncionariosToAttach = em.getReference(funcionariosCollectionFuncionariosToAttach.getClass(), funcionariosCollectionFuncionariosToAttach.getId());
                attachedFuncionariosCollection.add(funcionariosCollectionFuncionariosToAttach);
            }
            funcionariosHoristas.setFuncionariosCollection(attachedFuncionariosCollection);
            Collection<FolhasDePagamento> attachedFolhasDePagamentoCollection = new ArrayList<FolhasDePagamento>();
            for (FolhasDePagamento folhasDePagamentoCollectionFolhasDePagamentoToAttach : funcionariosHoristas.getFolhasDePagamentoCollection()) {
                folhasDePagamentoCollectionFolhasDePagamentoToAttach = em.getReference(folhasDePagamentoCollectionFolhasDePagamentoToAttach.getClass(), folhasDePagamentoCollectionFolhasDePagamentoToAttach.getId());
                attachedFolhasDePagamentoCollection.add(folhasDePagamentoCollectionFolhasDePagamentoToAttach);
            }
            funcionariosHoristas.setFolhasDePagamentoCollection(attachedFolhasDePagamentoCollection);
            em.persist(funcionariosHoristas);
            if (empresaId != null) {
                empresaId.getFuncionariosCollection().add(funcionariosHoristas);
                empresaId = em.merge(empresaId);
            }
            if (estadoId != null) {
                estadoId.getFuncionariosCollection().add(funcionariosHoristas);
                estadoId = em.merge(estadoId);
            }
            if (gerenteId != null) {
                gerenteId.getFuncionariosCollection().add(funcionariosHoristas);
                gerenteId = em.merge(gerenteId);
            }
            for (Funcionarios funcionariosCollectionFuncionarios : funcionariosHoristas.getFuncionariosCollection()) {
                model.Funcionarios oldGerenteIdOfFuncionariosCollectionFuncionarios = funcionariosCollectionFuncionarios.getGerenteId();
                funcionariosCollectionFuncionarios.setGerenteId(funcionariosHoristas);
                funcionariosCollectionFuncionarios = em.merge(funcionariosCollectionFuncionarios);
                if (oldGerenteIdOfFuncionariosCollectionFuncionarios != null) {
                    oldGerenteIdOfFuncionariosCollectionFuncionarios.getFuncionariosCollection().remove(funcionariosCollectionFuncionarios);
                    oldGerenteIdOfFuncionariosCollectionFuncionarios = em.merge(oldGerenteIdOfFuncionariosCollectionFuncionarios);
                }
            }
            for (FolhasDePagamento folhasDePagamentoCollectionFolhasDePagamento : funcionariosHoristas.getFolhasDePagamentoCollection()) {
                model.Funcionarios oldFuncionariosIdOfFolhasDePagamentoCollectionFolhasDePagamento = folhasDePagamentoCollectionFolhasDePagamento.getFuncionariosId();
                folhasDePagamentoCollectionFolhasDePagamento.setFuncionariosId(funcionariosHoristas);
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

    public void edit(FuncionariosHoristas funcionariosHoristas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FuncionariosHoristas persistentFuncionariosHoristas = em.find(FuncionariosHoristas.class, funcionariosHoristas.getId());
            Empresas empresaIdOld = persistentFuncionariosHoristas.getEmpresaId();
            Empresas empresaIdNew = funcionariosHoristas.getEmpresaId();
            Estado estadoIdOld = persistentFuncionariosHoristas.getEstadoId();
            Estado estadoIdNew = funcionariosHoristas.getEstadoId();
            Funcionarios gerenteIdOld = persistentFuncionariosHoristas.getGerenteId();
            Funcionarios gerenteIdNew = funcionariosHoristas.getGerenteId();
            Collection<Funcionarios> funcionariosCollectionOld = persistentFuncionariosHoristas.getFuncionariosCollection();
            Collection<Funcionarios> funcionariosCollectionNew = funcionariosHoristas.getFuncionariosCollection();
            Collection<FolhasDePagamento> folhasDePagamentoCollectionOld = persistentFuncionariosHoristas.getFolhasDePagamentoCollection();
            Collection<FolhasDePagamento> folhasDePagamentoCollectionNew = funcionariosHoristas.getFolhasDePagamentoCollection();
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
                funcionariosHoristas.setEmpresaId(empresaIdNew);
            }
            if (estadoIdNew != null) {
                estadoIdNew = em.getReference(estadoIdNew.getClass(), estadoIdNew.getId());
                funcionariosHoristas.setEstadoId(estadoIdNew);
            }
            if (gerenteIdNew != null) {
                gerenteIdNew = em.getReference(gerenteIdNew.getClass(), gerenteIdNew.getId());
                funcionariosHoristas.setGerenteId(gerenteIdNew);
            }
            Collection<Funcionarios> attachedFuncionariosCollectionNew = new ArrayList<Funcionarios>();
            for (Funcionarios funcionariosCollectionNewFuncionariosToAttach : funcionariosCollectionNew) {
                funcionariosCollectionNewFuncionariosToAttach = em.getReference(funcionariosCollectionNewFuncionariosToAttach.getClass(), funcionariosCollectionNewFuncionariosToAttach.getId());
                attachedFuncionariosCollectionNew.add(funcionariosCollectionNewFuncionariosToAttach);
            }
            funcionariosCollectionNew = attachedFuncionariosCollectionNew;
            funcionariosHoristas.setFuncionariosCollection(funcionariosCollectionNew);
            Collection<FolhasDePagamento> attachedFolhasDePagamentoCollectionNew = new ArrayList<FolhasDePagamento>();
            for (FolhasDePagamento folhasDePagamentoCollectionNewFolhasDePagamentoToAttach : folhasDePagamentoCollectionNew) {
                folhasDePagamentoCollectionNewFolhasDePagamentoToAttach = em.getReference(folhasDePagamentoCollectionNewFolhasDePagamentoToAttach.getClass(), folhasDePagamentoCollectionNewFolhasDePagamentoToAttach.getId());
                attachedFolhasDePagamentoCollectionNew.add(folhasDePagamentoCollectionNewFolhasDePagamentoToAttach);
            }
            folhasDePagamentoCollectionNew = attachedFolhasDePagamentoCollectionNew;
            funcionariosHoristas.setFolhasDePagamentoCollection(folhasDePagamentoCollectionNew);
            funcionariosHoristas = em.merge(funcionariosHoristas);
            if (empresaIdOld != null && !empresaIdOld.equals(empresaIdNew)) {
                empresaIdOld.getFuncionariosCollection().remove(funcionariosHoristas);
                empresaIdOld = em.merge(empresaIdOld);
            }
            if (empresaIdNew != null && !empresaIdNew.equals(empresaIdOld)) {
                empresaIdNew.getFuncionariosCollection().add(funcionariosHoristas);
                empresaIdNew = em.merge(empresaIdNew);
            }
            if (estadoIdOld != null && !estadoIdOld.equals(estadoIdNew)) {
                estadoIdOld.getFuncionariosCollection().remove(funcionariosHoristas);
                estadoIdOld = em.merge(estadoIdOld);
            }
            if (estadoIdNew != null && !estadoIdNew.equals(estadoIdOld)) {
                estadoIdNew.getFuncionariosCollection().add(funcionariosHoristas);
                estadoIdNew = em.merge(estadoIdNew);
            }
            if (gerenteIdOld != null && !gerenteIdOld.equals(gerenteIdNew)) {
                gerenteIdOld.getFuncionariosCollection().remove(funcionariosHoristas);
                gerenteIdOld = em.merge(gerenteIdOld);
            }
            if (gerenteIdNew != null && !gerenteIdNew.equals(gerenteIdOld)) {
                gerenteIdNew.getFuncionariosCollection().add(funcionariosHoristas);
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
                    FuncionariosHoristas oldGerenteIdOfFuncionariosCollectionNewFuncionarios = (FuncionariosHoristas) funcionariosCollectionNewFuncionarios.getGerenteId();
                    funcionariosCollectionNewFuncionarios.setGerenteId(funcionariosHoristas);
                    funcionariosCollectionNewFuncionarios = em.merge(funcionariosCollectionNewFuncionarios);
                    if (oldGerenteIdOfFuncionariosCollectionNewFuncionarios != null && !oldGerenteIdOfFuncionariosCollectionNewFuncionarios.equals(funcionariosHoristas)) {
                        oldGerenteIdOfFuncionariosCollectionNewFuncionarios.getFuncionariosCollection().remove(funcionariosCollectionNewFuncionarios);
                        oldGerenteIdOfFuncionariosCollectionNewFuncionarios = em.merge(oldGerenteIdOfFuncionariosCollectionNewFuncionarios);
                    }
                }
            }
            for (FolhasDePagamento folhasDePagamentoCollectionNewFolhasDePagamento : folhasDePagamentoCollectionNew) {
                if (!folhasDePagamentoCollectionOld.contains(folhasDePagamentoCollectionNewFolhasDePagamento)) {
                    FuncionariosHoristas oldFuncionariosIdOfFolhasDePagamentoCollectionNewFolhasDePagamento = (FuncionariosHoristas) folhasDePagamentoCollectionNewFolhasDePagamento.getFuncionariosId();
                    folhasDePagamentoCollectionNewFolhasDePagamento.setFuncionariosId(funcionariosHoristas);
                    folhasDePagamentoCollectionNewFolhasDePagamento = em.merge(folhasDePagamentoCollectionNewFolhasDePagamento);
                    if (oldFuncionariosIdOfFolhasDePagamentoCollectionNewFolhasDePagamento != null && !oldFuncionariosIdOfFolhasDePagamentoCollectionNewFolhasDePagamento.equals(funcionariosHoristas)) {
                        oldFuncionariosIdOfFolhasDePagamentoCollectionNewFolhasDePagamento.getFolhasDePagamentoCollection().remove(folhasDePagamentoCollectionNewFolhasDePagamento);
                        oldFuncionariosIdOfFolhasDePagamentoCollectionNewFolhasDePagamento = em.merge(oldFuncionariosIdOfFolhasDePagamentoCollectionNewFolhasDePagamento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = funcionariosHoristas.getId();
                if (findFuncionariosHoristas(id) == null) {
                    throw new NonexistentEntityException("The funcionariosHoristas with id " + id + " no longer exists.");
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
            FuncionariosHoristas funcionariosHoristas;
            try {
                funcionariosHoristas = em.getReference(FuncionariosHoristas.class, id);
                funcionariosHoristas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The funcionariosHoristas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<FolhasDePagamento> folhasDePagamentoCollectionOrphanCheck = funcionariosHoristas.getFolhasDePagamentoCollection();
            for (FolhasDePagamento folhasDePagamentoCollectionOrphanCheckFolhasDePagamento : folhasDePagamentoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This FuncionariosHoristas (" + funcionariosHoristas + ") cannot be destroyed since the FolhasDePagamento " + folhasDePagamentoCollectionOrphanCheckFolhasDePagamento + " in its folhasDePagamentoCollection field has a non-nullable funcionariosId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Empresas empresaId = funcionariosHoristas.getEmpresaId();
            if (empresaId != null) {
                empresaId.getFuncionariosCollection().remove(funcionariosHoristas);
                empresaId = em.merge(empresaId);
            }
            Estado estadoId = funcionariosHoristas.getEstadoId();
            if (estadoId != null) {
                estadoId.getFuncionariosCollection().remove(funcionariosHoristas);
                estadoId = em.merge(estadoId);
            }
            Funcionarios gerenteId = funcionariosHoristas.getGerenteId();
            if (gerenteId != null) {
                gerenteId.getFuncionariosCollection().remove(funcionariosHoristas);
                gerenteId = em.merge(gerenteId);
            }
            Collection<Funcionarios> funcionariosCollection = funcionariosHoristas.getFuncionariosCollection();
            for (Funcionarios funcionariosCollectionFuncionarios : funcionariosCollection) {
                funcionariosCollectionFuncionarios.setGerenteId(null);
                funcionariosCollectionFuncionarios = em.merge(funcionariosCollectionFuncionarios);
            }
            em.remove(funcionariosHoristas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FuncionariosHoristas> findFuncionariosHoristasEntities() {
        return findFuncionariosHoristasEntities(true, -1, -1);
    }

    public List<FuncionariosHoristas> findFuncionariosHoristasEntities(int maxResults, int firstResult) {
        return findFuncionariosHoristasEntities(false, maxResults, firstResult);
    }

    private List<FuncionariosHoristas> findFuncionariosHoristasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FuncionariosHoristas.class));
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

    public FuncionariosHoristas findFuncionariosHoristas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FuncionariosHoristas.class, id);
        } finally {
            em.close();
        }
    }

    public int getFuncionariosHoristasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FuncionariosHoristas> rt = cq.from(FuncionariosHoristas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
