package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "cargos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
@NamedQueries({
    @NamedQuery(name = "Cargos.findAll", query = "SELECT c FROM Cargos c")})
public abstract class Cargos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "setor")
    private String setor;
    @Column(name = "multiplicadorSalario")
    private Float multiplicadorSalario;
    @Column(name = "descontosINSS")
    private Float descontosINSS;
    @Column(name = "tipo")
    private String tipo;

    public Cargos() {
    }

    public Cargos(Integer id) {
        this.id = id;
    }

    public Cargos(String nome, String setor, Float multiplicadorSalario, Float descontosINSS, String tipo) {
        this.nome = nome;
        this.setor = setor;
        this.multiplicadorSalario = multiplicadorSalario;
        this.descontosINSS = descontosINSS;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public Float getMultiplicadorSalario() {
        return multiplicadorSalario;
    }

    public void setMultiplicadorSalario(Float multiplicadorSalario) {
        this.multiplicadorSalario = multiplicadorSalario;
    }

    public Float getDescontosINSS() {
        return descontosINSS;
    }

    public void setDescontosINSS(Float descontosINSS) {
        this.descontosINSS = descontosINSS;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public abstract Float getDescontoTipo();

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cargos)) {
            return false;
        }
        Cargos other = (Cargos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Cargos[ id=" + id + " ]";
    }

}
