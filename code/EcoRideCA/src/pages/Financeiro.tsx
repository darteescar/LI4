import { useMemo, useState } from "react";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import { TrendingUp, TrendingDown, Wallet } from "lucide-react";
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

import { api } from "@/services/api";
import { formatEUR, formatDateTime } from "@/lib/format";

// Backend TipoMovimento enum names
type TipoMovimento = "Salario" | "GastoPecas" | "LucroMaoObra" | "LucroVendaPecas";

const TIPO_LABELS: Record<TipoMovimento, string> = {
  LucroMaoObra:    "Mão de obra",
  LucroVendaPecas: "Venda de peças",
  Salario:         "Salário",
  GastoPecas:      "Compra de peças",
};

const TIPO_SINAL: Record<TipoMovimento, "+" | "-"> = {
  LucroMaoObra:    "+",
  LucroVendaPecas: "+",
  Salario:         "-",
  GastoPecas:      "-",
};

const TIPOS: TipoMovimento[] = ["LucroMaoObra", "LucroVendaPecas", "Salario", "GastoPecas"];

interface MovimentoFinanceiro {
  id: number;
  valor: number;
  data: string;
  descricao: string;
  tipo: TipoMovimento;
}

interface AnaliseFinanceira {
  receitas: number;
  despesas: number;
  saldo: number;
  movimentos: number;
}

interface ApiResponse {
  movimentos: MovimentoFinanceiro[];
  analise: AnaliseFinanceira;
}

interface Funcionario {
  id: number;
  nome: string;
  salario_bruto: number;
}

export default function Financeiro() {
  const qc = useQueryClient();
  const [desde, setDesde] = useState("");
  const [ate, setAte] = useState("");
  const [tipoFiltro, setTipoFiltro] = useState<TipoMovimento | "TODOS">("TODOS");
  const [salarioOpen, setSalarioOpen] = useState(false);

  const queryParams = useMemo(() => {
    const p = new URLSearchParams();
    if (desde) p.set("desde", desde);
    if (ate) p.set("ate", ate);
    if (tipoFiltro !== "TODOS") p.set("tipo", tipoFiltro);
    return p.toString();
  }, [desde, ate, tipoFiltro]);

  const { data } = useQuery<ApiResponse>({
    queryKey: ["movimentosfinanceiros", queryParams],
    queryFn: () => api.get<ApiResponse>(`/movimentosfinanceiros${queryParams ? `?${queryParams}` : ""}`),
  });

  const { data: funcionarios = [] } = useQuery<Funcionario[]>({
    queryKey: ["funcionarios"],
    queryFn: () => api.get<Funcionario[]>("/funcionarios"),
  });

  const movimentos = data?.movimentos ?? [];
  const analise = data?.analise;

  const porTipo = useMemo(() => {
    const acc: Record<TipoMovimento, number> = { LucroMaoObra: 0, LucroVendaPecas: 0, Salario: 0, GastoPecas: 0 };
    movimentos.forEach((m) => { acc[m.tipo] = (acc[m.tipo] ?? 0) + m.valor; });
    return acc;
  }, [movimentos]);

  return (
    <div>
      <PageHeader
        title="Financeiro"
        description="Receitas, despesas e movimentos da oficina"
      />

      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard label="Receitas"  value={analise?.receitas ?? 0}  icon={TrendingUp}  variant="success" />
        <StatCard label="Despesas"  value={analise?.despesas ?? 0}  icon={TrendingDown} variant="destructive" />
        <StatCard label="Saldo"     value={analise?.saldo ?? 0}     icon={Wallet}       variant={(analise?.saldo ?? 0) >= 0 ? "primary" : "destructive"} highlight />
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
                  {TIPO_SINAL[t]}{formatEUR(porTipo[t])}
                </span>
              </div>
            ))}
          </CardContent>
        </Card>

        <Card className="lg:col-span-2">
          <CardHeader><CardTitle className="text-base">Movimentos</CardTitle></CardHeader>
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
                    {TIPOS.map((t) => <SelectItem key={t} value={t}>{TIPO_LABELS[t]}</SelectItem>)}
                  </SelectContent>
                </Select>
              </div>
              <div className="flex items-end">
                <Button variant="outline" className="w-full" onClick={() => { setDesde(""); setAte(""); setTipoFiltro("TODOS"); }}>
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
                  const sinal = TIPO_SINAL[m.tipo] ?? "+";
                  return (
                    <TableRow key={m.id}>
                      <TableCell className="whitespace-nowrap text-xs text-muted-foreground">
                        {formatDateTime(m.data)}
                      </TableCell>
                      <TableCell>
                        <Badge variant={sinal === "+" ? "secondary" : "outline"}>
                          {TIPO_LABELS[m.tipo] ?? m.tipo}
                        </Badge>
                      </TableCell>
                      <TableCell className="text-sm">{m.descricao}</TableCell>
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

function StatCard({ label, value, icon: Icon, variant, highlight }: {
  label: string; value: number; icon: typeof TrendingUp;
  variant: "success" | "destructive" | "primary"; highlight?: boolean;
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