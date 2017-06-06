/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.HashMap;
import java.util.Stack;

/**
 *
 * @author luisr
 */
public class FuncionarioMemento {

    private static Stack<Estado> funcionarioEstado;
    private static FuncionarioMemento instance = new FuncionarioMemento();

    private FuncionarioMemento() {
        
    }

    public static FuncionarioMemento getInstance() {
        return instance;
    }

    public Estado getFuncionarioEstado() {
        return FuncionarioMemento.funcionarioEstado.pop();
    }

    public void setFuncionarioEstado(Estado funcionarioEstado) {
        FuncionarioMemento.funcionarioEstado.push(funcionarioEstado);
    }
    
}
