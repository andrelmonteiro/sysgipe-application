import { IMunicipio } from '@/shared/model/municipio.model';
import { IPessoa } from '@/shared/model/pessoa.model';

export interface IEndereco {
  id?: number;
  ativo?: number | null;
  atual?: boolean | null;
  bairro?: string | null;
  cep?: string | null;
  complemento?: string | null;
  dataExclusao?: Date | null;
  usuarioExclusaoId?: number | null;
  municipio?: IMunicipio | null;
  opessoas?: IPessoa[] | null;
}

export class Endereco implements IEndereco {
  constructor(
    public id?: number,
    public ativo?: number | null,
    public atual?: boolean | null,
    public bairro?: string | null,
    public cep?: string | null,
    public complemento?: string | null,
    public dataExclusao?: Date | null,
    public usuarioExclusaoId?: number | null,
    public municipio?: IMunicipio | null,
    public opessoas?: IPessoa[] | null
  ) {
    this.atual = this.atual ?? false;
  }
}
