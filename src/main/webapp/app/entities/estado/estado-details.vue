<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="estado">
        <h2 class="jh-entity-heading" data-cy="estadoDetailsHeading">
          <span v-text="$t('sysgipeApp.estado.detail.title')">Estado</span> {{ estado.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('sysgipeApp.estado.ativo')">Ativo</span>
          </dt>
          <dd>
            <span>{{ estado.ativo }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.estado.codigoIbge')">Codigo Ibge</span>
          </dt>
          <dd>
            <span>{{ estado.codigoIbge }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.estado.nome')">Nome</span>
          </dt>
          <dd>
            <span>{{ estado.nome }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.estado.sigla')">Sigla</span>
          </dt>
          <dd>
            <span>{{ estado.sigla }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.estado.pais')">Pais</span>
          </dt>
          <dd>
            <div v-if="estado.pais">
              <router-link :to="{ name: 'PaisView', params: { paisId: estado.pais.id } }">{{ estado.pais.nome }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="estado.id" :to="{ name: 'EstadoEdit', params: { estadoId: estado.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./estado-details.component.ts"></script>
