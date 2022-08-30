import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import { IArquivo, Arquivo } from '@/shared/model/arquivo.model';
import ArquivoService from './arquivo.service';

const validations: any = {
  arquivo: {
    assinado: {},
    ativo: {},
    dateCreated: {},
    diretorio: {},
    documentoId: {},
    hash: {},
    hashConteudo: {},
    historico: {},
    lacunaToken: {},
    lastUpdate: {},
    mimeType: {},
    nome: {},
  },
};

@Component({
  validations,
})
export default class ArquivoUpdate extends Vue {
  @Inject('arquivoService') private arquivoService: () => ArquivoService;
  @Inject('alertService') private alertService: () => AlertService;

  public arquivo: IArquivo = new Arquivo();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.arquivoId) {
        vm.retrieveArquivo(to.params.arquivoId);
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
    if (this.arquivo.id) {
      this.arquivoService()
        .update(this.arquivo)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('sysgipeApp.arquivo.updated', { param: param.id });
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
      this.arquivoService()
        .create(this.arquivo)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('sysgipeApp.arquivo.created', { param: param.id });
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

  public retrieveArquivo(arquivoId): void {
    this.arquivoService()
      .find(arquivoId)
      .then(res => {
        this.arquivo = res;
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
