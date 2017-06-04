package model.state;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import model.Estado;
import model.Funcionarios;
import model.dao.EstadoJpaController;

@Entity
@DiscriminatorValue(value = "Licença")
public class EstadoLicenca extends Estado {

    @Override
    public String ativo(Funcionarios funcionario) {
        Estado ativo = EstadoJpaController.getInstance().findByEstado("Ativo");
        funcionario.setEstadoId(ativo);
        return "Agora o funcionário está: " + ativo.getEstado();
    }

    @Override
    public String desligado(Funcionarios funcionario) {
        return "O funcionário não pode ser desligado";
    }

    @Override
    public String ferias(Funcionarios funcionario) {
        return "O funcionário não pode entrar de férias";
    }

    @Override
    public String licenca(Funcionarios funcionario) {
        return funcionario.getEstadoId().getMensagem();
    }

}
