/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import AssuntoComponent from '@/entities/assunto/assunto.vue';
import AssuntoClass from '@/entities/assunto/assunto.component';
import AssuntoService from '@/entities/assunto/assunto.service';
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
  describe('Assunto Management Component', () => {
    let wrapper: Wrapper<AssuntoClass>;
    let comp: AssuntoClass;
    let assuntoServiceStub: SinonStubbedInstance<AssuntoService>;

    beforeEach(() => {
      assuntoServiceStub = sinon.createStubInstance<AssuntoService>(AssuntoService);
      assuntoServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<AssuntoClass>(AssuntoComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          assuntoService: () => assuntoServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      assuntoServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllAssuntos();
      await comp.$nextTick();

      // THEN
      expect(assuntoServiceStub.retrieve.called).toBeTruthy();
      expect(comp.assuntos[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      assuntoServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(assuntoServiceStub.retrieve.callCount).toEqual(1);

      comp.removeAssunto();
      await comp.$nextTick();

      // THEN
      expect(assuntoServiceStub.delete.called).toBeTruthy();
      expect(assuntoServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
