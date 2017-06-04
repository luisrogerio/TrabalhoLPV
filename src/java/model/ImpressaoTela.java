/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.action.EmpresasController;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.FolhasDePagamentoJpaController;

/**
 *
 * @author luisr
 */
public class ImpressaoTela implements Impressao{

    @Override
    public void imprimir(Funcionarios funcionario, Date mesAnoDeRefencia, HttpServletRequest request, HttpServletResponse response) {
        FolhasDePagamento folha = FolhasDePagamentoJpaController.getInstance().findByMesAnoDeReferencia(mesAnoDeRefencia, funcionario.getId());
        HashMap<String, Float> valores;
        valores = funcionario.getValoresFolha(folha);
        Empresas empresa = (Empresas) request.getAttribute("empresa");

        SimpleDateFormat formato = new SimpleDateFormat("MM/yyyy");
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;

        try {
            out = response.getWriter();
            /* TODO output your page here. You may use following sample code. */
            out.println("<h1>Empresa "+empresa.getNome()+"</h1>");
            out.println("<h1>CNPJ: "+empresa.getCnpj()+"</h1>");
            out.println("<h1>Folha Mensal: "+formato.format(mesAnoDeRefencia)+"</h1>");
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
