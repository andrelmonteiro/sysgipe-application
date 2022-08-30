<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="sysgipeApp.pessoa.home.createOrEditLabel"
          data-cy="PessoaCreateUpdateHeading"
          v-text="$t('sysgipeApp.pessoa.home.createOrEditLabel')"
        >
          Create or edit a Pessoa
        </h2>
        <div>
          <div class="form-group" v-if="pessoa.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="pessoa.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.pessoa.ativo')" for="pessoa-ativo">Ativo</label>
            <input
              type="number"
              class="form-control"
              name="ativo"
              id="pessoa-ativo"
              data-cy="ativo"
              :class="{ valid: !$v.pessoa.ativo.$invalid, invalid: $v.pessoa.ativo.$invalid }"
              v-model.number="$v.pessoa.ativo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.pessoa.autoridade')" for="pessoa-autoridade">Autoridade</label>
            <input
              type="checkbox"
              class="form-check"
              name="autoridade"
              id="pessoa-autoridade"
              data-cy="autoridade"
              :class="{ valid: !$v.pessoa.autoridade.$invalid, invalid: $v.pessoa.autoridade.$invalid }"
              v-model="$v.pessoa.autoridade.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('sysgipeApp.pessoa.contato')" for="pessoa-contato">Contato</label>
            <input
              type="text"
              class="form-control"
              name="contato"
              id="pessoa-contato"
              data-cy="contato"
              :class="{ valid: !$v.pessoa.contato.$invalid, invalid: $v.pessoa.contato.$invalid }"
              v-model="$v.pessoa.contato.$model"
              required
            />
            <div v-if="$v.pessoa.contato.$anyDirty && $v.pessoa.contato.$invalid">
              <small class="form-text text-danger" v-if="!$v.pessoa.contato.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label v-text="$t('sysgipeApp.pessoa.enderecos')" for="pessoa-enderecos">Enderecos</label>
            <select
              class="form-control"
              id="pessoa-enderecos"
              data-cy="enderecos"
              multiple
              name="enderecos"
              v-if="pessoa.enderecos !== undefined"
              v-model="pessoa.enderecos"
            >
              <option
                v-bind:value="getSelected(pessoa.enderecos, enderecoOption)"
                v-for="enderecoOption in enderecos"
                :key="enderecoOption.id"
              >
                {{ enderecoOption.nome }}
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
            :disabled="$v.pessoa.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./pessoa-update.component.ts"></script>
