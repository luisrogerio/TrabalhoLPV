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
@DiscriminatorValue(value = "Ativo")
public class EstadoAtivo extends Estado {

    @Override
    public String ativo(Funcionarios funcionario) {
        return funcionario.getEstadoId().getMensagem();
    }

    @Override
    public String desligado(Funcionarios funcionario) {
        funcionario.saveToMemento();
        Estado desligado = EstadoJpaController.getInstance().findByEstado("Desligado");
        funcionario.setEstadoId(desligado);
        try {
            FuncionariosJpaController.getInstance().edit(funcionario);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(EstadoAtivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EstadoAtivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Agora o funcionário está " + desligado.getEstado();
    }

    @Override
    public String ferias(Funcionarios funcionario) {
        funcionario.saveToMemento();
        Estado ferias = EstadoJpaController.getInstance().findByEstado("Férias");
        funcionario.setEstadoId(ferias);
        try {
            FuncionariosJpaController.getInstance().edit(funcionario);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(EstadoAtivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EstadoAtivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Agora o funcionário está de " + ferias.getEstado();
    }

    @Override
    public String licenca(Funcionarios funcionario) {
        funcionario.saveToMemento();
        Estado licensa = EstadoJpaController.getInstance().findByEstado("Licença");
        funcionario.setEstadoId(licensa);
        try {
            FuncionariosJpaController.getInstance().edit(funcionario);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(EstadoAtivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EstadoAtivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Agora o funcionário está de " + licensa.getEstado();
    }

}
