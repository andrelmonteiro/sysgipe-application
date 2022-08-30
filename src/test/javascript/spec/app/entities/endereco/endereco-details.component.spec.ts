/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import EnderecoDetailComponent from '@/entities/endereco/endereco-details.vue';
import EnderecoClass from '@/entities/endereco/endereco-details.component';
import EnderecoService from '@/entities/endereco/endereco.service';
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
  describe('Endereco Management Detail Component', () => {
    let wrapper: Wrapper<EnderecoClass>;
    let comp: EnderecoClass;
    let enderecoServiceStub: SinonStubbedInstance<EnderecoService>;

    beforeEach(() => {
      enderecoServiceStub = sinon.createStubInstance<EnderecoService>(EnderecoService);

      wrapper = shallowMount<EnderecoClass>(EnderecoDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { enderecoService: () => enderecoServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundEndereco = { id: 123 };
        enderecoServiceStub.find.resolves(foundEndereco);

        // WHEN
        comp.retrieveEndereco(123);
        await comp.$nextTick();

        // THEN
        expect(comp.endereco).toBe(foundEndereco);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundEndereco = { id: 123 };
        enderecoServiceStub.find.resolves(foundEndereco);

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
