<template>
  <div>
    <h2 id="page-heading" data-cy="EnderecoHeading">
      <span v-text="$t('sysgipeApp.endereco.home.title')" id="endereco-heading">Enderecos</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('sysgipeApp.endereco.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'EnderecoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-endereco"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('sysgipeApp.endereco.home.createLabel')"> Create a new Endereco </span>
          </button>
        </router-link>
      </div>
    </h2>
    <div class="row">
      <div class="col-sm-12">
        <form name="searchForm" class="form-inline" v-on:submit.prevent="search(currentSearch)">
          <div class="input-group w-100 mt-3">
            <input
              type="text"
              class="form-control"
              name="currentSearch"
              id="currentSearch"
              v-bind:placeholder="$t('sysgipeApp.endereco.home.search')"
              v-model="currentSearch"
            />
            <button type="button" id="launch-search" class="btn btn-primary" v-on:click="search(currentSearch)">
              <font-awesome-icon icon="search"></font-awesome-icon>
            </button>
            <button type="button" id="clear-search" class="btn btn-secondary" v-on:click="clear()" v-if="currentSearch">
              <font-awesome-icon icon="trash"></font-awesome-icon>
            </button>
          </div>
        </form>
      </div>
    </div>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && enderecos && enderecos.length === 0">
      <span v-text="$t('sysgipeApp.endereco.home.notFound')">No enderecos found</span>
    </div>
    <div class="table-responsive" v-if="enderecos && enderecos.length > 0">
      <table class="table table-striped" aria-describedby="enderecos">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('sysgipeApp.endereco.ativo')">Ativo</span></th>
            <th scope="row"><span v-text="$t('sysgipeApp.endereco.atual')">Atual</span></th>
            <th scope="row"><span v-text="$t('sysgipeApp.endereco.bairro')">Bairro</span></th>
            <th scope="row"><span v-text="$t('sysgipeApp.endereco.cep')">Cep</span></th>
            <th scope="row"><span v-text="$t('sysgipeApp.endereco.complemento')">Complemento</span></th>
            <th scope="row"><span v-text="$t('sysgipeApp.endereco.dataExclusao')">Data Exclusao</span></th>
            <th scope="row"><span v-text="$t('sysgipeApp.endereco.usuarioExclusaoId')">Usuario Exclusao Id</span></th>
            <th scope="row"><span v-text="$t('sysgipeApp.endereco.municipio')">Municipio</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="endereco in enderecos" :key="endereco.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'EnderecoView', params: { enderecoId: endereco.id } }">{{ endereco.id }}</router-link>
            </td>
            <td>{{ endereco.ativo }}</td>
            <td>{{ endereco.atual }}</td>
            <td>{{ endereco.bairro }}</td>
            <td>{{ endereco.cep }}</td>
            <td>{{ endereco.complemento }}</td>
            <td>{{ endereco.dataExclusao }}</td>
            <td>{{ endereco.usuarioExclusaoId }}</td>
            <td>
              <div v-if="endereco.municipio">
                <router-link :to="{ name: 'MunicipioView', params: { municipioId: endereco.municipio.id } }">{{
                  endereco.municipio.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'EnderecoView', params: { enderecoId: endereco.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'EnderecoEdit', params: { enderecoId: endereco.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(endereco)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="sysgipeApp.endereco.delete.question" data-cy="enderecoDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-endereco-heading" v-text="$t('sysgipeApp.endereco.delete.question', { id: removeId })">
          Are you sure you want to delete this Endereco?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-endereco"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeEndereco()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./endereco.component.ts"></script>
