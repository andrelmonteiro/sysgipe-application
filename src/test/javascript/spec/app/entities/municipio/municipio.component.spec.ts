/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import MunicipioComponent from '@/entities/municipio/municipio.vue';
import MunicipioClass from '@/entities/municipio/municipio.component';
import MunicipioService from '@/entities/municipio/municipio.service';
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
  describe('Municipio Management Component', () => {
    let wrapper: Wrapper<MunicipioClass>;
    let comp: MunicipioClass;
    let municipioServiceStub: SinonStubbedInstance<MunicipioService>;

    beforeEach(() => {
      municipioServiceStub = sinon.createStubInstance<MunicipioService>(MunicipioService);
      municipioServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<MunicipioClass>(MunicipioComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          municipioService: () => municipioServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      municipioServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllMunicipios();
      await comp.$nextTick();

      // THEN
      expect(municipioServiceStub.retrieve.called).toBeTruthy();
      expect(comp.municipios[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      municipioServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(municipioServiceStub.retrieve.callCount).toEqual(1);

      comp.removeMunicipio();
      await comp.$nextTick();

      // THEN
      expect(municipioServiceStub.delete.called).toBeTruthy();
      expect(municipioServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
