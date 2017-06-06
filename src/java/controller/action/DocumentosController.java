/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.action;

import controller.ActionController;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Documentos;
import model.dao.DocumentosJpaController;

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

}
