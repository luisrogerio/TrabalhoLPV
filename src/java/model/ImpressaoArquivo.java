/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
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
public class ImpressaoArquivo implements Impressao {

    @Override
    public void imprimir(Funcionarios funcionario, Date mesAnoDeRefencia, HttpServletRequest request, HttpServletResponse response) {
        FileWriter arq;
        FolhasDePagamento folha = FolhasDePagamentoJpaController.getInstance().findByMesAnoDeReferencia(mesAnoDeRefencia, funcionario.getId());
        HashMap<String, Float> valores = funcionario.getValoresFolha(folha);
        try {

            SimpleDateFormat dateParser = new SimpleDateFormat("MM-yyyy");
            String fname = funcionario.getId() + dateParser.format(mesAnoDeRefencia) + "/folha.txt";
            File file = new File(fname);
            if (file.exists()) {
                arq = new FileWriter(file, true);
            } else {
                file.mkdirs();
                file.createNewFile();
                arq = new FileWriter(file);
            }

            PrintWriter out = new PrintWriter(arq);
            out.println("Empresa " + funcionario.getEmpresaId().getNome());
            out.println("CNPJ: " + funcionario.getEmpresaId().getCnpj() + "\tFolha Mensal: " + dateParser.format(mesAnoDeRefencia));
            out.println("Trabalhador: " + funcionario.getNome()
                    + "\t CPF: " + funcionario.getCpf()
                    + "\t Admissão: " + funcionario.getDataDeAdmissao());
            out.println("Setor: " + funcionario.getCargoId().getSetor()
                    + "\t Cargo: " + funcionario.getCargoId().getNome());
            out.println("Descrição\t|Referência\t|Proventos\t|Descontos");

            out.print("Salário Bruto\t|");
            if (funcionario instanceof FuncionariosHoristas) {
                out.print(funcionario.getHorasTrabalhadas() + " horas\t|");
            }
            if (funcionario instanceof FuncionariosMensalista) {
                out.print("30 dias\t|");
            }
            out.print(valores.get("salarioBruto") + "\t|\n");

            out.println("Horas Extras\t|" + folha.getHorasExtras() + "\t|" + valores.get("valorHorasExtras") + "\t|");

            out.println("INSS\t|" + (funcionario.getCargoId().getDescontoTipo()) * 100 + " %\t|\t|" + valores.get("valorDescontado"));

            out.println("Total de Proventos: " + valores.get("salarioLiquido"));
            out.println("Total de Descontos: " + valores.get("valorDescontado"));

            out.println("Líquido ==>" + valores.get("salarioLiquidoDescontado"));

            Path path = file.toPath();
            OutputStream output = response.getOutputStream();
            Files.copy(path, output);
        } catch (IOException ex) {
            Logger.getLogger(ImpressaoArquivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
