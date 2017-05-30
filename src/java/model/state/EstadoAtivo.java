package model.state;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import model.Estado;
import model.Funcionarios;
import model.dao.EstadoDesligadoJpaController;
import model.dao.EstadoFeriasJpaController;
import model.dao.EstadoLicencaJpaController;


@Entity
@DiscriminatorValue(value = "Ativo")
public class EstadoAtivo extends Estado {

    @Override
    public String ativo(Funcionarios funcionario) {
        return funcionario.getEstadoId().getEstado();
    }

    @Override
    public String desligado(Funcionarios funcionario) {
        Estado desligado = EstadoDesligadoJpaController.getInstance().findByEstado("Desligado");
        funcionario.setEstadoId(desligado);
        return desligado.getEstado();
    }

    @Override
    public String ferias(Funcionarios funcionario) {
        Estado ferias = EstadoFeriasJpaController.getInstance().findByEstado("Férias");
        funcionario.setEstadoId(ferias);
        return ferias.getEstado();
    }

    @Override
    public String licenca(Funcionarios funcionario) {
        Estado licensa = EstadoLicencaJpaController.getInstance().findByEstado("Licença");
        funcionario.setEstadoId(licensa);
        return licensa.getEstado();
    }

}
