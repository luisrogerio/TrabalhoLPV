package model.state;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import model.Estado;
import model.Funcionarios;
import model.dao.EstadoJpaController;

@Entity
@DiscriminatorValue(value = "Férias")
public class EstadoFerias extends Estado {

    @Override
    public String ativo(Funcionarios funcionario) {
        Estado ativo = EstadoJpaController.getInstance().findByEstado("Férias");
        funcionario.setEstadoId(ativo);
        return ativo.getEstado();
    }

    @Override
    public String desligado(Funcionarios funcionario) {
        return "O funcionário não pode ser desligado";
    }

    @Override
    public String ferias(Funcionarios funcionario) {
        return funcionario.getEstadoId().getMensagem();
    }

    @Override
    public String licenca(Funcionarios funcionario) {
        return "O funcionário não pode entrar de licença";
    }

}
