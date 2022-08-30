package br.com.sysgipe.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Arquivo.
 */
@Entity
@Table(name = "arquivo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "arquivo")
public class Arquivo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "assinado")
    private String assinado;

    @Column(name = "ativo")
    private Integer ativo;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    @Column(name = "diretorio")
    private String diretorio;

    @Column(name = "documento_id")
    private Integer documentoId;

    @Column(name = "hash")
    private String hash;

    @Column(name = "hash_conteudo")
    private String hashConteudo;

    @Column(name = "historico")
    private Boolean historico;

    @Column(name = "lacuna_token")
    private String lacunaToken;

    @Column(name = "last_update")
    private LocalDate lastUpdate;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "nome")
    private String nome;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Arquivo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssinado() {
        return this.assinado;
    }

    public Arquivo assinado(String assinado) {
        this.setAssinado(assinado);
        return this;
    }

    public void setAssinado(String assinado) {
        this.assinado = assinado;
    }

    public Integer getAtivo() {
        return this.ativo;
    }

    public Arquivo ativo(Integer ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Integer ativo) {
        this.ativo = ativo;
    }

    public LocalDate getDateCreated() {
        return this.dateCreated;
    }

    public Arquivo dateCreated(LocalDate dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDiretorio() {
        return this.diretorio;
    }

    public Arquivo diretorio(String diretorio) {
        this.setDiretorio(diretorio);
        return this;
    }

    public void setDiretorio(String diretorio) {
        this.diretorio = diretorio;
    }

    public Integer getDocumentoId() {
        return this.documentoId;
    }

    public Arquivo documentoId(Integer documentoId) {
        this.setDocumentoId(documentoId);
        return this;
    }

    public void setDocumentoId(Integer documentoId) {
        this.documentoId = documentoId;
    }

    public String getHash() {
        return this.hash;
    }

    public Arquivo hash(String hash) {
        this.setHash(hash);
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHashConteudo() {
        return this.hashConteudo;
    }

    public Arquivo hashConteudo(String hashConteudo) {
        this.setHashConteudo(hashConteudo);
        return this;
    }

    public void setHashConteudo(String hashConteudo) {
        this.hashConteudo = hashConteudo;
    }

    public Boolean getHistorico() {
        return this.historico;
    }

    public Arquivo historico(Boolean historico) {
        this.setHistorico(historico);
        return this;
    }

    public void setHistorico(Boolean historico) {
        this.historico = historico;
    }

    public String getLacunaToken() {
        return this.lacunaToken;
    }

    public Arquivo lacunaToken(String lacunaToken) {
        this.setLacunaToken(lacunaToken);
        return this;
    }

    public void setLacunaToken(String lacunaToken) {
        this.lacunaToken = lacunaToken;
    }

    public LocalDate getLastUpdate() {
        return this.lastUpdate;
    }

    public Arquivo lastUpdate(LocalDate lastUpdate) {
        this.setLastUpdate(lastUpdate);
        return this;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public Arquivo mimeType(String mimeType) {
        this.setMimeType(mimeType);
        return this;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getNome() {
        return this.nome;
    }

    public Arquivo nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Arquivo)) {
            return false;
        }
        return id != null && id.equals(((Arquivo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Arquivo{" +
            "id=" + getId() +
            ", assinado='" + getAssinado() + "'" +
            ", ativo=" + getAtivo() +
            ", dateCreated='" + getDateCreated() + "'" +
            ", diretorio='" + getDiretorio() + "'" +
            ", documentoId=" + getDocumentoId() +
            ", hash='" + getHash() + "'" +
            ", hashConteudo='" + getHashConteudo() + "'" +
            ", historico='" + getHistorico() + "'" +
            ", lacunaToken='" + getLacunaToken() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", mimeType='" + getMimeType() + "'" +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
