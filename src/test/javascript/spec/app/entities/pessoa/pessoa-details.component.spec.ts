/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import PessoaDetailComponent from '@/entities/pessoa/pessoa-details.vue';
import PessoaClass from '@/entities/pessoa/pessoa-details.component';
import PessoaService from '@/entities/pessoa/pessoa.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Pessoa Management Detail Component', () => {
    let wrapper: Wrapper<PessoaClass>;
    let comp: PessoaClass;
    let pessoaServiceStub: SinonStubbedInstance<PessoaService>;

    beforeEach(() => {
      pessoaServiceStub = sinon.createStubInstance<PessoaService>(PessoaService);

      wrapper = shallowMount<PessoaClass>(PessoaDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { pessoaService: () => pessoaServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundPessoa = { id: 123 };
        pessoaServiceStub.find.resolves(foundPessoa);

        // WHEN
        comp.retrievePessoa(123);
        await comp.$nextTick();

        // THEN
        expect(comp.pessoa).toBe(foundPessoa);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPessoa = { id: 123 };
        pessoaServiceStub.find.resolves(foundPessoa);

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
