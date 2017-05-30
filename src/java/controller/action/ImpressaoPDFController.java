package controller.action;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Funcionarios;
import model.Impressao;

public class ImpressaoPDFController implements ActionController, Impressao{

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) {
        // Pega a folha pelo id
        // Passa o Funcionário e mês de referência dessa folha
    }

    @Override
    public void imprimir(Funcionarios funcionario, Date mesAnoDeRefencia, HttpServletRequest request, HttpServletResponse response) {
        // iREPORTSSSSSSSSSSSSSSSSS :D
    }

}
