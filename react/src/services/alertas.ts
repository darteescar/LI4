import { getDB, persist, uid } from "@/lib/mock-db";
import type { Alerta, Role, TipoAlerta } from "@/lib/types";

const delay = () => new Promise((r) => setTimeout(r, 50));

export const TIPO_ALERTA_LABELS: Record<TipoAlerta, string> = {
  ORCAMENTO_APROVAR: "Orçamento por aprovar",
  OS_PAGAMENTO: "OS por cobrar",
  PECA_FALTA: "Peça em falta",
  DEFEITO_REPORTADO: "Defeito reportado",
  STOCK_BAIXO: "Stock baixo",
};

export const alertasService = {
  /** Lista alertas visíveis para um cargo. Se role indefinido, devolve todos. */
  async listForRole(role?: Role, opts?: { apenasNaoLidas?: boolean }): Promise<Alerta[]> {
    await delay();
    return getDB().alertas
      .filter((a) => (role ? a.rolesDestino.includes(role) : true))
      .filter((a) => (opts?.apenasNaoLidas ? !a.lida : true))
      .sort((a, b) => b.dataISO.localeCompare(a.dataISO));
  },

  async countUnread(role?: Role): Promise<number> {
    await delay();
    return getDB().alertas.filter((a) =>
      !a.lida && (role ? a.rolesDestino.includes(role) : true),
    ).length;
  },

  async marcarLida(id: string): Promise<void> {
    await delay();
    const a = getDB().alertas.find((x) => x.id === id);
    if (a) { a.lida = true; persist(); }
  },

  async marcarTratada(id: string, tratada = true): Promise<void> {
    await delay();
    const a = getDB().alertas.find((x) => x.id === id);
    if (a) { a.tratada = tratada; if (tratada) a.lida = true; persist(); }
  },

  async marcarTodasLidas(role?: Role): Promise<void> {
    await delay();
    getDB().alertas.forEach((a) => {
      if (!role || a.rolesDestino.includes(role)) a.lida = true;
    });
    persist();
  },

  async remover(id: string): Promise<void> {
    await delay();
    const db = getDB();
    db.alertas = db.alertas.filter((a) => a.id !== id);
    persist();
  },

  /**
   * Gera alertas automáticos para peças com stock abaixo do mínimo.
   * Evita duplicar alertas ainda não lidos para a mesma peça.
   */
  async gerarAlertasStockBaixo(): Promise<number> {
    await delay();
    const db = getDB();
    let criados = 0;
    db.pecas.forEach((p) => {
      if (p.stockAtual < p.stockMinimo) {
        const jaTem = db.alertas.some(
          (a) => a.tipo === "STOCK_BAIXO" && a.rota === `/stock/pecas` && !a.lida && a.mensagem.includes(p.referencia),
        );
        if (!jaTem) {
          db.alertas.unshift({
            id: uid("a"),
            tipo: "STOCK_BAIXO",
            mensagem: `Stock de ${p.nome} (${p.referencia}) abaixo do mínimo (${p.stockAtual}/${p.stockMinimo})`,
            dataISO: new Date().toISOString(),
            lida: false,
            rota: "/stock/pecas",
            rolesDestino: ["GESTOR_STOCK", "GERENTE"],
          });
          criados++;
        }
      }
    });
    if (criados > 0) persist();
    return criados;
  },

  /**
   * Cria um alerta de "possível defeito" para uma peça específica, dirigido ao
   * gestor de stock. Evita duplicados não-lidos.
   */
  async gerarAlertaPossivelDefeito(pecaId: string): Promise<void> {
    await delay();
    const db = getDB();
    const peca = db.pecas.find((p) => p.id === pecaId);
    if (!peca) return;
    const jaTem = db.alertas.some(
      (a) => a.tipo === "DEFEITO_REPORTADO" && !a.lida && a.mensagem.includes(peca.referencia),
    );
    if (jaTem) return;
    db.alertas.unshift({
      id: uid("a"),
      tipo: "DEFEITO_REPORTADO",
      mensagem: `Possível defeito em ${peca.nome} (${peca.referencia})`,
      dataISO: new Date().toISOString(),
      lida: false,
      rota: "/stock/defeitos",
      rolesDestino: ["GESTOR_STOCK", "GERENTE"],
    });
    persist();
  },
};
