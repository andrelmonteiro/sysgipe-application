<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="assunto">
        <h2 class="jh-entity-heading" data-cy="assuntoDetailsHeading">
          <span v-text="$t('sysgipeApp.assunto.detail.title')">Assunto</span> {{ assunto.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('sysgipeApp.assunto.arigo')">Arigo</span>
          </dt>
          <dd>
            <span>{{ assunto.arigo }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.assunto.ativo')">Ativo</span>
          </dt>
          <dd>
            <span>{{ assunto.ativo }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.assunto.codigo')">Codigo</span>
          </dt>
          <dd>
            <span>{{ assunto.codigo }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.assunto.dataExclusao')">Data Exclusao</span>
          </dt>
          <dd>
            <span>{{ assunto.dataExclusao }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.assunto.dispositivo')">Dispositivo</span>
          </dt>
          <dd>
            <span>{{ assunto.dispositivo }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.assunto.glossario')">Glossario</span>
          </dt>
          <dd>
            <span>{{ assunto.glossario }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.assunto.nome')">Nome</span>
          </dt>
          <dd>
            <span>{{ assunto.nome }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.assunto.observacao')">Observacao</span>
          </dt>
          <dd>
            <span>{{ assunto.observacao }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.assunto.paiId')">Pai Id</span>
          </dt>
          <dd>
            <span>{{ assunto.paiId }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.assunto.tipo')">Tipo</span>
          </dt>
          <dd>
            <span>{{ assunto.tipo }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.assunto.usuarioExclusaoId')">Usuario Exclusao Id</span>
          </dt>
          <dd>
            <span>{{ assunto.usuarioExclusaoId }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="assunto.id" :to="{ name: 'AssuntoEdit', params: { assuntoId: assunto.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./assunto-details.component.ts"></script>
