/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import AssuntoDetailComponent from '@/entities/assunto/assunto-details.vue';
import AssuntoClass from '@/entities/assunto/assunto-details.component';
import AssuntoService from '@/entities/assunto/assunto.service';
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
  describe('Assunto Management Detail Component', () => {
    let wrapper: Wrapper<AssuntoClass>;
    let comp: AssuntoClass;
    let assuntoServiceStub: SinonStubbedInstance<AssuntoService>;

    beforeEach(() => {
      assuntoServiceStub = sinon.createStubInstance<AssuntoService>(AssuntoService);

      wrapper = shallowMount<AssuntoClass>(AssuntoDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { assuntoService: () => assuntoServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundAssunto = { id: 123 };
        assuntoServiceStub.find.resolves(foundAssunto);

        // WHEN
        comp.retrieveAssunto(123);
        await comp.$nextTick();

        // THEN
        expect(comp.assunto).toBe(foundAssunto);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAssunto = { id: 123 };
        assuntoServiceStub.find.resolves(foundAssunto);

        // WHEN
        comp.beforeRouteEnter({ params: { assuntoId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.assunto).toBe(foundAssunto);
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
