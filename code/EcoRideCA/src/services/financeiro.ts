import { getDB, persist, uid } from "@/lib/mock-db";
import type { MovimentoFinanceiro, TipoMovimento } from "@/lib/types";

const delay = () => new Promise((r) => setTimeout(r, 60));

export interface FinanceiroFiltro {
  desde?: string; // ISO date
  ate?: string;   // ISO date
  tipos?: TipoMovimento[];
}

export interface FinanceiroResumo {
  receitas: number;     // LUCRO_MAO_OBRA + LUCRO_PECA (margem)
  despesas: number;     // SALARIO + GASTO_PECA
  saldo: number;        // receitas - despesas
  porTipo: Record<TipoMovimento, number>;
}

export const TIPO_LABELS: Record<TipoMovimento, string> = {
  LUCRO_MAO_OBRA: "Mão de obra",
  LUCRO_PECA: "Venda de peças",
  SALARIO: "Salário",
  GASTO_PECA: "Compra de peças",
};

export const TIPO_SINAL: Record<TipoMovimento, "+" | "-"> = {
  LUCRO_MAO_OBRA: "+",
  LUCRO_PECA: "+",
  SALARIO: "-",
  GASTO_PECA: "-",
};

export const financeiroService = {
  async list(filtro: FinanceiroFiltro = {}): Promise<MovimentoFinanceiro[]> {
    await delay();
    const desde = filtro.desde ? new Date(filtro.desde).getTime() : -Infinity;
    const ate = filtro.ate
      ? new Date(filtro.ate).getTime() + 24 * 60 * 60 * 1000 - 1
      : Infinity;
    return getDB().movimentos
      .filter((m) => {
        const t = new Date(m.data).getTime();
        if (t < desde || t > ate) return false;
        if (filtro.tipos && filtro.tipos.length > 0 && !filtro.tipos.includes(m.tipo)) return false;
        return true;
      })
      .sort((a, b) => b.data.localeCompare(a.data));
  },

  async resumo(filtro: FinanceiroFiltro = {}): Promise<FinanceiroResumo> {
    const movimentos = await this.list(filtro);
    const porTipo: Record<TipoMovimento, number> = {
      LUCRO_MAO_OBRA: 0, LUCRO_PECA: 0, SALARIO: 0, GASTO_PECA: 0,
    };
    for (const m of movimentos) porTipo[m.tipo] += m.valor;
    const receitas = porTipo.LUCRO_MAO_OBRA + porTipo.LUCRO_PECA;
    const despesas = porTipo.SALARIO + porTipo.GASTO_PECA;
    return { receitas, despesas, saldo: receitas - despesas, porTipo };
  },

  /**
   * Regista o pagamento mensal de um funcionário:
   *  - 1 movimento SALARIO com o vencimento bruto
   *  - se houver horas extra acumuladas, 1 movimento adicional com
   *    `horas * vencimentoHora` e o contador de horas é reposto a 0.
   */
  async registarSalario(data: {
    funcionarioId: string;
    funcionarioNome: string;
    valor: number;
    mesReferencia: string; // ex: "2025-04"
  }): Promise<MovimentoFinanceiro> {
    await delay();
    const db = getDB();
    const now = new Date().toISOString();
    const m: MovimentoFinanceiro = {
      id: uid("mv"),
      tipo: "SALARIO",
      valor: data.valor,
      data: now,
      descricao: `Salário ${data.funcionarioNome} (${data.mesReferencia})`,
      refId: data.funcionarioId,
    };
    db.movimentos.push(m);

    const f = db.funcionarios.find((x) => x.id === data.funcionarioId);
    if (f && f.horasExtraAcumuladas > 0 && f.vencimentoHora > 0) {
      const valorExtra = +(f.horasExtraAcumuladas * f.vencimentoHora).toFixed(2);
      db.movimentos.push({
        id: uid("mv"),
        tipo: "SALARIO",
        valor: valorExtra,
        data: now,
        descricao: `Horas extra ${data.funcionarioNome} (${f.horasExtraAcumuladas}h × ${f.vencimentoHora}€) — ${data.mesReferencia}`,
        refId: data.funcionarioId,
      });
      f.horasExtraAcumuladas = 0;
    }
    persist();
    return m;
  },
};
