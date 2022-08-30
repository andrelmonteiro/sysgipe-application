<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="endereco">
        <h2 class="jh-entity-heading" data-cy="enderecoDetailsHeading">
          <span v-text="$t('sysgipeApp.endereco.detail.title')">Endereco</span> {{ endereco.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('sysgipeApp.endereco.ativo')">Ativo</span>
          </dt>
          <dd>
            <span>{{ endereco.ativo }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.endereco.atual')">Atual</span>
          </dt>
          <dd>
            <span>{{ endereco.atual }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.endereco.bairro')">Bairro</span>
          </dt>
          <dd>
            <span>{{ endereco.bairro }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.endereco.cep')">Cep</span>
          </dt>
          <dd>
            <span>{{ endereco.cep }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.endereco.complemento')">Complemento</span>
          </dt>
          <dd>
            <span>{{ endereco.complemento }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.endereco.dataExclusao')">Data Exclusao</span>
          </dt>
          <dd>
            <span>{{ endereco.dataExclusao }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.endereco.usuarioExclusaoId')">Usuario Exclusao Id</span>
          </dt>
          <dd>
            <span>{{ endereco.usuarioExclusaoId }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.endereco.municipio')">Municipio</span>
          </dt>
          <dd>
            <div v-if="endereco.municipio">
              <router-link :to="{ name: 'MunicipioView', params: { municipioId: endereco.municipio.id } }">{{
                endereco.municipio.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="endereco.id" :to="{ name: 'EnderecoEdit', params: { enderecoId: endereco.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./endereco-details.component.ts"></script>
