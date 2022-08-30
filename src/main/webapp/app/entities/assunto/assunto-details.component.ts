import { Component, Vue, Inject } from 'vue-property-decorator';

import { IAssunto } from '@/shared/model/assunto.model';
import AssuntoService from './assunto.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class AssuntoDetails extends Vue {
  @Inject('assuntoService') private assuntoService: () => AssuntoService;
  @Inject('alertService') private alertService: () => AlertService;

  public assunto: IAssunto = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.assuntoId) {
        vm.retrieveAssunto(to.params.assuntoId);
      }
    });
  }

  public retrieveAssunto(assuntoId) {
    this.assuntoService()
      .find(assuntoId)
      .then(res => {
        this.assunto = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
