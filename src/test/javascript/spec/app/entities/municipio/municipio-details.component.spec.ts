/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import MunicipioDetailComponent from '@/entities/municipio/municipio-details.vue';
import MunicipioClass from '@/entities/municipio/municipio-details.component';
import MunicipioService from '@/entities/municipio/municipio.service';
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
  describe('Municipio Management Detail Component', () => {
    let wrapper: Wrapper<MunicipioClass>;
    let comp: MunicipioClass;
    let municipioServiceStub: SinonStubbedInstance<MunicipioService>;

    beforeEach(() => {
      municipioServiceStub = sinon.createStubInstance<MunicipioService>(MunicipioService);

      wrapper = shallowMount<MunicipioClass>(MunicipioDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { municipioService: () => municipioServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundMunicipio = { id: 123 };
        municipioServiceStub.find.resolves(foundMunicipio);

        // WHEN
        comp.retrieveMunicipio(123);
        await comp.$nextTick();

        // THEN
        expect(comp.municipio).toBe(foundMunicipio);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundMunicipio = { id: 123 };
        municipioServiceStub.find.resolves(foundMunicipio);

        // WHEN
        comp.beforeRouteEnter({ params: { municipioId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.municipio).toBe(foundMunicipio);
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
