import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IAssunto } from '@/shared/model/assunto.model';

import AssuntoService from './assunto.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Assunto extends Vue {
  @Inject('assuntoService') private assuntoService: () => AssuntoService;
  @Inject('alertService') private alertService: () => AlertService;

  public currentSearch = '';
  private removeId: number = null;

  public assuntos: IAssunto[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllAssuntos();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllAssuntos();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllAssuntos();
  }

  public retrieveAllAssuntos(): void {
    this.isFetching = true;
    if (this.currentSearch) {
      this.assuntoService()
        .search(this.currentSearch)
        .then(
          res => {
            this.assuntos = res;
            this.isFetching = false;
          },
          err => {
            this.isFetching = false;
            this.alertService().showHttpError(this, err.response);
          }
        );
      return;
    }
    this.assuntoService()
      .retrieve()
      .then(
        res => {
          this.assuntos = res.data;
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

  public prepareRemove(instance: IAssunto): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeAssunto(): void {
    this.assuntoService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('sysgipeApp.assunto.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllAssuntos();
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
