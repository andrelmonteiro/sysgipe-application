import { Component, Vue, Inject } from 'vue-property-decorator';

import { IMunicipio } from '@/shared/model/municipio.model';
import MunicipioService from './municipio.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class MunicipioDetails extends Vue {
  @Inject('municipioService') private municipioService: () => MunicipioService;
  @Inject('alertService') private alertService: () => AlertService;

  public municipio: IMunicipio = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.municipioId) {
        vm.retrieveMunicipio(to.params.municipioId);
      }
    });
  }

  public retrieveMunicipio(municipioId) {
    this.municipioService()
      .find(municipioId)
      .then(res => {
        this.municipio = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
