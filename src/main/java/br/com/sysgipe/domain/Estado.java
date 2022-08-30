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
 * A Estado.
 */
@Entity
@Table(name = "estado")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "estado")
public class Estado implements Serializable {

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

    @Column(name = "sigla")
    private String sigla;

    @OneToMany(mappedBy = "estado")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "enderecos", "estado" }, allowSetters = true)
    private Set<Municipio> municipios = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "estados" }, allowSetters = true)
    private Pais pais;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Estado id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAtivo() {
        return this.ativo;
    }

    public Estado ativo(Integer ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Integer ativo) {
        this.ativo = ativo;
    }

    public Integer getCodigoIbge() {
        return this.codigoIbge;
    }

    public Estado codigoIbge(Integer codigoIbge) {
        this.setCodigoIbge(codigoIbge);
        return this;
    }

    public void setCodigoIbge(Integer codigoIbge) {
        this.codigoIbge = codigoIbge;
    }

    public String getNome() {
        return this.nome;
    }

    public Estado nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return this.sigla;
    }

    public Estado sigla(String sigla) {
        this.setSigla(sigla);
        return this;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Set<Municipio> getMunicipios() {
        return this.municipios;
    }

    public void setMunicipios(Set<Municipio> municipios) {
        if (this.municipios != null) {
            this.municipios.forEach(i -> i.setEstado(null));
        }
        if (municipios != null) {
            municipios.forEach(i -> i.setEstado(this));
        }
        this.municipios = municipios;
    }

    public Estado municipios(Set<Municipio> municipios) {
        this.setMunicipios(municipios);
        return this;
    }

    public Estado addMunicipio(Municipio municipio) {
        this.municipios.add(municipio);
        municipio.setEstado(this);
        return this;
    }

    public Estado removeMunicipio(Municipio municipio) {
        this.municipios.remove(municipio);
        municipio.setEstado(null);
        return this;
    }

    public Pais getPais() {
        return this.pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Estado pais(Pais pais) {
        this.setPais(pais);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Estado)) {
            return false;
        }
        return id != null && id.equals(((Estado) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Estado{" +
            "id=" + getId() +
            ", ativo=" + getAtivo() +
            ", codigoIbge=" + getCodigoIbge() +
            ", nome='" + getNome() + "'" +
            ", sigla='" + getSigla() + "'" +
            "}";
    }
}
