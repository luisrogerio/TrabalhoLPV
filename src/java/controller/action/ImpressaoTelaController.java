package controller.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.FolhasDePagamento;
import model.Funcionarios;
import model.FuncionariosHoristas;
import model.FuncionariosMensalista;
import model.Impressao;
import model.dao.FolhasDePagamentoJpaController;

public class ImpressaoTelaController implements ActionController, Impressao {

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        request.getParameter("empresaId");
        /* 
         Parte que pega a empresa
         seta no response
         */

        /* 
         Parte que imprime os HTML inciais
         */
        /* 
         Itera todos os funcionários da empresa chamando o método imprimir
         */
        /* 
         Parte que imprime os HTML finais
         */
    }

    @Override
    public void imprimir(Funcionarios funcionario, Date mesAnoDeRefencia, HttpServletRequest request, HttpServletResponse response) {
        FolhasDePagamento folha = FolhasDePagamentoJpaController.getInstance().findByMesAnoDeReferencia(mesAnoDeRefencia, funcionario.getId());
        HashMap<String, Float> valores;
        valores = funcionario.getValoresFolha(folha);

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;

        try {
            out = response.getWriter();
            /* TODO output your page here. You may use following sample code. */
            out.println("<h1>Empresa Nome</h1>");
            out.println("<h1>CNPJ: 1324.2132.1213.21.31</h1>");
            out.println("<h1>Folha Mensal: Novembro/2014</h1>");
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
            Logger.getLogger(ImpressaoTelaController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }

    }

}
