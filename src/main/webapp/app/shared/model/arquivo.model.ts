export interface IArquivo {
  id?: number;
  assinado?: string | null;
  ativo?: number | null;
  dateCreated?: Date | null;
  diretorio?: string | null;
  documentoId?: number | null;
  hash?: string | null;
  hashConteudo?: string | null;
  historico?: boolean | null;
  lacunaToken?: string | null;
  lastUpdate?: Date | null;
  mimeType?: string | null;
  nome?: string | null;
}

export class Arquivo implements IArquivo {
  constructor(
    public id?: number,
    public assinado?: string | null,
    public ativo?: number | null,
    public dateCreated?: Date | null,
    public diretorio?: string | null,
    public documentoId?: number | null,
    public hash?: string | null,
    public hashConteudo?: string | null,
    public historico?: boolean | null,
    public lacunaToken?: string | null,
    public lastUpdate?: Date | null,
    public mimeType?: string | null,
    public nome?: string | null
  ) {
    this.historico = this.historico ?? false;
  }
}
