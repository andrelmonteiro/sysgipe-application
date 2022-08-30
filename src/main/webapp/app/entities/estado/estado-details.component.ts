import { Component, Vue, Inject } from 'vue-property-decorator';

import { IEstado } from '@/shared/model/estado.model';
import EstadoService from './estado.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class EstadoDetails extends Vue {
  @Inject('estadoService') private estadoService: () => EstadoService;
  @Inject('alertService') private alertService: () => AlertService;

  public estado: IEstado = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.estadoId) {
        vm.retrieveEstado(to.params.estadoId);
      }
    });
  }

  public retrieveEstado(estadoId) {
    this.estadoService()
      .find(estadoId)
      .then(res => {
        this.estado = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
