<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="sysgipeApp.assunto.home.createOrEditLabel"
          data-cy="AssuntoCreateUpdateHeading"
          v-text="$t('sysgipeApp.assunto.home.createOrEditLabel')"
        >
          Create or edit a Assunto
        </h2>
        <div>
          <div class="form-group" v-if="assunto.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="assunto.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.assunto.arigo')" for="assunto-arigo">Arigo</label>
            <input
              type="text"
              class="form-control"
              name="arigo"
              id="assunto-arigo"
              data-cy="arigo"
              :class="{ valid: !$v.assunto.arigo.$invalid, invalid: $v.assunto.arigo.$invalid }"
              v-model="$v.assunto.arigo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.assunto.ativo')" for="assunto-ativo">Ativo</label>
            <input
              type="checkbox"
              class="form-check"
              name="ativo"
              id="assunto-ativo"
              data-cy="ativo"
              :class="{ valid: !$v.assunto.ativo.$invalid, invalid: $v.assunto.ativo.$invalid }"
              v-model="$v.assunto.ativo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.assunto.codigo')" for="assunto-codigo">Codigo</label>
            <input
              type="number"
              class="form-control"
              name="codigo"
              id="assunto-codigo"
              data-cy="codigo"
              :class="{ valid: !$v.assunto.codigo.$invalid, invalid: $v.assunto.codigo.$invalid }"
              v-model.number="$v.assunto.codigo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.assunto.dataExclusao')" for="assunto-dataExclusao"
              >Data Exclusao</label
            >
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="assunto-dataExclusao"
                  v-model="$v.assunto.dataExclusao.$model"
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
                id="assunto-dataExclusao"
                data-cy="dataExclusao"
                type="text"
                class="form-control"
                name="dataExclusao"
                :class="{ valid: !$v.assunto.dataExclusao.$invalid, invalid: $v.assunto.dataExclusao.$invalid }"
                v-model="$v.assunto.dataExclusao.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.assunto.dispositivo')" for="assunto-dispositivo">Dispositivo</label>
            <input
              type="text"
              class="form-control"
              name="dispositivo"
              id="assunto-dispositivo"
              data-cy="dispositivo"
              :class="{ valid: !$v.assunto.dispositivo.$invalid, invalid: $v.assunto.dispositivo.$invalid }"
              v-model="$v.assunto.dispositivo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.assunto.glossario')" for="assunto-glossario">Glossario</label>
            <input
              type="text"
              class="form-control"
              name="glossario"
              id="assunto-glossario"
              data-cy="glossario"
              :class="{ valid: !$v.assunto.glossario.$invalid, invalid: $v.assunto.glossario.$invalid }"
              v-model="$v.assunto.glossario.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.assunto.nome')" for="assunto-nome">Nome</label>
            <input
              type="text"
              class="form-control"
              name="nome"
              id="assunto-nome"
              data-cy="nome"
              :class="{ valid: !$v.assunto.nome.$invalid, invalid: $v.assunto.nome.$invalid }"
              v-model="$v.assunto.nome.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.assunto.observacao')" for="assunto-observacao">Observacao</label>
            <input
              type="text"
              class="form-control"
              name="observacao"
              id="assunto-observacao"
              data-cy="observacao"
              :class="{ valid: !$v.assunto.observacao.$invalid, invalid: $v.assunto.observacao.$invalid }"
              v-model="$v.assunto.observacao.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.assunto.paiId')" for="assunto-paiId">Pai Id</label>
            <input
              type="number"
              class="form-control"
              name="paiId"
              id="assunto-paiId"
              data-cy="paiId"
              :class="{ valid: !$v.assunto.paiId.$invalid, invalid: $v.assunto.paiId.$invalid }"
              v-model.number="$v.assunto.paiId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.assunto.tipo')" for="assunto-tipo">Tipo</label>
            <input
              type="text"
              class="form-control"
              name="tipo"
              id="assunto-tipo"
              data-cy="tipo"
              :class="{ valid: !$v.assunto.tipo.$invalid, invalid: $v.assunto.tipo.$invalid }"
              v-model="$v.assunto.tipo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.assunto.usuarioExclusaoId')" for="assunto-usuarioExclusaoId"
              >Usuario Exclusao Id</label
            >
            <input
              type="number"
              class="form-control"
              name="usuarioExclusaoId"
              id="assunto-usuarioExclusaoId"
              data-cy="usuarioExclusaoId"
              :class="{ valid: !$v.assunto.usuarioExclusaoId.$invalid, invalid: $v.assunto.usuarioExclusaoId.$invalid }"
              v-model.number="$v.assunto.usuarioExclusaoId.$model"
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
            :disabled="$v.assunto.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./assunto-update.component.ts"></script>
