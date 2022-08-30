package br.com.sysgipe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Municipio.
 */
@Entity
@Table(name = "municipio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "municipio")
public class Municipio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "ativo")
    private Integer ativo;

    @Column(name = "codigo_ibge")
    private Integer codigoIbge;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @OneToMany(mappedBy = "municipio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "municipio", "opessoas" }, allowSetters = true)
    private Set<Endereco> enderecos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "municipios", "pais" }, allowSetters = true)
    private Estado estado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Municipio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAtivo() {
        return this.ativo;
    }

    public Municipio ativo(Integer ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Integer ativo) {
        this.ativo = ativo;
    }

    public Integer getCodigoIbge() {
        return this.codigoIbge;
    }

    public Municipio codigoIbge(Integer codigoIbge) {
        this.setCodigoIbge(codigoIbge);
        return this;
    }

    public void setCodigoIbge(Integer codigoIbge) {
        this.codigoIbge = codigoIbge;
    }

    public String getNome() {
        return this.nome;
    }

    public Municipio nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Endereco> getEnderecos() {
        return this.enderecos;
    }

    public void setEnderecos(Set<Endereco> enderecos) {
        if (this.enderecos != null) {
            this.enderecos.forEach(i -> i.setMunicipio(null));
        }
        if (enderecos != null) {
            enderecos.forEach(i -> i.setMunicipio(this));
        }
        this.enderecos = enderecos;
    }

    public Municipio enderecos(Set<Endereco> enderecos) {
        this.setEnderecos(enderecos);
        return this;
    }

    public Municipio addEndereco(Endereco endereco) {
        this.enderecos.add(endereco);
        endereco.setMunicipio(this);
        return this;
    }

    public Municipio removeEndereco(Endereco endereco) {
        this.enderecos.remove(endereco);
        endereco.setMunicipio(null);
        return this;
    }

    public Estado getEstado() {
        return this.estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Municipio estado(Estado estado) {
        this.setEstado(estado);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Municipio)) {
            return false;
        }
        return id != null && id.equals(((Municipio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Municipio{" +
            "id=" + getId() +
            ", ativo=" + getAtivo() +
            ", codigoIbge=" + getCodigoIbge() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
