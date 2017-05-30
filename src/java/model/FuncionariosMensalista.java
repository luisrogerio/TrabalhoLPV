package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("mensalista")
public class FuncionariosMensalista extends Funcionarios {

}
