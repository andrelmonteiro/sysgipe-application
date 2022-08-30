import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import EstadoService from '@/entities/estado/estado.service';
import { IEstado } from '@/shared/model/estado.model';

import { IPais, Pais } from '@/shared/model/pais.model';
import PaisService from './pais.service';

const validations: any = {
  pais: {
    ativo: {},
    nome: {
      required,
    },
    sigla: {},
  },
};

@Component({
  validations,
})
export default class PaisUpdate extends Vue {
  @Inject('paisService') private paisService: () => PaisService;
  @Inject('alertService') private alertService: () => AlertService;

  public pais: IPais = new Pais();

  @Inject('estadoService') private estadoService: () => EstadoService;

  public estados: IEstado[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.paisId) {
        vm.retrievePais(to.params.paisId);
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
    if (this.pais.id) {
      this.paisService()
        .update(this.pais)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('sysgipeApp.pais.updated', { param: param.id });
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
      this.paisService()
        .create(this.pais)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('sysgipeApp.pais.created', { param: param.id });
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

  public retrievePais(paisId): void {
    this.paisService()
      .find(paisId)
      .then(res => {
        this.pais = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.estadoService()
      .retrieve()
      .then(res => {
        this.estados = res.data;
      });
  }
}
