import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import ArquivoService from './arquivo/arquivo.service';
import AssuntoService from './assunto/assunto.service';
import PessoaService from './pessoa/pessoa.service';
import EstadoService from './estado/estado.service';
import PaisService from './pais/pais.service';
import MunicipioService from './municipio/municipio.service';
import EnderecoService from './endereco/endereco.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('arquivoService') private arquivoService = () => new ArquivoService();
  @Provide('assuntoService') private assuntoService = () => new AssuntoService();
  @Provide('pessoaService') private pessoaService = () => new PessoaService();
  @Provide('estadoService') private estadoService = () => new EstadoService();
  @Provide('paisService') private paisService = () => new PaisService();
  @Provide('municipioService') private municipioService = () => new MunicipioService();
  @Provide('enderecoService') private enderecoService = () => new EnderecoService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}
