package model.state;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import model.Estado;
import model.Funcionarios;

@Entity
@DiscriminatorValue(value = "Desligado")
public class EstadoDesligado extends Estado {

    @Override
    public String ativo(Funcionarios funcionario) {
        return "O funcionário não pode ser ativado";
    }

    @Override
    public String desligado(Funcionarios funcionario) {
        return funcionario.getEstadoId().getEstado();
    }

    @Override
    public String ferias(Funcionarios funcionario) {
        return "O funcionário não pode entrar de férias";
    }

    @Override
    public String licenca(Funcionarios funcionario) {
        return "O funcionário não pode entrar de licença";
    }

}
