package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("horista")
public class FuncionariosHoristas extends Funcionarios{

}
