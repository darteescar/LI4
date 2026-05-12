import { useEffect, useState } from "react";
import { useForm, useFieldArray } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { Plus, Trash2, AlertTriangle } from "lucide-react";
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
  Select, SelectContent, SelectItem, SelectTrigger, SelectValue,
} from "@/components/ui/select";
import {
  Table, TableBody, TableCell, TableHead, TableHeader, TableRow,
} from "@/components/ui/table";

import { api } from "@/services/api";
import { formatEUR } from "@/lib/format";
import { useAuth } from "@/context/AuthContext";
import { entradaNormalSchema, entradaGarantiaSchema } from "@/lib/validators";

interface Peca { id: number; referencia: string; nome: string; preco_venda: number; codFornecedor: number; }
interface StockEntry {
  id: number; codPeca: number; quantidade: number; estado: string;
  preco_compra: number; data_chegada: string;
  nr_serie?: string; garantia?: number;
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
type GarantiaForm = z.infer<typeof entradaGarantiaSchema>;

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
          pecas={pecas}
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
  open, onOpenChange, pecas, onSaved,
}: {
  open: boolean; onOpenChange: (v: boolean) => void;
  pecas: Peca[]; onSaved: () => void;
}) {
  const [comGarantia, setComGarantia] = useState(false);

  const normalForm = useForm<NormalForm>({
    resolver: zodResolver(entradaNormalSchema),
    defaultValues: { pecaId: 0, quantidade: "" as unknown as number, preco: "" as unknown as number, dataChegada: today() },
  });

  const garantiaForm = useForm<GarantiaForm>({
    resolver: zodResolver(entradaGarantiaSchema),
    defaultValues: { pecaId: 0, preco: "" as unknown as number, dataChegada: today(), unidades: [{ nr_serie: "", garantia: 12 }] },
  });

  const { fields, append, remove } = useFieldArray({
    control: garantiaForm.control,
    name: "unidades",
  });

  useEffect(() => {
    if (!open) {
      normalForm.reset({ pecaId: 0, quantidade: "" as unknown as number, preco: "" as unknown as number, dataChegada: today() });
      garantiaForm.reset({ pecaId: 0, preco: "" as unknown as number, dataChegada: today(), unidades: [{ nr_serie: "", garantia: 12 }] });
      setComGarantia(false);
    }
  }, [open]);

  const pecaId = comGarantia ? garantiaForm.watch("pecaId") : normalForm.watch("pecaId");
  const setPecaId = (n: number) => {
    normalForm.setValue("pecaId", n, { shouldValidate: true });
    garantiaForm.setValue("pecaId", n, { shouldValidate: true });
  };

  const preco = comGarantia ? garantiaForm.watch("preco") : normalForm.watch("preco");
  const setPreco = (n: number) => {
    normalForm.setValue("preco", n, { shouldValidate: true });
    garantiaForm.setValue("preco", n, { shouldValidate: true });
  };

  const dataChegada = comGarantia ? garantiaForm.watch("dataChegada") : normalForm.watch("dataChegada");
  const setDataChegada = (s: string) => {
    normalForm.setValue("dataChegada", s, { shouldValidate: true });
    garantiaForm.setValue("dataChegada", s, { shouldValidate: true });
  };

  const submitNormal = normalForm.handleSubmit(async (v) => {
    try {
      await api.post("/stocks", { codPeca: v.pecaId, preco: v.preco, quantidade: v.quantidade, dataChegada: v.dataChegada });
      toast.success("Entrada registada");
      onSaved();
    } catch (e) { toast.error((e as Error).message); }
  });

  const submitGarantia = garantiaForm.handleSubmit(async (v) => {
    try {
      for (const u of v.unidades) {
        await api.post("/stocks/garantia", {
          codPeca: v.pecaId, preco: v.preco, dataChegada: v.dataChegada,
          garantia: u.garantia, nr_serie: u.nr_serie.trim(),
        });
      }
      toast.success("Entrada registada");
      onSaved();
    } catch (e) { toast.error((e as Error).message); }
  });

  const isPending = comGarantia
    ? garantiaForm.formState.isSubmitting
    : normalForm.formState.isSubmitting;

  const pecaErr = comGarantia
    ? garantiaForm.formState.errors.pecaId?.message
    : normalForm.formState.errors.pecaId?.message;

  const precoErr = comGarantia
    ? garantiaForm.formState.errors.preco?.message
    : normalForm.formState.errors.preco?.message;

  const dataErr = comGarantia
    ? garantiaForm.formState.errors.dataChegada?.message
    : normalForm.formState.errors.dataChegada?.message;

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-xl">
        <DialogHeader><DialogTitle>Nova entrada de stock</DialogTitle></DialogHeader>
        <form onSubmit={comGarantia ? submitGarantia : submitNormal} className="grid gap-3 sm:grid-cols-2">

          <div className="sm:col-span-2 space-y-1">
            <Label className="text-xs">Peça</Label>
            <Select value={String(pecaId)} onValueChange={(v) => setPecaId(Number(v))}>
              <SelectTrigger><SelectValue placeholder="Escolher peça…" /></SelectTrigger>
              <SelectContent>
                {pecas.map((p) => (
                  <SelectItem key={p.id} value={String(p.id)}>{p.referencia} — {p.nome}</SelectItem>
                ))}
              </SelectContent>
            </Select>
            {pecaErr && <p className="text-xs text-destructive">{pecaErr}</p>}
          </div>

          <div className="space-y-1">
            <Label className="text-xs">Tipo</Label>
            <Select value={comGarantia ? "garantia" : "normal"} onValueChange={(v) => setComGarantia(v === "garantia")}>
              <SelectTrigger><SelectValue /></SelectTrigger>
              <SelectContent>
                <SelectItem value="normal">Fungível (sem série)</SelectItem>
                <SelectItem value="garantia">Com nº série e garantia</SelectItem>
              </SelectContent>
            </Select>
          </div>

          {!comGarantia && (
            <F label="Quantidade" error={normalForm.formState.errors.quantidade?.message}>
              <Input type="number" min={1} placeholder="1" {...normalForm.register("quantidade")} />
            </F>
          )}

          <F label="Preço de compra (€)" error={precoErr}>
            <Input type="number" step="0.01" min={0} placeholder="0.00"
              value={preco}
              onChange={(e) => setPreco(Number(e.target.value))}
            />
          </F>

          <F label="Data de chegada" error={dataErr}>
            <Input type="date"
              value={dataChegada}
              onChange={(e) => setDataChegada(e.target.value)}
            />
          </F>

          {comGarantia && (
            <div className="sm:col-span-2 rounded-md border border-warning/30 bg-warning-soft/40 p-3 space-y-2">
              <div className="flex items-center gap-2 text-sm text-warning">
                <AlertTriangle className="h-4 w-4" />
                Preenche o nº de série e garantia para cada unidade.
              </div>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead className="w-10">#</TableHead>
                    <TableHead>Nº de série</TableHead>
                    <TableHead className="w-36">Garantia (meses)</TableHead>
                    <TableHead className="w-10" />
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {fields.map((field, i) => (
                    <TableRow key={field.id}>
                      <TableCell>{i + 1}</TableCell>
                      <TableCell>
                        <Input className="h-8" placeholder="Nº de série"
                          {...garantiaForm.register(`unidades.${i}.nr_serie`)} />
                        {garantiaForm.formState.errors.unidades?.[i]?.nr_serie && (
                          <p className="text-xs text-destructive mt-0.5">
                            {garantiaForm.formState.errors.unidades[i]!.nr_serie!.message}
                          </p>
                        )}
                      </TableCell>
                      <TableCell>
                        <Input type="number" min={1} max={120} className="h-8"
                          {...garantiaForm.register(`unidades.${i}.garantia`)} />
                        {garantiaForm.formState.errors.unidades?.[i]?.garantia && (
                          <p className="text-xs text-destructive mt-0.5">
                            {garantiaForm.formState.errors.unidades[i]!.garantia!.message}
                          </p>
                        )}
                      </TableCell>
                      <TableCell>
                        <Button type="button" variant="ghost" size="icon"
                          onClick={() => remove(i)} disabled={fields.length === 1}>
                          <Trash2 className="h-3.5 w-3.5 text-destructive" />
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
              <Button type="button" variant="outline" size="sm" onClick={() => append({ nr_serie: "", garantia: 12 })}>
                <Plus className="h-3.5 w-3.5" /> Adicionar unidade
              </Button>
            </div>
          )}

          <DialogFooter className="sm:col-span-2">
            <Button type="button" variant="outline" onClick={() => onOpenChange(false)}>Cancelar</Button>
            <Button type="submit" disabled={isPending}>
              {isPending ? "A registar…" : "Registar entrada"}
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
