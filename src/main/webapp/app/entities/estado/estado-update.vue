<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="sysgipeApp.estado.home.createOrEditLabel"
          data-cy="EstadoCreateUpdateHeading"
          v-text="$t('sysgipeApp.estado.home.createOrEditLabel')"
        >
          Create or edit a Estado
        </h2>
        <div>
          <div class="form-group" v-if="estado.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="estado.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.estado.ativo')" for="estado-ativo">Ativo</label>
            <input
              type="number"
              class="form-control"
              name="ativo"
              id="estado-ativo"
              data-cy="ativo"
              :class="{ valid: !$v.estado.ativo.$invalid, invalid: $v.estado.ativo.$invalid }"
              v-model.number="$v.estado.ativo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.estado.codigoIbge')" for="estado-codigoIbge">Codigo Ibge</label>
            <input
              type="number"
              class="form-control"
              name="codigoIbge"
              id="estado-codigoIbge"
              data-cy="codigoIbge"
              :class="{ valid: !$v.estado.codigoIbge.$invalid, invalid: $v.estado.codigoIbge.$invalid }"
              v-model.number="$v.estado.codigoIbge.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.estado.nome')" for="estado-nome">Nome</label>
            <input
              type="text"
              class="form-control"
              name="nome"
              id="estado-nome"
              data-cy="nome"
              :class="{ valid: !$v.estado.nome.$invalid, invalid: $v.estado.nome.$invalid }"
              v-model="$v.estado.nome.$model"
              required
            />
            <div v-if="$v.estado.nome.$anyDirty && $v.estado.nome.$invalid">
              <small class="form-text text-danger" v-if="!$v.estado.nome.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.estado.sigla')" for="estado-sigla">Sigla</label>
            <input
              type="text"
              class="form-control"
              name="sigla"
              id="estado-sigla"
              data-cy="sigla"
              :class="{ valid: !$v.estado.sigla.$invalid, invalid: $v.estado.sigla.$invalid }"
              v-model="$v.estado.sigla.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.estado.pais')" for="estado-pais">Pais</label>
            <select class="form-control" id="estado-pais" data-cy="pais" name="pais" v-model="estado.pais">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="estado.pais && paisOption.id === estado.pais.id ? estado.pais : paisOption"
                v-for="paisOption in pais"
                :key="paisOption.id"
              >
                {{ paisOption.nome }}
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
            :disabled="$v.estado.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./estado-update.component.ts"></script>
