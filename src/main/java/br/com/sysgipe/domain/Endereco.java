package br.com.sysgipe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Endereco.
 */
@Entity
@Table(name = "endereco")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "endereco")
public class Endereco implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "ativo")
    private Integer ativo;

    @Column(name = "atual")
    private Boolean atual;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "cep")
    private String cep;

    @Column(name = "complemento")
    private String complemento;

    @Column(name = "data_exclusao")
    private LocalDate dataExclusao;

    @Column(name = "usuario_exclusao_id")
    private Integer usuarioExclusaoId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "enderecos", "estado" }, allowSetters = true)
    private Municipio municipio;

    @ManyToMany(mappedBy = "enderecos")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "enderecos" }, allowSetters = true)
    private Set<Pessoa> opessoas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Endereco id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAtivo() {
        return this.ativo;
    }

    public Endereco ativo(Integer ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Integer ativo) {
        this.ativo = ativo;
    }

    public Boolean getAtual() {
        return this.atual;
    }

    public Endereco atual(Boolean atual) {
        this.setAtual(atual);
        return this;
    }

    public void setAtual(Boolean atual) {
        this.atual = atual;
    }

    public String getBairro() {
        return this.bairro;
    }

    public Endereco bairro(String bairro) {
        this.setBairro(bairro);
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return this.cep;
    }

    public Endereco cep(String cep) {
        this.setCep(cep);
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getComplemento() {
        return this.complemento;
    }

    public Endereco complemento(String complemento) {
        this.setComplemento(complemento);
        return this;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public LocalDate getDataExclusao() {
        return this.dataExclusao;
    }

    public Endereco dataExclusao(LocalDate dataExclusao) {
        this.setDataExclusao(dataExclusao);
        return this;
    }

    public void setDataExclusao(LocalDate dataExclusao) {
        this.dataExclusao = dataExclusao;
    }

    public Integer getUsuarioExclusaoId() {
        return this.usuarioExclusaoId;
    }

    public Endereco usuarioExclusaoId(Integer usuarioExclusaoId) {
        this.setUsuarioExclusaoId(usuarioExclusaoId);
        return this;
    }

    public void setUsuarioExclusaoId(Integer usuarioExclusaoId) {
        this.usuarioExclusaoId = usuarioExclusaoId;
    }

    public Municipio getMunicipio() {
        return this.municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Endereco municipio(Municipio municipio) {
        this.setMunicipio(municipio);
        return this;
    }

    public Set<Pessoa> getOpessoas() {
        return this.opessoas;
    }

    public void setOpessoas(Set<Pessoa> pessoas) {
        if (this.opessoas != null) {
            this.opessoas.forEach(i -> i.removeEnderecos(this));
        }
        if (pessoas != null) {
            pessoas.forEach(i -> i.addEnderecos(this));
        }
        this.opessoas = pessoas;
    }

    public Endereco opessoas(Set<Pessoa> pessoas) {
        this.setOpessoas(pessoas);
        return this;
    }

    public Endereco addOpessoas(Pessoa pessoa) {
        this.opessoas.add(pessoa);
        pessoa.getEnderecos().add(this);
        return this;
    }

    public Endereco removeOpessoas(Pessoa pessoa) {
        this.opessoas.remove(pessoa);
        pessoa.getEnderecos().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Endereco)) {
            return false;
        }
        return id != null && id.equals(((Endereco) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Endereco{" +
            "id=" + getId() +
            ", ativo=" + getAtivo() +
            ", atual='" + getAtual() + "'" +
            ", bairro='" + getBairro() + "'" +
            ", cep='" + getCep() + "'" +
            ", complemento='" + getComplemento() + "'" +
            ", dataExclusao='" + getDataExclusao() + "'" +
            ", usuarioExclusaoId=" + getUsuarioExclusaoId() +
            "}";
    }
}
