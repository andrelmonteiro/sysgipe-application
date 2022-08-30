import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import EnderecoService from '@/entities/endereco/endereco.service';
import { IEndereco } from '@/shared/model/endereco.model';

import EstadoService from '@/entities/estado/estado.service';
import { IEstado } from '@/shared/model/estado.model';

import { IMunicipio, Municipio } from '@/shared/model/municipio.model';
import MunicipioService from './municipio.service';

const validations: any = {
  municipio: {
    ativo: {},
    codigoIbge: {},
    nome: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class MunicipioUpdate extends Vue {
  @Inject('municipioService') private municipioService: () => MunicipioService;
  @Inject('alertService') private alertService: () => AlertService;

  public municipio: IMunicipio = new Municipio();

  @Inject('enderecoService') private enderecoService: () => EnderecoService;

  public enderecos: IEndereco[] = [];

  @Inject('estadoService') private estadoService: () => EstadoService;

  public estados: IEstado[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.municipioId) {
        vm.retrieveMunicipio(to.params.municipioId);
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
    if (this.municipio.id) {
      this.municipioService()
        .update(this.municipio)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('sysgipeApp.municipio.updated', { param: param.id });
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
      this.municipioService()
        .create(this.municipio)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('sysgipeApp.municipio.created', { param: param.id });
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

  public retrieveMunicipio(municipioId): void {
    this.municipioService()
      .find(municipioId)
      .then(res => {
        this.municipio = res;
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
    this.estadoService()
      .retrieve()
      .then(res => {
        this.estados = res.data;
      });
  }
}
