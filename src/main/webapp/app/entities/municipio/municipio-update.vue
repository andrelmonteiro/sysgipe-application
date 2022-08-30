<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="sysgipeApp.municipio.home.createOrEditLabel"
          data-cy="MunicipioCreateUpdateHeading"
          v-text="$t('sysgipeApp.municipio.home.createOrEditLabel')"
        >
          Create or edit a Municipio
        </h2>
        <div>
          <div class="form-group" v-if="municipio.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="municipio.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.municipio.ativo')" for="municipio-ativo">Ativo</label>
            <input
              type="number"
              class="form-control"
              name="ativo"
              id="municipio-ativo"
              data-cy="ativo"
              :class="{ valid: !$v.municipio.ativo.$invalid, invalid: $v.municipio.ativo.$invalid }"
              v-model.number="$v.municipio.ativo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.municipio.codigoIbge')" for="municipio-codigoIbge">Codigo Ibge</label>
            <input
              type="number"
              class="form-control"
              name="codigoIbge"
              id="municipio-codigoIbge"
              data-cy="codigoIbge"
              :class="{ valid: !$v.municipio.codigoIbge.$invalid, invalid: $v.municipio.codigoIbge.$invalid }"
              v-model.number="$v.municipio.codigoIbge.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.municipio.nome')" for="municipio-nome">Nome</label>
            <input
              type="text"
              class="form-control"
              name="nome"
              id="municipio-nome"
              data-cy="nome"
              :class="{ valid: !$v.municipio.nome.$invalid, invalid: $v.municipio.nome.$invalid }"
              v-model="$v.municipio.nome.$model"
              required
            />
            <div v-if="$v.municipio.nome.$anyDirty && $v.municipio.nome.$invalid">
              <small class="form-text text-danger" v-if="!$v.municipio.nome.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.municipio.estado')" for="municipio-estado">Estado</label>
            <select class="form-control" id="municipio-estado" data-cy="estado" name="estado" v-model="municipio.estado">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="municipio.estado && estadoOption.id === municipio.estado.id ? municipio.estado : estadoOption"
                v-for="estadoOption in estados"
                :key="estadoOption.id"
              >
                {{ estadoOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.municipio.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./municipio-update.component.ts"></script>
