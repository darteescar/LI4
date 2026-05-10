import { getDB, persist, uid } from "@/lib/mock-db";
import type {
  OS, EstadoOS, ItemReparacaoOS, ItemPecaOS, ChecklistSeguranca,
  MetodoPagamento, Alerta,
} from "@/lib/types";

const delay = () => new Promise((r) => setTimeout(r, 80));

function nextNumero(): string {
  const ano = new Date().getFullYear();
  const db = getDB();
  const n = db.ordens.filter((o) => o.numero.startsWith(`OS-${ano}-`)).length + 1;
  return `OS-${ano}-${String(n).padStart(3, "0")}`;
}

function pushAlerta(a: Omit<Alerta, "id" | "dataISO" | "lida">) {
  getDB().alertas.unshift({
    id: uid("a"),
    dataISO: new Date().toISOString(),
    lida: false,
    ...a,
  });
}

export interface CreateOSInput {
  clienteId: string;
  trotineteId: string;
  acessorios: string[];
  fotos: string[];
  descricaoProblema: string;
  criadaPor: string;
}

export const osService = {
  async list(): Promise<OS[]> {
    await delay();
    // Ordenar da mais antiga para a mais recente
    return [...getDB().ordens].sort((a, b) => a.criadaEm.localeCompare(b.criadaEm));
  },
  async get(id: string): Promise<OS | null> {
    await delay();
    return getDB().ordens.find((o) => o.id === id) ?? null;
  },
  /**
   * Devolve as OS antigas (paga/cancelada/em curso) de uma trotinete,
   * excluindo a OS actual. Apenas para consulta histórica.
   */
  async listByTrotinete(trotineteId: string, excludeId?: string): Promise<OS[]> {
    await delay();
    return getDB().ordens
      .filter((o) => o.trotineteId === trotineteId && o.id !== excludeId)
      .sort((a, b) => b.criadaEm.localeCompare(a.criadaEm));
  },
  /**
   * Verifica se um cliente tem OS *anteriores* (criadas antes da actual)
   * em "AGUARDA_PAGAMENTO". Apenas as mais antigas bloqueiam — assim a OS
   * mais antiga consegue sempre ser paga.
   */
  async pagamentosEmAtraso(clienteId: string, excludeId?: string): Promise<OS[]> {
    await delay();
    const db = getDB();
    const atual = excludeId ? db.ordens.find((o) => o.id === excludeId) : undefined;
    return db.ordens
      .filter((o) =>
        o.clienteId === clienteId &&
        o.id !== excludeId &&
        o.estado === "AGUARDA_PAGAMENTO" &&
        (!atual || o.criadaEm < atual.criadaEm),
      )
      .sort((a, b) => a.criadaEm.localeCompare(b.criadaEm));
  },
  async create(data: CreateOSInput): Promise<OS> {
    await delay();
    const now = new Date().toISOString();
    const os: OS = {
      id: uid("os"),
      numero: nextNumero(),
      clienteId: data.clienteId,
      trotineteId: data.trotineteId,
      acessorios: data.acessorios,
      fotos: data.fotos,
      descricaoProblema: data.descricaoProblema,
      estado: "REGISTADA",
      reparacoes: [],
      pecas: [],
      criadaEm: now,
      atualizadaEm: now,
      criadaPor: data.criadaPor,
    };
    getDB().ordens.push(os);
    persist();
    return os;
  },
  async update(id: string, patch: Partial<OS>): Promise<OS> {
    await delay();
    const arr = getDB().ordens;
    const i = arr.findIndex((o) => o.id === id);
    if (i < 0) throw new Error("OS não encontrada");
    arr[i] = { ...arr[i], ...patch, atualizadaEm: new Date().toISOString() };
    persist();
    return arr[i];
  },
  async remove(id: string): Promise<void> {
    await delay();
    const db = getDB();
    db.ordens = db.ordens.filter((o) => o.id !== id);
    persist();
  },
  async setEstado(id: string, estado: EstadoOS): Promise<OS> {
    return this.update(id, { estado });
  },
  /**
   * Auto-atribuição: o mecânico "pega" numa OS REGISTADA que ainda não tenha dono.
   * Operação atómica — se outro mecânico já a apanhou, devolve erro.
   */
  async pegarOS(id: string, mecanicoId: string): Promise<OS> {
    await delay();
    const arr = getDB().ordens;
    const i = arr.findIndex((o) => o.id === id);
    if (i < 0) throw new Error("OS não encontrada");
    const cur = arr[i];
    if (cur.mecanicoId && cur.mecanicoId !== mecanicoId) {
      throw new Error("Esta OS já foi atribuída a outro mecânico");
    }
    if (cur.estado !== "REGISTADA") {
      throw new Error("Só é possível pegar em OS no estado Registada");
    }
    arr[i] = {
      ...cur,
      mecanicoId,
      estado: "EM_DIAGNOSTICO",
      atualizadaEm: new Date().toISOString(),
    };
    persist();
    return arr[i];
  },
  async submeterDiagnostico(
    id: string,
    reparacoes: ItemReparacaoOS[],
    pecas: ItemPecaOS[],
    notas?: string,
  ): Promise<OS> {
    const updated = await this.update(id, {
      reparacoes, pecas, notasMecanico: notas, estado: "AGUARDA_APROVACAO",
    });
    pushAlerta({
      tipo: "ORCAMENTO_APROVAR",
      mensagem: `Orçamento da ${updated.numero} aguarda aprovação do cliente`,
      rota: `/os/${updated.id}`,
      rolesDestino: ["SECRETARIA", "GERENTE"],
    });
    return updated;
  },
  async aprovarOrcamento(id: string): Promise<OS> {
    return this.setEstado(id, "APROVADA");
  },
  async iniciarReparacao(id: string): Promise<OS> {
    return this.setEstado(id, "EM_REPARACAO");
  },
  /**
   * Durante a reparação, o mecânico detectou novas peças/reparações necessárias
   * que não estavam no diagnóstico inicial. A OS volta para AGUARDA_APROVACAO
   * para o cliente aprovar o orçamento revisto.
   */
  async adicionarExtras(
    id: string,
    reparacoes: ItemReparacaoOS[],
    pecas: ItemPecaOS[],
    notas?: string,
  ): Promise<OS> {
    const updated = await this.update(id, {
      reparacoes, pecas,
      notasMecanico: notas,
      estado: "AGUARDA_APROVACAO",
    });
    pushAlerta({
      tipo: "ORCAMENTO_APROVAR",
      mensagem: `Orçamento revisto da ${updated.numero} aguarda nova aprovação`,
      rota: `/os/${updated.id}`,
      rolesDestino: ["SECRETARIA", "GERENTE"],
    });
    return updated;
  },
  async requisitarPecas(id: string): Promise<OS> {
    const os = await this.get(id);
    if (os) {
      pushAlerta({
        tipo: "PECA_FALTA",
        mensagem: `${os.numero} requer peças em falta`,
        rota: `/stock/encomendas`,
        rolesDestino: ["GESTOR_STOCK", "GERENTE"],
      });
    }
    return this.setEstado(id, "AGUARDA_PECAS");
  },
  /**
   * Defeito de uma peça reportado durante a reparação.
   * Cria registo de peça defeituosa (que retira do stock) via pecasDefeituosasService
   * e regista a descrição na própria OS para histórico.
   */
  async reportarDefeito(
    id: string,
    data: { pecaId: string; quantidade: number; motivo: string; numeroSerie?: string; reportadaPor: string },
  ): Promise<OS> {
    const { pecasDefeituosasService } = await import("./stock");
    await pecasDefeituosasService.reportar({
      pecaId: data.pecaId,
      quantidade: data.quantidade,
      motivo: data.motivo,
      numeroSerie: data.numeroSerie,
      osId: id,
      reportadaPor: data.reportadaPor,
    });
    const cur = await this.get(id);
    const prev = cur?.defeitoReportado ? `${cur.defeitoReportado}\n` : "";
    const linha = `${new Date().toLocaleString("pt-PT")} — ${data.quantidade}× peça (${data.motivo})${data.numeroSerie ? ` [S/N ${data.numeroSerie}]` : ""}`;
    return this.update(id, { defeitoReportado: `${prev}${linha}` });
  },
  async concluirReparacao(
    id: string,
    reparacoes: ItemReparacaoOS[],
    pecas: ItemPecaOS[],
    checklist: ChecklistSeguranca,
  ): Promise<OS> {
    // descontar stock das peças usadas
    const db = getDB();
    pecas.forEach((ip) => {
      if (ip.usada) {
        const p = db.pecas.find((x) => x.id === ip.pecaId);
        if (p) p.stockAtual = Math.max(0, p.stockAtual - ip.quantidade);
      }
    });
    const updated = await this.update(id, {
      reparacoes, pecas, checklist, estado: "CONCLUIDA",
    });
    pushAlerta({
      tipo: "OS_PAGAMENTO",
      mensagem: `${updated.numero} concluída — notificar cliente para pagamento`,
      rota: `/os/${updated.id}`,
      rolesDestino: ["SECRETARIA", "GERENTE"],
    });
    return updated;
  },
  async marcarAguardaPagamento(id: string): Promise<OS> {
    return this.setEstado(id, "AGUARDA_PAGAMENTO");
  },
  async registarPagamento(id: string, metodo: MetodoPagamento): Promise<OS> {
    const os = await this.get(id);
    if (os) {
      // Bloquear se houver pagamentos anteriores em atraso para o mesmo cliente
      const atraso = await this.pagamentosEmAtraso(os.clienteId, os.id);
      if (atraso.length > 0) {
        const nums = atraso.map((o) => o.numero).join(", ");
        throw new Error(
          `Cliente tem OS por pagar em atraso: ${nums}. É necessário liquidar essas OS antes de pagar a actual.`,
        );
      }
    }
    const updated = await this.update(id, { metodoPagamento: metodo, estado: "PAGA" });
    if (os) {
      const db = getDB();
      // movimentos financeiros
      let totalMaoObra = 0;
      let totalPecasVenda = 0;
      os.reparacoes.forEach((ir) => {
        const r = db.reparacoes.find((x) => x.id === ir.reparacaoId);
        if (r && ir.feita) totalMaoObra += r.preco;
      });
      os.pecas.forEach((ip) => {
        const p = db.pecas.find((x) => x.id === ip.pecaId);
        if (p && ip.usada) {
          totalPecasVenda += p.precoVenda * ip.quantidade;
        }
      });
      const now = new Date().toISOString();
      if (totalMaoObra > 0) {
        db.movimentos.push({
          id: uid("mv"), tipo: "LUCRO_MAO_OBRA", valor: totalMaoObra,
          data: now, descricao: `Mão de obra ${updated.numero}`, refId: updated.id,
        });
      }
      if (totalPecasVenda > 0) {
        db.movimentos.push({
          id: uid("mv"), tipo: "LUCRO_PECA", valor: totalPecasVenda,
          data: now, descricao: `Venda de peças ${updated.numero}`, refId: updated.id,
        });
      }
      persist();
    }
    return updated;
  },
  async cancelar(id: string): Promise<OS> {
    return this.setEstado(id, "CANCELADA");
  },
};

export function calcOrcamento(
  reparacoes: ItemReparacaoOS[],
  pecas: ItemPecaOS[],
): { maoObra: number; pecas: number; total: number } {
  const db = getDB();
  let maoObra = 0;
  let pecasTotal = 0;
  reparacoes.forEach((ir) => {
    const r = db.reparacoes.find((x) => x.id === ir.reparacaoId);
    if (r) maoObra += r.preco;
  });
  pecas.forEach((ip) => {
    const p = db.pecas.find((x) => x.id === ip.pecaId);
    if (p) pecasTotal += p.precoVenda * ip.quantidade;
  });
  return { maoObra, pecas: pecasTotal, total: maoObra + pecasTotal };
}
