/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ArquivoUpdateComponent from '@/entities/arquivo/arquivo-update.vue';
import ArquivoClass from '@/entities/arquivo/arquivo-update.component';
import ArquivoService from '@/entities/arquivo/arquivo.service';

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
  describe('Arquivo Management Update Component', () => {
    let wrapper: Wrapper<ArquivoClass>;
    let comp: ArquivoClass;
    let arquivoServiceStub: SinonStubbedInstance<ArquivoService>;

    beforeEach(() => {
      arquivoServiceStub = sinon.createStubInstance<ArquivoService>(ArquivoService);

      wrapper = shallowMount<ArquivoClass>(ArquivoUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          arquivoService: () => arquivoServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.arquivo = entity;
        arquivoServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(arquivoServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.arquivo = entity;
        arquivoServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(arquivoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundArquivo = { id: 123 };
        arquivoServiceStub.find.resolves(foundArquivo);
        arquivoServiceStub.retrieve.resolves([foundArquivo]);

        // WHEN
        comp.beforeRouteEnter({ params: { arquivoId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.arquivo).toBe(foundArquivo);
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
