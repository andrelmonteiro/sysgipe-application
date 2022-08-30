import { Component, Vue, Inject } from 'vue-property-decorator';

import { IPessoa } from '@/shared/model/pessoa.model';
import PessoaService from './pessoa.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class PessoaDetails extends Vue {
  @Inject('pessoaService') private pessoaService: () => PessoaService;
  @Inject('alertService') private alertService: () => AlertService;

  public pessoa: IPessoa = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.pessoaId) {
        vm.retrievePessoa(to.params.pessoaId);
      }
    });
  }

  public retrievePessoa(pessoaId) {
    this.pessoaService()
      .find(pessoaId)
      .then(res => {
        this.pessoa = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
