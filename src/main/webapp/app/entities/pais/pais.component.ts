import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IPais } from '@/shared/model/pais.model';

import PaisService from './pais.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Pais extends Vue {
  @Inject('paisService') private paisService: () => PaisService;
  @Inject('alertService') private alertService: () => AlertService;

  public currentSearch = '';
  private removeId: number = null;

  public pais: IPais[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllPaiss();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllPaiss();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllPaiss();
  }

  public retrieveAllPaiss(): void {
    this.isFetching = true;
    if (this.currentSearch) {
      this.paisService()
        .search(this.currentSearch)
        .then(
          res => {
            this.pais = res;
            this.isFetching = false;
          },
          err => {
            this.isFetching = false;
            this.alertService().showHttpError(this, err.response);
          }
        );
      return;
    }
    this.paisService()
      .retrieve()
      .then(
        res => {
          this.pais = res.data;
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

  public prepareRemove(instance: IPais): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removePais(): void {
    this.paisService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('sysgipeApp.pais.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllPaiss();
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
