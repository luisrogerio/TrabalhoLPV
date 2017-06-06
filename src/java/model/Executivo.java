/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author luisr
 */
@Entity
@DiscriminatorValue(value = "Executivo")
public class Executivo extends Cargos{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "setor")
    private String setor;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "multiplicadorSalario")
    private Float multiplicadorSalario;
    @Column(name = "descontosINSS")
    private Float descontosINSS;
    @Column(name = "tipo")
    private String tipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cargoId")
    private Collection<Funcionarios> funcionariosCollection;

    public Executivo() {
    }

    public Executivo(String nome, String setor, Float multiplicadorSalario, Float descontosINSS) {
        super(nome, setor, multiplicadorSalario, descontosINSS, "Executivo");
    }
    
    @Override
    public Float getDescontoTipo() {
        return 3 * this.getDescontosINSS();
    }

    public Executivo(Integer id) {
        this.id = id;
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

    public Collection<Funcionarios> getFuncionariosCollection() {
        return funcionariosCollection;
    }

    public void setFuncionariosCollection(Collection<Funcionarios> funcionariosCollection) {
        this.funcionariosCollection = funcionariosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Executivo)) {
            return false;
        }
        Executivo other = (Executivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Executivo[ id=" + id + " ]";
    }
    
}
