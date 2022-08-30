import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IArquivo } from '@/shared/model/arquivo.model';

import ArquivoService from './arquivo.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Arquivo extends Vue {
  @Inject('arquivoService') private arquivoService: () => ArquivoService;
  @Inject('alertService') private alertService: () => AlertService;

  public currentSearch = '';
  private removeId: number = null;

  public arquivos: IArquivo[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllArquivos();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllArquivos();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllArquivos();
  }

  public retrieveAllArquivos(): void {
    this.isFetching = true;
    if (this.currentSearch) {
      this.arquivoService()
        .search(this.currentSearch)
        .then(
          res => {
            this.arquivos = res;
            this.isFetching = false;
          },
          err => {
            this.isFetching = false;
            this.alertService().showHttpError(this, err.response);
          }
        );
      return;
    }
    this.arquivoService()
      .retrieve()
      .then(
        res => {
          this.arquivos = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IArquivo): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeArquivo(): void {
    this.arquivoService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('sysgipeApp.arquivo.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllArquivos();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
