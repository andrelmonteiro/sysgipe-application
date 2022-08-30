/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ArquivoComponent from '@/entities/arquivo/arquivo.vue';
import ArquivoClass from '@/entities/arquivo/arquivo.component';
import ArquivoService from '@/entities/arquivo/arquivo.service';
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
  describe('Arquivo Management Component', () => {
    let wrapper: Wrapper<ArquivoClass>;
    let comp: ArquivoClass;
    let arquivoServiceStub: SinonStubbedInstance<ArquivoService>;

    beforeEach(() => {
      arquivoServiceStub = sinon.createStubInstance<ArquivoService>(ArquivoService);
      arquivoServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ArquivoClass>(ArquivoComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          arquivoService: () => arquivoServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      arquivoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllArquivos();
      await comp.$nextTick();

      // THEN
      expect(arquivoServiceStub.retrieve.called).toBeTruthy();
      expect(comp.arquivos[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      arquivoServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(arquivoServiceStub.retrieve.callCount).toEqual(1);

      comp.removeArquivo();
      await comp.$nextTick();

      // THEN
      expect(arquivoServiceStub.delete.called).toBeTruthy();
      expect(arquivoServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
