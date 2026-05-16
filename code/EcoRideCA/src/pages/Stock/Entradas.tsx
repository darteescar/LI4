import { useEffect, useMemo, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { Plus, Trash2, Search, X } from "lucide-react";
import { toast } from "sonner";

import { PageHeader } from "@/components/layout/PageHeader";
import { StockTabs } from "@/components/layout/StockTabs";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Badge } from "@/components/ui/badge";
import {
  Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle,
} from "@/components/ui/dialog";
import {
  Table, TableBody, TableCell, TableHead, TableHeader, TableRow,
} from "@/components/ui/table";
import {
  Select, SelectContent, SelectItem, SelectTrigger, SelectValue,
} from "@/components/ui/select";
import { api } from "@/services/api";
import { formatEUR } from "@/lib/format";
import { useAuth } from "@/context/AuthContext";
import { entradaNormalSchema } from "@/lib/validators";

interface Peca { id: number; referencia: string; marca: string; nome: string; preco_venda: number; codFornecedor: number; }
interface Fornecedor { id: number; nome: string; telemovel: string; email: string; }
interface StockEntry {
  id: number; codPeca: number; quantidade: number; estado: string;
  preco_compra: number; data_chegada: string;
}

const ESTADO_LABELS: Record<string, string> = {
  StockEncomendado: "Encomendado",
  StockEmArmazem: "Em armazém",
  StockComPossivelDefeito: "Possível defeito",
  StockPendenteDeDevolucao: "Pendente devolução",
  StockEnviadoParaFornecedor: "Enviado fornecedor",
  StockDevolvidoFornecedor: "Devolvido",
  StockinvalidoParaDevolucao: "Inválido devolução",
  StockUsadoConserto: "Usado em conserto",
};

type NormalForm = z.infer<typeof entradaNormalSchema>;

function today() { return new Date().toISOString().slice(0, 10); }

export default function StockEntradas() {
  const { role } = useAuth();
  const qc = useQueryClient();
  const [open, setOpen] = useState(false);
  const [historico, setHistorico] = useState(false);

  const [filtroEstado, setFiltroEstado] = useState("ALL");
  const [filtroDataDesde, setFiltroDataDesde] = useState("");
  const [filtroDataAte, setFiltroDataAte] = useState("");
  const [filtroFornecedor, setFiltroFornecedor] = useState("TODOS");
  const [filtroRef, setFiltroRef] = useState("");

  const canEdit = role === "GERENTE" || role === "GESTOR_STOCK";

  const { data: stocks = [], isLoading } = useQuery<StockEntry[]>({
    queryKey: ["stocks", historico],
    queryFn: () => api.get<StockEntry[]>(`/stocks${historico ? "?historico=true" : ""}`),
  });

  const { data: pecas = [] } = useQuery<Peca[]>({
    queryKey: ["pecas"],
    queryFn: () => api.get<Peca[]>("/pecas"),
  });

  const { data: fornecedores = [] } = useQuery<Fornecedor[]>({
    queryKey: ["fornecedores"],
    queryFn: () => api.get<Fornecedor[]>("/fornecedores"),
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => api.delete(`/stocks/${id}`),
    onSuccess: () => {
      toast.success("Entrada eliminada");
      qc.invalidateQueries({ queryKey: ["stocks"] });
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const pecaLabel = (codPeca: number) => {
    const p = pecas.find((x) => x.id === codPeca);
    return p ? `${p.referencia} · ${p.nome}` : `#${codPeca}`;
  };

  const sortedStocks = useMemo(() => {
    const filtered = stocks.filter((s) => {
      if (filtroEstado !== "ALL" && s.estado !== filtroEstado) return false;
      if (filtroDataDesde && s.data_chegada && s.data_chegada < filtroDataDesde) return false;
      if (filtroDataAte && s.data_chegada && s.data_chegada > filtroDataAte) return false;
      if (filtroFornecedor !== "TODOS") {
        const p = pecas.find((x) => x.id === s.codPeca);
        if (!p || p.codFornecedor !== Number(filtroFornecedor)) return false;
      }
      if (filtroRef.trim()) {
        const p = pecas.find((x) => x.id === s.codPeca);
        if (!p) return false;
        const q = filtroRef.toLowerCase();
        if (!p.referencia.toLowerCase().includes(q) && !p.nome.toLowerCase().includes(q)) return false;
      }
      return true;
    });
    return filtered.sort((a, b) => (b.data_chegada ?? "").localeCompare(a.data_chegada ?? ""));
  }, [stocks, pecas, filtroEstado, filtroDataDesde, filtroDataAte, filtroFornecedor, filtroRef]);

  return (
    <div>
      <PageHeader
        title="Stock"
        description="Catálogo de peças, entradas, devoluções e encomendas"
        actions={
          canEdit ? (
            <Button onClick={() => setOpen(true)}>
              <Plus className="h-4 w-4" /> Nova entrada
            </Button>
          ) : null
        }
      />
      <StockTabs />

      <div className="mb-4 grid gap-3 rounded-lg border bg-card p-3 shadow-sm sm:grid-cols-2 lg:grid-cols-3">
        <div className="space-y-1">
          <Label className="text-xs">Estado</Label>
          <Select value={filtroEstado} onValueChange={setFiltroEstado}>
            <SelectTrigger><SelectValue /></SelectTrigger>
            <SelectContent>
              <SelectItem value="ALL">Todos</SelectItem>
              {Object.entries(ESTADO_LABELS).map(([k, v]) => (
                <SelectItem key={k} value={k}>{v}</SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
        <div className="space-y-1">
          <Label className="text-xs">Fornecedor</Label>
          <Select value={filtroFornecedor} onValueChange={setFiltroFornecedor}>
            <SelectTrigger><SelectValue /></SelectTrigger>
            <SelectContent>
              <SelectItem value="TODOS">Todos</SelectItem>
              {fornecedores.map((f) => (
                <SelectItem key={f.id} value={String(f.id)}>{f.nome}</SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
        <div className="space-y-1">
          <Label className="text-xs">Referência / Nome da peça</Label>
          <div className="relative">
            <Search className="absolute left-2.5 top-2.5 h-3.5 w-3.5 text-muted-foreground pointer-events-none" />
            <Input className="pl-8" placeholder="Ex: BAT-36V" value={filtroRef} onChange={(e) => setFiltroRef(e.target.value)} />
          </div>
        </div>
        <div className="space-y-1">
          <Label className="text-xs">Data chegada desde</Label>
          <Input type="date" value={filtroDataDesde} onChange={(e) => setFiltroDataDesde(e.target.value)} />
        </div>
        <div className="space-y-1">
          <Label className="text-xs">Data chegada até</Label>
          <Input type="date" value={filtroDataAte} onChange={(e) => setFiltroDataAte(e.target.value)} />
        </div>
        <div className="flex items-end justify-end">
          <Button variant="outline" size="sm" onClick={() => { setFiltroEstado("ALL"); setFiltroFornecedor("TODOS"); setFiltroRef(""); setFiltroDataDesde(""); setFiltroDataAte(""); }}>
            Limpar filtros
          </Button>
        </div>
      </div>

      <div className="mb-3 flex justify-end">
        <Button
          variant={historico ? "default" : "outline"}
          size="sm"
          onClick={() => setHistorico((v) => !v)}
        >
          {historico ? "Ocultar histórico" : "Ver histórico completo"}
        </Button>
      </div>

      <div className="rounded-lg border bg-card shadow-sm">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Data chegada</TableHead>
              <TableHead>Peça</TableHead>
              <TableHead>Qtd</TableHead>
              <TableHead>Preço compra</TableHead>
              <TableHead>Estado</TableHead>
              {canEdit && <TableHead className="w-[1%] text-right">Ações</TableHead>}
            </TableRow>
          </TableHeader>
          <TableBody>
            {isLoading ? (
              <TableRow><TableCell colSpan={canEdit ? 6 : 5} className="h-24 text-center text-sm text-muted-foreground">A carregar…</TableCell></TableRow>
            ) : sortedStocks.length === 0 ? (
              <TableRow><TableCell colSpan={canEdit ? 6 : 5} className="h-24 text-center text-sm text-muted-foreground">Sem entradas de stock</TableCell></TableRow>
            ) : sortedStocks.map((s) => (
              <TableRow key={s.id}>
                <TableCell className="text-xs text-muted-foreground">{s.data_chegada ?? "—"}</TableCell>
                <TableCell className="font-medium">{pecaLabel(s.codPeca)}</TableCell>
                <TableCell>{s.quantidade}</TableCell>
                <TableCell>{formatEUR(s.preco_compra)}</TableCell>
                <TableCell>
                  <Badge variant="secondary" className="text-xs">
                    {ESTADO_LABELS[s.estado] ?? s.estado}
                  </Badge>
                </TableCell>
                {canEdit && (
                  <TableCell className="text-right">
                    <ConfirmDialog
                      trigger={<Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
                      title="Eliminar esta entrada de stock?"
                      description="O registo de stock será removido."
                      destructive
                      onConfirm={() => deleteMutation.mutate(s.id)}
                    />
                  </TableCell>
                )}
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
      <div className="mt-2 text-xs text-muted-foreground">
        {sortedStocks.length} de {stocks.length} entradas
      </div>

      {canEdit && (
        <EntradaDialog
          open={open}
          onOpenChange={setOpen}
          onSaved={() => {
            setOpen(false);
            qc.invalidateQueries({ queryKey: ["stocks"] });
          }}
        />
      )}
    </div>
  );
}

function EntradaDialog({
  open, onOpenChange, onSaved,
}: {
  open: boolean; onOpenChange: (v: boolean) => void; onSaved: () => void;
}) {
  const { data: pecas = [] } = useQuery<Peca[]>({
    queryKey: ["pecasAtivas"],
    queryFn: () => api.get<Peca[]>("/pecasAtivas"),
    enabled: open,
  });

  const { data: fornecedores = [] } = useQuery<Fornecedor[]>({
    queryKey: ["fornecedores"],
    queryFn: () => api.get<Fornecedor[]>("/fornecedores"),
    enabled: open,
  });

  const [filtroFornecedor, setFiltroFornecedor] = useState("TODOS");
  const [filtroRef, setFiltroRef] = useState("");

  const form = useForm<NormalForm>({
    resolver: zodResolver(entradaNormalSchema),
    defaultValues: { pecaId: 0, quantidade: "" as unknown as number, preco: "" as unknown as number, dataChegada: today() },
  });

  useEffect(() => {
    if (!open) {
      form.reset({ pecaId: 0, quantidade: "" as unknown as number, preco: "" as unknown as number, dataChegada: today() });
      setFiltroFornecedor("TODOS");
      setFiltroRef("");
    }
  }, [open]);

  const pecaIdSelecionada = form.watch("pecaId");
  const pecaSelecionada = pecas.find((p) => p.id === pecaIdSelecionada) ?? null;

  const pecasFiltradas = useMemo(() => pecas.filter((p) => {
    if (filtroFornecedor !== "TODOS" && p.codFornecedor !== Number(filtroFornecedor)) return false;
    if (filtroRef.trim() &&
        !p.referencia.toLowerCase().includes(filtroRef.toLowerCase()) &&
        !p.nome.toLowerCase().includes(filtroRef.toLowerCase())) return false;
    return true;
  }), [pecas, filtroFornecedor, filtroRef]);

  const fornecedorNome = (id: number) => fornecedores.find((f) => f.id === id)?.nome ?? "—";

  const limparFiltros = () => { setFiltroFornecedor("TODOS"); setFiltroRef(""); };

  const submit = form.handleSubmit(async (v) => {
    try {
      await api.post("/stocks", {
        codPeca: v.pecaId,
        preco: v.preco,
        quantidade: v.quantidade,
        dataChegada: v.dataChegada,
      });
      toast.success("Entrada registada");
      onSaved();
    } catch (e) { toast.error((e as Error).message); }
  });

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl">
        <DialogHeader><DialogTitle>Nova entrada de stock</DialogTitle></DialogHeader>
        <form onSubmit={submit} className="flex flex-col gap-4">

          {/* ── Filtros de pesquisa ── */}
          <div className="grid gap-2 rounded-lg border bg-muted/40 p-3 sm:grid-cols-3">
            <div className="space-y-1">
              <Label className="text-xs">Fornecedor</Label>
              <Select value={filtroFornecedor} onValueChange={(v) => { setFiltroFornecedor(v); form.setValue("pecaId", 0); }}>
                <SelectTrigger><SelectValue /></SelectTrigger>
                <SelectContent>
                  <SelectItem value="TODOS">Todos</SelectItem>
                  {fornecedores.map((f) => (
                    <SelectItem key={f.id} value={String(f.id)}>{f.nome}</SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-1">
              <Label className="text-xs">Referência / Nome</Label>
              <div className="relative">
                <Search className="absolute left-2.5 top-2.5 h-3.5 w-3.5 text-muted-foreground pointer-events-none" />
                <Input
                  className="pl-8"
                  placeholder="Ex: BAT-36V"
                  value={filtroRef}
                  onChange={(e) => { setFiltroRef(e.target.value); form.setValue("pecaId", 0); }}
                />
              </div>
            </div>
            <div className="flex items-end">
              <Button type="button" variant="outline" size="sm" onClick={limparFiltros} className="w-full gap-1">
                <X className="h-3.5 w-3.5" /> Limpar filtros
              </Button>
            </div>
          </div>

          {/* ── Tabela de seleção de peça ── */}
          <div className="space-y-1">
            <Label className="text-xs">
              Peça{" "}
              <span className="text-muted-foreground font-normal">
                — {pecasFiltradas.length} resultado{pecasFiltradas.length !== 1 ? "s" : ""}
              </span>
            </Label>
            {form.formState.errors.pecaId && (
              <p className="text-xs text-destructive">{form.formState.errors.pecaId.message}</p>
            )}
            <div className="rounded-md border max-h-52 overflow-y-auto">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead className="w-6" />
                    <TableHead>Referência</TableHead>
                    <TableHead>Nome</TableHead>
                    <TableHead>Fornecedor</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {pecasFiltradas.length === 0 ? (
                    <TableRow>
                      <TableCell colSpan={4} className="h-16 text-center text-sm text-muted-foreground">
                        Nenhuma peça encontrada.
                      </TableCell>
                    </TableRow>
                  ) : pecasFiltradas.map((p) => {
                    const selected = pecaIdSelecionada === p.id;
                    return (
                      <TableRow
                        key={p.id}
                        className={`cursor-pointer transition-colors ${selected ? "bg-primary/10 hover:bg-primary/15" : "hover:bg-muted/50"}`}
                        onClick={() => form.setValue("pecaId", selected ? 0 : p.id, { shouldValidate: true })}
                      >
                        <TableCell className="pr-0">
                          <div className={`h-3.5 w-3.5 rounded-full border-2 mx-auto transition-colors ${selected ? "border-primary bg-primary" : "border-muted-foreground/40"}`} />
                        </TableCell>
                        <TableCell><span className="font-mono text-xs">{p.referencia}</span></TableCell>
                        <TableCell className="font-medium">{p.nome}</TableCell>
                        <TableCell className="text-xs text-muted-foreground">{fornecedorNome(p.codFornecedor)}</TableCell>
                      </TableRow>
                    );
                  })}
                </TableBody>
              </Table>
            </div>
            {pecaSelecionada && (
              <p className="text-xs text-muted-foreground">
                Selecionada: <span className="font-medium text-foreground">{pecaSelecionada.referencia} — {pecaSelecionada.nome}</span>
              </p>
            )}
          </div>

          {/* ── Campos de quantidade, preço e data ── */}
          <div className="grid gap-3 sm:grid-cols-3">
            <F label="Quantidade" error={form.formState.errors.quantidade?.message}>
              <Input type="number" min={1} placeholder="1" {...form.register("quantidade")} />
            </F>
            <F label="Preço de compra (€)" error={form.formState.errors.preco?.message}>
              <Input type="number" step="0.01" min={0} placeholder="0.00" {...form.register("preco")} />
            </F>
            <F label="Data de chegada" error={form.formState.errors.dataChegada?.message}>
              <Input type="date" {...form.register("dataChegada")} />
            </F>
          </div>

          <DialogFooter>
            <Button type="button" variant="outline" onClick={() => onOpenChange(false)}>Cancelar</Button>
            <Button type="submit" disabled={form.formState.isSubmitting}>
              {form.formState.isSubmitting ? "A registar…" : "Registar entrada"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}

function F({ label, error, children }: { label: string; error?: string; children: React.ReactNode }) {
  return (
    <div className="space-y-1">
      <Label className="text-xs">{label}</Label>
      {children}
      {error && <p className="text-xs text-destructive">{error}</p>}
    </div>
  );
}