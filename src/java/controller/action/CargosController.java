/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.action;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Cargos;
import model.Executivo;
import model.Operacional;
import model.Tatico;
import model.dao.CargosJpaController;
import model.dao.exceptions.NonexistentEntityException;
import util.ReflectionMethods;

/**
 *
 * @author luisr
 */
public class CargosController implements ActionController {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
        ReflectionMethods.getMethod(this.getClass(), request.getParameter("method")).invoke(this, request, response);
    }

    protected void index(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Cargos> cargos = null;
        cargos = CargosJpaController.getInstance().findCargosEntities();
        request.setAttribute("cargos", cargos);
        request.getRequestDispatcher("views/cargos/index.jsp").forward(request, response);
    }

    protected void adicionar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String tipo = request.getParameter("tipo");
        request.setAttribute("tipo", tipo);
        request.getRequestDispatcher("views/cargos/adicionar.jsp").forward(request, response);
    }

    protected void salvar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {

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

        request.getRequestDispatcher("frontController?controller=CargosController&method=index").forward(request, response);
    }

    protected void deletar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        try {
            CargosJpaController.getInstance().destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CargosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.getRequestDispatcher("frontController?controller=CargosController&method=index").forward(request, response);
    }

}
