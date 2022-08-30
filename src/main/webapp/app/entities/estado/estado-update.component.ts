import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import MunicipioService from '@/entities/municipio/municipio.service';
import { IMunicipio } from '@/shared/model/municipio.model';

import PaisService from '@/entities/pais/pais.service';
import { IPais } from '@/shared/model/pais.model';

import { IEstado, Estado } from '@/shared/model/estado.model';
import EstadoService from './estado.service';

const validations: any = {
  estado: {
    ativo: {},
    codigoIbge: {},
    nome: {
      required,
    },
    sigla: {},
  },
};

@Component({
  validations,
})
export default class EstadoUpdate extends Vue {
  @Inject('estadoService') private estadoService: () => EstadoService;
  @Inject('alertService') private alertService: () => AlertService;

  public estado: IEstado = new Estado();

  @Inject('municipioService') private municipioService: () => MunicipioService;

  public municipios: IMunicipio[] = [];

  @Inject('paisService') private paisService: () => PaisService;

  public pais: IPais[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.estadoId) {
        vm.retrieveEstado(to.params.estadoId);
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
    if (this.estado.id) {
      this.estadoService()
        .update(this.estado)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('sysgipeApp.estado.updated', { param: param.id });
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
      this.estadoService()
        .create(this.estado)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('sysgipeApp.estado.created', { param: param.id });
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

  public retrieveEstado(estadoId): void {
    this.estadoService()
      .find(estadoId)
      .then(res => {
        this.estado = res;
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
    this.paisService()
      .retrieve()
      .then(res => {
        this.pais = res.data;
      });
  }
}
