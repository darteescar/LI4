import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { Plus, Trash2, ChevronDown } from "lucide-react";
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
  Command, CommandEmpty, CommandGroup, CommandInput, CommandItem, CommandList,
} from "@/components/ui/command";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";

import { api } from "@/services/api";
import { formatEUR } from "@/lib/format";
import { useAuth } from "@/context/AuthContext";
import { entradaNormalSchema } from "@/lib/validators";

interface Peca { id: number; referencia: string; marca: string; nome: string; preco_venda: number; codFornecedor: number; }
interface StockEntry {
  id: number; codPeca: number; quantidade: number; estado: string;
  preco_compra: number; data_chegada: string;
  nr_serie?: string;
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

  const canEdit = role === "GERENTE" || role === "GESTOR_STOCK";

  const { data: stocks = [], isLoading } = useQuery<StockEntry[]>({
    queryKey: ["stocks"],
    queryFn: () => api.get<StockEntry[]>("/stocks"),
  });

  const { data: pecas = [] } = useQuery<Peca[]>({
    queryKey: ["pecas"],
    queryFn: () => api.get<Peca[]>("/pecas"),
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

  const sortedStocks = [...stocks].sort((a, b) =>
    (b.data_chegada ?? "").localeCompare(a.data_chegada ?? "")
  );

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

      <div className="rounded-lg border bg-card shadow-sm">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Data chegada</TableHead>
              <TableHead>Peça</TableHead>
              <TableHead>Qtd</TableHead>
              <TableHead>Preço compra</TableHead>
              <TableHead>Nº série</TableHead>
              <TableHead>Estado</TableHead>
              {canEdit && <TableHead className="w-[1%] text-right">Ações</TableHead>}
            </TableRow>
          </TableHeader>
          <TableBody>
            {isLoading ? (
              <TableRow><TableCell colSpan={canEdit ? 7 : 6} className="h-24 text-center text-sm text-muted-foreground">A carregar…</TableCell></TableRow>
            ) : sortedStocks.length === 0 ? (
              <TableRow><TableCell colSpan={canEdit ? 7 : 6} className="h-24 text-center text-sm text-muted-foreground">Sem entradas de stock</TableCell></TableRow>
            ) : sortedStocks.map((s) => (
              <TableRow key={s.id}>
                <TableCell className="text-xs text-muted-foreground">{s.data_chegada ?? "—"}</TableCell>
                <TableCell className="font-medium">{pecaLabel(s.codPeca)}</TableCell>
                <TableCell>{s.quantidade}</TableCell>
                <TableCell>{formatEUR(s.preco_compra)}</TableCell>
                <TableCell>
                  {s.nr_serie
                    ? <Badge variant="outline" className="font-mono text-[10px]">{s.nr_serie}</Badge>
                    : <span className="text-xs text-muted-foreground">—</span>}
                </TableCell>
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
      <div className="mt-2 text-xs text-muted-foreground">{sortedStocks.length} entradas</div>

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

  const form = useForm<NormalForm>({
    resolver: zodResolver(entradaNormalSchema),
    defaultValues: { pecaId: 0, quantidade: "" as unknown as number, preco: "" as unknown as number, dataChegada: today() },
  });

  useEffect(() => {
    if (!open) {
      form.reset({ pecaId: 0, quantidade: "" as unknown as number, preco: "" as unknown as number, dataChegada: today() });
    }
  }, [open]);

  const pecaId = form.watch("pecaId");
  const setPecaId = (n: number) => form.setValue("pecaId", n, { shouldValidate: true });

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
      <DialogContent className="max-w-xl">
        <DialogHeader><DialogTitle>Nova entrada de stock</DialogTitle></DialogHeader>
        <form onSubmit={submit} className="grid gap-3 sm:grid-cols-2">

          <div className="sm:col-span-2 space-y-1">
            <Label className="text-xs">Peça</Label>
            <PecaCombobox pecas={pecas} value={pecaId} onChange={setPecaId} />
            {form.formState.errors.pecaId && (
              <p className="text-xs text-destructive">{form.formState.errors.pecaId.message}</p>
            )}
          </div>

          <F label="Quantidade" error={form.formState.errors.quantidade?.message}>
            <Input type="number" min={1} placeholder="1" {...form.register("quantidade")} />
          </F>

          <F label="Preço de compra (€)" error={form.formState.errors.preco?.message}>
            <Input type="number" step="0.01" min={0} placeholder="0.00" {...form.register("preco")} />
          </F>

          <F label="Data de chegada" error={form.formState.errors.dataChegada?.message}>
            <Input type="date" {...form.register("dataChegada")} />
          </F>

          <DialogFooter className="sm:col-span-2">
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

function PecaCombobox({
  pecas, value, onChange,
}: {
  pecas: Peca[]; value: number; onChange: (id: number) => void;
}) {
  const [open, setOpen] = useState(false);
  const [query, setQuery] = useState("");

  const selected = value ? pecas.find((p) => p.id === value) : undefined;

  const filtered = pecas.filter((p) => {
    if (!query.trim()) return true;
    const q = query.toLowerCase();
    return (
      p.referencia.toLowerCase().includes(q) ||
      p.nome.toLowerCase().includes(q) ||
      (p.marca ?? "").toLowerCase().includes(q)
    );
  });

  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          type="button"
          variant="outline"
          role="combobox"
          aria-expanded={open}
          className="w-full justify-between font-normal"
        >
          {selected ? (
            <span className="truncate">
              <span className="font-mono text-xs">{selected.referencia}</span>
              {selected.marca && <span className="text-muted-foreground"> · {selected.marca}</span>}
              {" — "}{selected.nome}
            </span>
          ) : (
            <span className="text-muted-foreground">Escolher peça…</span>
          )}
          <ChevronDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
        </Button>
      </PopoverTrigger>
      <PopoverContent className="p-0" align="start" style={{ width: "var(--radix-popover-trigger-width)" }}>
        <Command shouldFilter={false}>
          <CommandInput
            placeholder="Pesquisar por referência, marca ou nome…"
            value={query}
            onValueChange={setQuery}
          />
          <CommandList>
            {filtered.length === 0 ? (
              <CommandEmpty>Nenhuma peça encontrada.</CommandEmpty>
            ) : (
              <CommandGroup>
                {filtered.map((p) => (
                  <CommandItem
                    key={p.id}
                    value={String(p.id)}
                    onSelect={() => { onChange(p.id); setQuery(""); setOpen(false); }}
                  >
                    <span className="font-mono text-xs mr-1">{p.referencia}</span>
                    {p.marca && <span className="text-muted-foreground text-xs">· {p.marca} · </span>}
                    <span className="text-sm">{p.nome}</span>
                  </CommandItem>
                ))}
              </CommandGroup>
            )}
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
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