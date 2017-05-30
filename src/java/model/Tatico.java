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
@DiscriminatorValue(value = "Tatico")
public class Tatico extends Cargos{

    public Tatico() {
    }

    public Tatico(String nome, String setor, Float multiplicadorSalario, Float descontosINSS) {
        super(nome, setor, multiplicadorSalario, descontosINSS, "Tatico");
    }

    @Override
    public Float getDescontoTipo() {
        return 2 * this.getDescontosINSS();
    }
    
}
