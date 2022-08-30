import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

// prettier-ignore
const Arquivo = () => import('@/entities/arquivo/arquivo.vue');
// prettier-ignore
const ArquivoUpdate = () => import('@/entities/arquivo/arquivo-update.vue');
// prettier-ignore
const ArquivoDetails = () => import('@/entities/arquivo/arquivo-details.vue');
// prettier-ignore
const Assunto = () => import('@/entities/assunto/assunto.vue');
// prettier-ignore
const AssuntoUpdate = () => import('@/entities/assunto/assunto-update.vue');
// prettier-ignore
const AssuntoDetails = () => import('@/entities/assunto/assunto-details.vue');
// prettier-ignore
const Pessoa = () => import('@/entities/pessoa/pessoa.vue');
// prettier-ignore
const PessoaUpdate = () => import('@/entities/pessoa/pessoa-update.vue');
// prettier-ignore
const PessoaDetails = () => import('@/entities/pessoa/pessoa-details.vue');
// prettier-ignore
const Estado = () => import('@/entities/estado/estado.vue');
// prettier-ignore
const EstadoUpdate = () => import('@/entities/estado/estado-update.vue');
// prettier-ignore
const EstadoDetails = () => import('@/entities/estado/estado-details.vue');
// prettier-ignore
const Pais = () => import('@/entities/pais/pais.vue');
// prettier-ignore
const PaisUpdate = () => import('@/entities/pais/pais-update.vue');
// prettier-ignore
const PaisDetails = () => import('@/entities/pais/pais-details.vue');
// prettier-ignore
const Municipio = () => import('@/entities/municipio/municipio.vue');
// prettier-ignore
const MunicipioUpdate = () => import('@/entities/municipio/municipio-update.vue');
// prettier-ignore
const MunicipioDetails = () => import('@/entities/municipio/municipio-details.vue');
// prettier-ignore
const Endereco = () => import('@/entities/endereco/endereco.vue');
// prettier-ignore
const EnderecoUpdate = () => import('@/entities/endereco/endereco-update.vue');
// prettier-ignore
const EnderecoDetails = () => import('@/entities/endereco/endereco-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'arquivo',
      name: 'Arquivo',
      component: Arquivo,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'arquivo/new',
      name: 'ArquivoCreate',
      component: ArquivoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'arquivo/:arquivoId/edit',
      name: 'ArquivoEdit',
      component: ArquivoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'arquivo/:arquivoId/view',
      name: 'ArquivoView',
      component: ArquivoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'assunto',
      name: 'Assunto',
      component: Assunto,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'assunto/new',
      name: 'AssuntoCreate',
      component: AssuntoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'assunto/:assuntoId/edit',
      name: 'AssuntoEdit',
      component: AssuntoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'assunto/:assuntoId/view',
      name: 'AssuntoView',
      component: AssuntoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pessoa',
      name: 'Pessoa',
      component: Pessoa,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pessoa/new',
      name: 'PessoaCreate',
      component: PessoaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pessoa/:pessoaId/edit',
      name: 'PessoaEdit',
      component: PessoaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pessoa/:pessoaId/view',
      name: 'PessoaView',
      component: PessoaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado',
      name: 'Estado',
      component: Estado,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado/new',
      name: 'EstadoCreate',
      component: EstadoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado/:estadoId/edit',
      name: 'EstadoEdit',
      component: EstadoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'estado/:estadoId/view',
      name: 'EstadoView',
      component: EstadoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pais',
      name: 'Pais',
      component: Pais,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pais/new',
      name: 'PaisCreate',
      component: PaisUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pais/:paisId/edit',
      name: 'PaisEdit',
      component: PaisUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'pais/:paisId/view',
      name: 'PaisView',
      component: PaisDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'municipio',
      name: 'Municipio',
      component: Municipio,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'municipio/new',
      name: 'MunicipioCreate',
      component: MunicipioUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'municipio/:municipioId/edit',
      name: 'MunicipioEdit',
      component: MunicipioUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'municipio/:municipioId/view',
      name: 'MunicipioView',
      component: MunicipioDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'endereco',
      name: 'Endereco',
      component: Endereco,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'endereco/new',
      name: 'EnderecoCreate',
      component: EnderecoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'endereco/:enderecoId/edit',
      name: 'EnderecoEdit',
      component: EnderecoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'endereco/:enderecoId/view',
      name: 'EnderecoView',
      component: EnderecoDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
