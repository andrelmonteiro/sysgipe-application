/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import PaisDetailComponent from '@/entities/pais/pais-details.vue';
import PaisClass from '@/entities/pais/pais-details.component';
import PaisService from '@/entities/pais/pais.service';
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
  describe('Pais Management Detail Component', () => {
    let wrapper: Wrapper<PaisClass>;
    let comp: PaisClass;
    let paisServiceStub: SinonStubbedInstance<PaisService>;

    beforeEach(() => {
      paisServiceStub = sinon.createStubInstance<PaisService>(PaisService);

      wrapper = shallowMount<PaisClass>(PaisDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { paisService: () => paisServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundPais = { id: 123 };
        paisServiceStub.find.resolves(foundPais);

        // WHEN
        comp.retrievePais(123);
        await comp.$nextTick();

        // THEN
        expect(comp.pais).toBe(foundPais);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPais = { id: 123 };
        paisServiceStub.find.resolves(foundPais);

        // WHEN
        comp.beforeRouteEnter({ params: { paisId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.pais).toBe(foundPais);
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
