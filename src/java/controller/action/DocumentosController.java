/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.action;

import com.sun.xml.internal.ws.util.StringUtils;
import controller.ActionController;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Documentos;
import model.Funcionarios;
import model.dao.DocumentosJpaController;
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

    public void deletar(HttpServletRequest request, HttpServletResponse response) throws NonexistentEntityException, ServletException, IOException {
        DocumentosJpaController.getInstance().destroy(Integer.parseInt(request.getParameter("id")));
        request.getRequestDispatcher("frontController?controller=DocumentosController&method=index").forward(request, response);
    }

    public void assinar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mensagem;
        int id = Integer.parseInt(request.getParameter("id"));
        Documentos documento = DocumentosJpaController.getInstance().findDocumentos(id);
        List<Funcionarios> funcionarios = (List<Funcionarios>) documento.getFuncionariosCollection();
        if (!funcionarios.isEmpty()) {
            Funcionarios funcionario = funcionarios.get(0);
            mensagem = "O documento " + StringUtils.capitalize(documento.getNome()) + " foi assinado por " + StringUtils.capitalize(funcionario.getNome()) + " do " + StringUtils.capitalize(funcionario.getCargoId().getNome())+".";
        } else {
            mensagem = "Não existe funcionário responsavel por assinar este documento.";
        }
        request.setAttribute("mensagem", mensagem);
        request.getRequestDispatcher("frontController?controller=DocumentosController&method=index").forward(request, response);
    }
}
