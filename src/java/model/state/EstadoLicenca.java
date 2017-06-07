package model.state;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import model.Estado;
import model.Funcionarios;
import model.dao.EstadoJpaController;
import model.dao.FuncionariosJpaController;
import model.dao.exceptions.NonexistentEntityException;

@Entity
@DiscriminatorValue(value = "Licença")
public class EstadoLicenca extends Estado {

    @Override
    public String ativo(Funcionarios funcionario) {
        funcionario.saveToMemento();
        Estado ativo = EstadoJpaController.getInstance().findByEstado("Ativo");
        funcionario.setEstadoId(ativo);
        try {
            FuncionariosJpaController.getInstance().edit(funcionario);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(EstadoAtivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EstadoAtivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Agora o funcionário está " + ativo.getEstado();
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
