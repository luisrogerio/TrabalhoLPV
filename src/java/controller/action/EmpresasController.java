package controller.action;

import controller.ActionController;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Empresas;
import model.Funcionarios;
import model.Impressao;
import model.ImpressaoTela;
import model.dao.EmpresasJpaController;
import model.dao.exceptions.IllegalOrphanException;
import model.dao.exceptions.NonexistentEntityException;

public class EmpresasController extends ActionController{

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
        String mes = request.getParameter("mes");
        String ano = request.getParameter("ano");
        SimpleDateFormat dateParser = new SimpleDateFormat("MM/yyyy");
        Date data = null;
        try {
            data = dateParser.parse(mes + "/" + ano);
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
            out.println("<title>Folha de Pagamento - "+ dateParser.format(data)+" - Todos os Funcionários</title>");
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
            imprimir(funcionario, data, request, response, new ImpressaoTela());
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

    public void imprimir(Funcionarios funcionario, Date mesAnoDeRefencia, HttpServletRequest request, HttpServletResponse response, Impressao impressao) {
        impressao.imprimir(funcionario, mesAnoDeRefencia, request, response);

    }
}
