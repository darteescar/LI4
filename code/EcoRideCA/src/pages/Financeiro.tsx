import { useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import { TrendingUp, TrendingDown, Wallet, Plus } from "lucide-react";
import { toast } from "sonner";

import { PageHeader } from "@/components/layout/PageHeader";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select, SelectContent, SelectItem, SelectTrigger, SelectValue,
} from "@/components/ui/select";
import {
  Table, TableBody, TableCell, TableHead, TableHeader, TableRow,
} from "@/components/ui/table";
import {
  Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle, DialogTrigger,
} from "@/components/ui/dialog";
import { Badge } from "@/components/ui/badge";

import {
  financeiroService, TIPO_LABELS, TIPO_SINAL,
} from "@/services/financeiro";
import { funcionariosService } from "@/services/entities";
import { formatEUR, formatDateTime } from "@/lib/format";
import type { MovimentoFinanceiro, TipoMovimento, Funcionario } from "@/lib/types";

const TIPOS: TipoMovimento[] = ["LUCRO_MAO_OBRA", "LUCRO_PECA", "SALARIO", "GASTO_PECA"];

export default function Financeiro() {
  const [movimentos, setMovimentos] = useState<MovimentoFinanceiro[]>([]);
  const [funcionarios, setFuncionarios] = useState<Funcionario[]>([]);
  const [desde, setDesde] = useState<string>("");
  const [ate, setAte] = useState<string>("");
  const [tipoFiltro, setTipoFiltro] = useState<TipoMovimento | "TODOS">("TODOS");
  const [salarioOpen, setSalarioOpen] = useState(false);

  const reload = async () => {
    const [m, f] = await Promise.all([
      financeiroService.list({
        desde: desde || undefined,
        ate: ate || undefined,
        tipos: tipoFiltro === "TODOS" ? undefined : [tipoFiltro],
      }),
      funcionariosService.list(),
    ]);
    setMovimentos(m);
    setFuncionarios(f.filter((x) => x.ativo));
  };

  useEffect(() => { reload(); /* eslint-disable-next-line */ }, [desde, ate, tipoFiltro]);

  const resumo = useMemo(() => {
    const porTipo: Record<TipoMovimento, number> = {
      LUCRO_MAO_OBRA: 0, LUCRO_PECA: 0, SALARIO: 0, GASTO_PECA: 0,
    };
    movimentos.forEach((m) => { porTipo[m.tipo] += m.valor; });
    const receitas = porTipo.LUCRO_MAO_OBRA + porTipo.LUCRO_PECA;
    const despesas = porTipo.SALARIO + porTipo.GASTO_PECA;
    return { receitas, despesas, saldo: receitas - despesas, porTipo };
  }, [movimentos]);

  const limparFiltros = () => {
    setDesde(""); setAte(""); setTipoFiltro("TODOS");
  };

  return (
    <div>
      <PageHeader
        title="Financeiro"
        description="Receitas, despesas e movimentos da oficina"
        actions={
          <Dialog open={salarioOpen} onOpenChange={setSalarioOpen}>
            <DialogTrigger asChild>
              <Button><Plus className="h-4 w-4" /> Registar salário</Button>
            </DialogTrigger>
            <SalarioDialog
              funcionarios={funcionarios}
              onSaved={() => { setSalarioOpen(false); reload(); }}
            />
          </Dialog>
        }
      />

      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard
          label="Receitas"
          value={resumo.receitas}
          icon={TrendingUp}
          variant="success"
        />
        <StatCard
          label="Despesas"
          value={resumo.despesas}
          icon={TrendingDown}
          variant="destructive"
        />
        <StatCard
          label="Saldo"
          value={resumo.saldo}
          icon={Wallet}
          variant={resumo.saldo >= 0 ? "primary" : "destructive"}
          highlight
        />
        <Card className="shadow-sm">
          <CardContent className="space-y-1 p-4">
            <div className="text-xs font-medium text-muted-foreground">Movimentos no período</div>
            <div className="text-2xl font-semibold">{movimentos.length}</div>
            <div className="text-xs text-muted-foreground">
              {desde || ate ? "filtrado" : "todos os registos"}
            </div>
          </CardContent>
        </Card>
      </div>

      <div className="mt-6 grid gap-4 lg:grid-cols-3">
        <Card className="lg:col-span-1">
          <CardHeader><CardTitle className="text-base">Detalhe por tipo</CardTitle></CardHeader>
          <CardContent className="space-y-2 text-sm">
            {TIPOS.map((t) => (
              <div key={t} className="flex items-center justify-between border-b py-1.5 last:border-0">
                <span className="flex items-center gap-2">
                  <span className={`inline-block h-2 w-2 rounded-full ${TIPO_SINAL[t] === "+" ? "bg-success" : "bg-destructive"}`} />
                  {TIPO_LABELS[t]}
                </span>
                <span className={`font-medium ${TIPO_SINAL[t] === "+" ? "text-success" : "text-destructive"}`}>
                  {TIPO_SINAL[t]}{formatEUR(resumo.porTipo[t])}
                </span>
              </div>
            ))}
          </CardContent>
        </Card>

        <Card className="lg:col-span-2">
          <CardHeader>
            <CardTitle className="text-base">Movimentos</CardTitle>
          </CardHeader>
          <CardContent className="space-y-3">
            <div className="grid gap-2 sm:grid-cols-4">
              <div className="space-y-1">
                <Label className="text-xs">Desde</Label>
                <Input type="date" value={desde} onChange={(e) => setDesde(e.target.value)} />
              </div>
              <div className="space-y-1">
                <Label className="text-xs">Até</Label>
                <Input type="date" value={ate} onChange={(e) => setAte(e.target.value)} />
              </div>
              <div className="space-y-1">
                <Label className="text-xs">Tipo</Label>
                <Select value={tipoFiltro} onValueChange={(v) => setTipoFiltro(v as typeof tipoFiltro)}>
                  <SelectTrigger><SelectValue /></SelectTrigger>
                  <SelectContent>
                    <SelectItem value="TODOS">Todos</SelectItem>
                    {TIPOS.map((t) => (
                      <SelectItem key={t} value={t}>{TIPO_LABELS[t]}</SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div className="flex items-end">
                <Button variant="outline" className="w-full" onClick={limparFiltros}>
                  Limpar filtros
                </Button>
              </div>
            </div>

            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Data</TableHead>
                  <TableHead>Tipo</TableHead>
                  <TableHead>Descrição</TableHead>
                  <TableHead className="text-right">Valor</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {movimentos.length === 0 && (
                  <TableRow>
                    <TableCell colSpan={4} className="py-8 text-center text-sm text-muted-foreground">
                      Sem movimentos no período seleccionado.
                    </TableCell>
                  </TableRow>
                )}
                {movimentos.map((m) => {
                  const sinal = TIPO_SINAL[m.tipo];
                  return (
                    <TableRow key={m.id}>
                      <TableCell className="whitespace-nowrap text-xs text-muted-foreground">
                        {formatDateTime(m.data)}
                      </TableCell>
                      <TableCell>
                        <Badge variant={sinal === "+" ? "secondary" : "outline"}>
                          {TIPO_LABELS[m.tipo]}
                        </Badge>
                      </TableCell>
                      <TableCell className="text-sm">
                        {m.descricao}
                        {m.refId && m.tipo !== "SALARIO" && m.descricao.includes("OS-") && (
                          <Link
                            to={`/os/${m.refId}`}
                            className="ml-2 text-xs text-primary hover:underline"
                          >
                            ver OS
                          </Link>
                        )}
                      </TableCell>
                      <TableCell className={`text-right font-medium ${sinal === "+" ? "text-success" : "text-destructive"}`}>
                        {sinal}{formatEUR(m.valor)}
                      </TableCell>
                    </TableRow>
                  );
                })}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}

function StatCard({
  label, value, icon: Icon, variant, highlight,
}: {
  label: string; value: number;
  icon: typeof TrendingUp;
  variant: "success" | "destructive" | "primary";
  highlight?: boolean;
}) {
  const colors = {
    success: "text-success bg-success-soft",
    destructive: "text-destructive bg-destructive/10",
    primary: "text-primary bg-primary-soft",
  }[variant];
  return (
    <Card className={`shadow-sm ${highlight ? "ring-1 ring-primary/30" : ""}`}>
      <CardContent className="flex items-center gap-4 p-5">
        <div className={`flex h-11 w-11 items-center justify-center rounded-lg ${colors}`}>
          <Icon className="h-5 w-5" />
        </div>
        <div>
          <div className="text-xs font-medium text-muted-foreground">{label}</div>
          <div className="text-2xl font-semibold">{formatEUR(value)}</div>
        </div>
      </CardContent>
    </Card>
  );
}

function SalarioDialog({
  funcionarios, onSaved,
}: {
  funcionarios: Funcionario[];
  onSaved: () => void;
}) {
  const [funcId, setFuncId] = useState<string>(funcionarios[0]?.id ?? "");
  const [valor, setValor] = useState<string>("");
  const [mes, setMes] = useState<string>(new Date().toISOString().slice(0, 7));

  // Quando muda funcionário, preencher valor com o vencimento bruto
  useEffect(() => {
    const f = funcionarios.find((x) => x.id === funcId);
    if (f) setValor(String(f.vencimentoBruto));
  }, [funcId, funcionarios]);

  const submit = async () => {
    const f = funcionarios.find((x) => x.id === funcId);
    if (!f) { toast.error("Escolhe o funcionário"); return; }
    const v = Number(valor);
    if (!Number.isFinite(v) || v <= 0) { toast.error("Valor inválido"); return; }
    if (!/^\d{4}-\d{2}$/.test(mes)) { toast.error("Mês inválido (formato AAAA-MM)"); return; }
    await financeiroService.registarSalario({
      funcionarioId: f.id,
      funcionarioNome: f.nome,
      valor: v,
      mesReferencia: mes,
    });
    toast.success("Salário registado");
    onSaved();
  };

  return (
    <DialogContent>
      <DialogHeader>
        <DialogTitle>Registar pagamento de salário</DialogTitle>
      </DialogHeader>
      <div className="space-y-3">
        <div className="space-y-1">
          <Label className="text-xs">Funcionário</Label>
          <Select value={funcId} onValueChange={setFuncId}>
            <SelectTrigger><SelectValue /></SelectTrigger>
            <SelectContent>
              {funcionarios.map((f) => (
                <SelectItem key={f.id} value={f.id}>
                  {f.nome} — {f.cargo}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
        <div className="grid gap-3 sm:grid-cols-2">
          <div className="space-y-1">
            <Label className="text-xs">Valor (€)</Label>
            <Input type="number" min={0} step="0.01" value={valor}
              onChange={(e) => setValor(e.target.value)} />
          </div>
          <div className="space-y-1">
            <Label className="text-xs">Mês de referência</Label>
            <Input type="month" value={mes} onChange={(e) => setMes(e.target.value)} />
          </div>
        </div>
      </div>
      <DialogFooter>
        <Button onClick={submit}>Registar</Button>
      </DialogFooter>
    </DialogContent>
  );
}
