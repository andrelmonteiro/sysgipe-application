/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import EnderecoUpdateComponent from '@/entities/endereco/endereco-update.vue';
import EnderecoClass from '@/entities/endereco/endereco-update.component';
import EnderecoService from '@/entities/endereco/endereco.service';

import MunicipioService from '@/entities/municipio/municipio.service';

import PessoaService from '@/entities/pessoa/pessoa.service';
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
  describe('Endereco Management Update Component', () => {
    let wrapper: Wrapper<EnderecoClass>;
    let comp: EnderecoClass;
    let enderecoServiceStub: SinonStubbedInstance<EnderecoService>;

    beforeEach(() => {
      enderecoServiceStub = sinon.createStubInstance<EnderecoService>(EnderecoService);

      wrapper = shallowMount<EnderecoClass>(EnderecoUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          enderecoService: () => enderecoServiceStub,
          alertService: () => new AlertService(),

          municipioService: () =>
            sinon.createStubInstance<MunicipioService>(MunicipioService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          pessoaService: () =>
            sinon.createStubInstance<PessoaService>(PessoaService, {
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
        comp.endereco = entity;
        enderecoServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(enderecoServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.endereco = entity;
        enderecoServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(enderecoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundEndereco = { id: 123 };
        enderecoServiceStub.find.resolves(foundEndereco);
        enderecoServiceStub.retrieve.resolves([foundEndereco]);

        // WHEN
        comp.beforeRouteEnter({ params: { enderecoId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.endereco).toBe(foundEndereco);
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
