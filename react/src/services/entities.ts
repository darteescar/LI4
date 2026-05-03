// Generic CRUD helpers + per-entity services. All async to mimic future REST API.
import { getDB, persist, uid } from "@/lib/mock-db";
import type {
  Funcionario, Cliente, Trotinete, Fornecedor, Peca, Reparacao,
  EntradaStock, HorasExtra,
} from "@/lib/types";

const delay = () => new Promise((r) => setTimeout(r, 80));

// ---------- Funcionários
export const funcionariosService = {
  async list(): Promise<Funcionario[]> { await delay(); return [...getDB().funcionarios]; },
  async create(data: Omit<Funcionario, "id" | "ativo">): Promise<Funcionario> {
    await delay();
    const f: Funcionario = { id: uid("u"), ativo: true, ...data };
    getDB().funcionarios.push(f); persist(); return f;
  },
  async update(id: string, data: Partial<Funcionario>): Promise<Funcionario> {
    await delay();
    const arr = getDB().funcionarios;
    const i = arr.findIndex((x) => x.id === id);
    if (i < 0) throw new Error("Não encontrado");
    arr[i] = { ...arr[i], ...data };
    persist(); return arr[i];
  },
  async remove(id: string): Promise<void> {
    await delay();
    const db = getDB();
    db.funcionarios = db.funcionarios.filter((x) => x.id !== id);
    persist();
  },
  async addHorasExtra(data: Omit<HorasExtra, "id">): Promise<HorasExtra> {
    await delay();
    const h: HorasExtra = { id: uid("he"), ...data };
    const db = getDB();
    db.horasExtra.push(h);
    // Acumular no funcionário
    const f = db.funcionarios.find((x) => x.id === data.funcionarioId);
    if (f) f.horasExtraAcumuladas = (f.horasExtraAcumuladas ?? 0) + data.horas;
    persist();
    return h;
  },
  async listHorasExtra(funcionarioId?: string): Promise<HorasExtra[]> {
    await delay();
    const all = getDB().horasExtra;
    return funcionarioId ? all.filter((h) => h.funcionarioId === funcionarioId) : [...all];
  },
};

// ---------- Clientes
export const clientesService = {
  async list(): Promise<Cliente[]> { await delay(); return [...getDB().clientes]; },
  async get(id: string): Promise<Cliente | null> {
    await delay();
    return getDB().clientes.find((c) => c.id === id) ?? null;
  },
  async create(data: Omit<Cliente, "id" | "criadoEm">): Promise<Cliente> {
    await delay();
    const c: Cliente = { id: uid("c"), criadoEm: new Date().toISOString(), ...data };
    getDB().clientes.push(c); persist(); return c;
  },
  async update(id: string, data: Partial<Cliente>): Promise<Cliente> {
    await delay();
    const arr = getDB().clientes;
    const i = arr.findIndex((x) => x.id === id);
    if (i < 0) throw new Error("Não encontrado");
    arr[i] = { ...arr[i], ...data };
    persist(); return arr[i];
  },
  async remove(id: string): Promise<void> {
    await delay();
    const db = getDB();
    db.clientes = db.clientes.filter((x) => x.id !== id);
    db.trotinetes = db.trotinetes.filter((t) => t.clienteId !== id);
    persist();
  },
};

// ---------- Trotinetes
export const trotinetesService = {
  async list(): Promise<Trotinete[]> { await delay(); return [...getDB().trotinetes]; },
  async byCliente(clienteId: string): Promise<Trotinete[]> {
    await delay();
    return getDB().trotinetes.filter((t) => t.clienteId === clienteId);
  },
  async create(data: Omit<Trotinete, "id">): Promise<Trotinete> {
    await delay();
    const t: Trotinete = { id: uid("t"), ...data };
    getDB().trotinetes.push(t); persist(); return t;
  },
  async update(id: string, data: Partial<Trotinete>): Promise<Trotinete> {
    await delay();
    const arr = getDB().trotinetes;
    const i = arr.findIndex((x) => x.id === id);
    if (i < 0) throw new Error("Não encontrado");
    arr[i] = { ...arr[i], ...data };
    persist(); return arr[i];
  },
  async remove(id: string): Promise<void> {
    await delay();
    const db = getDB();
    db.trotinetes = db.trotinetes.filter((x) => x.id !== id);
    persist();
  },
};

// ---------- Fornecedores
export const fornecedoresService = {
  async list(): Promise<Fornecedor[]> { await delay(); return [...getDB().fornecedores]; },
  async create(data: Omit<Fornecedor, "id">): Promise<Fornecedor> {
    await delay();
    const f: Fornecedor = { id: uid("f"), ...data };
    getDB().fornecedores.push(f); persist(); return f;
  },
  async update(id: string, data: Partial<Fornecedor>): Promise<Fornecedor> {
    await delay();
    const arr = getDB().fornecedores;
    const i = arr.findIndex((x) => x.id === id);
    if (i < 0) throw new Error("Não encontrado");
    arr[i] = { ...arr[i], ...data };
    persist(); return arr[i];
  },
  async remove(id: string): Promise<void> {
    await delay();
    const db = getDB();
    db.fornecedores = db.fornecedores.filter((x) => x.id !== id);
    persist();
  },
};

// ---------- Peças
export const pecasService = {
  async list(): Promise<Peca[]> { await delay(); return [...getDB().pecas]; },
  async create(data: Omit<Peca, "id" | "stockAtual"> & { stockAtual?: number }): Promise<Peca> {
    await delay();
    const p: Peca = { id: uid("p"), stockAtual: data.stockAtual ?? 0, ...data };
    getDB().pecas.push(p); persist(); return p;
  },
  async update(id: string, data: Partial<Peca>): Promise<Peca> {
    await delay();
    const arr = getDB().pecas;
    const i = arr.findIndex((x) => x.id === id);
    if (i < 0) throw new Error("Não encontrado");
    arr[i] = { ...arr[i], ...data };
    persist(); return arr[i];
  },
  async remove(id: string): Promise<void> {
    await delay();
    const db = getDB();
    db.pecas = db.pecas.filter((x) => x.id !== id);
    persist();
  },
};

// ---------- Entradas Stock
export const entradasService = {
  async list(): Promise<EntradaStock[]> { await delay(); return [...getDB().entradas]; },
  async create(data: Omit<EntradaStock, "id">): Promise<EntradaStock> {
    await delay();
    if (data.precoUnitario > 70) {
      if (!data.unidades || data.unidades.length !== data.quantidade) {
        throw new Error(
          "Para peças acima de 70€ é obrigatório registar nº de série e garantia para cada unidade",
        );
      }
      const seen = new Set<string>();
      for (const u of data.unidades) {
        if (!u.numeroSerie?.trim()) throw new Error("Nº de série em falta numa unidade");
        if (seen.has(u.numeroSerie)) throw new Error(`Nº de série duplicado: ${u.numeroSerie}`);
        seen.add(u.numeroSerie);
      }
    }
    const e: EntradaStock = { id: uid("e"), ...data };
    const db = getDB();
    db.entradas.push(e);
    const peca = db.pecas.find((p) => p.id === data.pecaId);
    if (peca) {
      peca.stockAtual += data.quantidade;
    }
    // Movimento financeiro: gasto na compra de peças
    db.movimentos.push({
      id: uid("mv"),
      tipo: "GASTO_PECA",
      valor: data.precoUnitario * data.quantidade,
      data: data.data,
      descricao: `Compra ${peca?.nome ?? "peça"}${peca ? ` (${peca.referencia})` : ""} × ${data.quantidade}`,
      refId: e.id,
    });
    persist();
    // Auto-verificar alertas de stock baixo (importação dinâmica para evitar ciclo)
    const { alertasService } = await import("./alertas");
    await alertasService.gerarAlertasStockBaixo();
    return e;
  },
};

// ---------- Reparações
export const reparacoesService = {
  async list(): Promise<Reparacao[]> { await delay(); return [...getDB().reparacoes]; },
  async create(data: Omit<Reparacao, "id">): Promise<Reparacao> {
    await delay();
    const r: Reparacao = { id: uid("r"), ...data };
    getDB().reparacoes.push(r); persist(); return r;
  },
  async update(id: string, data: Partial<Reparacao>): Promise<Reparacao> {
    await delay();
    const arr = getDB().reparacoes;
    const i = arr.findIndex((x) => x.id === id);
    if (i < 0) throw new Error("Não encontrado");
    arr[i] = { ...arr[i], ...data };
    persist(); return arr[i];
  },
  async remove(id: string): Promise<void> {
    await delay();
    const db = getDB();
    db.reparacoes = db.reparacoes.filter((x) => x.id !== id);
    persist();
  },
};
