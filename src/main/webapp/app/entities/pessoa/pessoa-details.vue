<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="pessoa">
        <h2 class="jh-entity-heading" data-cy="pessoaDetailsHeading">
          <span v-text="$t('sysgipeApp.pessoa.detail.title')">Pessoa</span> {{ pessoa.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('sysgipeApp.pessoa.ativo')">Ativo</span>
          </dt>
          <dd>
            <span>{{ pessoa.ativo }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.pessoa.autoridade')">Autoridade</span>
          </dt>
          <dd>
            <span>{{ pessoa.autoridade }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.pessoa.contato')">Contato</span>
          </dt>
          <dd>
            <span>{{ pessoa.contato }}</span>
          </dd>
          <dt>
            <span v-text="$t('sysgipeApp.pessoa.enderecos')">Enderecos</span>
          </dt>
          <dd>
            <span v-for="(enderecos, i) in pessoa.enderecos" :key="enderecos.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'EnderecoView', params: { enderecoId: enderecos.id } }">{{ enderecos.nome }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="pessoa.id" :to="{ name: 'PessoaEdit', params: { pessoaId: pessoa.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./pessoa-details.component.ts"></script>
