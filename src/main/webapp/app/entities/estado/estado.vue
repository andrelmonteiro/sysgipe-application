<template>
  <div>
    <h2 id="page-heading" data-cy="EstadoHeading">
      <span v-text="$t('sysgipeApp.estado.home.title')" id="estado-heading">Estados</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('sysgipeApp.estado.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'EstadoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-estado"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('sysgipeApp.estado.home.createLabel')"> Create a new Estado </span>
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
              v-bind:placeholder="$t('sysgipeApp.estado.home.search')"
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
    <div class="alert alert-warning" v-if="!isFetching && estados && estados.length === 0">
      <span v-text="$t('sysgipeApp.estado.home.notFound')">No estados found</span>
    </div>
    <div class="table-responsive" v-if="estados && estados.length > 0">
      <table class="table table-striped" aria-describedby="estados">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('sysgipeApp.estado.ativo')">Ativo</span></th>
            <th scope="row"><span v-text="$t('sysgipeApp.estado.codigoIbge')">Codigo Ibge</span></th>
            <th scope="row"><span v-text="$t('sysgipeApp.estado.nome')">Nome</span></th>
            <th scope="row"><span v-text="$t('sysgipeApp.estado.sigla')">Sigla</span></th>
            <th scope="row"><span v-text="$t('sysgipeApp.estado.pais')">Pais</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="estado in estados" :key="estado.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'EstadoView', params: { estadoId: estado.id } }">{{ estado.id }}</router-link>
            </td>
            <td>{{ estado.ativo }}</td>
            <td>{{ estado.codigoIbge }}</td>
            <td>{{ estado.nome }}</td>
            <td>{{ estado.sigla }}</td>
            <td>
              <div v-if="estado.pais">
                <router-link :to="{ name: 'PaisView', params: { paisId: estado.pais.id } }">{{ estado.pais.nome }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'EstadoView', params: { estadoId: estado.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'EstadoEdit', params: { estadoId: estado.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(estado)"
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
        ><span id="sysgipeApp.estado.delete.question" data-cy="estadoDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-estado-heading" v-text="$t('sysgipeApp.estado.delete.question', { id: removeId })">
          Are you sure you want to delete this Estado?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-estado"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeEstado()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./estado.component.ts"></script>
