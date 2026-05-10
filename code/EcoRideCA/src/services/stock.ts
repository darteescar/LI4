import { getDB, persist, uid } from "@/lib/mock-db";
import type {
  Devolucao, EstadoDevolucao, Encomenda, EstadoEncomenda, ItemEncomenda, Alerta,
  PecaDefeituosa,
} from "@/lib/types";

const delay = () => new Promise((r) => setTimeout(r, 80));

function pushAlerta(a: Omit<Alerta, "id" | "dataISO" | "lida">) {
  getDB().alertas.unshift({
    id: uid("a"),
    dataISO: new Date().toISOString(),
    lida: false,
    ...a,
  });
}

// ---------- Devoluções
export const devolucoesService = {
  async list(): Promise<Devolucao[]> {
    await delay();
    return [...getDB().devolucoes].sort((a, b) => b.data.localeCompare(a.data));
  },
  async create(data: Omit<Devolucao, "id" | "estado" | "data">): Promise<Devolucao> {
    await delay();
    const d: Devolucao = {
      id: uid("dv"),
      estado: "PENDENTE",
      data: new Date().toISOString(),
      ...data,
    };
    getDB().devolucoes.push(d);
    persist();
    return d;
  },
  async update(id: string, patch: Partial<Devolucao>): Promise<Devolucao> {
    await delay();
    const arr = getDB().devolucoes;
    const i = arr.findIndex((d) => d.id === id);
    if (i < 0) throw new Error("Devolução não encontrada");
    arr[i] = { ...arr[i], ...patch };
    persist();
    return arr[i];
  },
  async setEstado(id: string, estado: EstadoDevolucao): Promise<Devolucao> {
    const dv = await this.update(id, { estado });
    if (estado === "DEVOLVIDA") {
      // descontar do stock atual (peça saiu da oficina)
      const db = getDB();
      const peca = db.pecas.find((p) => p.id === dv.pecaId);
      if (peca) peca.stockAtual = Math.max(0, peca.stockAtual - dv.quantidade);
      persist();
    }
    return dv;
  },
  async remove(id: string): Promise<void> {
    await delay();
    const db = getDB();
    db.devolucoes = db.devolucoes.filter((d) => d.id !== id);
    persist();
  },
};

// ---------- Encomendas
export interface SugestaoEncomenda {
  fornecedorId: string;
  itens: ItemEncomenda[];
}

export const encomendasService = {
  async list(): Promise<Encomenda[]> {
    await delay();
    return [...getDB().encomendas].sort((a, b) => b.criadaEm.localeCompare(a.criadaEm));
  },
  async get(id: string): Promise<Encomenda | null> {
    await delay();
    return getDB().encomendas.find((e) => e.id === id) ?? null;
  },
  /**
   * Análise automática: peças cujo stockAtual < stockMinimo,
   * agrupadas por fornecedor, com sugestão de quantidade e preço (último preço de compra).
   */
  async gerarSugestoes(): Promise<SugestaoEncomenda[]> {
    await delay();
    const db = getDB();
    const map = new Map<string, ItemEncomenda[]>();
    db.pecas
      .filter((p) => p.stockAtual < p.stockMinimo)
      .forEach((p) => {
        const necessario = Math.max(p.stockMinimo - p.stockAtual, p.stockMinimo);
        // Último preço de compra (da última entrada) ou preço de venda como fallback
        const ultimaEntrada = [...db.entradas]
          .filter((e) => e.pecaId === p.id)
          .sort((a, b) => b.data.localeCompare(a.data))[0];
        const item: ItemEncomenda = {
          pecaId: p.id,
          quantidade: necessario,
          precoUnitario: ultimaEntrada?.precoUnitario ?? 0,
        };
        const arr = map.get(p.fornecedorId) ?? [];
        arr.push(item);
        map.set(p.fornecedorId, arr);
      });
    return Array.from(map.entries()).map(([fornecedorId, itens]) => ({ fornecedorId, itens }));
  },
  async create(data: {
    fornecedorId: string;
    itens: ItemEncomenda[];
    criadaPor: string;
  }): Promise<Encomenda> {
    await delay();
    const e: Encomenda = {
      id: uid("enc"),
      fornecedorId: data.fornecedorId,
      itens: data.itens,
      estado: "RASCUNHO",
      criadaEm: new Date().toISOString(),
      criadaPor: data.criadaPor,
    };
    getDB().encomendas.push(e);
    persist();
    return e;
  },
  async update(id: string, patch: Partial<Encomenda>): Promise<Encomenda> {
    await delay();
    const arr = getDB().encomendas;
    const i = arr.findIndex((e) => e.id === id);
    if (i < 0) throw new Error("Encomenda não encontrada");
    arr[i] = { ...arr[i], ...patch };
    persist();
    return arr[i];
  },
  async setEstado(
    id: string,
    estado: EstadoEncomenda,
    seriesPorPeca?: Record<string, { numeroSerie: string; garantiaMeses: number }[]>,
  ): Promise<Encomenda> {
    const cur = getDB().encomendas.find((e) => e.id === id);
    if (!cur) throw new Error("Encomenda não encontrada");
    const patch: Partial<Encomenda> = { estado };
    if (estado === "ENVIADA" && !cur.enviadaEm) patch.enviadaEm = new Date().toISOString();
    if (estado === "RECEBIDA") {
      // Validar séries para itens > 70€
      for (const it of cur.itens) {
        if (it.precoUnitario > 70) {
          const series = seriesPorPeca?.[it.pecaId];
          if (!series || series.length !== it.quantidade) {
            throw new Error(
              "Para peças acima de 70€ é obrigatório registar nº de série e garantia para cada unidade",
            );
          }
          const seen = new Set<string>();
          for (const u of series) {
            if (!u.numeroSerie?.trim()) throw new Error("Nº de série em falta numa unidade");
            if (seen.has(u.numeroSerie)) throw new Error(`Nº de série duplicado: ${u.numeroSerie}`);
            seen.add(u.numeroSerie);
          }
        }
      }
      patch.recebidaEm = new Date().toISOString();
      const db = getDB();
      cur.itens.forEach((it) => {
        const series = seriesPorPeca?.[it.pecaId];
        db.entradas.push({
          id: uid("e"),
          pecaId: it.pecaId,
          quantidade: it.quantidade,
          precoUnitario: it.precoUnitario,
          data: new Date().toISOString(),
          unidades: series && series.length === it.quantidade ? series : undefined,
          registadoPor: cur.criadaPor,
        });
        const peca = db.pecas.find((p) => p.id === it.pecaId);
        if (peca) {
          peca.stockAtual += it.quantidade;
        }
        // Movimento financeiro: gasto na compra
        db.movimentos.push({
          id: uid("mv"),
          tipo: "GASTO_PECA",
          valor: it.precoUnitario * it.quantidade,
          data: new Date().toISOString(),
          descricao: `Compra ${peca?.nome ?? "peça"}${peca ? ` (${peca.referencia})` : ""} × ${it.quantidade} (encomenda)`,
          refId: cur.id,
        });
      });
    }
    return this.update(id, patch);
  },
  async remove(id: string): Promise<void> {
    await delay();
    const db = getDB();
    const enc = db.encomendas.find((e) => e.id === id);
    if (!enc) return;
    if (enc.estado !== "RASCUNHO") {
      throw new Error("Só é possível eliminar encomendas em rascunho");
    }
    db.encomendas = db.encomendas.filter((e) => e.id !== id);
    persist();
  },
};

// ---------- Peças defeituosas
export interface ReportarDefeitoInput {
  pecaId: string;
  quantidade: number;
  motivo: string;
  numeroSerie?: string;
  osId?: string;
  reportadaPor: string;
}

export const pecasDefeituosasService = {
  async list(): Promise<PecaDefeituosa[]> {
    await delay();
    return [...getDB().pecasDefeituosas].sort((a, b) => b.data.localeCompare(a.data));
  },
  /**
   * Mecânico reporta uma peça com defeito numa OS:
   *  - retira a quantidade do stock (mesmo que vá a zero)
   *  - cria registo POR_TRATAR para o gestor de stock processar
   */
  async reportar(data: ReportarDefeitoInput): Promise<PecaDefeituosa> {
    await delay();
    const db = getDB();
    const peca = db.pecas.find((p) => p.id === data.pecaId);
    if (!peca) throw new Error("Peça não encontrada");
    if (data.quantidade < 1) throw new Error("Quantidade inválida");
    peca.stockAtual = Math.max(0, peca.stockAtual - data.quantidade);
    const def: PecaDefeituosa = {
      id: uid("pd"),
      pecaId: data.pecaId,
      quantidade: data.quantidade,
      numeroSerie: data.numeroSerie?.trim() || undefined,
      motivo: data.motivo.trim(),
      osId: data.osId,
      reportadaPor: data.reportadaPor,
      data: new Date().toISOString(),
      estado: "POR_TRATAR",
    };
    db.pecasDefeituosas.unshift(def);
    pushAlerta({
      tipo: "DEFEITO_REPORTADO",
      mensagem: `Peça defeituosa: ${peca.referencia} — ${peca.nome}${def.numeroSerie ? ` (S/N ${def.numeroSerie})` : ""}`,
      rota: "/stock/defeitos",
      rolesDestino: ["GESTOR_STOCK", "GERENTE"],
    });
    persist();
    return def;
  },
  /**
   * Gestor de stock cria uma devolução a partir de uma peça defeituosa.
   * Não desconta stock outra vez (já saiu quando o mecânico reportou).
   */
  async submeterDevolucao(id: string, fornecedorId: string): Promise<Devolucao> {
    await delay();
    const db = getDB();
    const def = db.pecasDefeituosas.find((d) => d.id === id);
    if (!def) throw new Error("Registo não encontrado");
    if (def.estado === "ENVIADA_PARA_DEVOLUCAO") {
      throw new Error("Esta peça já foi enviada para devolução");
    }
    const dv: Devolucao = {
      id: uid("dv"),
      pecaId: def.pecaId,
      quantidade: def.quantidade,
      motivo: `[Defeito reportado] ${def.motivo}${def.numeroSerie ? ` · S/N ${def.numeroSerie}` : ""}`,
      estado: "PENDENTE",
      fornecedorId,
      data: new Date().toISOString(),
    };
    db.devolucoes.push(dv);
    def.estado = "ENVIADA_PARA_DEVOLUCAO";
    def.devolucaoId = dv.id;
    persist();
    return dv;
  },
  async remove(id: string): Promise<void> {
    await delay();
    const db = getDB();
    db.pecasDefeituosas = db.pecasDefeituosas.filter((d) => d.id !== id);
    persist();
  },
};
export function checkStockAlerts() {
  const db = getDB();
  db.pecas.forEach((p) => {
    if (p.stockAtual < p.stockMinimo) {
      const jaExiste = db.alertas.some(
        (a) => a.tipo === "STOCK_BAIXO" && !a.lida && a.mensagem.includes(p.referencia),
      );
      if (!jaExiste) {
        pushAlerta({
          tipo: "STOCK_BAIXO",
          mensagem: `Stock baixo: ${p.referencia} — ${p.nome} (${p.stockAtual}/${p.stockMinimo})`,
          rota: "/stock/pecas",
          rolesDestino: ["GESTOR_STOCK", "GERENTE"],
        });
      }
    }
  });
  persist();
}
