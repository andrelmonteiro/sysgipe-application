export interface IAssunto {
  id?: number;
  arigo?: string | null;
  ativo?: boolean | null;
  codigo?: number | null;
  dataExclusao?: Date | null;
  dispositivo?: string | null;
  glossario?: string | null;
  nome?: string | null;
  observacao?: string | null;
  paiId?: number | null;
  tipo?: string | null;
  usuarioExclusaoId?: number | null;
}

export class Assunto implements IAssunto {
  constructor(
    public id?: number,
    public arigo?: string | null,
    public ativo?: boolean | null,
    public codigo?: number | null,
    public dataExclusao?: Date | null,
    public dispositivo?: string | null,
    public glossario?: string | null,
    public nome?: string | null,
    public observacao?: string | null,
    public paiId?: number | null,
    public tipo?: string | null,
    public usuarioExclusaoId?: number | null
  ) {
    this.ativo = this.ativo ?? false;
  }
}
