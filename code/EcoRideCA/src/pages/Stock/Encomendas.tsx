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
interface StockEntry { id: number; codPeca: number; quantidade: number; estado: string; }


export default function StockEncomendas() {
  const qc = useQueryClient();
  const { role: userRole } = useAuth();
  const [openNew, setOpenNew] = useState(false);
  const [openAuto, setOpenAuto] = useState(false);
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

  const fornecedorNome = (id: number) => fornecedores.find((f) => f.id === id)?.nome ?? "—";

  return (
    <div>
      <PageHeader
        title="Stock"
        description="Catálogo de peças, entradas, devoluções e encomendas"
        actions={
          canEdit ? (
            <div className="flex gap-2">
              <Button variant="outline" onClick={() => setOpenAuto(true)}>
                <Sparkles className="h-4 w-4" /> Lista automática
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
          <ListaAutomaticaDialog
            open={openAuto}
            onOpenChange={setOpenAuto}
            pecas={pecas}
            fornecedores={fornecedores}
            onSaved={() => { setOpenAuto(false); invalidate(); }}
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

function ListaAutomaticaDialog({
  open, onOpenChange, pecas, fornecedores, onSaved,
}: {
  open: boolean; onOpenChange: (v: boolean) => void;
  pecas: Peca[]; fornecedores: Fornecedor[]; onSaved: () => void;
}) {
  const [autoResult, setAutoResult] = useState<Record<string, Record<string, number>> | null>(null);
  const [generating, setGenerating] = useState(false);
  const [selectedFornId, setSelectedFornId] = useState<number | null>(null);
  const [itens, setItens] = useState<ItemEncomenda[]>([]);
  const [saving, setSaving] = useState(false);

  const hasResult = autoResult !== null;

  const reset = () => {
    setAutoResult(null);
    setSelectedFornId(null);
    setItens([]);
  };

  useEffect(() => { if (!open) { reset(); setSaving(false); } }, [open]);

  const generate = async () => {
    setGenerating(true);
    try {
      const result = await api.post<Record<string, Record<string, number>>>("/encomendas/automatica", {});
      setAutoResult(result);
    } catch (e) {
      toast.error((e as Error).message);
    } finally {
      setGenerating(false);
    }
  };

  const handleClose = () => {
    reset();
    onOpenChange(false);
  };

  const pickFornecedor = (id: number) => {
    setSelectedFornId(id);
    if (!autoResult) return;
    const pecasSugeridas = autoResult[String(id)];
    if (!pecasSugeridas) return;
    setItens(
      Object.entries(pecasSugeridas).map(([codPecaStr, qtd]) => {
        return { codPeca: Number(codPecaStr), quantidade: qtd, preco_compra: 0 };
      })
    );
  };

  const updateQty = (i: number, v: number) =>
    setItens((prev) => prev.map((x, j) => j === i ? { ...x, quantidade: Math.max(1, v) } : x));

  const updatePreco = (i: number, v: string) =>
    setItens((prev) => prev.map((x, j) => j === i ? { ...x, preco_compra: v === "" ? 0 : Number(v) } : x));

  const removeItem = (i: number) => setItens((prev) => prev.filter((_, j) => j !== i));

  const pecaLabel = (codPeca: number) => {
    const p = pecas.find((x) => x.id === codPeca);
    return p ? `${p.referencia} · ${p.nome}` : `#${codPeca}`;
  };

  const submit = async () => {
    if (!selectedFornId || itens.length === 0) return;
    setSaving(true);
    try {
      await api.post("/encomendas", {
        cod_fornecedor: selectedFornId,
        itens: itens.map((i) => ({ codPeca: i.codPeca, quantidade: i.quantidade, preco_compra: i.preco_compra })),
      });
      toast.success("Encomenda criada");
      onSaved();
    } catch (e) {
      toast.error((e as Error).message);
      setSaving(false);
    }
  };

  const fornIds = hasResult ? Object.keys(autoResult).map(Number) : [];
  const fornComEncomendas = fornecedores.filter((f) => fornIds.includes(f.id));

  return (
    <Dialog open={open} onOpenChange={(v) => { if (!v) handleClose(); }}>
      <DialogContent className="max-w-3xl max-h-[90vh] overflow-y-auto">
        <DialogHeader><DialogTitle>Lista de encomenda automática</DialogTitle></DialogHeader>

        {!hasResult ? (
          <div className="space-y-3 py-4">
            <p className="text-sm text-muted-foreground">
              Gera automaticamente encomendas para todas as peças abaixo do stock mínimo,
              agrupadas por fornecedor. Depois escolhes o fornecedor e ajustas as quantidades.
            </p>
            <Button onClick={generate} disabled={generating}>
              <Sparkles className="h-4 w-4" /> {generating ? "A gerar…" : "Gerar lista"}
            </Button>
          </div>
        ) : fornComEncomendas.length === 0 ? (
          <div className="rounded-md border border-success/30 bg-success-soft p-4 text-sm text-success">
            Todas as peças estão acima do stock mínimo. Não há sugestões de encomenda.
          </div>
        ) : (
          <div className="space-y-5">
            <div className="space-y-2">
              <Label className="text-sm font-medium">1. Seleciona o fornecedor</Label>
              <div className="grid gap-2 sm:grid-cols-2">
                {fornComEncomendas.map((f) => {
                  const pecasFornecedor = autoResult[String(f.id)];
                  const nItems = pecasFornecedor ? Object.keys(pecasFornecedor).length : 0;
                  const isSelected = selectedFornId === f.id;
                  return (
                    <button
                      key={f.id}
                      type="button"
                      onClick={() => pickFornecedor(f.id)}
                      className={`rounded-lg border p-3 text-left transition-colors ${
                        isSelected
                          ? "border-primary bg-primary/5 ring-1 ring-primary"
                          : "border-border hover:border-primary/40"
                      }`}
                    >
                      <div className="font-medium text-sm">{f.nome}</div>
                      <div className="mt-0.5 text-xs text-muted-foreground">
                        {nItems} peça{nItems !== 1 ? "s" : ""} a encomendar
                      </div>
                    </button>
                  );
                })}
              </div>
            </div>

            {selectedFornId !== null && itens.length > 0 && (
              <div className="space-y-2">
                <Label className="text-sm font-medium">2. Revê e ajusta os itens</Label>
                <div className="rounded-md border">
                  <Table>
                    <TableHeader>
                      <TableRow>
                        <TableHead>Peça</TableHead>
                        <TableHead className="w-28">Quantidade</TableHead>
                        <TableHead className="w-36">Preço unit. (€)</TableHead>
                        <TableHead className="w-8" />
                      </TableRow>
                    </TableHeader>
                    <TableBody>
                      {itens.map((item, i) => (
                        <TableRow key={item.codPeca}>
                          <TableCell className="font-medium text-sm">{pecaLabel(item.codPeca)}</TableCell>
                          <TableCell>
                            <Input type="number" min={1} className="h-8"
                              value={item.quantidade}
                              onChange={(e) => updateQty(i, Number(e.target.value))} />
                          </TableCell>
                          <TableCell>
                            <Input type="number" step="0.01" min={0} className="h-8"
                              placeholder="0.00"
                              value={item.preco_compra || ""}
                              onChange={(e) => updatePreco(i, e.target.value)} />
                          </TableCell>
                          <TableCell>
                            <Button variant="ghost" size="icon" onClick={() => removeItem(i)}
                              disabled={itens.length === 1}>
                              <Trash2 className="h-4 w-4 text-destructive" />
                            </Button>
                          </TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </div>
              </div>
            )}
          </div>
        )}

        <DialogFooter>
          <Button variant="outline" onClick={handleClose} disabled={saving}>Cancelar</Button>
          <Button onClick={submit} disabled={saving || !selectedFornId || itens.length === 0}>
            {saving ? "A criar…" : "Criar encomenda"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
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

  const { data: pecasFornecedor = [], isLoading: isLoadingPecas } = useQuery<Peca[]>({
    queryKey: ["pecas", "fornecedor", codFornecedor],
    queryFn: () => api.get<Peca[]>(`/fornecedores/${codFornecedor}/pecas`),
    enabled: !!codFornecedor,
  });

  useEffect(() => {
    if (open) {
      setCodFornecedor(""); setItens([]); setPecaId(""); setQty(""); setPreco("");
    }
  }, [open]);

  const handleFornecedorChange = (val: string) => {
    setCodFornecedor(val);
    setItens([]);
    setPecaId("");
  };

  const addItem = () => {
    if (!pecaId) { toast.error("Escolhe uma peça"); return; }
    if (qty < 1) { toast.error("Quantidade ≥ 1"); return; }
    if (itens.some((i) => i.codPeca === Number(pecaId))) { toast.error("Peça já adicionada"); return; }
    setItens((p) => [...p, { codPeca: Number(pecaId), quantidade: qty, preco_compra: preco }]);
    setPecaId(""); setQty(""); setPreco("");
  };

  const pecasDisponiveis = useMemo(
    () => pecasFornecedor.filter((p) => p.ativa && !itens.some((i) => i.codPeca === p.id)),
    [pecasFornecedor, itens],
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
            <Select value={codFornecedor} onValueChange={handleFornecedorChange}>
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
                <Select value={pecaId} onValueChange={setPecaId} disabled={!codFornecedor || isLoadingPecas}>
                  <SelectTrigger><SelectValue placeholder={!codFornecedor ? "Escolher fornecedor..." : isLoadingPecas ? "A carregar..." : "Escolher…" } /></SelectTrigger>
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
  const [saving, setSaving] = useState(false);

  const submit = async () => {
    if (!encomenda) return;
    setSaving(true);
    try {
      await api.patch(`/encomendas/${encomenda.id}/recebida`, {});
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
          <p className="text-sm text-muted-foreground">
            Esta encomenda contém {nItems} stock{nItems !== 1 ? "s" : ""}. O tempo de garantia será obtido automaticamente a partir de cada peça.
          </p>
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