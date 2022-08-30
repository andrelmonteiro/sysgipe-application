import { Component, Vue, Inject } from 'vue-property-decorator';

import { IPais } from '@/shared/model/pais.model';
import PaisService from './pais.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class PaisDetails extends Vue {
  @Inject('paisService') private paisService: () => PaisService;
  @Inject('alertService') private alertService: () => AlertService;

  public pais: IPais = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.paisId) {
        vm.retrievePais(to.params.paisId);
      }
    });
  }

  public retrievePais(paisId) {
    this.paisService()
      .find(paisId)
      .then(res => {
        this.pais = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
