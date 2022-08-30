import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import EnderecoService from '@/entities/endereco/endereco.service';
import { IEndereco } from '@/shared/model/endereco.model';

import { IPessoa, Pessoa } from '@/shared/model/pessoa.model';
import PessoaService from './pessoa.service';

const validations: any = {
  pessoa: {
    ativo: {},
    autoridade: {},
    contato: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class PessoaUpdate extends Vue {
  @Inject('pessoaService') private pessoaService: () => PessoaService;
  @Inject('alertService') private alertService: () => AlertService;

  public pessoa: IPessoa = new Pessoa();

  @Inject('enderecoService') private enderecoService: () => EnderecoService;

  public enderecos: IEndereco[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.pessoaId) {
        vm.retrievePessoa(to.params.pessoaId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
    this.pessoa.enderecos = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.pessoa.id) {
      this.pessoaService()
        .update(this.pessoa)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('sysgipeApp.pessoa.updated', { param: param.id });
          return (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.pessoaService()
        .create(this.pessoa)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('sysgipeApp.pessoa.created', { param: param.id });
          (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrievePessoa(pessoaId): void {
    this.pessoaService()
      .find(pessoaId)
      .then(res => {
        this.pessoa = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.enderecoService()
      .retrieve()
      .then(res => {
        this.enderecos = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      return selectedVals.find(value => option.id === value.id) ?? option;
    }
    return option;
  }
}
