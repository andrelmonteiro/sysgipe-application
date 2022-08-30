/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import PessoaUpdateComponent from '@/entities/pessoa/pessoa-update.vue';
import PessoaClass from '@/entities/pessoa/pessoa-update.component';
import PessoaService from '@/entities/pessoa/pessoa.service';

import EnderecoService from '@/entities/endereco/endereco.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Pessoa Management Update Component', () => {
    let wrapper: Wrapper<PessoaClass>;
    let comp: PessoaClass;
    let pessoaServiceStub: SinonStubbedInstance<PessoaService>;

    beforeEach(() => {
      pessoaServiceStub = sinon.createStubInstance<PessoaService>(PessoaService);

      wrapper = shallowMount<PessoaClass>(PessoaUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          pessoaService: () => pessoaServiceStub,
          alertService: () => new AlertService(),

          enderecoService: () =>
            sinon.createStubInstance<EnderecoService>(EnderecoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.pessoa = entity;
        pessoaServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pessoaServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.pessoa = entity;
        pessoaServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(pessoaServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPessoa = { id: 123 };
        pessoaServiceStub.find.resolves(foundPessoa);
        pessoaServiceStub.retrieve.resolves([foundPessoa]);

        // WHEN
        comp.beforeRouteEnter({ params: { pessoaId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.pessoa).toBe(foundPessoa);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
