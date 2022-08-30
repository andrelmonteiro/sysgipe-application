import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import { IAssunto, Assunto } from '@/shared/model/assunto.model';
import AssuntoService from './assunto.service';

const validations: any = {
  assunto: {
    arigo: {},
    ativo: {},
    codigo: {},
    dataExclusao: {},
    dispositivo: {},
    glossario: {},
    nome: {},
    observacao: {},
    paiId: {},
    tipo: {},
    usuarioExclusaoId: {},
  },
};

@Component({
  validations,
})
export default class AssuntoUpdate extends Vue {
  @Inject('assuntoService') private assuntoService: () => AssuntoService;
  @Inject('alertService') private alertService: () => AlertService;

  public assunto: IAssunto = new Assunto();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.assuntoId) {
        vm.retrieveAssunto(to.params.assuntoId);
      }
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
    if (this.assunto.id) {
      this.assuntoService()
        .update(this.assunto)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('sysgipeApp.assunto.updated', { param: param.id });
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
      this.assuntoService()
        .create(this.assunto)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('sysgipeApp.assunto.created', { param: param.id });
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

  public retrieveAssunto(assuntoId): void {
    this.assuntoService()
      .find(assuntoId)
      .then(res => {
        this.assunto = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
