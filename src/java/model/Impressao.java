package model;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Impressao {
    public void imprimir(Funcionarios funcionario, Date mesAnoDeRefencia, HttpServletRequest request, HttpServletResponse response);
}
