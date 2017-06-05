package model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("horista")
public class FuncionariosHoristas extends Funcionarios {

    @Column(name = "horasTrabalhadas")
    private Integer horasTrabalhadas;

    public FuncionariosHoristas() {
        super("horista");
    }

    @Override
    public Integer getHorasTrabalhadas() {
        return horasTrabalhadas;
    }

    public void setHorasTrabalhadas(int horasTrabalhadas) {
        this.horasTrabalhadas = horasTrabalhadas;
    }

}
