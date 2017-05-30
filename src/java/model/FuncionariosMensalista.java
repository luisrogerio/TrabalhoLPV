package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("mensalista")
public class FuncionariosMensalista extends Funcionarios {

    /* 
    Método retorna valor constante para funcionários considerados 
    mensalistas de Horas Trabalhadas considerando 20 dias úteis e 
    8 horas por dia    
    */
    @Override
    public Integer getHorasTrabalhadas() {
        return 20 * 8; 
    }

}
