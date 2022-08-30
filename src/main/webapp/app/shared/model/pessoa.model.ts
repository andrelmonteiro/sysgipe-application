import { IEndereco } from '@/shared/model/endereco.model';

export interface IPessoa {
  id?: number;
  ativo?: number | null;
  autoridade?: boolean | null;
  contato?: string;
  enderecos?: IEndereco[] | null;
}

export class Pessoa implements IPessoa {
  constructor(
    public id?: number,
    public ativo?: number | null,
    public autoridade?: boolean | null,
    public contato?: string,
    public enderecos?: IEndereco[] | null
  ) {
    this.autoridade = this.autoridade ?? false;
  }
}
