import { Component, Vue, Inject } from 'vue-property-decorator';

import { IArquivo } from '@/shared/model/arquivo.model';
import ArquivoService from './arquivo.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ArquivoDetails extends Vue {
  @Inject('arquivoService') private arquivoService: () => ArquivoService;
  @Inject('alertService') private alertService: () => AlertService;

  public arquivo: IArquivo = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.arquivoId) {
        vm.retrieveArquivo(to.params.arquivoId);
      }
    });
  }

  public retrieveArquivo(arquivoId) {
    this.arquivoService()
      .find(arquivoId)
      .then(res => {
        this.arquivo = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
