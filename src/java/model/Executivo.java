/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author luisr
 */
@Entity
@DiscriminatorValue(value = "Executivo")
public class Executivo extends Cargos{

    public Executivo() {
    }

    public Executivo(String nome, String setor, Float multiplicadorSalario, Float descontosINSS) {
        super(nome, setor, multiplicadorSalario, descontosINSS, "Executivo");
    }
    
    @Override
    public Float getDescontoTipo() {
        return 3 * this.getDescontosINSS();
    }
    
}
