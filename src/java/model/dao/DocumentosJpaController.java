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
import model.Documentos;
import model.dao.exceptions.IllegalOrphanException;
import model.dao.exceptions.NonexistentEntityException;

/**
 *
 * @author luisr
 */
public class DocumentosJpaController implements Serializable {

    private static DocumentosJpaController instance = new DocumentosJpaController();
    private EntityManagerFactory emf = null;

    public static DocumentosJpaController getInstance() {
        return instance;
    }

    private DocumentosJpaController() {
    }

    public EntityManager getEntityManager() {
        return PersistenceUtil.getEntityManager();
    }

    public void create(Documentos documentos) {
        if (documentos.getDocumentosCollection() == null) {
            documentos.setDocumentosCollection(new ArrayList<Documentos>());
        }
        if (documentos.getFuncionariosCollection() == null) {
            documentos.setFuncionariosCollection(new ArrayList<Funcionarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documentos documentoId = documentos.getDocumentoId();
            if (documentoId != null) {
                documentoId = em.getReference(documentoId.getClass(), documentoId.getId());
                documentos.setDocumentoId(documentoId);
            }
            Documentos documentosId = documentos.getDocumentoId();
            if (documentosId != null) {
                documentosId = em.getReference(documentosId.getClass(), documentosId.getId());
                documentos.setDocumentoId(documentosId);
            }
            Collection<Documentos> attachedDocumentosCollection = new ArrayList<Documentos>();
            for (Documentos documentosCollectionDocumentosToAttach : documentos.getDocumentosCollection()) {
                documentosCollectionDocumentosToAttach = em.getReference(documentosCollectionDocumentosToAttach.getClass(), documentosCollectionDocumentosToAttach.getId());
                attachedDocumentosCollection.add(documentosCollectionDocumentosToAttach);
            }
            documentos.setDocumentosCollection(attachedDocumentosCollection);
            Collection<Funcionarios> attachedFuncionariosCollection = new ArrayList<Funcionarios>();
            for (Funcionarios funcionariosCollectionFuncionariosToAttach : documentos.getFuncionariosCollection()) {
                funcionariosCollectionFuncionariosToAttach = em.getReference(funcionariosCollectionFuncionariosToAttach.getClass(), funcionariosCollectionFuncionariosToAttach.getId());
                attachedFuncionariosCollection.add(funcionariosCollectionFuncionariosToAttach);
            }
            documentos.setFuncionariosCollection(attachedFuncionariosCollection);
            em.persist(documentos);
            if (documentoId != null) {
                documentoId.getDocumentosCollection().add(documentos);
                documentoId = em.merge(documentoId);
            }
            if (documentosId != null) {
                documentosId.getDocumentosCollection().add(documentos);
                documentosId = em.merge(documentosId);
            }
            for (Documentos documentosCollectionDocumentos : documentos.getDocumentosCollection()) {
                Documentos oldDocumentoIdOfDocumentosCollectionDocumentos = documentosCollectionDocumentos.getDocumentoId();
                documentosCollectionDocumentos.setDocumentoId(documentos);
                documentosCollectionDocumentos = em.merge(documentosCollectionDocumentos);
                if (oldDocumentoIdOfDocumentosCollectionDocumentos != null) {
                    oldDocumentoIdOfDocumentosCollectionDocumentos.getDocumentosCollection().remove(documentosCollectionDocumentos);
                    oldDocumentoIdOfDocumentosCollectionDocumentos = em.merge(oldDocumentoIdOfDocumentosCollectionDocumentos);
                }
            }
            for (Funcionarios funcionariosCollectionFuncionarios : documentos.getFuncionariosCollection()) {
                funcionariosCollectionFuncionarios.getDocumentosCollection().add(documentos);
                funcionariosCollectionFuncionarios = em.merge(funcionariosCollectionFuncionarios);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Documentos documentos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documentos persistentDocumentos = em.find(Documentos.class, documentos.getId());
            Documentos documentoIdOld = persistentDocumentos.getDocumentoId();
            Documentos documentoIdNew = documentos.getDocumentoId();
            Documentos documentosIdOld = persistentDocumentos.getDocumentoId();
            Documentos documentosIdNew = documentos.getDocumentoId();
            Collection<Documentos> documentosCollectionOld = persistentDocumentos.getDocumentosCollection();
            Collection<Documentos> documentosCollectionNew = documentos.getDocumentosCollection();
            Collection<Funcionarios> funcionariosCollectionOld = persistentDocumentos.getFuncionariosCollection();
            Collection<Funcionarios> funcionariosCollectionNew = documentos.getFuncionariosCollection();
            List<String> illegalOrphanMessages = null;
            for (Documentos documentosCollectionOldDocumentos : documentosCollectionOld) {
                if (!documentosCollectionNew.contains(documentosCollectionOldDocumentos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Documentos " + documentosCollectionOldDocumentos + " since its documentoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (documentoIdNew != null) {
                documentoIdNew = em.getReference(documentoIdNew.getClass(), documentoIdNew.getId());
                documentos.setDocumentoId(documentoIdNew);
            }
            if (documentosIdNew != null) {
                documentosIdNew = em.getReference(documentosIdNew.getClass(), documentosIdNew.getId());
                documentos.setDocumentoId(documentosIdNew);
            }
            Collection<Documentos> attachedDocumentosCollectionNew = new ArrayList<Documentos>();
            for (Documentos documentosCollectionNewDocumentosToAttach : documentosCollectionNew) {
                documentosCollectionNewDocumentosToAttach = em.getReference(documentosCollectionNewDocumentosToAttach.getClass(), documentosCollectionNewDocumentosToAttach.getId());
                attachedDocumentosCollectionNew.add(documentosCollectionNewDocumentosToAttach);
            }
            documentosCollectionNew = attachedDocumentosCollectionNew;
            documentos.setDocumentosCollection(documentosCollectionNew);
            Collection<Funcionarios> attachedFuncionariosCollectionNew = new ArrayList<Funcionarios>();
            for (Funcionarios funcionariosCollectionNewFuncionariosToAttach : funcionariosCollectionNew) {
                funcionariosCollectionNewFuncionariosToAttach = em.getReference(funcionariosCollectionNewFuncionariosToAttach.getClass(), funcionariosCollectionNewFuncionariosToAttach.getId());
                attachedFuncionariosCollectionNew.add(funcionariosCollectionNewFuncionariosToAttach);
            }
            funcionariosCollectionNew = attachedFuncionariosCollectionNew;
            documentos.setFuncionariosCollection(funcionariosCollectionNew);
            documentos = em.merge(documentos);
            if (documentoIdOld != null && !documentoIdOld.equals(documentoIdNew)) {
                documentoIdOld.getDocumentosCollection().remove(documentos);
                documentoIdOld = em.merge(documentoIdOld);
            }
            if (documentoIdNew != null && !documentoIdNew.equals(documentoIdOld)) {
                documentoIdNew.getDocumentosCollection().add(documentos);
                documentoIdNew = em.merge(documentoIdNew);
            }
            if (documentosIdOld != null && !documentosIdOld.equals(documentosIdNew)) {
                documentosIdOld.getDocumentosCollection().remove(documentos);
                documentosIdOld = em.merge(documentosIdOld);
            }
            if (documentosIdNew != null && !documentosIdNew.equals(documentosIdOld)) {
                documentosIdNew.getDocumentosCollection().add(documentos);
                documentosIdNew = em.merge(documentosIdNew);
            }
            for (Documentos documentosCollectionNewDocumentos : documentosCollectionNew) {
                if (!documentosCollectionOld.contains(documentosCollectionNewDocumentos)) {
                    Documentos oldDocumentoIdOfDocumentosCollectionNewDocumentos = documentosCollectionNewDocumentos.getDocumentoId();
                    documentosCollectionNewDocumentos.setDocumentoId(documentos);
                    documentosCollectionNewDocumentos = em.merge(documentosCollectionNewDocumentos);
                    if (oldDocumentoIdOfDocumentosCollectionNewDocumentos != null && !oldDocumentoIdOfDocumentosCollectionNewDocumentos.equals(documentos)) {
                        oldDocumentoIdOfDocumentosCollectionNewDocumentos.getDocumentosCollection().remove(documentosCollectionNewDocumentos);
                        oldDocumentoIdOfDocumentosCollectionNewDocumentos = em.merge(oldDocumentoIdOfDocumentosCollectionNewDocumentos);
                    }
                }
            }
            for (Funcionarios funcionariosCollectionOldFuncionarios : funcionariosCollectionOld) {
                if (!funcionariosCollectionNew.contains(funcionariosCollectionOldFuncionarios)) {
                    funcionariosCollectionOldFuncionarios.getDocumentosCollection().remove(documentos);
                    funcionariosCollectionOldFuncionarios = em.merge(funcionariosCollectionOldFuncionarios);
                }
            }
            for (Funcionarios funcionariosCollectionNewFuncionarios : funcionariosCollectionNew) {
                if (!funcionariosCollectionOld.contains(funcionariosCollectionNewFuncionarios)) {
                    funcionariosCollectionNewFuncionarios.getDocumentosCollection().add(documentos);
                    funcionariosCollectionNewFuncionarios = em.merge(funcionariosCollectionNewFuncionarios);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = documentos.getId();
                if (findDocumentos(id) == null) {
                    throw new NonexistentEntityException("The documentos with id " + id + " no longer exists.");
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
            Documentos documentos;
            try {
                documentos = em.getReference(Documentos.class, id);
                documentos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The documentos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Documentos> documentosCollectionOrphanCheck = documentos.getDocumentosCollection();
            for (Documentos documentosCollectionOrphanCheckDocumentos : documentosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Documentos (" + documentos + ") cannot be destroyed since the Documentos " + documentosCollectionOrphanCheckDocumentos + " in its documentosCollection field has a non-nullable documentoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Documentos documentoId = documentos.getDocumentoId();
            if (documentoId != null) {
                documentoId.getDocumentosCollection().remove(documentos);
                documentoId = em.merge(documentoId);
            }
            Documentos documentosId = documentos.getDocumentoId();
            if (documentosId != null) {
                documentosId.getDocumentosCollection().remove(documentos);
                documentosId = em.merge(documentosId);
            }
            Collection<Funcionarios> funcionariosCollection = documentos.getFuncionariosCollection();
            for (Funcionarios funcionariosCollectionFuncionarios : funcionariosCollection) {
                funcionariosCollectionFuncionarios.getDocumentosCollection().remove(documentos);
                funcionariosCollectionFuncionarios = em.merge(funcionariosCollectionFuncionarios);
            }
            em.remove(documentos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Documentos> findDocumentosEntities() {
        return findDocumentosEntities(true, -1, -1);
    }

    public List<Documentos> findDocumentosEntities(int maxResults, int firstResult) {
        return findDocumentosEntities(false, maxResults, firstResult);
    }

    private List<Documentos> findDocumentosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Documentos.class));
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

    public Documentos findDocumentos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Documentos.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocumentosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Documentos> rt = cq.from(Documentos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Documentos> findAllNotMyself(int id) {
        return this.getEntityManager().createNamedQuery("findAllNotMyself", Documentos.class).setParameter("id", id).getResultList();
    }
}
