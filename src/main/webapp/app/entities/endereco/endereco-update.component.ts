import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import MunicipioService from '@/entities/municipio/municipio.service';
import { IMunicipio } from '@/shared/model/municipio.model';

import PessoaService from '@/entities/pessoa/pessoa.service';
import { IPessoa } from '@/shared/model/pessoa.model';

import { IEndereco, Endereco } from '@/shared/model/endereco.model';
import EnderecoService from './endereco.service';

const validations: any = {
  endereco: {
    ativo: {},
    atual: {},
    bairro: {},
    cep: {},
    complemento: {},
    dataExclusao: {},
    usuarioExclusaoId: {},
  },
};

@Component({
  validations,
})
export default class EnderecoUpdate extends Vue {
  @Inject('enderecoService') private enderecoService: () => EnderecoService;
  @Inject('alertService') private alertService: () => AlertService;

  public endereco: IEndereco = new Endereco();

  @Inject('municipioService') private municipioService: () => MunicipioService;

  public municipios: IMunicipio[] = [];

  @Inject('pessoaService') private pessoaService: () => PessoaService;

  public pessoas: IPessoa[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.enderecoId) {
        vm.retrieveEndereco(to.params.enderecoId);
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
  }

  public save(): void {
    this.isSaving = true;
    if (this.endereco.id) {
      this.enderecoService()
        .update(this.endereco)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('sysgipeApp.endereco.updated', { param: param.id });
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
      this.enderecoService()
        .create(this.endereco)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('sysgipeApp.endereco.created', { param: param.id });
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

  public retrieveEndereco(enderecoId): void {
    this.enderecoService()
      .find(enderecoId)
      .then(res => {
        this.endereco = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.municipioService()
      .retrieve()
      .then(res => {
        this.municipios = res.data;
      });
    this.pessoaService()
      .retrieve()
      .then(res => {
        this.pessoas = res.data;
      });
  }
}
