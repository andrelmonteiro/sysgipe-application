import { IMunicipio } from '@/shared/model/municipio.model';
import { IPais } from '@/shared/model/pais.model';

export interface IEstado {
  id?: number;
  ativo?: number | null;
  codigoIbge?: number | null;
  nome?: string;
  sigla?: string | null;
  municipios?: IMunicipio[] | null;
  pais?: IPais | null;
}

export class Estado implements IEstado {
  constructor(
    public id?: number,
    public ativo?: number | null,
    public codigoIbge?: number | null,
    public nome?: string,
    public sigla?: string | null,
    public municipios?: IMunicipio[] | null,
    public pais?: IPais | null
  ) {}
}
