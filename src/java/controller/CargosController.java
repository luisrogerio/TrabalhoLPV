/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Cargos;
import model.Executivo;
import model.Operacional;
import model.Tatico;
import model.dao.CargosJpaController;
import model.dao.exceptions.NonexistentEntityException;

/**
 *
 * @author luisr
 */
public class CargosController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String metodo = request.getParameter("metodo");
        if (metodo.equals("index")) {
            index(request, response);
        } else if (metodo.equals("adicionarExecutivo")) {
            adicionar(request, response, "Executivo");
        } else if (metodo.equals("adicionarTatico")) {
            adicionar(request, response, "Tatico");
        } else if (metodo.equals("adicionarOperacional")) {
            adicionar(request, response, "Operacional");
        } else if (metodo.equals("salvarCargo")) {
            salvar(request, response);
        } else if (metodo.equals("deletar")) {
            deletar(request, response);
        }
    }

    protected void index(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Cargos> cargos = null;
        cargos = CargosJpaController.getInstance().findCargosEntities();
        request.setAttribute("cargos", cargos);
        request.getRequestDispatcher("views/cargos/index.jsp").forward(request, response);
    }

    protected void adicionar(HttpServletRequest request, HttpServletResponse response, String tipo)
            throws ServletException, IOException {
        request.setAttribute("tipo", tipo);
        request.getRequestDispatcher("views/cargos/adicionar.jsp").forward(request, response);
    }

    protected void salvar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Cargos cargo = null;

        String nome = request.getParameter("nome");
        String setor = request.getParameter("setor");
        Float multiplicadorSalario = Float.parseFloat(request.getParameter("multiplicadorSalario"));
        Float descontoINSS = Float.parseFloat(String.valueOf(request.getParameter("descontoINSS").replace(",", ".")));

        if (request.getParameter("tipo").equals("Executivo")) {
            cargo = new Executivo(nome, setor, multiplicadorSalario, descontoINSS);
        } else if (request.getParameter("tipo").equals("Tatico")) {
            cargo = new Tatico(nome, setor, multiplicadorSalario, descontoINSS);
        } else if (request.getParameter("tipo").equals("Operacional")) {
            cargo = new Operacional(nome, setor, multiplicadorSalario, descontoINSS);
        }

        try {
            CargosJpaController.getInstance().create(cargo);
        } catch (Exception ex) {
            Logger.getLogger(CargosController.class.getName()).log(Level.SEVERE, null, ex);
        }

        request.getRequestDispatcher("CargosController?metodo=index").forward(request, response);
    }

    protected void deletar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        try {
            CargosJpaController.getInstance().destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CargosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.getRequestDispatcher("CargosController?metodo=index").forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
