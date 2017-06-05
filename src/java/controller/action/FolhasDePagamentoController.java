/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.action;

import controller.ActionController;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.FolhasDePagamento;
import model.Funcionarios;
import model.Impressao;
import model.ImpressaoArquivo;
import model.dao.FolhasDePagamentoJpaController;
import model.dao.FuncionariosJpaController;

/**
 *
 * @author luisr
 */
public class FolhasDePagamentoController extends ActionController{

    public void adicionarFolha(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Funcionarios funcionario = FuncionariosJpaController.getInstance().findFuncionarios(Integer.parseInt(request.getParameter("id")));
        request.setAttribute("funcionario", funcionario);
        request.getRequestDispatcher("views/folhasDePagamento/adicionar.jsp").forward(request, response);
    }
    
    public void salvarFolha(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        FolhasDePagamento folha = null;
        
        
        Integer horasExtras = Integer.parseInt(request.getParameter("horas_extras"));
        
        String mes = request.getParameter("mes");
        String ano = request.getParameter("ano");
        SimpleDateFormat dateParser = new SimpleDateFormat("MM/yyyy");
        Date data = null;
        try {
            data = dateParser.parse(mes + "/" + ano);
        } catch (ParseException ex) {
            Logger.getLogger(EmpresasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Integer funcionarioId = Integer.parseInt(request.getParameter("funcionarioId"));
        Funcionarios funcionario = FuncionariosJpaController.getInstance().findFuncionarios(funcionarioId);
        
        folha = new FolhasDePagamento(data, horasExtras, funcionario);
        folha.addObserver(funcionario);
        folha.salvar();
        folha.notifyObservers();

        request.getRequestDispatcher("frontController?controller=FuncionariosController&method=visualizar&id="+funcionarioId).forward(request, response);
    }
    
    public void gerarFolhaIndividual(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        FolhasDePagamento folha = FolhasDePagamentoJpaController.getInstance().findFolhasDePagamento(Integer.parseInt(request.getParameter("id")));
        /* 
        Chamar método imprimir, criar PDF pegando a folha
        Mandar referências do Funcionário
        */
        imprimir(folha, new ImpressaoArquivo(), request, response);
        
        request.getRequestDispatcher("views/folhasDePagamento/adicionar.jsp").forward(request, response);
    }
    
    public void imprimir(FolhasDePagamento folha, Impressao impressao, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        impressao.imprimir(folha.getFuncionariosId(), folha.getMesAnoDeReferencia(), request, response);
    }
    
    
}
