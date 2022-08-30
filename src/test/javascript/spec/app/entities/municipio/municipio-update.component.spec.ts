/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import MunicipioUpdateComponent from '@/entities/municipio/municipio-update.vue';
import MunicipioClass from '@/entities/municipio/municipio-update.component';
import MunicipioService from '@/entities/municipio/municipio.service';

import EnderecoService from '@/entities/endereco/endereco.service';

import EstadoService from '@/entities/estado/estado.service';
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
  describe('Municipio Management Update Component', () => {
    let wrapper: Wrapper<MunicipioClass>;
    let comp: MunicipioClass;
    let municipioServiceStub: SinonStubbedInstance<MunicipioService>;

    beforeEach(() => {
      municipioServiceStub = sinon.createStubInstance<MunicipioService>(MunicipioService);

      wrapper = shallowMount<MunicipioClass>(MunicipioUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          municipioService: () => municipioServiceStub,
          alertService: () => new AlertService(),

          enderecoService: () =>
            sinon.createStubInstance<EnderecoService>(EnderecoService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          estadoService: () =>
            sinon.createStubInstance<EstadoService>(EstadoService, {
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
        comp.municipio = entity;
        municipioServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(municipioServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.municipio = entity;
        municipioServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(municipioServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundMunicipio = { id: 123 };
        municipioServiceStub.find.resolves(foundMunicipio);
        municipioServiceStub.retrieve.resolves([foundMunicipio]);

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
