package controller.action;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Funcionarios;
import model.Impressao;

public class ImpressaoPDFController implements Command, Impressao {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
        // Pega a folha pelo id
        // Passa o Funcionário e mês de referência dessa folha
    }

    @Override
    public void imprimir(Funcionarios funcionario, Date mesAnoDeRefencia, HttpServletRequest request, HttpServletResponse response) {
        // iREPORTSSSSSSSSSSSSSSSSS :D
        //NÃÃÃÃÃÃÃÃÃÃOOOOO!!!!
    }
}
