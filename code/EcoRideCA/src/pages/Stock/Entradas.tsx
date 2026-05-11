import { useEffect, useState } from "react";
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

interface UnidadeSerie { nr_serie: string; garantia: number; }

function EntradaDialog({
  open, onOpenChange, pecas, onSaved,
}: {
  open: boolean; onOpenChange: (v: boolean) => void;
  pecas: Peca[]; onSaved: () => void;
}) {
  const [pecaId, setPecaId] = useState("");
  const [quantidade, setQuantidade] = useState(1);
  const [preco, setPreco] = useState(0);
  const [dataChegada, setDataChegada] = useState(new Date().toISOString().slice(0, 10));
  const [comGarantia, setComGarantia] = useState(false);
  const [unidades, setUnidades] = useState<UnidadeSerie[]>([]);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (open) {
      setPecaId(""); setQuantidade(1); setPreco(0);
      setDataChegada(new Date().toISOString().slice(0, 10));
      setComGarantia(false); setUnidades([]);
    }
  }, [open]);

  useEffect(() => {
    if (!comGarantia) { setUnidades([]); return; }
    setUnidades((cur) => {
      const next = [...cur];
      while (next.length < quantidade) next.push({ nr_serie: "", garantia: 12 });
      while (next.length > quantidade) next.pop();
      return next;
    });
  }, [quantidade, comGarantia]);

  const submit = async () => {
    if (!pecaId) { toast.error("Escolhe uma peça"); return; }
    if (quantidade < 1) { toast.error("Quantidade ≥ 1"); return; }
    setSaving(true);
    try {
      if (comGarantia) {
        for (const u of unidades) {
          if (!u.nr_serie.trim()) { toast.error("Preenche todos os nºs de série"); setSaving(false); return; }
          await api.post("/stocks/garantia", {
            codPeca: Number(pecaId), preco, dataChegada, garantia: u.garantia, nr_serie: u.nr_serie.trim(),
          });
        }
      } else {
        await api.post("/stocks", {
          codPeca: Number(pecaId), preco, quantidade, dataChegada,
        });
      }
      toast.success("Entrada registada");
      onSaved();
    } catch (e) { toast.error((e as Error).message); }
    finally { setSaving(false); }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-xl">
        <DialogHeader><DialogTitle>Nova entrada de stock</DialogTitle></DialogHeader>
        <div className="grid gap-3 sm:grid-cols-2">
          <div className="sm:col-span-2 space-y-1">
            <Label className="text-xs">Peça</Label>
            <Select value={pecaId} onValueChange={setPecaId}>
              <SelectTrigger><SelectValue placeholder="Escolher peça…" /></SelectTrigger>
              <SelectContent>
                {pecas.map((p) => (
                  <SelectItem key={p.id} value={String(p.id)}>{p.referencia} — {p.nome}</SelectItem>
                ))}
              </SelectContent>
            </Select>
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
            <div className="space-y-1">
              <Label className="text-xs">Quantidade</Label>
              <Input type="number" min={1} value={quantidade} onChange={(e) => setQuantidade(Number(e.target.value))} />
            </div>
          )}
          {comGarantia && (
            <div className="space-y-1">
              <Label className="text-xs">Nº de unidades</Label>
              <Input type="number" min={1} value={quantidade} onChange={(e) => setQuantidade(Number(e.target.value))} />
            </div>
          )}
          <div className="space-y-1">
            <Label className="text-xs">Preço de compra (€)</Label>
            <Input type="number" step="0.01" min={0} value={preco} onChange={(e) => setPreco(Number(e.target.value))} />
          </div>
          <div className="space-y-1">
            <Label className="text-xs">Data de chegada</Label>
            <Input type="date" value={dataChegada} onChange={(e) => setDataChegada(e.target.value)} />
          </div>

          {comGarantia && unidades.length > 0 && (
            <div className="sm:col-span-2 rounded-md border border-warning/30 bg-warning-soft/40 p-3">
              <div className="mb-2 flex items-center gap-2 text-sm text-warning">
                <AlertTriangle className="h-4 w-4" />
                Preenche o nº de série e garantia para cada unidade.
              </div>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead className="w-10">#</TableHead>
                    <TableHead>Nº de série</TableHead>
                    <TableHead className="w-32">Garantia (meses)</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {unidades.map((u, i) => (
                    <TableRow key={i}>
                      <TableCell>{i + 1}</TableCell>
                      <TableCell>
                        <Input value={u.nr_serie} className="h-8"
                          onChange={(e) => setUnidades((arr) =>
                            arr.map((x, j) => j === i ? { ...x, nr_serie: e.target.value } : x))}
                          placeholder="Nº de série" />
                      </TableCell>
                      <TableCell>
                        <Input type="number" min={0} max={120} value={u.garantia} className="h-8"
                          onChange={(e) => setUnidades((arr) =>
                            arr.map((x, j) => j === i ? { ...x, garantia: Number(e.target.value) } : x))} />
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
          <Button onClick={submit} disabled={saving}>
            {saving ? "A registar…" : "Registar entrada"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
