package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Observable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import model.dao.FolhasDePagamentoJpaController;

@Entity
@Table(name = "folhas_de_pagamento")
@NamedQueries({
    @NamedQuery(name = "FolhasDePagamento.findAll", query = "SELECT f FROM FolhasDePagamento f"),
    @NamedQuery(name = "findByMesAnoDeReferencia", query = "SELECT f FROM FolhasDePagamento f WHERE f.mesAnoDeReferencia = :mesAnoDeReferencia AND f.funcionariosId.id = :funcionariosId")
})
public class FolhasDePagamento extends Observable implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "mes_ano_de_referencia")
    @Temporal(TemporalType.DATE)
    private Date mesAnoDeReferencia;
    @Column(name = "horas_extras")
    private Integer horasExtras;
    @JoinColumn(name = "funcionarios_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Funcionarios funcionariosId;

    public FolhasDePagamento() {
    }

    public FolhasDePagamento(Integer id) {
        this.id = id;
    }

    public FolhasDePagamento(Date mesAnoDeReferencia, Integer horasExtras, Funcionarios funcionariosId) {
        this.mesAnoDeReferencia = mesAnoDeReferencia;
        this.horasExtras = horasExtras;
        this.funcionariosId = funcionariosId;
    }
    
    public void salvar(){
        setChanged();
        FolhasDePagamentoJpaController.getInstance().create(this);
    }
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getMesAnoDeReferencia() {
        return mesAnoDeReferencia;
    }

    public void setMesAnoDeReferencia(Date mesAnoDeReferencia) {
        this.mesAnoDeReferencia = mesAnoDeReferencia;
    }

    public Integer getHorasExtras() {
        return horasExtras;
    }

    public void setHorasExtras(Integer horasExtras) {
        this.horasExtras = horasExtras;
    }

    public Funcionarios getFuncionariosId() {
        return funcionariosId;
    }

    public void setFuncionariosId(Funcionarios funcionariosId) {
        this.funcionariosId = funcionariosId;
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
        if (!(object instanceof FolhasDePagamento)) {
            return false;
        }
        FolhasDePagamento other = (FolhasDePagamento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.FolhasDePagamento[ id=" + id + " ]";
    }

}
