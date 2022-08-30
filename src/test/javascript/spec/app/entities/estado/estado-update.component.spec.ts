/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import EstadoUpdateComponent from '@/entities/estado/estado-update.vue';
import EstadoClass from '@/entities/estado/estado-update.component';
import EstadoService from '@/entities/estado/estado.service';

import MunicipioService from '@/entities/municipio/municipio.service';

import PaisService from '@/entities/pais/pais.service';
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
  describe('Estado Management Update Component', () => {
    let wrapper: Wrapper<EstadoClass>;
    let comp: EstadoClass;
    let estadoServiceStub: SinonStubbedInstance<EstadoService>;

    beforeEach(() => {
      estadoServiceStub = sinon.createStubInstance<EstadoService>(EstadoService);

      wrapper = shallowMount<EstadoClass>(EstadoUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          estadoService: () => estadoServiceStub,
          alertService: () => new AlertService(),

          municipioService: () =>
            sinon.createStubInstance<MunicipioService>(MunicipioService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          paisService: () =>
            sinon.createStubInstance<PaisService>(PaisService, {
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
        comp.estado = entity;
        estadoServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(estadoServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.estado = entity;
        estadoServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(estadoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundEstado = { id: 123 };
        estadoServiceStub.find.resolves(foundEstado);
        estadoServiceStub.retrieve.resolves([foundEstado]);

        // WHEN
        comp.beforeRouteEnter({ params: { estadoId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.estado).toBe(foundEstado);
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
