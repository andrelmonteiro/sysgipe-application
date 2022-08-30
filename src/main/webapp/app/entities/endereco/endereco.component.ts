import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IEndereco } from '@/shared/model/endereco.model';

import EnderecoService from './endereco.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Endereco extends Vue {
  @Inject('enderecoService') private enderecoService: () => EnderecoService;
  @Inject('alertService') private alertService: () => AlertService;

  public currentSearch = '';
  private removeId: number = null;

  public enderecos: IEndereco[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllEnderecos();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllEnderecos();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllEnderecos();
  }

  public retrieveAllEnderecos(): void {
    this.isFetching = true;
    if (this.currentSearch) {
      this.enderecoService()
        .search(this.currentSearch)
        .then(
          res => {
            this.enderecos = res;
            this.isFetching = false;
          },
          err => {
            this.isFetching = false;
            this.alertService().showHttpError(this, err.response);
          }
        );
      return;
    }
    this.enderecoService()
      .retrieve()
      .then(
        res => {
          this.enderecos = res.data;
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

  public prepareRemove(instance: IEndereco): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeEndereco(): void {
    this.enderecoService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('sysgipeApp.endereco.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllEnderecos();
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
