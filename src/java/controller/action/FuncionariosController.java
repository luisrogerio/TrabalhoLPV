/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.action;

import controller.ActionController;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Cargos;
import model.Estado;
import model.Funcionarios;
import model.FuncionariosMensalista;
import model.dao.CargosJpaController;
import model.dao.EmpresasJpaController;
import model.dao.EstadoJpaController;
import model.dao.FuncionariosJpaController;
import model.dao.exceptions.IllegalOrphanException;
import model.dao.exceptions.NonexistentEntityException;
import model.state.EstadoDesligado;

/**
 *
 * @author luisr
 */
public class FuncionariosController extends ActionController {

    public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Funcionarios> funcionarios;
        funcionarios = FuncionariosJpaController.getInstance().findAllFuncionariosNotDesligado();
        request.setAttribute("funcionarios", funcionarios);
        request.getRequestDispatcher("views/funcionarios/index.jsp").forward(request, response);
    }

    public void adicionar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("acao", "Contratar");
        request.setAttribute("method", "salvar");
        request.setAttribute("cargos", CargosJpaController.getInstance().findCargosEntities());
        request.setAttribute("gerentes", FuncionariosJpaController.getInstance().findFuncionariosEntities());
        request.setAttribute("empresas", EmpresasJpaController.getInstance().findEmpresasEntities());
        request.getRequestDispatcher("views/funcionarios/adicionar.jsp").forward(request, response);
    }

    public void salvar(HttpServletRequest request, HttpServletResponse response) throws ServletException, ClassNotFoundException,
            InstantiationException, IllegalAccessException, ParseException, IOException {
        Funcionarios funcionario;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        funcionario = (Funcionarios) Class.forName("model." + request.getParameter("tipo")).newInstance();
        funcionario.setDataDeAdmissao(formatter.parse(request.getParameter("dataAdmissao")));
        funcionario.setCpf(request.getParameter("cpf"));
        funcionario.setEmail(request.getParameter("email"));
        funcionario.setNome(request.getParameter("nome"));

        String cargoId = Optional.ofNullable(request.getParameter("cargo")).orElse("0");
        String gerenteId = Optional.ofNullable(request.getParameter("gerente")).orElse("0");
        funcionario.setCargoId(CargosJpaController.getInstance().findCargos(Integer.parseInt(cargoId)));
        funcionario.setGerenteId(FuncionariosJpaController.getInstance().findFuncionarios(Integer.parseInt(gerenteId)));

        String estado = request.getParameter("estado");
        estado = new String(estado.getBytes(), "UTF-8");
        funcionario.setEstadoId(EstadoJpaController.getInstance().findByEstado(estado));
        funcionario.setEmpresaId(EmpresasJpaController.getInstance().findEmpresas(Integer.parseInt(request.getParameter("empresa"))));
        FuncionariosJpaController.getInstance().create(funcionario);
        request.getRequestDispatcher("frontController?controller=FuncionariosController&method=index").forward(request, response);
    }

    public void editar(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Funcionarios funcionario;
        request.setAttribute("method", "");
        Integer id = Integer.parseInt(request.getParameter("id"));
        funcionario = FuncionariosJpaController.getInstance().findFuncionarios(id);
        request.setAttribute("funcionario", funcionario);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        request.setAttribute("dataAdimissao", formatter.format(funcionario.getDataDeAdmissao()));
        request.setAttribute("acao", request.getParameter("acao"));
        request.setAttribute("cargos", CargosJpaController.getInstance().findCargosEntities());
        request.setAttribute("gerentes", FuncionariosJpaController.getInstance().findFuncionariosEntities());
        request.setAttribute("empresas", EmpresasJpaController.getInstance().findEmpresasEntities());
        request.getRequestDispatcher("views/funcionarios/adicionar.jsp").forward(request, response);
    }

    public void desligar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, IllegalOrphanException, Exception {
        Integer id = Integer.parseInt(request.getParameter("id"));
        Funcionarios funcionario;
        try {
            funcionario = FuncionariosJpaController.getInstance().findFuncionarios(id);
            funcionario.setEstadoId(new EstadoDesligado());
            FuncionariosJpaController.getInstance().edit(funcionario);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(FuncionariosJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.getRequestDispatcher("frontController?controller=FuncionariosController&method=index").forward(request, response);
    }

}
