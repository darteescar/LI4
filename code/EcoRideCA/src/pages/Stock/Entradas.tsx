import { useEffect, useState } from "react";
import { Plus, Trash2, AlertTriangle, Undo2, ShieldAlert } from "lucide-react";
import { toast } from "sonner";

import { PageHeader } from "@/components/layout/PageHeader";
import { StockTabs } from "@/components/layout/StockTabs";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { DataTable, type Column } from "@/components/data-table";
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

import {
  entradasService, pecasService, fornecedoresService,
} from "@/services/entities";
import { devolucoesService, pecasDefeituosasService } from "@/services/stock";
import { useAuth } from "@/context/AuthContext";
import { formatDateTime, formatEUR } from "@/lib/format";
import type { EntradaStock, Peca, Fornecedor, UnidadeSerie } from "@/lib/types";

export default function StockEntradas() {
  const { user, role } = useAuth();
  const [entradas, setEntradas] = useState<EntradaStock[]>([]);
  const [pecas, setPecas] = useState<Peca[]>([]);
  const [fornecedores, setFornecedores] = useState<Fornecedor[]>([]);
  const [open, setOpen] = useState(false);
  const [devolverEntrada, setDevolverEntrada] = useState<EntradaStock | null>(null);
  const [defeitoEntrada, setDefeitoEntrada] = useState<EntradaStock | null>(null);

  const reload = async () => {
    const [e, p, f] = await Promise.all([
      entradasService.list(),
      pecasService.list(),
      fornecedoresService.list(),
    ]);
    setEntradas([...e].sort((a, b) => b.data.localeCompare(a.data)));
    setPecas(p);
    setFornecedores(f);
  };
  useEffect(() => { reload(); }, []);

  const pecaNome = (id: string) => {
    const p = pecas.find((x) => x.id === id);
    return p ? `${p.referencia} · ${p.nome}` : "—";
  };

  const canEdit = role === "GERENTE" || role === "GESTOR_STOCK";

  const handleEliminar = async (e: EntradaStock) => {
    await entradasService.remove(e.id);
    toast.success("Entrada eliminada — stock revertido");
    reload();
  };

  const columns: Column<EntradaStock>[] = [
    { key: "data", header: "Data", cell: (e) => <span className="text-xs">{formatDateTime(e.data)}</span> },
    { key: "peca", header: "Peça", cell: (e) => <span className="font-medium">{pecaNome(e.pecaId)}</span> },
    { key: "qtd", header: "Qtd", cell: (e) => e.quantidade },
    { key: "preco", header: "Preço unit.", cell: (e) => formatEUR(e.precoUnitario) },
    { key: "total", header: "Total", cell: (e) => formatEUR(e.precoUnitario * e.quantidade) },
    {
      key: "serie",
      header: "Nºs de série",
      cell: (e) =>
        e.unidades && e.unidades.length > 0 ? (
          <div className="flex flex-wrap gap-1">
            {e.unidades.slice(0, 2).map((u, i) => (
              <Badge key={i} variant="outline" className="text-[10px] font-mono">{u.numeroSerie}</Badge>
            ))}
            {e.unidades.length > 2 && (
              <Badge variant="outline" className="text-[10px]">+{e.unidades.length - 2}</Badge>
            )}
          </div>
        ) : <span className="text-xs text-muted-foreground">—</span>,
    },
    ...(canEdit ? [{
      key: "acoes",
      header: "Ações",
      cell: (e: EntradaStock) => (
        <div className="flex justify-end gap-1">
          <Button
            variant="ghost" size="icon" title="Devolver ao fornecedor"
            onClick={() => setDevolverEntrada(e)}
          >
            <Undo2 className="h-4 w-4" />
          </Button>
          <Button
            variant="ghost" size="icon" title="Reportar defeito"
            onClick={() => setDefeitoEntrada(e)}
          >
            <ShieldAlert className="h-4 w-4 text-warning" />
          </Button>
          <ConfirmDialog
            trigger={
              <Button variant="ghost" size="icon" title="Eliminar entrada">
                <Trash2 className="h-4 w-4 text-destructive" />
              </Button>
            }
            title="Eliminar esta entrada?"
            description="O stock atual da peça será revertido na quantidade desta entrada."
            destructive
            onConfirm={() => handleEliminar(e)}
          />
        </div>
      ),
    } as Column<EntradaStock>] : []),
  ];

  return (
    <div>
      <PageHeader
        title="Stock"
        description="Catálogo de peças, entradas, devoluções e encomendas"
        actions={
          canEdit && (
            <Button onClick={() => setOpen(true)}><Plus className="h-4 w-4" /> Nova entrada</Button>
          )
        }
      />
      <StockTabs />

      <DataTable
        data={entradas}
        columns={columns}
        emptyMessage="Sem entradas registadas"
        searchPlaceholder="Pesquisar…"
      />

      <EntradaDialog
        open={open}
        onOpenChange={setOpen}
        pecas={pecas}
        userId={user?.id ?? ""}
        onSaved={() => { setOpen(false); reload(); }}
      />

      <DevolverDialog
        entrada={devolverEntrada}
        pecas={pecas}
        fornecedores={fornecedores}
        onClose={() => setDevolverEntrada(null)}
        onSaved={() => { setDevolverEntrada(null); reload(); }}
      />

      <DefeitoDialog
        entrada={defeitoEntrada}
        pecas={pecas}
        userId={user?.id ?? ""}
        onClose={() => setDefeitoEntrada(null)}
        onSaved={() => { setDefeitoEntrada(null); reload(); }}
      />
    </div>
  );
}

function DevolverDialog({
  entrada, pecas, fornecedores, onClose, onSaved,
}: {
  entrada: EntradaStock | null;
  pecas: Peca[];
  fornecedores: Fornecedor[];
  onClose: () => void;
  onSaved: () => void;
}) {
  const peca = entrada ? pecas.find((p) => p.id === entrada.pecaId) ?? null : null;
  const [quantidade, setQuantidade] = useState(1);
  const [motivo, setMotivo] = useState("");
  const [fornecedorId, setFornecedorId] = useState("");

  useEffect(() => {
    if (entrada) {
      setQuantidade(entrada.quantidade);
      setMotivo("");
      setFornecedorId(peca?.fornecedorId ?? "");
    }
  }, [entrada, peca?.fornecedorId]);

  const submit = async () => {
    if (!entrada || !peca) return;
    if (quantidade < 1 || quantidade > entrada.quantidade) {
      toast.error("Quantidade inválida"); return;
    }
    if (!fornecedorId) { toast.error("Escolhe um fornecedor"); return; }
    if (!motivo.trim()) { toast.error("Indica o motivo"); return; }
    try {
      await devolucoesService.create({
        pecaId: entrada.pecaId,
        quantidade,
        motivo: motivo.trim(),
        fornecedorId,
      });
      toast.success("Devolução criada — pendente");
      onSaved();
    } catch (e) { toast.error((e as Error).message); }
  };

  return (
    <Dialog open={!!entrada} onOpenChange={(v) => !v && onClose()}>
      <DialogContent>
        <DialogHeader><DialogTitle>Devolver ao fornecedor</DialogTitle></DialogHeader>
        {entrada && (
          <div className="grid gap-3">
            <div className="text-sm">
              <span className="text-muted-foreground">Peça:</span>{" "}
              <span className="font-medium">{peca ? `${peca.referencia} · ${peca.nome}` : "—"}</span>
            </div>
            <div className="space-y-1">
              <Label className="text-xs">Fornecedor</Label>
              <Select value={fornecedorId} onValueChange={setFornecedorId}>
                <SelectTrigger><SelectValue placeholder="Escolher fornecedor…" /></SelectTrigger>
                <SelectContent>
                  {fornecedores.map((f) => (
                    <SelectItem key={f.id} value={f.id}>{f.nome}</SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-1">
              <Label className="text-xs">Quantidade (máx {entrada.quantidade})</Label>
              <Input
                type="number" min={1} max={entrada.quantidade}
                value={quantidade}
                onChange={(e) => setQuantidade(Number(e.target.value))}
              />
            </div>
            <div className="space-y-1">
              <Label className="text-xs">Motivo</Label>
              <Textarea value={motivo} onChange={(e) => setMotivo(e.target.value)} rows={3} />
            </div>
          </div>
        )}
        <DialogFooter>
          <Button variant="outline" onClick={onClose}>Cancelar</Button>
          <Button onClick={submit}>Criar devolução</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

function DefeitoDialog({
  entrada, pecas, userId, onClose, onSaved,
}: {
  entrada: EntradaStock | null;
  pecas: Peca[];
  userId: string;
  onClose: () => void;
  onSaved: () => void;
}) {
  const peca = entrada ? pecas.find((p) => p.id === entrada.pecaId) ?? null : null;
  const [quantidade, setQuantidade] = useState(1);
  const [motivo, setMotivo] = useState("");
  const [numeroSerie, setNumeroSerie] = useState("");

  useEffect(() => {
    if (entrada) {
      setQuantidade(1);
      setMotivo("");
      setNumeroSerie("");
    }
  }, [entrada]);

  const submit = async () => {
    if (!entrada) return;
    if (quantidade < 1 || quantidade > entrada.quantidade) {
      toast.error("Quantidade inválida"); return;
    }
    if (!motivo.trim()) { toast.error("Indica o motivo"); return; }
    try {
      await pecasDefeituosasService.reportar({
        pecaId: entrada.pecaId,
        quantidade,
        motivo: motivo.trim(),
        numeroSerie: numeroSerie.trim() || undefined,
        reportadaPor: userId,
      });
      toast.success("Defeito reportado");
      onSaved();
    } catch (e) { toast.error((e as Error).message); }
  };

  return (
    <Dialog open={!!entrada} onOpenChange={(v) => !v && onClose()}>
      <DialogContent>
        <DialogHeader><DialogTitle>Reportar peça defeituosa</DialogTitle></DialogHeader>
        {entrada && (
          <div className="grid gap-3">
            <div className="text-sm">
              <span className="text-muted-foreground">Peça:</span>{" "}
              <span className="font-medium">{peca ? `${peca.referencia} · ${peca.nome}` : "—"}</span>
            </div>
            <div className="space-y-1">
              <Label className="text-xs">Quantidade (máx {entrada.quantidade})</Label>
              <Input
                type="number" min={1} max={entrada.quantidade}
                value={quantidade}
                onChange={(e) => setQuantidade(Number(e.target.value))}
              />
            </div>
            <div className="space-y-1">
              <Label className="text-xs">Nº de série (opcional)</Label>
              <Input value={numeroSerie} onChange={(e) => setNumeroSerie(e.target.value)} />
            </div>
            <div className="space-y-1">
              <Label className="text-xs">Motivo</Label>
              <Textarea value={motivo} onChange={(e) => setMotivo(e.target.value)} rows={3} />
            </div>
          </div>
        )}
        <DialogFooter>
          <Button variant="outline" onClick={onClose}>Cancelar</Button>
          <Button onClick={submit}>Reportar defeito</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

function EntradaDialog({
  open, onOpenChange, pecas, userId, onSaved,
}: {
  open: boolean;
  onOpenChange: (v: boolean) => void;
  pecas: Peca[];
  userId: string;
  onSaved: () => void;
}) {
  const [pecaId, setPecaId] = useState("");
  const [quantidade, setQuantidade] = useState(1);
  const [precoUnitario, setPrecoUnitario] = useState(0);
  const [data, setData] = useState(new Date().toISOString().slice(0, 10));
  const [unidades, setUnidades] = useState<UnidadeSerie[]>([]);

  useEffect(() => {
    if (open) {
      setPecaId(""); setQuantidade(1); setPrecoUnitario(0);
      setData(new Date().toISOString().slice(0, 10));
      setUnidades([]);
    }
  }, [open]);

  const handlePecaChange = (id: string) => {
    setPecaId(id);
    setPrecoUnitario(0);
  };

  const exigeSerie = precoUnitario > 70;

  useEffect(() => {
    if (!exigeSerie) { setUnidades([]); return; }
    setUnidades((cur) => {
      const next = [...cur];
      while (next.length < quantidade) next.push({ numeroSerie: "", garantiaMeses: 12 });
      while (next.length > quantidade) next.pop();
      return next;
    });
  }, [quantidade, exigeSerie]);

  const submit = async () => {
    if (!pecaId) { toast.error("Escolhe uma peça"); return; }
    if (quantidade < 1) { toast.error("Quantidade ≥ 1"); return; }
    if (precoUnitario < 0) { toast.error("Preço inválido"); return; }
    try {
      await entradasService.create({
        pecaId, quantidade, precoUnitario,
        data: new Date(data).toISOString(),
        unidades: exigeSerie ? unidades : undefined,
        registadoPor: userId,
      });
      toast.success("Entrada registada — stock atualizado");
      onSaved();
    } catch (e) {
      toast.error((e as Error).message);
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl">
        <DialogHeader>
          <DialogTitle>Nova entrada de stock</DialogTitle>
        </DialogHeader>
        <div className="grid gap-3 sm:grid-cols-2">
          <div className="sm:col-span-2 space-y-1">
            <Label className="text-xs">Peça</Label>
            <Select value={pecaId} onValueChange={handlePecaChange}>
              <SelectTrigger><SelectValue placeholder="Escolher peça…" /></SelectTrigger>
              <SelectContent>
                {pecas.map((p) => (
                  <SelectItem key={p.id} value={p.id}>{p.referencia} — {p.nome}</SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
          <div className="space-y-1">
            <Label className="text-xs">Quantidade</Label>
            <Input type="number" min={1} value={quantidade} onChange={(e) => setQuantidade(Number(e.target.value))} />
          </div>
          <div className="space-y-1">
            <Label className="text-xs">Preço de compra unitário (€)</Label>
            <Input type="number" step="0.01" min={0} value={precoUnitario} onChange={(e) => setPrecoUnitario(Number(e.target.value))} />
            <p className="text-[10px] text-muted-foreground">
              Valor pago ao fornecedor por unidade. Não altera o preço de catálogo da peça.
            </p>
          </div>
          <div className="sm:col-span-2 space-y-1">
            <Label className="text-xs">Data</Label>
            <Input type="date" value={data} onChange={(e) => setData(e.target.value)} />
          </div>

          {exigeSerie && (
            <div className="sm:col-span-2 rounded-md border border-warning/30 bg-warning-soft/40 p-3">
              <div className="mb-2 flex items-center gap-2 text-sm text-warning">
                <AlertTriangle className="h-4 w-4" />
                <strong>Preço &gt; 70€</strong> — registar nº de série e garantia para cada unidade.
              </div>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead className="w-12">#</TableHead>
                    <TableHead>Nº de série</TableHead>
                    <TableHead className="w-32">Garantia (meses)</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {unidades.map((u, i) => (
                    <TableRow key={i}>
                      <TableCell>{i + 1}</TableCell>
                      <TableCell>
                        <Input
                          value={u.numeroSerie}
                          onChange={(e) => setUnidades((arr) =>
                            arr.map((x, j) => j === i ? { ...x, numeroSerie: e.target.value } : x))}
                          placeholder="Nº de série"
                          className="h-8"
                        />
                      </TableCell>
                      <TableCell>
                        <Input
                          type="number" min={0} max={120}
                          value={u.garantiaMeses}
                          onChange={(e) => setUnidades((arr) =>
                            arr.map((x, j) => j === i ? { ...x, garantiaMeses: Number(e.target.value) } : x))}
                          className="h-8"
                        />
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          )}
        </div>
        <DialogFooter>
          <Button variant="outline" onClick={() => onOpenChange(false)}>Cancelar</Button>
          <Button onClick={submit}>Registar entrada</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
