/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import { DATE_FORMAT } from '@/shared/date/filters';
import AssuntoService from '@/entities/assunto/assunto.service';
import { Assunto } from '@/shared/model/assunto.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Assunto Service', () => {
    let service: AssuntoService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new AssuntoService();
      currentDate = new Date();
      elemDefault = new Assunto(123, 'AAAAAAA', false, 0, currentDate, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, 'AAAAAAA', 0);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            dataExclusao: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault
        );
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Assunto', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
            dataExclusao: dayjs(currentDate).format(DATE_FORMAT),
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dataExclusao: currentDate,
          },
          returnedFromService
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Assunto', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Assunto', async () => {
        const returnedFromService = Object.assign(
          {
            arigo: 'BBBBBB',
            ativo: true,
            codigo: 1,
            dataExclusao: dayjs(currentDate).format(DATE_FORMAT),
            dispositivo: 'BBBBBB',
            glossario: 'BBBBBB',
            nome: 'BBBBBB',
            observacao: 'BBBBBB',
            paiId: 1,
            tipo: 'BBBBBB',
            usuarioExclusaoId: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataExclusao: currentDate,
          },
          returnedFromService
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Assunto', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Assunto', async () => {
        const patchObject = Object.assign(
          {
            dispositivo: 'BBBBBB',
            glossario: 'BBBBBB',
            nome: 'BBBBBB',
            observacao: 'BBBBBB',
            paiId: 1,
            usuarioExclusaoId: 1,
          },
          new Assunto()
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dataExclusao: currentDate,
          },
          returnedFromService
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Assunto', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Assunto', async () => {
        const returnedFromService = Object.assign(
          {
            arigo: 'BBBBBB',
            ativo: true,
            codigo: 1,
            dataExclusao: dayjs(currentDate).format(DATE_FORMAT),
            dispositivo: 'BBBBBB',
            glossario: 'BBBBBB',
            nome: 'BBBBBB',
            observacao: 'BBBBBB',
            paiId: 1,
            tipo: 'BBBBBB',
            usuarioExclusaoId: 1,
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dataExclusao: currentDate,
          },
          returnedFromService
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Assunto', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Assunto', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Assunto', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
