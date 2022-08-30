<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="arquivo">
        <h2 class="jh-entity-heading" data-cy="arquivoDetailsHeading">
          <span v-text="$t('sysgipeApp.arquivo.detail.title')">Arquivo</span> {{ arquivo.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('sysgipeApp.arquivo.assinado')">Assinado</span>
          </dt>
          <dd>
            <span>{{ arquivo.assinado }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.arquivo.ativo')">Ativo</span>
          </dt>
          <dd>
            <span>{{ arquivo.ativo }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.arquivo.dateCreated')">Date Created</span>
          </dt>
          <dd>
            <span>{{ arquivo.dateCreated }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.arquivo.diretorio')">Diretorio</span>
          </dt>
          <dd>
            <span>{{ arquivo.diretorio }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.arquivo.documentoId')">Documento Id</span>
          </dt>
          <dd>
            <span>{{ arquivo.documentoId }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.arquivo.hash')">Hash</span>
          </dt>
          <dd>
            <span>{{ arquivo.hash }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.arquivo.hashConteudo')">Hash Conteudo</span>
          </dt>
          <dd>
            <span>{{ arquivo.hashConteudo }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.arquivo.historico')">Historico</span>
          </dt>
          <dd>
            <span>{{ arquivo.historico }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.arquivo.lacunaToken')">Lacuna Token</span>
          </dt>
          <dd>
            <span>{{ arquivo.lacunaToken }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.arquivo.lastUpdate')">Last Update</span>
          </dt>
          <dd>
            <span>{{ arquivo.lastUpdate }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.arquivo.mimeType')">Mime Type</span>
          </dt>
          <dd>
            <span>{{ arquivo.mimeType }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.arquivo.nome')">Nome</span>
          </dt>
          <dd>
            <span>{{ arquivo.nome }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="arquivo.id" :to="{ name: 'ArquivoEdit', params: { arquivoId: arquivo.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./arquivo-details.component.ts"></script>
