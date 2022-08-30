package br.com.sysgipe.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Assunto.
 */
@Entity
@Table(name = "assunto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "assunto")
public class Assunto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "arigo")
    private String arigo;

    @Column(name = "ativo")
    private Boolean ativo;

    @Column(name = "codigo")
    private Integer codigo;

    @Column(name = "data_exclusao")
    private LocalDate dataExclusao;

    @Column(name = "dispositivo")
    private String dispositivo;

    @Column(name = "glossario")
    private String glossario;

    @Column(name = "nome")
    private String nome;

    @Column(name = "observacao")
    private String observacao;

    @Column(name = "pai_id")
    private Integer paiId;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "usuario_exclusao_id")
    private Integer usuarioExclusaoId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Assunto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArigo() {
        return this.arigo;
    }

    public Assunto arigo(String arigo) {
        this.setArigo(arigo);
        return this;
    }

    public void setArigo(String arigo) {
        this.arigo = arigo;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public Assunto ativo(Boolean ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getCodigo() {
        return this.codigo;
    }

    public Assunto codigo(Integer codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public LocalDate getDataExclusao() {
        return this.dataExclusao;
    }

    public Assunto dataExclusao(LocalDate dataExclusao) {
        this.setDataExclusao(dataExclusao);
        return this;
    }

    public void setDataExclusao(LocalDate dataExclusao) {
        this.dataExclusao = dataExclusao;
    }

    public String getDispositivo() {
        return this.dispositivo;
    }

    public Assunto dispositivo(String dispositivo) {
        this.setDispositivo(dispositivo);
        return this;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public String getGlossario() {
        return this.glossario;
    }

    public Assunto glossario(String glossario) {
        this.setGlossario(glossario);
        return this;
    }

    public void setGlossario(String glossario) {
        this.glossario = glossario;
    }

    public String getNome() {
        return this.nome;
    }

    public Assunto nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public Assunto observacao(String observacao) {
        this.setObservacao(observacao);
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Integer getPaiId() {
        return this.paiId;
    }

    public Assunto paiId(Integer paiId) {
        this.setPaiId(paiId);
        return this;
    }

    public void setPaiId(Integer paiId) {
        this.paiId = paiId;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Assunto tipo(String tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getUsuarioExclusaoId() {
        return this.usuarioExclusaoId;
    }

    public Assunto usuarioExclusaoId(Integer usuarioExclusaoId) {
        this.setUsuarioExclusaoId(usuarioExclusaoId);
        return this;
    }

    public void setUsuarioExclusaoId(Integer usuarioExclusaoId) {
        this.usuarioExclusaoId = usuarioExclusaoId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Assunto)) {
            return false;
        }
        return id != null && id.equals(((Assunto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Assunto{" +
            "id=" + getId() +
            ", arigo='" + getArigo() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", codigo=" + getCodigo() +
            ", dataExclusao='" + getDataExclusao() + "'" +
            ", dispositivo='" + getDispositivo() + "'" +
            ", glossario='" + getGlossario() + "'" +
            ", nome='" + getNome() + "'" +
            ", observacao='" + getObservacao() + "'" +
            ", paiId=" + getPaiId() +
            ", tipo='" + getTipo() + "'" +
            ", usuarioExclusaoId=" + getUsuarioExclusaoId() +
            "}";
    }
}
