import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IMunicipio } from '@/shared/model/municipio.model';

import MunicipioService from './municipio.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Municipio extends Vue {
  @Inject('municipioService') private municipioService: () => MunicipioService;
  @Inject('alertService') private alertService: () => AlertService;

  public currentSearch = '';
  private removeId: number = null;

  public municipios: IMunicipio[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllMunicipios();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllMunicipios();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllMunicipios();
  }

  public retrieveAllMunicipios(): void {
    this.isFetching = true;
    if (this.currentSearch) {
      this.municipioService()
        .search(this.currentSearch)
        .then(
          res => {
            this.municipios = res;
            this.isFetching = false;
          },
          err => {
            this.isFetching = false;
            this.alertService().showHttpError(this, err.response);
          }
        );
      return;
    }
    this.municipioService()
      .retrieve()
      .then(
        res => {
          this.municipios = res.data;
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

  public prepareRemove(instance: IMunicipio): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeMunicipio(): void {
    this.municipioService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('sysgipeApp.municipio.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllMunicipios();
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
