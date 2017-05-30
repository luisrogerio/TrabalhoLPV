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
import model.Funcionarios;
import model.dao.FuncionariosJpaController;

/**
 *
 * @author luisr
 */
public class FuncionariosController extends ActionController {

    public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Funcionarios> funcionarios = null;
        funcionarios = FuncionariosJpaController.getInstance().findAllFuncionariosNotDesligado();
        request.setAttribute("funcionarios", funcionarios);
        request.getRequestDispatcher("views/funcionarios/index.jsp").forward(request, response);
    }
}
