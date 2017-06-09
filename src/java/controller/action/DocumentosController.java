/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.action;

import controller.ActionController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Documentos;
import model.Funcionarios;
import model.dao.DocumentosJpaController;
import model.dao.exceptions.IllegalOrphanException;
import model.dao.exceptions.NonexistentEntityException;

/**
 *
 * @author luisr
 */
public class DocumentosController extends ActionController {

    public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Documentos> listDocumentos = null;
        listDocumentos = DocumentosJpaController.getInstance().findDocumentosEntities();
        request.setAttribute("documentos", listDocumentos);
        request.getRequestDispatcher("views/documentos/index.jsp").forward(request, response);
    }

    public void salvar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Documentos newDocumento = new Documentos();
        newDocumento.setNome(request.getParameter("nome"));
        newDocumento.setDescricao(request.getParameter("descricao"));
        DocumentosJpaController.getInstance().create(newDocumento);
        request.getRequestDispatcher("frontController?controller=DocumentosController&method=index").forward(request, response);
    }

    public void adicionar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("views/documentos/adicionar.jsp").forward(request, response);
    }

    public void deletar(HttpServletRequest request, HttpServletResponse response) throws NonexistentEntityException, ServletException, IOException, IllegalOrphanException {
        DocumentosJpaController.getInstance().destroy(Integer.parseInt(request.getParameter("id")));
        request.getRequestDispatcher("frontController?controller=DocumentosController&method=index").forward(request, response);
    }

    public void assinar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mensagem;
        int id = Integer.parseInt(request.getParameter("id"));
        Documentos documento = DocumentosJpaController.getInstance().findDocumentos(id);
        String retornoEmLista = "<ul>" + documento.getNome();
        retornoEmLista = this.getDocumentosAssociados(documento, retornoEmLista);

        request.setAttribute("docAssociados", retornoEmLista);
        List<Funcionarios> funcionarios = (List<Funcionarios>) documento.getFuncionariosCollection();
        request.setAttribute("funcionariosResponsaveis", funcionarios);
        if (!funcionarios.isEmpty()) {
            mensagem = "O documento " + documento.getNome() + " deve pode ser assinado pelo funcionário: ";
        } else {
            mensagem = "Não existe funcionário responsável por assinar este documento.";
        }
        request.setAttribute("mensagem", mensagem);
        request.getRequestDispatcher("frontController?controller=DocumentosController&method=index").forward(request, response);
    }

    public void callAssociar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Documentos documento = DocumentosJpaController.getInstance().findDocumentos(id);
        request.setAttribute("documento", documento);
        List<Documentos> docs = DocumentosJpaController.getInstance().findAllNotMyself(id);
        request.setAttribute("allDocumentos", docs);
        request.getRequestDispatcher("views/documentos/associarDocumentos.jsp").forward(request, response);
    }

    public void associarDocumento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NonexistentEntityException, Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        Documentos documentoPai = DocumentosJpaController.getInstance().findDocumentos(id);
        String[] idDocumentosFilhos = request.getParameterValues("documentos");

        if (idDocumentosFilhos != null) {
            ArrayList<String> documentosIdFromView = new ArrayList(Arrays.asList(idDocumentosFilhos));
            Documentos docTemp;
            List<Documentos> documentosFilhos = new ArrayList();
            for (String docFilho : documentosIdFromView) {
                id = Integer.parseInt(docFilho);
                docTemp = DocumentosJpaController.getInstance().findDocumentos(id);
                documentosFilhos.add(docTemp);
            }
            documentoPai.setDocumentosCollection(documentosFilhos);
            DocumentosJpaController.getInstance().edit(documentoPai);
            request.getRequestDispatcher("frontController?controller=DocumentosController&method=index").forward(request, response);
        } else {
            request.setAttribute("mensagem", "Marque pelo menos uma opção de documento.");
            request.getRequestDispatcher("frontController?controller=DocumentosController&"
                    + "method=callAssociar&id=" + id).forward(request, response);
        }

    }

    private String getDocumentosAssociados(Documentos documento, String retorno) {
        List<Documentos> documentosAssociados = (List<Documentos>) documento.getDocumentosCollection();
        if (!documentosAssociados.isEmpty()) {
            retorno = retorno + "<ul>";
            for (Documentos docFilho : documentosAssociados) {
                retorno = retorno + "<li>" + docFilho.getNome() + "</li>";
                retorno = getDocumentosAssociados(docFilho, retorno);
            }
            retorno = retorno + "</ul>";
        }
        return retorno;
    }

}
