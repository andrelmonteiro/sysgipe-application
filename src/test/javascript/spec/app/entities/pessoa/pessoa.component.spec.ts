/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import PessoaComponent from '@/entities/pessoa/pessoa.vue';
import PessoaClass from '@/entities/pessoa/pessoa.component';
import PessoaService from '@/entities/pessoa/pessoa.service';
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
  describe('Pessoa Management Component', () => {
    let wrapper: Wrapper<PessoaClass>;
    let comp: PessoaClass;
    let pessoaServiceStub: SinonStubbedInstance<PessoaService>;

    beforeEach(() => {
      pessoaServiceStub = sinon.createStubInstance<PessoaService>(PessoaService);
      pessoaServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<PessoaClass>(PessoaComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          pessoaService: () => pessoaServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      pessoaServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllPessoas();
      await comp.$nextTick();

      // THEN
      expect(pessoaServiceStub.retrieve.called).toBeTruthy();
      expect(comp.pessoas[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      pessoaServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(pessoaServiceStub.retrieve.callCount).toEqual(1);

      comp.removePessoa();
      await comp.$nextTick();

      // THEN
      expect(pessoaServiceStub.delete.called).toBeTruthy();
      expect(pessoaServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
