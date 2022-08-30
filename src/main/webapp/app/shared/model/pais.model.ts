import { IEstado } from '@/shared/model/estado.model';

export interface IPais {
  id?: number;
  ativo?: number | null;
  nome?: string;
  sigla?: string | null;
  estados?: IEstado[] | null;
}

export class Pais implements IPais {
  constructor(
    public id?: number,
    public ativo?: number | null,
    public nome?: string,
    public sigla?: string | null,
    public estados?: IEstado[] | null
  ) {}
}
