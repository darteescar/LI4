import { useEffect, useMemo, useState } from "react";
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

import { pecasService, fornecedoresService } from "@/services/entities";
import { formatEUR } from "@/lib/format";
import type { Peca, Fornecedor } from "@/lib/types";
import { useAuth } from "@/context/AuthContext";

interface PecaForm {
  referencia: string;
  nome: string;
  descricao: string;
  fornecedorId: string;
  tipo: string;
  precoVenda: number;
  stockMinimo: number;
}

const EMPTY: PecaForm = {
  referencia: "", nome: "", descricao: "", fornecedorId: "", tipo: "",
  precoVenda: 0, stockMinimo: 0,
};

export default function StockPecas() {
  const { role } = useAuth();
  const [pecas, setPecas] = useState<Peca[]>([]);
  const [fornecedores, setFornecedores] = useState<Fornecedor[]>([]);
  const [editing, setEditing] = useState<Peca | null>(null);
  const [open, setOpen] = useState(false);

  // Filtros
  const [filtroFornecedor, setFiltroFornecedor] = useState<string>("TODOS");
  const [filtroReferencia, setFiltroReferencia] = useState<string>("");

  const reload = async () => {
    const [p, f] = await Promise.all([
      pecasService.list(),
      fornecedoresService.list(),
    ]);
    setPecas(p); setFornecedores(f);
  };
  useEffect(() => { reload(); }, []);

  const fornecedorNome = (id: string) => fornecedores.find((f) => f.id === id)?.nome ?? "—";

  const canEdit = role === "GERENTE" || role === "GESTOR_STOCK";

  const filtered = useMemo(() => {
    return pecas.filter((p) => {
      if (filtroFornecedor !== "TODOS" && p.fornecedorId !== filtroFornecedor) return false;
      if (filtroReferencia.trim() && !p.referencia.toLowerCase().includes(filtroReferencia.toLowerCase())) return false;
      return true;
    });
  }, [pecas, filtroFornecedor, filtroReferencia]);

  const limparFiltros = () => {
    setFiltroFornecedor("TODOS"); setFiltroReferencia("");
  };

  const handleOpen = (p: Peca | null) => { setEditing(p); setOpen(true); };

  const handleRemove = async (p: Peca) => {
    await pecasService.remove(p.id);
    toast.success("Peça eliminada");
    reload();
  };

  return (
    <div>
      <PageHeader
        title="Stock"
        description="Catálogo de peças, entradas, devoluções e encomendas"
        actions={
          canEdit && (
            <Button onClick={() => handleOpen(null)}><Plus className="h-4 w-4" /> Nova peça</Button>
          )
        }
      />
      <StockTabs />

      {/* Filtros */}
      <div className="mb-4 grid gap-3 rounded-lg border bg-card p-3 shadow-sm sm:grid-cols-2 lg:grid-cols-3">
        <div className="space-y-1">
          <Label className="text-xs">Fornecedor</Label>
          <Select value={filtroFornecedor} onValueChange={setFiltroFornecedor}>
            <SelectTrigger><SelectValue /></SelectTrigger>
            <SelectContent>
              <SelectItem value="TODOS">Todos</SelectItem>
              {fornecedores.map((f) => (
                <SelectItem key={f.id} value={f.id}>{f.nome}</SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
        <div className="space-y-1">
          <Label className="text-xs">Referência</Label>
          <Input value={filtroReferencia} onChange={(e) => setFiltroReferencia(e.target.value)} placeholder="Ex: BAT-36V" />
        </div>
        <div className="flex items-end justify-end">
          <Button variant="outline" size="sm" onClick={limparFiltros}>Limpar filtros</Button>
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
              {canEdit && <TableHead className="w-[1%] text-right">Ações</TableHead>}
            </TableRow>
          </TableHeader>
          <TableBody>
            {filtered.length === 0 ? (
              <TableRow>
                <TableCell colSpan={canEdit ? 6 : 5} className="h-24 text-center text-sm text-muted-foreground">
                  Sem peças que correspondam aos filtros
                </TableCell>
              </TableRow>
            ) : filtered.map((p) => {
              const baixo = p.stockAtual > 0 && p.stockAtual < p.stockMinimo;
              const semStock = p.stockAtual === 0;
              return (
                <TableRow key={p.id}>
                  <TableCell><span className="font-mono text-xs">{p.referencia}</span></TableCell>
                  <TableCell><span className="font-medium">{p.nome}</span></TableCell>
                  <TableCell>{fornecedorNome(p.fornecedorId)}</TableCell>
                  <TableCell>{formatEUR(p.precoVenda)}</TableCell>
                  <TableCell>
                    <div className="flex items-center gap-2">
                      <Badge variant={semStock ? "destructive" : baixo ? "destructive" : "secondary"}>{p.stockAtual}</Badge>
                      <span className="text-xs text-muted-foreground">/ {p.stockMinimo}</span>
                      {baixo && <AlertCircle className="h-3.5 w-3.5 text-destructive" />}
                    </div>
                  </TableCell>
                  {canEdit && (
                    <TableCell className="text-right">
                      <div className="flex justify-end gap-1">
                        <Button variant="ghost" size="icon" onClick={() => handleOpen(p)}>
                          <Pencil className="h-4 w-4" />
                        </Button>
                        <ConfirmDialog
                          trigger={<Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
                          title="Eliminar peça?"
                          description={`A peça ${p.nome} será removida do catálogo.`}
                          destructive
                          onConfirm={() => handleRemove(p)}
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
        onSaved={() => { setOpen(false); reload(); }}
      />
    </div>
  );
}

function PecaDialog({
  open, onOpenChange, editing, fornecedores, onSaved,
}: {
  open: boolean;
  onOpenChange: (v: boolean) => void;
  editing: Peca | null;
  fornecedores: Fornecedor[];
  onSaved: () => void;
}) {
  const [form, setForm] = useState<PecaForm>(EMPTY);

  useEffect(() => {
    if (open) {
      setForm(
        editing
          ? {
              referencia: editing.referencia, nome: editing.nome, descricao: editing.descricao,
              fornecedorId: editing.fornecedorId, tipo: editing.tipo,
              precoVenda: editing.precoVenda, stockMinimo: editing.stockMinimo,
            }
          : EMPTY,
      );
    }
  }, [open, editing]);

  const set = <K extends keyof PecaForm>(k: K, v: PecaForm[K]) =>
    setForm((f) => ({ ...f, [k]: v }));

  const submit = async () => {
    if (!form.referencia.trim()) { toast.error("Referência obrigatória"); return; }
    if (!form.nome.trim()) { toast.error("Nome obrigatório"); return; }
    if (!form.fornecedorId) { toast.error("Fornecedor obrigatório"); return; }
    if (!form.tipo.trim()) { toast.error("Tipo obrigatório"); return; }
    if (form.precoVenda < 0) { toast.error("Preço inválido"); return; }
    if (form.stockMinimo < 0) { toast.error("Stock mínimo inválido"); return; }

    try {
      if (editing) {
        await pecasService.update(editing.id, form);
        toast.success("Peça atualizada");
      } else {
        await pecasService.create(form);
        toast.success("Peça criada");
      }
      onSaved();
    } catch (e) {
      toast.error((e as Error).message);
    }
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
          <Field label="Tipo">
            <Input value={form.tipo} onChange={(e) => set("tipo", e.target.value)} placeholder="ex: Bateria, Pneu, Motor…" />
          </Field>
          <Field label="Fornecedor">
            <Select value={form.fornecedorId} onValueChange={(v) => set("fornecedorId", v)}>
              <SelectTrigger><SelectValue placeholder="Escolher fornecedor…" /></SelectTrigger>
              <SelectContent>
                {fornecedores.map((f) => (
                  <SelectItem key={f.id} value={f.id}>{f.nome}</SelectItem>
                ))}
              </SelectContent>
            </Select>
          </Field>
          <Field label="Preço de venda (€)">
            <Input type="number" step="0.01" min={0} value={form.precoVenda}
              onChange={(e) => set("precoVenda", Number(e.target.value))} />
          </Field>
          <Field label="Stock mínimo">
            <Input type="number" min={0} value={form.stockMinimo}
              onChange={(e) => set("stockMinimo", Number(e.target.value))} />
          </Field>
        </div>
        <DialogFooter>
          <Button variant="outline" onClick={() => onOpenChange(false)}>Cancelar</Button>
          <Button onClick={submit}>{editing ? "Guardar" : "Criar"}</Button>
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
