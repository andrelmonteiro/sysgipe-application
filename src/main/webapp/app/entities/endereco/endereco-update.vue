<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="sysgipeApp.endereco.home.createOrEditLabel"
          data-cy="EnderecoCreateUpdateHeading"
          v-text="$t('sysgipeApp.endereco.home.createOrEditLabel')"
        >
          Create or edit a Endereco
        </h2>
        <div>
          <div class="form-group" v-if="endereco.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="endereco.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.endereco.ativo')" for="endereco-ativo">Ativo</label>
            <input
              type="number"
              class="form-control"
              name="ativo"
              id="endereco-ativo"
              data-cy="ativo"
              :class="{ valid: !$v.endereco.ativo.$invalid, invalid: $v.endereco.ativo.$invalid }"
              v-model.number="$v.endereco.ativo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.endereco.atual')" for="endereco-atual">Atual</label>
            <input
              type="checkbox"
              class="form-check"
              name="atual"
              id="endereco-atual"
              data-cy="atual"
              :class="{ valid: !$v.endereco.atual.$invalid, invalid: $v.endereco.atual.$invalid }"
              v-model="$v.endereco.atual.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.endereco.bairro')" for="endereco-bairro">Bairro</label>
            <input
              type="text"
              class="form-control"
              name="bairro"
              id="endereco-bairro"
              data-cy="bairro"
              :class="{ valid: !$v.endereco.bairro.$invalid, invalid: $v.endereco.bairro.$invalid }"
              v-model="$v.endereco.bairro.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.endereco.cep')" for="endereco-cep">Cep</label>
            <input
              type="text"
              class="form-control"
              name="cep"
              id="endereco-cep"
              data-cy="cep"
              :class="{ valid: !$v.endereco.cep.$invalid, invalid: $v.endereco.cep.$invalid }"
              v-model="$v.endereco.cep.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.endereco.complemento')" for="endereco-complemento">Complemento</label>
            <input
              type="text"
              class="form-control"
              name="complemento"
              id="endereco-complemento"
              data-cy="complemento"
              :class="{ valid: !$v.endereco.complemento.$invalid, invalid: $v.endereco.complemento.$invalid }"
              v-model="$v.endereco.complemento.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.endereco.dataExclusao')" for="endereco-dataExclusao"
              >Data Exclusao</label
            >
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="endereco-dataExclusao"
                  v-model="$v.endereco.dataExclusao.$model"
                  name="dataExclusao"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="endereco-dataExclusao"
                data-cy="dataExclusao"
                type="text"
                class="form-control"
                name="dataExclusao"
                :class="{ valid: !$v.endereco.dataExclusao.$invalid, invalid: $v.endereco.dataExclusao.$invalid }"
                v-model="$v.endereco.dataExclusao.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.endereco.usuarioExclusaoId')" for="endereco-usuarioExclusaoId"
              >Usuario Exclusao Id</label
            >
            <input
              type="number"
              class="form-control"
              name="usuarioExclusaoId"
              id="endereco-usuarioExclusaoId"
              data-cy="usuarioExclusaoId"
              :class="{ valid: !$v.endereco.usuarioExclusaoId.$invalid, invalid: $v.endereco.usuarioExclusaoId.$invalid }"
              v-model.number="$v.endereco.usuarioExclusaoId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.endereco.municipio')" for="endereco-municipio">Municipio</label>
            <select class="form-control" id="endereco-municipio" data-cy="municipio" name="municipio" v-model="endereco.municipio">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="endereco.municipio && municipioOption.id === endereco.municipio.id ? endereco.municipio : municipioOption"
                v-for="municipioOption in municipios"
                :key="municipioOption.id"
              >
                {{ municipioOption.id }}
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
            :disabled="$v.endereco.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./endereco-update.component.ts"></script>
