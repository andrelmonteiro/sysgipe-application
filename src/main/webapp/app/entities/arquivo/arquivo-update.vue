<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="sysgipeApp.arquivo.home.createOrEditLabel"
          data-cy="ArquivoCreateUpdateHeading"
          v-text="$t('sysgipeApp.arquivo.home.createOrEditLabel')"
        >
          Create or edit a Arquivo
        </h2>
        <div>
          <div class="form-group" v-if="arquivo.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="arquivo.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.arquivo.assinado')" for="arquivo-assinado">Assinado</label>
            <input
              type="text"
              class="form-control"
              name="assinado"
              id="arquivo-assinado"
              data-cy="assinado"
              :class="{ valid: !$v.arquivo.assinado.$invalid, invalid: $v.arquivo.assinado.$invalid }"
              v-model="$v.arquivo.assinado.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.arquivo.ativo')" for="arquivo-ativo">Ativo</label>
            <input
              type="number"
              class="form-control"
              name="ativo"
              id="arquivo-ativo"
              data-cy="ativo"
              :class="{ valid: !$v.arquivo.ativo.$invalid, invalid: $v.arquivo.ativo.$invalid }"
              v-model.number="$v.arquivo.ativo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.arquivo.dateCreated')" for="arquivo-dateCreated">Date Created</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="arquivo-dateCreated"
                  v-model="$v.arquivo.dateCreated.$model"
                  name="dateCreated"
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
                id="arquivo-dateCreated"
                data-cy="dateCreated"
                type="text"
                class="form-control"
                name="dateCreated"
                :class="{ valid: !$v.arquivo.dateCreated.$invalid, invalid: $v.arquivo.dateCreated.$invalid }"
                v-model="$v.arquivo.dateCreated.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.arquivo.diretorio')" for="arquivo-diretorio">Diretorio</label>
            <input
              type="text"
              class="form-control"
              name="diretorio"
              id="arquivo-diretorio"
              data-cy="diretorio"
              :class="{ valid: !$v.arquivo.diretorio.$invalid, invalid: $v.arquivo.diretorio.$invalid }"
              v-model="$v.arquivo.diretorio.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.arquivo.documentoId')" for="arquivo-documentoId">Documento Id</label>
            <input
              type="number"
              class="form-control"
              name="documentoId"
              id="arquivo-documentoId"
              data-cy="documentoId"
              :class="{ valid: !$v.arquivo.documentoId.$invalid, invalid: $v.arquivo.documentoId.$invalid }"
              v-model.number="$v.arquivo.documentoId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.arquivo.hash')" for="arquivo-hash">Hash</label>
            <input
              type="text"
              class="form-control"
              name="hash"
              id="arquivo-hash"
              data-cy="hash"
              :class="{ valid: !$v.arquivo.hash.$invalid, invalid: $v.arquivo.hash.$invalid }"
              v-model="$v.arquivo.hash.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.arquivo.hashConteudo')" for="arquivo-hashConteudo"
              >Hash Conteudo</label
            >
            <input
              type="text"
              class="form-control"
              name="hashConteudo"
              id="arquivo-hashConteudo"
              data-cy="hashConteudo"
              :class="{ valid: !$v.arquivo.hashConteudo.$invalid, invalid: $v.arquivo.hashConteudo.$invalid }"
              v-model="$v.arquivo.hashConteudo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.arquivo.historico')" for="arquivo-historico">Historico</label>
            <input
              type="checkbox"
              class="form-check"
              name="historico"
              id="arquivo-historico"
              data-cy="historico"
              :class="{ valid: !$v.arquivo.historico.$invalid, invalid: $v.arquivo.historico.$invalid }"
              v-model="$v.arquivo.historico.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.arquivo.lacunaToken')" for="arquivo-lacunaToken">Lacuna Token</label>
            <input
              type="text"
              class="form-control"
              name="lacunaToken"
              id="arquivo-lacunaToken"
              data-cy="lacunaToken"
              :class="{ valid: !$v.arquivo.lacunaToken.$invalid, invalid: $v.arquivo.lacunaToken.$invalid }"
              v-model="$v.arquivo.lacunaToken.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.arquivo.lastUpdate')" for="arquivo-lastUpdate">Last Update</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="arquivo-lastUpdate"
                  v-model="$v.arquivo.lastUpdate.$model"
                  name="lastUpdate"
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
                id="arquivo-lastUpdate"
                data-cy="lastUpdate"
                type="text"
                class="form-control"
                name="lastUpdate"
                :class="{ valid: !$v.arquivo.lastUpdate.$invalid, invalid: $v.arquivo.lastUpdate.$invalid }"
                v-model="$v.arquivo.lastUpdate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.arquivo.mimeType')" for="arquivo-mimeType">Mime Type</label>
            <input
              type="text"
              class="form-control"
              name="mimeType"
              id="arquivo-mimeType"
              data-cy="mimeType"
              :class="{ valid: !$v.arquivo.mimeType.$invalid, invalid: $v.arquivo.mimeType.$invalid }"
              v-model="$v.arquivo.mimeType.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.arquivo.nome')" for="arquivo-nome">Nome</label>
            <input
              type="text"
              class="form-control"
              name="nome"
              id="arquivo-nome"
              data-cy="nome"
              :class="{ valid: !$v.arquivo.nome.$invalid, invalid: $v.arquivo.nome.$invalid }"
              v-model="$v.arquivo.nome.$model"
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
            :disabled="$v.arquivo.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./arquivo-update.component.ts"></script>
