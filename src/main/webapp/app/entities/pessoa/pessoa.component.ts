import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IPessoa } from '@/shared/model/pessoa.model';

import PessoaService from './pessoa.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Pessoa extends Vue {
  @Inject('pessoaService') private pessoaService: () => PessoaService;
  @Inject('alertService') private alertService: () => AlertService;

  public currentSearch = '';
  private removeId: number = null;

  public pessoas: IPessoa[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllPessoas();
  }

  public search(query): void {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.retrieveAllPessoas();
  }

  public clear(): void {
    this.currentSearch = '';
    this.retrieveAllPessoas();
  }

  public retrieveAllPessoas(): void {
    this.isFetching = true;
    if (this.currentSearch) {
      this.pessoaService()
        .search(this.currentSearch)
        .then(
          res => {
            this.pessoas = res;
            this.isFetching = false;
          },
          err => {
            this.isFetching = false;
            this.alertService().showHttpError(this, err.response);
          }
        );
      return;
    }
    this.pessoaService()
      .retrieve()
      .then(
        res => {
          this.pessoas = res.data;
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

  public prepareRemove(instance: IPessoa): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removePessoa(): void {
    this.pessoaService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('sysgipeApp.pessoa.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllPessoas();
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
