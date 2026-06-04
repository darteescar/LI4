// Domain types — shared by mock services and (future) REST API

export type Role = "GERENTE" | "GESTOR_STOCK" | "SECRETARIA" | "MECANICO";

export const ROLE_LABELS: Record<Role, string> = {
  GERENTE: "Gerente",
  GESTOR_STOCK: "Gestor de Stock",
  SECRETARIA: "Secretária",
  MECANICO: "Mecânico",
};

export interface Funcionario {
  id: string;
  nome: string;
  identificador: string; // username
  /** Password definida pelo gerente. */
  password?: string;
  email: string;
  telemovel: string;
  nif: string;
  niss: string;
  nus: string;
  iban: string;
  morada: string;
  codigoPostal: string;
  cargo: Role;
  /** Vencimento por hora (€/h) usado para pagar horas extras. */
  vencimentoHora: number;
  /** Vencimento bruto mensal (€). */
  vencimentoBruto: number;
  /** Horas extras acumuladas desde o último pagamento de salário. */
  horasExtraAcumuladas: number;
  dataAdmissao: string; // ISO
  ativo: boolean;
}

export interface Cliente {
  id: string;
  nome: string;
  nif: string;
  telemovel: string;
  email: string;
  morada: string;
  codigoPostal: string;
  criadoEm: string;
}

export interface Trotinete {
  id: string;
  clienteId: string;
  marca: string;
  modelo: string;
  numeroSerie: string;
  motor: string; // tipo do motor
}

export interface Fornecedor {
  id: string;
  nome: string;
  telemovel: string;
  email: string;
}

export interface Peca {
  id: string;
  referencia: string;
  nome: string;
  descricao: string;
  fornecedorId: string;
  /** Preço de venda ao cliente (catálogo). */
  precoVenda: number;
  /** Categoria/tipo da peça (ex: bateria, motor, pneu) — usado para filtros. */
  tipo: string;
  stockAtual: number;
  stockMinimo: number;
  disponivel: boolean;
}


export type EstadoDevolucao = "PENDENTE" | "DEVOLVIDA" | "INVALIDA";

export interface Devolucao {
  id: string;
  pecaId: string;
  quantidade: number;
  motivo: string;
  estado: EstadoDevolucao;
  fornecedorId: string;
  data: string;
}

export type EstadoEncomenda = "RASCUNHO" | "ENVIADA" | "RECEBIDA";

export interface ItemEncomenda {
  pecaId: string;
  quantidade: number;
  precoUnitario: number;
}

export interface Encomenda {
  id: string;
  fornecedorId: string;
  itens: ItemEncomenda[];
  estado: EstadoEncomenda;
  criadaEm: string;
  enviadaEm?: string;
  recebidaEm?: string;
  criadaPor: string;
}

export interface Reparacao {
  id: string;
  nomenclatura: string;
  descricao: string;
  preco: number; // mão de obra
  disponivel: boolean;
}

export type EstadoOS =
  | "REGISTADA"
  | "EM_DIAGNOSTICO"
  | "AGUARDA_APROVACAO"
  | "APROVADA"
  | "EM_REPARACAO"
  | "AGUARDA_PECAS"
  | "CONCLUIDA"
  | "AGUARDA_PAGAMENTO"
  | "PAGA"
  | "CANCELADA";

export const ESTADO_OS_LABELS: Record<EstadoOS, string> = {
  REGISTADA: "Registada",
  EM_DIAGNOSTICO: "Em diagnóstico",
  AGUARDA_APROVACAO: "Aguarda aprovação",
  APROVADA: "Aprovada",
  EM_REPARACAO: "Em reparação",
  AGUARDA_PECAS: "Aguarda peças",
  CONCLUIDA: "Concluída",
  AGUARDA_PAGAMENTO: "Aguarda pagamento",
  PAGA: "Paga",
  CANCELADA: "Cancelada",
};

export interface ItemReparacaoOS {
  reparacaoId: string;
  feita?: boolean;
}

export interface ItemPecaOS {
  pecaId: string;
  quantidade: number;
  usada?: boolean;
}

export interface ChecklistSeguranca {
  travoes: boolean;
  luzes: boolean;
  pneus: boolean;
  aceleracao: boolean;
  travagem: boolean;
  visor: boolean;
  testeConducao: boolean;
}

export type MetodoPagamento = "NUMERARIO" | "MULTIBANCO" | "MBWAY";

export interface OS {
  id: string;
  numero: string; // OS-2025-001
  clienteId: string;
  trotineteId: string;
  acessorios: string[];
  descricaoProblema: string;
  estado: EstadoOS;
  mecanicoId?: string;
  reparacoes: ItemReparacaoOS[];
  pecas: ItemPecaOS[];
  notasMecanico?: string;
  defeitoReportado?: string;
  checklist?: ChecklistSeguranca;
  metodoPagamento?: MetodoPagamento;
  criadaEm: string;
  atualizadaEm: string;
  criadaPor: string;
}

export type TipoMovimento = "SALARIO" | "GASTO_PECA" | "LUCRO_MAO_OBRA" | "LUCRO_PECA";

export interface MovimentoFinanceiro {
  id: string;
  tipo: TipoMovimento;
  valor: number;
  data: string;
  descricao: string;
  refId?: string;
}

export type TipoAlerta =
  | "ORCAMENTO_APROVAR"
  | "OS_PAGAMENTO"
  | "PECA_FALTA"
  | "DEFEITO_REPORTADO"
  | "STOCK_BAIXO";

export interface Alerta {
  id: string;
  tipo: TipoAlerta;
  mensagem: string;
  dataISO: string;
  lida: boolean;
  /** Marcado como tratado pelo destinatário. */
  tratada?: boolean;
  rota?: string;
  rolesDestino: Role[];
}

export interface HorasExtra {
  id: string;
  funcionarioId: string;
  data: string;
  horas: number;
}

export type EstadoPecaDefeituosa = "POR_TRATAR" | "ENVIADA_PARA_DEVOLUCAO";

/**
 * Peça reportada como defeituosa por um mecânico durante uma reparação.
 * Sai automaticamente do stock e fica numa lista no Stock,
 * à espera que o gestor de stock crie a devolução ao fornecedor.
 */
export interface PecaDefeituosa {
  id: string;
  pecaId: string;
  quantidade: number;
  numeroSerie?: string;
  motivo: string;
  osId?: string;
  reportadaPor: string; // funcionario id
  data: string;
  estado: EstadoPecaDefeituosa;
  /** Preenchido quando o gestor cria a devolução correspondente. */
  devolucaoId?: string;
}
