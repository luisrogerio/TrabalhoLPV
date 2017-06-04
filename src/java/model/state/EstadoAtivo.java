package model.state;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import model.Estado;
import model.Funcionarios;
import model.dao.EstadoJpaController;

@Entity
@DiscriminatorValue(value = "Ativo")
public class EstadoAtivo extends Estado {

    @Override
    public String ativo(Funcionarios funcionario) {
        return funcionario.getEstadoId().getMensagem();
    }

    @Override
    public String desligado(Funcionarios funcionario) {
        Estado desligado = EstadoJpaController.getInstance().findByEstado("Desligado");
        funcionario.setEstadoId(desligado);
        return "Agora o funcionário está: " + desligado.getEstado();
    }

    @Override
    public String ferias(Funcionarios funcionario) {
        Estado ferias = EstadoJpaController.getInstance().findByEstado("Férias");
        funcionario.setEstadoId(ferias);
        return "Agora o funcionário está: " + ferias.getEstado();
    }

    @Override
    public String licenca(Funcionarios funcionario) {
        Estado licensa = EstadoJpaController.getInstance().findByEstado("Licença");
        funcionario.setEstadoId(licensa);
        return "Agora o funcionário está: " + licensa.getEstado();
    }

}
