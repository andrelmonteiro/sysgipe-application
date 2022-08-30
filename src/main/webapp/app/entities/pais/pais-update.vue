<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="sysgipeApp.pais.home.createOrEditLabel"
          data-cy="PaisCreateUpdateHeading"
          v-text="$t('sysgipeApp.pais.home.createOrEditLabel')"
        >
          Create or edit a Pais
        </h2>
        <div>
          <div class="form-group" v-if="pais.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="pais.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.pais.ativo')" for="pais-ativo">Ativo</label>
            <input
              type="number"
              class="form-control"
              name="ativo"
              id="pais-ativo"
              data-cy="ativo"
              :class="{ valid: !$v.pais.ativo.$invalid, invalid: $v.pais.ativo.$invalid }"
              v-model.number="$v.pais.ativo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.pais.nome')" for="pais-nome">Nome</label>
            <input
              type="text"
              class="form-control"
              name="nome"
              id="pais-nome"
              data-cy="nome"
              :class="{ valid: !$v.pais.nome.$invalid, invalid: $v.pais.nome.$invalid }"
              v-model="$v.pais.nome.$model"
              required
            />
            <div v-if="$v.pais.nome.$anyDirty && $v.pais.nome.$invalid">
              <small class="form-text text-danger" v-if="!$v.pais.nome.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.pais.sigla')" for="pais-sigla">Sigla</label>
            <input
              type="text"
              class="form-control"
              name="sigla"
              id="pais-sigla"
              data-cy="sigla"
              :class="{ valid: !$v.pais.sigla.$invalid, invalid: $v.pais.sigla.$invalid }"
              v-model="$v.pais.sigla.$model"
            />
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
            :disabled="$v.pais.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./pais-update.component.ts"></script>
