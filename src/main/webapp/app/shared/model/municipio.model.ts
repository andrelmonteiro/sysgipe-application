import { IEndereco } from '@/shared/model/endereco.model';
import { IEstado } from '@/shared/model/estado.model';

export interface IMunicipio {
  id?: number;
  ativo?: number | null;
  codigoIbge?: number | null;
  nome?: string;
  enderecos?: IEndereco[] | null;
  estado?: IEstado | null;
}

export class Municipio implements IMunicipio {
  constructor(
    public id?: number,
    public ativo?: number | null,
    public codigoIbge?: number | null,
    public nome?: string,
    public enderecos?: IEndereco[] | null,
    public estado?: IEstado | null
  ) {}
}
