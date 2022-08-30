/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import EstadoComponent from '@/entities/estado/estado.vue';
import EstadoClass from '@/entities/estado/estado.component';
import EstadoService from '@/entities/estado/estado.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Estado Management Component', () => {
    let wrapper: Wrapper<EstadoClass>;
    let comp: EstadoClass;
    let estadoServiceStub: SinonStubbedInstance<EstadoService>;

    beforeEach(() => {
      estadoServiceStub = sinon.createStubInstance<EstadoService>(EstadoService);
      estadoServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<EstadoClass>(EstadoComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          estadoService: () => estadoServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      estadoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllEstados();
      await comp.$nextTick();

      // THEN
      expect(estadoServiceStub.retrieve.called).toBeTruthy();
      expect(comp.estados[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      estadoServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(estadoServiceStub.retrieve.callCount).toEqual(1);

      comp.removeEstado();
      await comp.$nextTick();

      // THEN
      expect(estadoServiceStub.delete.called).toBeTruthy();
      expect(estadoServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
