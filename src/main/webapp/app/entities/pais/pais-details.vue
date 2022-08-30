<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="pais">
        <h2 class="jh-entity-heading" data-cy="paisDetailsHeading">
          <span v-text="$t('sysgipeApp.pais.detail.title')">Pais</span> {{ pais.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('sysgipeApp.pais.ativo')">Ativo</span>
          </dt>
          <dd>
            <span>{{ pais.ativo }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.pais.nome')">Nome</span>
          </dt>
          <dd>
            <span>{{ pais.nome }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.pais.sigla')">Sigla</span>
          </dt>
          <dd>
            <span>{{ pais.sigla }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="pais.id" :to="{ name: 'PaisEdit', params: { paisId: pais.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./pais-details.component.ts"></script>
