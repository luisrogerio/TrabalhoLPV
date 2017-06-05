/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.action;

import controller.ActionController;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static model.Estado_.mensagem;
import model.FolhasDePagamento;
import model.Funcionarios;
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
        request.setAttribute("acao", request.getParameter("acao"));
        request.setAttribute("method", "salvar");
        request.setAttribute("cargos", CargosJpaController.getInstance().findCargosEntities());
        request.setAttribute("gerentes", FuncionariosJpaController.getInstance().findFuncionariosEntities());
        request.setAttribute("empresas", EmpresasJpaController.getInstance().findEmpresasEntities());
        if (request.getParameter("acao").equals("Alterar")) {
            Funcionarios funcionario;
            Integer id = Integer.parseInt(request.getParameter("id"));
            funcionario = FuncionariosJpaController.getInstance().findFuncionarios(id);
            request.setAttribute("funcionario", funcionario);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            request.setAttribute("dataAdimissao", formatter.format(funcionario.getDataDeAdmissao()));
            request.setAttribute("method", "editar");
        }
        request.getRequestDispatcher("views/funcionarios/adicionar.jsp").forward(request, response);
    }

    public void salvar(HttpServletRequest request, HttpServletResponse response) throws ServletException, ClassNotFoundException,
            InstantiationException, IllegalAccessException, ParseException, IOException {

        FuncionariosJpaController.getInstance().create(this.getFuncionarioFromView(request, response));
        request.getRequestDispatcher("frontController?controller=FuncionariosController&method=index").forward(request, response);
    }

    public void editar(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ClassNotFoundException, ParseException, InstantiationException, IllegalAccessException, NonexistentEntityException, Exception {
        Funcionarios funcionario;
        Integer id = Integer.parseInt(request.getParameter("id"));
        funcionario = this.getFuncionarioFromView(request, response);
        funcionario.setId(id);
        if (funcionario.getGerenteId() == null) {
            funcionario.setFuncionariosCollection(new ArrayList<Funcionarios>());
        }
        if (funcionario.getFolhasDePagamentoCollection() == null) {
            funcionario.setFolhasDePagamentoCollection(new ArrayList<FolhasDePagamento>());
        }
        FuncionariosJpaController.getInstance().edit(funcionario);
        request.getRequestDispatcher("frontController?controller=FuncionariosController&method=index").forward(request, response);
    }

    private Funcionarios getFuncionarioFromView(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ClassNotFoundException, ParseException, InstantiationException, IllegalAccessException {
        Funcionarios funcionario;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        funcionario = (Funcionarios) Class.forName("model." + request.getParameter("tipo")).newInstance();
        funcionario.setDataDeAdmissao(formatter.parse(request.getParameter("dataAdmissao")));
        funcionario.setCpf(request.getParameter("cpf"));
        funcionario.setEmail(request.getParameter("email"));
        funcionario.setNome(request.getParameter("nome"));
       // funcionario.set

        String cargoId = Optional.ofNullable(request.getParameter("cargo")).orElse("0");
        String gerenteId = Optional.ofNullable(request.getParameter("gerente")).orElse("0");
        funcionario.setCargoId(CargosJpaController.getInstance().findCargos(Integer.parseInt(cargoId)));
        funcionario.setGerenteId(FuncionariosJpaController.getInstance().findFuncionarios(Integer.parseInt(gerenteId)));

        String estado = request.getParameter("estado");
        estado = new String(estado.getBytes(), "UTF-8");
        funcionario.setEstadoId(EstadoJpaController.getInstance().findByEstado(estado));
        funcionario.setEmpresaId(EmpresasJpaController.getInstance().findEmpresas(Integer.parseInt(request.getParameter("empresa"))));
        return funcionario;
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

    public void visualizar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Funcionarios funcionario;
        Integer id = Integer.parseInt(request.getParameter("id"));
        funcionario = FuncionariosJpaController.getInstance().findFuncionarios(id);
        request.setAttribute("funcionario", funcionario);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        request.setAttribute("dataAdimissao", formatter.format(funcionario.getDataDeAdmissao()));
        request.getRequestDispatcher("views/funcionarios/opcoes.jsp").forward(request, response);
    }

    public void alterarEstado(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ServletException, IOException {
        Funcionarios funcionario;
        Integer id = Integer.parseInt(request.getParameter("id"));
        funcionario = FuncionariosJpaController.getInstance().findFuncionarios(id);
        String estado = request.getParameter("novoEstado");
        estado = estado.concat("Funcionario");
        Method metodo = funcionario.getMethod(estado);
        Parameter[] parametros = metodo.getParameters();
        String mensagem = (String) metodo.invoke(funcionario, parametros);
        request.setAttribute("mensagem", mensagem);
        request.setAttribute("funcionario", funcionario);
        request.getRequestDispatcher("views/funcionarios/opcoes.jsp").forward(request, response);
    }
}
