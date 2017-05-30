package controller.action;

import controller.ActionController;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Empresas;
import model.FolhasDePagamento;
import model.Funcionarios;
import model.FuncionariosHoristas;
import model.FuncionariosMensalista;
import model.Impressao;
import model.dao.EmpresasJpaController;
import model.dao.FolhasDePagamentoJpaController;
import model.dao.exceptions.IllegalOrphanException;
import model.dao.exceptions.NonexistentEntityException;

public class EmpresasController extends ActionController implements Impressao{

    public void index(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Empresas> empresas = null;
        empresas = EmpresasJpaController.getInstance().findEmpresasEntities();
        request.setAttribute("empresas", empresas);
        request.getRequestDispatcher("views/empresas/index.jsp").forward(request, response);
    }

    public void adicionar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("views/empresas/adicionar.jsp").forward(request, response);
    }

    public void salvar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        Empresas empresa = null;

        String nome = request.getParameter("nome");
        String CNPJ = request.getParameter("cnpj");
        
        empresa = new Empresas(nome, CNPJ);

        try {
            EmpresasJpaController.getInstance().create(empresa);
        } catch (Exception ex) {
            Logger.getLogger(EmpresasController.class.getName()).log(Level.SEVERE, null, ex);
        }

        request.getRequestDispatcher("frontController?controller=EmpresasController&method=index").forward(request, response);
    }

    public void deletar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, IllegalOrphanException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        try {
            EmpresasJpaController.getInstance().destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(EmpresasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.getRequestDispatcher("frontController?controller=EmpresasController&method=index").forward(request, response);
    }
    
    public void pegarMesAno(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        Empresas empresa = EmpresasJpaController.getInstance().findEmpresas(id);
        request.setAttribute("empresa", empresa);
        
        request.getRequestDispatcher("views/empresas/formFolhaPagamento.jsp").forward(request, response);
    }
    
    public void gerarFolhasDePagamento(HttpServletRequest request, HttpServletResponse response) {
        request.getParameter("empresaId");
        /* 
         Parte que pega a empresa
         seta no request
         Pega a data também (CADÊ A DATA MANO?)
         */
        Integer id = Integer.parseInt(request.getParameter("id"));
        //Data aqui
        String dataNaoFormatada = request.getParameter("mesAno");
        SimpleDateFormat data = new SimpleDateFormat("mm/yyyy");
        try {
            data.parse(dataNaoFormatada);
        } catch (ParseException ex) {
            Logger.getLogger(EmpresasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Empresas empresa = EmpresasJpaController.getInstance().findEmpresas(id);
        request.setAttribute("empresa", empresa);
        

        /* 
         Parte que imprime os HTML inciais
         */
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Folha de Pagamento - Novembro/2014 - Todos os Funcionários</title>");
            out.println("</head>");
            out.println("<body>");
        } catch (IOException ex) {
            Logger.getLogger(EmpresasController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        /* 
         Itera todos os funcionários da empresa chamando o método imprimir
         */
        
        for (Iterator iterator = empresa.getFuncionariosCollection().iterator(); iterator.hasNext();) {
            Funcionarios funcionario = (Funcionarios) iterator.next();
            imprimir(funcionario, data, request, response);
        }
        
        /* 
         Parte que imprime os HTML finais
         */
        try {
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    @Override
    public void imprimir(Funcionarios funcionario, Date mesAnoDeRefencia, HttpServletRequest request, HttpServletResponse response) {
        FolhasDePagamento folha = FolhasDePagamentoJpaController.getInstance().findByMesAnoDeReferencia(mesAnoDeRefencia, funcionario.getId());
        HashMap<String, Float> valores;
        valores = funcionario.getValoresFolha(folha);
        Empresas empresa = (Empresas) request.getAttribute("empresa");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;

        try {
            out = response.getWriter();
            /* TODO output your page here. You may use following sample code. */
            out.println("<h1>Empresa "+empresa.getNome()+"</h1>");
            out.println("<h1>CNPJ: "+empresa.getCnpj()+"</h1>");
            out.println("<h1>Folha Mensal: "+"Novembro/2014"+"</h1>");
            out.println("<hr />");
            out.println("<h1>Trabalhador: " + funcionario.getNome()
                    + " CPF: " + funcionario.getCpf()
                    + " Admissão: " + funcionario.getDataDeAdmissao()
                    + "</h1>");
            out.println("<hr />");
            out.println("<h1>Setor: " + funcionario.getCargoId().getSetor()
                    + " Cargo: " + funcionario.getCargoId().getNome()
                    + "</h1>");
            out.println("<table>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Descrição</th>");
            out.println("<th>Referência</th>");
            out.println("<th>Proventos</th>");
            out.println("<th>Descontos</th>");
            out.println("</tr>");

            out.println("<tr>");
            out.println("<td>Salário Bruto</td>");
            if (funcionario instanceof FuncionariosHoristas) {
                out.println("<td>" + funcionario.getHorasTrabalhadas() + " horas</td>");
            }
            if (funcionario instanceof FuncionariosMensalista) {
                out.println("<td>30 dias</td>");
            }
            out.println("<td>" + valores.get("salarioBruto") + "</td>");
            out.println("<td></td>");
            out.println("</tr>");

            out.println("<tr>");
            out.println("<td>Horas Extras</td>");
            out.println("<td>" + folha.getHorasExtras() + "</td>");
            out.println("<td>" + valores.get("valorHorasExtras") + "</td>");
            out.println("<td></td>");
            out.println("</tr>");

            out.println("<tr>");
            out.println("<td>INSS</td>");
            out.println("<td>" + (funcionario.getCargoId().getDescontoTipo()) * 100 + " %</td>");
            out.println("<td></td>");
            out.println("<td>" + valores.get("valorDescontado") + "</td>");
            out.println("</tr>");

            out.println("<tr>");
            out.println("<td colspan='2' rowspan='2'></td>");
            out.println("<td>Total de Proventos: " + valores.get("salarioLiquido") + "</td>");
            out.println("<td>Total de Descontos: " + valores.get("valorDescontado") + "</td>");
            out.println("</tr>");

            out.println("<tr>");
            out.println("<td>Líquido ==></td>");
            out.println("<td>" + valores.get("salarioLiquidoDescontado") + "</td>");
            out.println("</tr>");

            out.println("</thead>");
            out.println("</table>");
            out.println("<hr />");

        } catch (IOException ex) {
            Logger.getLogger(EmpresasController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }

    }
}
