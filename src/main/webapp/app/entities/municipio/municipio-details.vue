<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="municipio">
        <h2 class="jh-entity-heading" data-cy="municipioDetailsHeading">
          <span v-text="$t('sysgipeApp.municipio.detail.title')">Municipio</span> {{ municipio.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('sysgipeApp.municipio.ativo')">Ativo</span>
          </dt>
          <dd>
            <span>{{ municipio.ativo }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.municipio.codigoIbge')">Codigo Ibge</span>
          </dt>
          <dd>
            <span>{{ municipio.codigoIbge }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.municipio.nome')">Nome</span>
          </dt>
          <dd>
            <span>{{ municipio.nome }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.municipio.estado')">Estado</span>
          </dt>
          <dd>
            <div v-if="municipio.estado">
              <router-link :to="{ name: 'EstadoView', params: { estadoId: municipio.estado.id } }">{{ municipio.estado.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="municipio.id"
          :to="{ name: 'MunicipioEdit', params: { municipioId: municipio.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./municipio-details.component.ts"></script>
