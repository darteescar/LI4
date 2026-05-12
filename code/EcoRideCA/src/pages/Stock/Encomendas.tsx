import { useEffect, useMemo, useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { Plus, Trash2, Sparkles, Send, PackageCheck } from "lucide-react";
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

type EstadoEncomenda = "RASCUNHO" | "ENVIADA" | "RECEBIDA";

interface Encomenda {
  id: number; codFornecedor: number; data_criacao: string;
  data_rececao: string | null; data_envio: string | null;
  estado: EstadoEncomenda; codStocks: number[];
}

interface Peca { id: number; referencia: string; nome: string; preco_venda: number; codFornecedor: number; ativa: boolean; }
interface Fornecedor { id: number; nome: string; }

const ESTADO_LABELS: Record<EstadoEncomenda, string> = {
  RASCUNHO: "Rascunho",
  ENVIADA: "Enviada",
  RECEBIDA: "Recebida",
};

const ESTADO_VARIANT: Record<EstadoEncomenda, "outline" | "secondary"> = {
  RASCUNHO: "outline",
  ENVIADA: "secondary",
  RECEBIDA: "secondary",
};

interface ItemEncomenda { codPeca: number; quantidade: number; preco_compra: number; }

export default function StockEncomendas() {
  const qc = useQueryClient();
  const { role: userRole } = useAuth();
  const [openNew, setOpenNew] = useState(false);
  const [openReceber, setOpenReceber] = useState<Encomenda | null>(null);

  const canEdit = userRole === "GERENTE" || userRole === "GESTOR_STOCK";

  const { data: encomendas = [], isLoading } = useQuery<Encomenda[]>({
    queryKey: ["encomendas"],
    queryFn: () => api.get<Encomenda[]>("/encomendas"),
  });

  const { data: pecas = [] } = useQuery<Peca[]>({
    queryKey: ["pecas"],
    queryFn: () => api.get<Peca[]>("/pecas"),
  });

  const { data: fornecedores = [] } = useQuery<Fornecedor[]>({
    queryKey: ["fornecedores"],
    queryFn: () => api.get<Fornecedor[]>("/fornecedores"),
  });

  const invalidate = () => qc.invalidateQueries({ queryKey: ["encomendas"] });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => api.delete(`/encomendas/${id}`),
    onSuccess: () => { toast.success("Encomenda removida"); invalidate(); },
    onError: (e: Error) => toast.error(e.message),
  });

  const enviadaMutation = useMutation({
    mutationFn: (id: number) => api.patch(`/encomendas/${id}/enviada`, {}),
    onSuccess: () => { toast.success("Marcada como enviada ao fornecedor"); invalidate(); },
    onError: (e: Error) => toast.error(e.message),
  });

  const autoMutation = useMutation({
    mutationFn: () => api.post<Encomenda>("/encomendas/automatica", {}),
    onSuccess: () => { toast.success("Lista automática gerada"); invalidate(); },
    onError: (e: Error) => toast.error(e.message),
  });

  const fornecedorNome = (id: number) => fornecedores.find((f) => f.id === id)?.nome ?? "—";

  return (
    <div>
      <PageHeader
        title="Stock"
        description="Catálogo de peças, entradas, devoluções e encomendas"
        actions={
          canEdit ? (
            <div className="flex gap-2">
              <Button variant="outline" onClick={() => autoMutation.mutate()} disabled={autoMutation.isPending}>
                <Sparkles className="h-4 w-4" />
                {autoMutation.isPending ? "A gerar…" : "Lista automática"}
              </Button>
              <Button onClick={() => setOpenNew(true)}>
                <Plus className="h-4 w-4" /> Nova encomenda
              </Button>
            </div>
          ) : null
        }
      />
      <StockTabs />

      <div className="rounded-lg border bg-card shadow-sm">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Nº</TableHead>
              <TableHead>Fornecedor</TableHead>
              <TableHead>Data criação</TableHead>
              <TableHead>Data envio</TableHead>
              <TableHead>Stocks</TableHead>
              <TableHead>Estado</TableHead>
              {canEdit && <TableHead className="w-[1%] text-right">Ações</TableHead>}
            </TableRow>
          </TableHeader>
          <TableBody>
            {isLoading ? (
              <TableRow><TableCell colSpan={canEdit ? 7 : 6} className="h-24 text-center text-sm text-muted-foreground">A carregar…</TableCell></TableRow>
            ) : encomendas.length === 0 ? (
              <TableRow><TableCell colSpan={canEdit ? 7 : 6} className="h-24 text-center text-sm text-muted-foreground">Sem encomendas</TableCell></TableRow>
            ) : encomendas.map((e) => (
              <TableRow key={e.id}>
                <TableCell className="font-mono text-xs">ENC-{e.id}</TableCell>
                <TableCell className="font-medium">{fornecedorNome(e.codFornecedor)}</TableCell>
                <TableCell className="text-xs text-muted-foreground">{e.data_criacao}</TableCell>
                <TableCell className="text-xs text-muted-foreground">{e.data_envio ?? "—"}</TableCell>
                <TableCell>{e.codStocks.length}</TableCell>
                <TableCell>
                  <Badge variant={ESTADO_VARIANT[e.estado]} className="text-xs">
                    {ESTADO_LABELS[e.estado] ?? e.estado}
                  </Badge>
                </TableCell>
                {canEdit && (
                  <TableCell className="text-right">
                    <div className="flex justify-end gap-1">
                      {e.estado === "RASCUNHO" && (
                        <ConfirmDialog
                          trigger={<Button variant="ghost" size="icon" title="Marcar como enviada"><Send className="h-4 w-4" /></Button>}
                          title="Marcar encomenda como enviada?"
                          description="A encomenda será marcada como enviada ao fornecedor."
                          onConfirm={() => enviadaMutation.mutate(e.id)}
                        />
                      )}
                      {e.estado === "ENVIADA" && (
                        <Button variant="ghost" size="icon" title="Registar receção"
                          onClick={() => setOpenReceber(e)}>
                          <PackageCheck className="h-4 w-4 text-success" />
                        </Button>
                      )}
                      {e.estado === "RASCUNHO" && (
                        <ConfirmDialog
                          trigger={<Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
                          title="Eliminar esta encomenda?"
                          destructive
                          onConfirm={() => deleteMutation.mutate(e.id)}
                        />
                      )}
                    </div>
                  </TableCell>
                )}
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
      <div className="mt-2 text-xs text-muted-foreground">{encomendas.length} encomendas</div>

      {canEdit && (
        <>
          <NovaEncomendaDialog
            open={openNew}
            onOpenChange={setOpenNew}
            pecas={pecas}
            fornecedores={fornecedores}
            onSaved={() => { setOpenNew(false); invalidate(); }}
          />
          <ReceberEncomendaDialog
            encomenda={openReceber}
            onClose={() => setOpenReceber(null)}
            onSaved={() => { setOpenReceber(null); invalidate(); qc.invalidateQueries({ queryKey: ["stocks"] }); }}
          />
        </>
      )}
    </div>
  );
}

function NovaEncomendaDialog({
  open, onOpenChange, pecas, fornecedores, onSaved,
}: {
  open: boolean; onOpenChange: (v: boolean) => void;
  pecas: Peca[]; fornecedores: Fornecedor[]; onSaved: () => void;
}) {
  const [codFornecedor, setCodFornecedor] = useState("");
  const [itens, setItens] = useState<ItemEncomenda[]>([]);
  const [pecaId, setPecaId] = useState("");
  const [qty, setQty] = useState<number | "">("");
  const [preco, setPreco] = useState<number | "">("");
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (open) {
      setCodFornecedor(""); setItens([]); setPecaId(""); setQty(""); setPreco("");
    }
  }, [open]);

  const addItem = () => {
    if (!pecaId) { toast.error("Escolhe uma peça"); return; }
    if (qty < 1) { toast.error("Quantidade ≥ 1"); return; }
    if (itens.some((i) => i.codPeca === Number(pecaId))) { toast.error("Peça já adicionada"); return; }
    setItens((p) => [...p, { codPeca: Number(pecaId), quantidade: qty, preco_compra: preco }]);
    setPecaId(""); setQty(""); setPreco("");
  };

  const pecasDisponiveis = useMemo(
    () => pecas.filter((p) => p.ativa && !itens.some((i) => i.codPeca === p.id)),
    [pecas, itens],
  );

  const submit = async () => {
    if (!codFornecedor) { toast.error("Escolhe o fornecedor"); return; }
    if (itens.length === 0) { toast.error("Adiciona pelo menos uma peça"); return; }
    setSaving(true);
    try {
      await api.post("/encomendas", {
        cod_fornecedor: Number(codFornecedor),
        itens: itens.map((i) => ({ codPeca: i.codPeca, quantidade: i.quantidade, preco_compra: i.preco_compra })),
      });
      toast.success("Encomenda criada");
      onSaved();
    } catch (e) { toast.error((e as Error).message); }
    finally { setSaving(false); }
  };

  const pecaLabel = (codPeca: number) => {
    const p = pecas.find((x) => x.id === codPeca);
    return p ? `${p.referencia} · ${p.nome}` : `#${codPeca}`;
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl">
        <DialogHeader><DialogTitle>Nova encomenda</DialogTitle></DialogHeader>
        <div className="space-y-4">
          <div className="space-y-1">
            <Label className="text-xs">Fornecedor</Label>
            <Select value={codFornecedor} onValueChange={setCodFornecedor}>
              <SelectTrigger><SelectValue placeholder="Escolher fornecedor…" /></SelectTrigger>
              <SelectContent>
                {fornecedores.map((f) => (
                  <SelectItem key={f.id} value={String(f.id)}>{f.nome}</SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="space-y-2 rounded-md border p-3">
            <div className="text-sm font-medium">Adicionar peça</div>
            <div className="grid gap-2 sm:grid-cols-4">
              <div className="sm:col-span-2 space-y-1">
                <Label className="text-xs">Peça</Label>
                <Select value={pecaId} onValueChange={setPecaId}>
                  <SelectTrigger><SelectValue placeholder="Escolher…" /></SelectTrigger>
                  <SelectContent>
                    {pecasDisponiveis.map((p) => (
                      <SelectItem key={p.id} value={String(p.id)}>{p.referencia} — {p.nome}</SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-1">
                <Label className="text-xs">Qtd</Label>
                <Input type="number" min={1} placeholder="1" value={qty} onChange={(e) => setQty(e.target.value === "" ? "" : Number(e.target.value))} />
              </div>
              <div className="space-y-1">
                <Label className="text-xs">Preço (€)</Label>
                <Input type="number" step="0.01" min={0} placeholder="0.00" value={preco} onChange={(e) => setPreco(e.target.value === "" ? "" : Number(e.target.value))} />
              </div>
            </div>
            <Button type="button" size="sm" onClick={addItem} disabled={!pecaId}>
              <Plus className="h-3 w-3" /> Adicionar
            </Button>
          </div>

          {itens.length > 0 && (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Peça</TableHead>
                  <TableHead>Qtd</TableHead>
                  <TableHead>Preço unit.</TableHead>
                  <TableHead>Subtotal</TableHead>
                  <TableHead />
                </TableRow>
              </TableHeader>
              <TableBody>
                {itens.map((item, i) => (
                  <TableRow key={i}>
                    <TableCell className="font-medium">{pecaLabel(item.codPeca)}</TableCell>
                    <TableCell>{item.quantidade}</TableCell>
                    <TableCell>{formatEUR(item.preco_compra)}</TableCell>
                    <TableCell>{formatEUR(item.preco_compra * item.quantidade)}</TableCell>
                    <TableCell>
                      <Button variant="ghost" size="icon"
                        onClick={() => setItens((p) => p.filter((_, j) => j !== i))}>
                        <Trash2 className="h-4 w-4 text-destructive" />
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </div>
        <DialogFooter>
          <Button variant="outline" onClick={() => onOpenChange(false)}>Cancelar</Button>
          <Button onClick={submit} disabled={saving || itens.length === 0}>
            {saving ? "A criar…" : "Criar encomenda"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

function ReceberEncomendaDialog({
  encomenda, onClose, onSaved,
}: {
  encomenda: Encomenda | null; onClose: () => void; onSaved: () => void;
}) {
  const [numeros, setNumeros] = useState<string[]>([]);
  const [garantias, setGarantias] = useState<number[]>([]);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (encomenda) {
      setNumeros(encomenda.codStocks.map(() => ""));
      setGarantias(encomenda.codStocks.map(() => 0));
    }
  }, [encomenda]);

  const submit = async () => {
    if (!encomenda) return;
    setSaving(true);
    try {
      await api.patch(`/encomendas/${encomenda.id}/recebida`, {
        numeros_serie: numeros,
        garantias,
      });
      toast.success("Encomenda marcada como recebida");
      onSaved();
    } catch (e) { toast.error((e as Error).message); }
    finally { setSaving(false); }
  };

  const nItems = encomenda?.codStocks.length ?? 0;

  return (
    <Dialog open={!!encomenda} onOpenChange={(v) => !v && onClose()}>
      <DialogContent className="max-w-xl">
        <DialogHeader><DialogTitle>Registar receção da encomenda</DialogTitle></DialogHeader>
        {nItems > 0 && (
          <div className="space-y-2">
            <p className="text-sm text-muted-foreground">
              Preenche os nºs de série e garantias para os {nItems} stock(s) desta encomenda (deixa vazio se não aplicável).
            </p>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead className="w-10">#</TableHead>
                  <TableHead>Nº de série</TableHead>
                  <TableHead className="w-32">Garantia (meses)</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {encomenda?.codStocks.map((_, i) => (
                  <TableRow key={i}>
                    <TableCell>{i + 1}</TableCell>
                    <TableCell>
                      <Input value={numeros[i] ?? ""} className="h-8"
                        onChange={(e) => setNumeros((arr) => arr.map((v, j) => j === i ? e.target.value : v))}
                        placeholder="Opcional" />
                    </TableCell>
                    <TableCell>
                      <Input type="number" min={0} value={garantias[i] ?? 0} className="h-8"
                        onChange={(e) => setGarantias((arr) => arr.map((v, j) => j === i ? Number(e.target.value) : v))} />
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>
        )}
        <DialogFooter>
          <Button variant="outline" onClick={onClose}>Cancelar</Button>
          <Button onClick={submit} disabled={saving}>
            {saving ? "A registar…" : "Confirmar receção"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
