/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import PaisUpdateComponent from '@/entities/pais/pais-update.vue';
import PaisClass from '@/entities/pais/pais-update.component';
import PaisService from '@/entities/pais/pais.service';

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
  describe('Pais Management Update Component', () => {
    let wrapper: Wrapper<PaisClass>;
    let comp: PaisClass;
    let paisServiceStub: SinonStubbedInstance<PaisService>;

    beforeEach(() => {
      paisServiceStub = sinon.createStubInstance<PaisService>(PaisService);

      wrapper = shallowMount<PaisClass>(PaisUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          paisService: () => paisServiceStub,
          alertService: () => new AlertService(),

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
        comp.pais = entity;
        paisServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paisServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.pais = entity;
        paisServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paisServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPais = { id: 123 };
        paisServiceStub.find.resolves(foundPais);
        paisServiceStub.retrieve.resolves([foundPais]);

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
