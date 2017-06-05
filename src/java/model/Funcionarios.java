package model;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import static javax.persistence.DiscriminatorType.STRING;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import model.dao.FuncionariosJpaController;
import model.dao.exceptions.NonexistentEntityException;

@Entity
@Table(name = "funcionarios")
@DiscriminatorColumn(name = "tipo", discriminatorType = STRING, length = 45)
@NamedQueries({
    @NamedQuery(name = "Funcionarios.findAll", query = "SELECT f FROM Funcionarios f"),
    @NamedQuery(name = "findAllFuncionariosNotDesligado", query = "SELECT f FROM Funcionarios f JOIN f.estadoId e WHERE e.estado != 'Desligado'"),
    @NamedQuery(name = "findAllGerentes", query = "SELECT f FROM Funcionarios f JOIN f.gerenteId g WHERE f.id = g.id")
})
public abstract class Funcionarios implements Serializable, Observer {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "cpf")
    private String cpf;
    @Column(name = "email")
    private String email;
    @Column(name = "senha")
    private String senha;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "nova_folha")
    private Integer novaFolha;
    @Column(name = "data_de_admissao")
    @Temporal(TemporalType.DATE)
    private Date dataDeAdmissao;
    @JoinColumn(name = "cargo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cargos cargoId;
    @JoinColumn(name = "empresa_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresaId;
    @JoinColumn(name = "estado_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Estado estadoId;
    @OneToMany(mappedBy = "gerenteId")
    private Collection<Funcionarios> funcionariosCollection;
    @JoinColumn(name = "gerente_id", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Funcionarios gerenteId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionariosId")
    private Collection<FolhasDePagamento> folhasDePagamentoCollection;

    public Funcionarios() {
    }

    public Funcionarios(Integer id) {
        this.id = id;
    }

    public Funcionarios(String tipo) {
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Date getDataDeAdmissao() {
        return dataDeAdmissao;
    }

    public void setDataDeAdmissao(Date dataDeAdmissao) {
        this.dataDeAdmissao = dataDeAdmissao;
    }

    public Cargos getCargoId() {
        return cargoId;
    }

    public void setCargoId(Cargos cargoId) {
        this.cargoId = cargoId;
    }

    public Empresas getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Empresas empresaId) {
        this.empresaId = empresaId;
    }

    public Estado getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Estado estadoId) {
        this.estadoId = estadoId;
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

    public Funcionarios getGerenteId() {
        return gerenteId;
    }

    public void setGerenteId(Funcionarios gerenteId) {
        this.gerenteId = gerenteId;
    }

    public Collection<FolhasDePagamento> getFolhasDePagamentoCollection() {
        return folhasDePagamentoCollection;
    }

    public void setFolhasDePagamentoCollection(Collection<FolhasDePagamento> folhasDePagamentoCollection) {
        this.folhasDePagamentoCollection = folhasDePagamentoCollection;
    }

    public String ativarFuncionario() {
        return this.getEstadoId().ativo(this);
    }

    public String desligarFuncionario() {
        return this.getEstadoId().desligado(this);
    }

    public String licencaFuncionario() {
        return this.getEstadoId().licenca(this);
    }

    public String feriasFuncionario() {
        return this.getEstadoId().ferias(this);
    }

    public HashMap<String, Float> getValoresFolha(FolhasDePagamento folhaCorrente) {
        HashMap<String, Float> valoresFolha = new HashMap<String, Float>();
        Float salarioLiquido, valorDescontado, salarioBruto, valorHorasExtras, salarioLiquidoDescontado;
        salarioBruto = this.getHorasTrabalhadas() * this.cargoId.getMultiplicadorSalario();
        valorDescontado = salarioBruto * this.cargoId.getDescontoTipo();
        valorHorasExtras = (float) this.getHorasTrabalhadas() * folhaCorrente.getHorasExtras();
        salarioLiquido = salarioBruto + valorHorasExtras;
        salarioLiquidoDescontado = salarioLiquido - valorDescontado;
        valoresFolha.put("salarioLiquido", salarioLiquido);
        valoresFolha.put("salarioBruto", salarioBruto);
        valoresFolha.put("valorDescontado", valorDescontado);
        valoresFolha.put("valorHorasExtras", valorHorasExtras);
        valoresFolha.put("salarioLiquidoDescontado", salarioLiquidoDescontado);
        return valoresFolha;
    }

    public void gerarFolhaDePagamento(Impressao impressao) {

    }

    public abstract Integer getHorasTrabalhadas();

    public Method getMethod(String methodName) {
        Method[] methods;
        try {
            methods = Class.forName(this.getClass().getName()).getSuperclass().getDeclaredMethods();
            for (Method currentMethod : methods) {
                if (currentMethod.getName().equals(methodName)) {
                    return currentMethod;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Funcionarios.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
        if (!(object instanceof Funcionarios)) {
            return false;
        }
        Funcionarios other = (Funcionarios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Funcionarios[ id=" + id + " ]";
    }

    @Override
    public void update(Observable o, Object arg) {
        FolhasDePagamento folha = (FolhasDePagamento) o;
        try {
            this.novaFolha = folha.getId();
            FuncionariosJpaController.getInstance().edit(this);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(Funcionarios.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Funcionarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
