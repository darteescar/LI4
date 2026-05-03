// In-memory mock DB. Persisted to localStorage so the app survives reloads.
import type {
  Funcionario, Cliente, Trotinete, Fornecedor, Peca, EntradaStock,
  Devolucao, Encomenda, Reparacao, OS, MovimentoFinanceiro, Alerta, HorasExtra,
  PecaDefeituosa,
} from "./types";

export interface DB {
  funcionarios: Funcionario[];
  clientes: Cliente[];
  trotinetes: Trotinete[];
  fornecedores: Fornecedor[];
  pecas: Peca[];
  entradas: EntradaStock[];
  devolucoes: Devolucao[];
  encomendas: Encomenda[];
  reparacoes: Reparacao[];
  ordens: OS[];
  movimentos: MovimentoFinanceiro[];
  alertas: Alerta[];
  horasExtra: HorasExtra[];
  pecasDefeituosas: PecaDefeituosa[];
}

const STORAGE_KEY = "oficina-mock-db-v2";

function seed(): DB {
  const funcionarios: Funcionario[] = [
    {
      id: "u-gerente", nome: "Ana Carvalho", identificador: "ana", email: "ana@oficina.pt",
      telemovel: "912345678", nif: "212345678", niss: "12345678901", nus: "987654321",
      iban: "PT50000201231234567890154", morada: "Rua A, 1, Lisboa", codigoPostal: "1000-001",
      cargo: "GERENTE", vencimentoHora: 12, vencimentoBruto: 2200, horasExtraAcumuladas: 0,
      dataAdmissao: "2020-01-15", ativo: true, password: "1234",
    },
    {
      id: "u-secretaria", nome: "Beatriz Lopes", identificador: "bea", email: "bea@oficina.pt",
      telemovel: "913000000", nif: "223456789", niss: "12345678902", nus: "987654322",
      iban: "PT50000201231234567890155", morada: "Rua B, 2, Lisboa", codigoPostal: "1000-002",
      cargo: "SECRETARIA", vencimentoHora: 7, vencimentoBruto: 1100, horasExtraAcumuladas: 0,
      dataAdmissao: "2021-03-10", ativo: true, password: "1234",
    },
    {
      id: "u-stock", nome: "Carlos Mendes", identificador: "carlos", email: "carlos@oficina.pt",
      telemovel: "914000000", nif: "234567890", niss: "12345678903", nus: "987654323",
      iban: "PT50000201231234567890156", morada: "Rua C, 3, Lisboa", codigoPostal: "1000-003",
      cargo: "GESTOR_STOCK", vencimentoHora: 8, vencimentoBruto: 1300, horasExtraAcumuladas: 0,
      dataAdmissao: "2021-06-01", ativo: true, password: "1234",
    },
    {
      id: "u-mecanico", nome: "Diogo Ferreira", identificador: "diogo", email: "diogo@oficina.pt",
      telemovel: "915000000", nif: "245678901", niss: "12345678904", nus: "987654324",
      iban: "PT50000201231234567890157", morada: "Rua D, 4, Lisboa", codigoPostal: "1000-004",
      cargo: "MECANICO", vencimentoHora: 8, vencimentoBruto: 1250, horasExtraAcumuladas: 0,
      dataAdmissao: "2022-09-15", ativo: true, password: "1234",
    },
  ];

  const fornecedores: Fornecedor[] = [
    { id: "f-1", nome: "TrotiPeças Lda", nif: "501234567", telemovel: "210000001", email: "geral@trotipecas.pt" },
    { id: "f-2", nome: "EletroMobile SA", nif: "501234568", telemovel: "210000002", email: "vendas@eletromobile.pt" },
  ];

  const pecas: Peca[] = [
    { id: "p-1", referencia: "BAT-36V", nome: "Bateria 36V", descricao: "Bateria de lítio 36V 10Ah", fornecedorId: "f-2", tipo: "Bateria", precoVenda: 180, stockAtual: 5, stockMinimo: 3 },
    { id: "p-2", referencia: "PNEU-8.5", nome: "Pneu 8.5\"", descricao: "Pneu tubeless 8.5 polegadas", fornecedorId: "f-1", tipo: "Pneu", precoVenda: 30, stockAtual: 12, stockMinimo: 6 },
    { id: "p-3", referencia: "TRV-DISC", nome: "Travão de disco", descricao: "Conjunto travão disco", fornecedorId: "f-1", tipo: "Travão", precoVenda: 60, stockAtual: 2, stockMinimo: 4 },
    { id: "p-4", referencia: "MOT-350W", nome: "Motor 350W", descricao: "Motor brushless 350W", fornecedorId: "f-2", tipo: "Motor", precoVenda: 150, stockAtual: 4, stockMinimo: 2 },
    { id: "p-5", referencia: "VIS-LCD", nome: "Visor LCD", descricao: "Visor LCD com botões", fornecedorId: "f-2", tipo: "Eletrónica", precoVenda: 40, stockAtual: 0, stockMinimo: 2 },
  ];

  const reparacoes: Reparacao[] = [
    { id: "r-1", nomenclatura: "Troca de bateria", descricao: "Substituição completa da bateria", preco: 25 },
    { id: "r-2", nomenclatura: "Troca de pneu", descricao: "Substituição de pneu dianteiro/traseiro", preco: 15 },
    { id: "r-3", nomenclatura: "Reparação travões", descricao: "Afinação e troca de calços", preco: 20 },
    { id: "r-4", nomenclatura: "Diagnóstico geral", descricao: "Inspeção completa", preco: 10 },
    { id: "r-5", nomenclatura: "Reparação eletrónica", descricao: "Reparação de placa controladora", preco: 40 },
  ];

  const clientes: Cliente[] = [
    { id: "c-1", nome: "Pedro Santos", nif: "201234567", telemovel: "961111111", email: "pedro@mail.pt", morada: "Av. da Liberdade, 100", codigoPostal: "1250-001", criadoEm: new Date().toISOString() },
    { id: "c-2", nome: "Maria Oliveira", nif: "201234568", telemovel: "962222222", email: "maria@mail.pt", morada: "Rua Augusta, 50", codigoPostal: "1100-053", criadoEm: new Date().toISOString() },
  ];

  const trotinetes: Trotinete[] = [
    { id: "t-1", clienteId: "c-1", marca: "Xiaomi", modelo: "M365 Pro", numeroSerie: "XM365P-00001", motor: "Brushless" },
    { id: "t-2", clienteId: "c-2", marca: "Segway", modelo: "Ninebot G30", numeroSerie: "SG-G30-00002", motor: "Brushless" },
  ];

  return {
    funcionarios, clientes, trotinetes, fornecedores, pecas, reparacoes,
    entradas: [], devolucoes: [], encomendas: [], ordens: [], movimentos: [], alertas: [], horasExtra: [],
    pecasDefeituosas: [],
  };
}

let _db: DB | null = null;

export function getDB(): DB {
  if (_db) return _db;
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (raw) {
      const parsed = JSON.parse(raw) as Partial<DB>;
      _db = {
        ...seed(),
        ...parsed,
        pecasDefeituosas: parsed.pecasDefeituosas ?? [],
      } as DB;
      // Migração: garantir que campos novos existem em funcionários
      _db.funcionarios = _db.funcionarios.map((f) => ({
        ...f,
        vencimentoHora: f.vencimentoHora ?? 0,
        vencimentoBruto: f.vencimentoBruto ?? 0,
        horasExtraAcumuladas: f.horasExtraAcumuladas ?? 0,
        password: f.password ?? "1234",
      }));
      // Migração: garantir tipo nas peças
      _db.pecas = _db.pecas.map((p) => ({ ...p, tipo: p.tipo ?? "Outro" }));
      // Migração: alertas com flag tratada
      _db.alertas = (_db.alertas ?? []).map((a) => ({ ...a, tratada: a.tratada ?? false }));
      persist();
      return _db;
    }
  } catch {}
  _db = seed();
  persist();
  return _db;
}

export function persist() {
  if (!_db) return;
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(_db));
  } catch {}
}

export function resetDB() {
  _db = seed();
  persist();
}

export function uid(prefix = "id"): string {
  return `${prefix}-${Date.now().toString(36)}-${Math.random().toString(36).slice(2, 7)}`;
}
