import { useEffect, useMemo, useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { Plus, Pencil, Trash2, AlertCircle } from "lucide-react";
import { toast } from "sonner";

import { PageHeader } from "@/components/layout/PageHeader";
import { StockTabs } from "@/components/layout/StockTabs";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Badge } from "@/components/ui/badge";
import {
  Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle,
} from "@/components/ui/dialog";
import {
  Select, SelectContent, SelectItem, SelectTrigger, SelectValue,
} from "@/components/ui/select";
import {
  Table, TableBody, TableCell, TableHead, TableHeader, TableRow,
} from "@/components/ui/table";

import { api } from "@/services/api";
import { formatEUR } from "@/lib/format";
import { useAuth } from "@/context/AuthContext";

interface Peca {
  id: number; referencia: string; nome: string; descricao: string;
  stock_minimo: number; preco_venda: number; codFornecedor: number; ativa: boolean;
}

interface Fornecedor { id: number; nome: string; telemovel: string; email: string; }

interface StockEntry {
  id: number; codPeca: number; quantidade: number; estado: string;
  preco_compra: number; data_chegada: string;
}

interface PecaForm {
  referencia: string; nome: string; descricao: string;
  codFornecedor: number; preco_venda: number; stock_minimo: number; ativa: boolean;
}

const EMPTY: PecaForm = {
  referencia: "", nome: "", descricao: "",
  codFornecedor: 0, preco_venda: 0, stock_minimo: 0, ativa: true,
};

export default function StockPecas() {
  const { role } = useAuth();
  const qc = useQueryClient();
  const [editing, setEditing] = useState<Peca | null>(null);
  const [open, setOpen] = useState(false);
  const [filtroFornecedor, setFiltroFornecedor] = useState("TODOS");
  const [filtroRef, setFiltroRef] = useState("");

  const { data: pecas = [], isLoading } = useQuery<Peca[]>({
    queryKey: ["pecas"],
    queryFn: () => api.get<Peca[]>("/pecas"),
  });

  const { data: fornecedores = [] } = useQuery<Fornecedor[]>({
    queryKey: ["fornecedores"],
    queryFn: () => api.get<Fornecedor[]>("/fornecedores"),
  });

  const { data: stocks = [] } = useQuery<StockEntry[]>({
    queryKey: ["stocks"],
    queryFn: () => api.get<StockEntry[]>("/stocks"),
  });

  const stockPorPeca = useMemo(() => {
    const map: Record<number, number> = {};
    stocks.forEach((s) => {
      if (s.estado === "StockEmArmazem") {
        map[s.codPeca] = (map[s.codPeca] ?? 0) + s.quantidade;
      }
    });
    return map;
  }, [stocks]);

  const fornecedorNome = (id: number) => fornecedores.find((f) => f.id === id)?.nome ?? "—";

  const canEdit = role === "GERENTE" || role === "GESTOR_STOCK";

  const filtered = useMemo(() => pecas.filter((p) => {
    if (filtroFornecedor !== "TODOS" && p.codFornecedor !== Number(filtroFornecedor)) return false;
    if (filtroRef.trim() &&
        !p.referencia.toLowerCase().includes(filtroRef.toLowerCase()) &&
        !p.nome.toLowerCase().includes(filtroRef.toLowerCase())) return false;
    return true;
  }), [pecas, filtroFornecedor, filtroRef]);

  const deleteMutation = useMutation({
    mutationFn: (id: number) => api.delete(`/pecas/${id}`),
    onSuccess: () => { toast.success("Peça eliminada"); qc.invalidateQueries({ queryKey: ["pecas"] }); },
    onError: (e: Error) => toast.error(e.message),
  });

  return (
    <div>
      <PageHeader
        title="Stock"
        description="Catálogo de peças, entradas, devoluções e encomendas"
        actions={
          canEdit ? (
            <Button onClick={() => { setEditing(null); setOpen(true); }}>
              <Plus className="h-4 w-4" /> Nova peça
            </Button>
          ) : null
        }
      />
      <StockTabs />

      <div className="mb-4 grid gap-3 rounded-lg border bg-card p-3 shadow-sm sm:grid-cols-2 lg:grid-cols-3">
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
          <Label className="text-xs">Referência / Nome</Label>
          <Input value={filtroRef} onChange={(e) => setFiltroRef(e.target.value)} placeholder="Ex: BAT-36V" />
        </div>
        <div className="flex items-end justify-end">
          <Button variant="outline" size="sm" onClick={() => { setFiltroFornecedor("TODOS"); setFiltroRef(""); }}>
            Limpar filtros
          </Button>
        </div>
      </div>

      <div className="rounded-lg border bg-card shadow-sm">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Referência</TableHead>
              <TableHead>Nome</TableHead>
              <TableHead>Fornecedor</TableHead>
              <TableHead>Preço venda</TableHead>
              <TableHead>Stock</TableHead>
              <TableHead>Estado</TableHead>
              {canEdit && <TableHead className="w-[1%] text-right">Ações</TableHead>}
            </TableRow>
          </TableHeader>
          <TableBody>
            {isLoading ? (
              <TableRow><TableCell colSpan={canEdit ? 7 : 6} className="h-24 text-center text-sm text-muted-foreground">A carregar…</TableCell></TableRow>
            ) : filtered.length === 0 ? (
              <TableRow><TableCell colSpan={canEdit ? 7 : 6} className="h-24 text-center text-sm text-muted-foreground">Sem peças</TableCell></TableRow>
            ) : filtered.map((p) => {
              const atual = stockPorPeca[p.id] ?? 0;
              const baixo = atual > 0 && atual < p.stock_minimo;
              const semStock = atual === 0;
              return (
                <TableRow key={p.id}>
                  <TableCell><span className="font-mono text-xs">{p.referencia}</span></TableCell>
                  <TableCell><span className="font-medium">{p.nome}</span></TableCell>
                  <TableCell>{fornecedorNome(p.codFornecedor)}</TableCell>
                  <TableCell>{formatEUR(p.preco_venda)}</TableCell>
                  <TableCell>
                    <div className="flex items-center gap-2">
                      <Badge variant={semStock ? "destructive" : baixo ? "destructive" : "secondary"}>{atual}</Badge>
                      <span className="text-xs text-muted-foreground">/ {p.stock_minimo}</span>
                      {baixo && <AlertCircle className="h-3.5 w-3.5 text-destructive" />}
                    </div>
                  </TableCell>
                  <TableCell>
                    {p.ativa
                      ? <span className="inline-flex items-center rounded-md bg-success-soft px-2 py-1 text-xs font-medium text-success">Ativa</span>
                      : <span className="inline-flex items-center rounded-md bg-destructive/10 px-2 py-1 text-xs font-medium text-destructive">Inativa</span>}
                  </TableCell>
                  {canEdit && (
                    <TableCell className="text-right">
                      <div className="flex justify-end gap-1">
                        <Button variant="ghost" size="icon" onClick={() => { setEditing(p); setOpen(true); }}>
                          <Pencil className="h-4 w-4" />
                        </Button>
                        <ConfirmDialog
                          trigger={<Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
                          title="Eliminar peça?"
                          description={`A peça ${p.nome} será removida do catálogo.`}
                          destructive
                          onConfirm={() => deleteMutation.mutate(p.id)}
                        />
                      </div>
                    </TableCell>
                  )}
                </TableRow>
              );
            })}
          </TableBody>
        </Table>
      </div>
      <div className="mt-2 text-xs text-muted-foreground">
        {filtered.length} de {pecas.length} peças
      </div>

      <PecaDialog
        open={open}
        onOpenChange={setOpen}
        editing={editing}
        fornecedores={fornecedores}
        onSaved={() => {
          setOpen(false);
          qc.invalidateQueries({ queryKey: ["pecas"] });
        }}
      />
    </div>
  );
}

function PecaDialog({
  open, onOpenChange, editing, fornecedores, onSaved,
}: {
  open: boolean; onOpenChange: (v: boolean) => void;
  editing: Peca | null; fornecedores: Fornecedor[]; onSaved: () => void;
}) {
  const [form, setForm] = useState<PecaForm>(EMPTY);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (open) {
      setForm(editing
        ? {
            referencia: editing.referencia, nome: editing.nome, descricao: editing.descricao,
            codFornecedor: editing.codFornecedor, preco_venda: editing.preco_venda,
            stock_minimo: editing.stock_minimo, ativa: editing.ativa,
          }
        : { ...EMPTY, codFornecedor: fornecedores[0]?.id ?? 0 });
    }
  }, [open, editing, fornecedores]);

  const set = <K extends keyof PecaForm>(k: K, v: PecaForm[K]) =>
    setForm((f) => ({ ...f, [k]: v }));

  const submit = async () => {
    if (!form.referencia.trim()) { toast.error("Referência obrigatória"); return; }
    if (!form.nome.trim()) { toast.error("Nome obrigatório"); return; }
    if (!form.codFornecedor) { toast.error("Fornecedor obrigatório"); return; }
    setSaving(true);
    try {
      if (editing) {
        await api.patch(`/pecas/${editing.id}`, {
          referencia: form.referencia, nome: form.nome, descricao: form.descricao,
          stock_minimo: form.stock_minimo, preco_venda: form.preco_venda,
          codFornecedor: form.codFornecedor, ativa: form.ativa,
        });
        toast.success("Peça atualizada");
      } else {
        await api.post("/pecas", {
          referencia: form.referencia, nome: form.nome, descricao: form.descricao,
          stock_minimo: form.stock_minimo, preco_venda: form.preco_venda,
          codFornecedor: form.codFornecedor, ativa: form.ativa,
        });
        toast.success("Peça criada");
      }
      onSaved();
    } catch (e) { toast.error((e as Error).message); }
    finally { setSaving(false); }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl">
        <DialogHeader>
          <DialogTitle>{editing ? "Editar peça" : "Nova peça"}</DialogTitle>
        </DialogHeader>
        <div className="grid gap-3 sm:grid-cols-2">
          <Field label="Referência">
            <Input value={form.referencia} onChange={(e) => set("referencia", e.target.value)} />
          </Field>
          <Field label="Nome">
            <Input value={form.nome} onChange={(e) => set("nome", e.target.value)} />
          </Field>
          <div className="sm:col-span-2">
            <Field label="Descrição">
              <Textarea rows={2} value={form.descricao} onChange={(e) => set("descricao", e.target.value)} />
            </Field>
          </div>
          <Field label="Fornecedor">
            <Select value={String(form.codFornecedor)} onValueChange={(v) => set("codFornecedor", Number(v))}>
              <SelectTrigger><SelectValue placeholder="Escolher fornecedor…" /></SelectTrigger>
              <SelectContent>
                {fornecedores.map((f) => (
                  <SelectItem key={f.id} value={String(f.id)}>{f.nome}</SelectItem>
                ))}
              </SelectContent>
            </Select>
          </Field>
          <Field label="Preço de venda (€)">
            <Input type="number" step="0.01" min={0} value={form.preco_venda}
              onChange={(e) => set("preco_venda", Number(e.target.value))} />
          </Field>
          <Field label="Stock mínimo">
            <Input type="number" min={0} value={form.stock_minimo}
              onChange={(e) => set("stock_minimo", Number(e.target.value))} />
          </Field>
          <Field label="Estado">
            <Select value={form.ativa ? "true" : "false"}
              onValueChange={(v) => set("ativa", v === "true")}>
              <SelectTrigger><SelectValue /></SelectTrigger>
              <SelectContent>
                <SelectItem value="true">Ativa</SelectItem>
                <SelectItem value="false">Inativa</SelectItem>
              </SelectContent>
            </Select>
          </Field>
        </div>
        <DialogFooter>
          <Button variant="outline" onClick={() => onOpenChange(false)}>Cancelar</Button>
          <Button onClick={submit} disabled={saving}>
            {saving ? "A guardar…" : editing ? "Guardar" : "Criar"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

function Field({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div className="space-y-1">
      <Label className="text-xs">{label}</Label>
      {children}
    </div>
  );
}
