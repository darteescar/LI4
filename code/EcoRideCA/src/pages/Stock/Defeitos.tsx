import { useMemo, useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { Trash2, Wrench } from "lucide-react";
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
  Table, TableBody, TableCell, TableHead, TableHeader, TableRow,
} from "@/components/ui/table";

import { api } from "@/services/api";
import { useAuth } from "@/context/AuthContext";
import { formatEUR } from "@/lib/format";

interface Defeito {
  id: number; codStock: number; motivo: string; idFuncionario: number;
  estadoAnterior: string;
}

interface StockEntry {
  id: number; codPeca: number; quantidade: number;
  preco_compra: number; data_chegada: string; estado: string;
}
interface Peca { id: number; referencia: string; nome: string; }

function today() { return new Date().toISOString().slice(0, 10); }

export default function StockDefeitos() {
  const { role } = useAuth();
  const qc = useQueryClient();
  const [resolverDefeito, setResolverDefeito] = useState<Defeito | null>(null);

  const canEdit = role === "GERENTE" || role === "GESTOR_STOCK";

  const { data: defeitos = [], isLoading } = useQuery<Defeito[]>({
    queryKey: ["defeitos"],
    queryFn: () => api.get<Defeito[]>("/defeitos"),
  });

  const { data: stocks = [] } = useQuery<StockEntry[]>({
    queryKey: ["stocks", "historico"],
    queryFn: () => api.get<StockEntry[]>("/stocks?historico=true"),
  });

  const { data: pecas = [] } = useQuery<Peca[]>({
    queryKey: ["pecas"],
    queryFn: () => api.get<Peca[]>("/pecas"),
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => api.delete(`/defeitos/${id}`),
    onSuccess: () => {
      toast.success("Defeito removido");
      qc.invalidateQueries({ queryKey: ["defeitos"] });
      qc.invalidateQueries({ queryKey: ["stocks"] });
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const stockInfo = (codStock: number) => {
    const s = stocks.find((x) => x.id === codStock);
    if (!s) return { label: `Stock #${codStock}`, stock: null, peca: null };
    const p = pecas.find((x) => x.id === s.codPeca) ?? null;
    const label = p ? `${p.referencia} · ${p.nome}` : `Peça #${s.codPeca}`;
    return { label, stock: s, peca: p };
  };

  const invalidateAll = () => {
    qc.invalidateQueries({ queryKey: ["defeitos"] });
    qc.invalidateQueries({ queryKey: ["stocks"] });
    qc.invalidateQueries({ queryKey: ["devolucoes"] });
  };

  return (
    <div>
      <PageHeader
        title="Stock"
        description="Catálogo de peças, entradas, devoluções e encomendas"
      />
      <StockTabs />

      <div className="rounded-lg border bg-card shadow-sm">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Nº</TableHead>
              <TableHead>Peça</TableHead>
              <TableHead>Qtd bloqueada</TableHead>
              <TableHead>Data chegada</TableHead>
              <TableHead>Motivo do defeito</TableHead>
              {canEdit && <TableHead className="w-[1%] text-right">Ações</TableHead>}
            </TableRow>
          </TableHeader>
          <TableBody>
            {isLoading ? (
              <TableRow><TableCell colSpan={canEdit ? 6 : 5} className="h-24 text-center text-sm text-muted-foreground">A carregar…</TableCell></TableRow>
            ) : defeitos.length === 0 ? (
              <TableRow><TableCell colSpan={canEdit ? 6 : 5} className="h-24 text-center text-sm text-muted-foreground">Sem defeitos registados</TableCell></TableRow>
            ) : defeitos.map((d) => {
              const { label, stock } = stockInfo(d.codStock);
              return (
                <TableRow key={d.id}>
                  <TableCell className="font-mono text-xs">DEF-{d.id}</TableCell>
                  <TableCell className="font-medium">{label}</TableCell>
                  <TableCell>
                    {stock
                      ? <Badge variant="destructive" className="font-mono">{stock.quantidade}</Badge>
                      : "—"}
                  </TableCell>
                  <TableCell className="text-xs text-muted-foreground">
                    {stock?.data_chegada ?? "—"}
                  </TableCell>
                  <TableCell className="max-w-[220px] truncate text-sm" title={d.motivo}>
                    {d.motivo}
                  </TableCell>
                  {canEdit && (
                    <TableCell className="text-right">
                      <div className="flex justify-end gap-1">
                        <Button
                          variant="outline"
                          size="sm"
                          className="gap-1 text-xs"
                          onClick={() => setResolverDefeito(d)}
                        >
                          <Wrench className="h-3.5 w-3.5" /> Resolver
                        </Button>
                        <ConfirmDialog
                          trigger={
                            <Button variant="ghost" size="icon" title="Remover registo">
                              <Trash2 className="h-4 w-4 text-destructive" />
                            </Button>
                          }
                          title="Remover registo de defeito?"
                          description="O stock bloqueado não será alterado — apenas o registo do defeito é removido."
                          destructive
                          onConfirm={() => deleteMutation.mutate(d.id)}
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
      <div className="mt-2 text-xs text-muted-foreground">{defeitos.length} defeito{defeitos.length !== 1 ? "s" : ""}</div>

      <ResolverDefeitoDialog
        defeito={resolverDefeito}
        stocks={stocks}
        pecas={pecas}
        onClose={() => setResolverDefeito(null)}
        onSaved={() => { setResolverDefeito(null); invalidateAll(); }}
      />
    </div>
  );
}

// ---------- Resolver Defeito Dialog ----------
function ResolverDefeitoDialog({
  defeito, stocks, pecas, onClose, onSaved,
}: {
  defeito: Defeito | null;
  stocks: StockEntry[];
  pecas: Peca[];
  onClose: () => void;
  onSaved: () => void;
}) {
  // "devolver" = split + devolução ao fornecedor
  // "repor"    = descartar (todas as unidades voltam ao stock)
  type Acao = "devolver" | "repor" | null;

  const [acao, setAcao] = useState<Acao>(null);
  const [qtdDefeituosa, setQtdDefeituosa] = useState(1);
  const [motivo, setMotivo] = useState("");
  const [data, setData] = useState(today());
  const [saving, setSaving] = useState(false);

  const stock = useMemo(
    () => defeito ? stocks.find((s) => s.id === defeito.codStock) ?? null : null,
    [defeito, stocks],
  );
  const peca = useMemo(
    () => stock ? pecas.find((p) => p.id === stock.codPeca) ?? null : null,
    [stock, pecas],
  );

  const qtdTotal = stock?.quantidade ?? 1;
  const qtdBoa = qtdTotal - qtdDefeituosa;

  const reset = () => {
    setAcao(null); setQtdDefeituosa(1); setMotivo(""); setData(today()); setSaving(false);
  };

  const handleClose = () => { reset(); onClose(); };

  const submeter = async () => {
    if (!defeito || !acao) return;
    setSaving(true);
    try {
      if (acao === "devolver") {
        // Split + devolução ao fornecedor
        await api.patch(`/defeitos/${defeito.id}/split`, {
          quantidade: qtdDefeituosa,
          motivo: motivo.trim(),
          data,
        });
        toast.success(
          qtdBoa > 0
            ? `${qtdDefeituosa} unidade${qtdDefeituosa > 1 ? "s" : ""} enviada${qtdDefeituosa > 1 ? "s" : ""} para devolução, ${qtdBoa} reposta${qtdBoa > 1 ? "s" : ""} em stock`
            : "Devolução ao fornecedor criada"
        );
      } else {
        // Descartar — todas as unidades voltam ao estado anterior
        await api.patch(`/defeitos/${defeito.id}/descartar`, {});
        toast.success("Stock reposto — defeito descartado");
      }
      reset();
      onSaved();
    } catch (e) { toast.error((e as Error).message); }
    finally { setSaving(false); }
  };

  const podeSubmeter =
    acao === "repor" ||
    (acao === "devolver" && qtdDefeituosa >= 1 && qtdDefeituosa <= qtdTotal && motivo.trim().length > 0);

  return (
    <Dialog open={!!defeito} onOpenChange={(v) => { if (!v) handleClose(); }}>
      <DialogContent className="max-w-lg">
        <DialogHeader>
          <DialogTitle>Resolver defeito — DEF-{defeito?.id}</DialogTitle>
        </DialogHeader>

        {stock && peca && (
          <div className="space-y-4">

            {/* Info do stock bloqueado */}
            <div className="rounded-md border bg-muted/40 p-3 text-sm space-y-1">
              <div className="font-medium">
                <span className="font-mono text-xs text-muted-foreground">{peca.referencia}</span>
                {" — "}{peca.nome}
              </div>
              <div className="flex gap-4 text-xs text-muted-foreground">
                <span>Qtd bloqueada: <strong className="text-foreground">{qtdTotal}</strong></span>
                <span>Entrada: <strong className="text-foreground">{stock.data_chegada}</strong></span>
                <span>Preço compra: <strong className="text-foreground">{formatEUR(stock.preco_compra)}</strong></span>
              </div>
              <div className="text-xs text-muted-foreground">
                Motivo: <span className="italic">{defeito?.motivo}</span>
              </div>
            </div>

            {/* Escolha da ação */}
            <div className="space-y-1">
              <Label className="text-xs">O que fazer com este stock?</Label>
              <div className="grid grid-cols-2 gap-2">
                <button
                  type="button"
                  onClick={() => setAcao("devolver")}
                  className={`rounded-md border p-3 text-left text-sm transition-colors ${
                    acao === "devolver"
                      ? "border-primary bg-primary/5 text-primary"
                      : "hover:bg-muted/50"
                  }`}
                >
                  <div className="font-medium">Devolver ao fornecedor</div>
                  <div className="text-xs text-muted-foreground mt-0.5">
                    Cria uma devolução para as unidades defeituosas
                  </div>
                </button>
                <button
                  type="button"
                  onClick={() => { setAcao("repor"); setQtdDefeituosa(0); }}
                  className={`rounded-md border p-3 text-left text-sm transition-colors ${
                    acao === "repor"
                      ? "border-success bg-success-soft text-success"
                      : "hover:bg-muted/50"
                  }`}
                >
                  <div className="font-medium">Repor no stock</div>
                  <div className="text-xs text-muted-foreground mt-0.5">
                    Defeito descartado — todas as unidades voltam ao armazém
                  </div>
                </button>
              </div>
            </div>

            {/* Configuração da devolução */}
            {acao === "devolver" && (
              <div className="space-y-3">
                <div className="space-y-1">
                  <Label className="text-xs">
                    Unidades defeituosas
                    <span className="ml-1 font-normal text-muted-foreground">(de {qtdTotal} total)</span>
                  </Label>
                  <div className="flex items-center gap-3">
                    <Input
                      type="number"
                      min={1}
                      max={qtdTotal}
                      value={qtdDefeituosa}
                      onChange={(e) => setQtdDefeituosa(Math.min(qtdTotal, Math.max(1, Number(e.target.value))))}
                      className="w-24"
                    />
                    {qtdBoa > 0 && (
                      <span className="text-xs text-muted-foreground">
                        → <span className="text-success font-medium">{qtdBoa}</span> unidade{qtdBoa > 1 ? "s" : ""} voltam ao stock
                      </span>
                    )}
                    {qtdBoa === 0 && (
                      <span className="text-xs text-muted-foreground">
                        → todas as unidades são devolvidas ao fornecedor
                      </span>
                    )}
                  </div>
                </div>

                <div className="space-y-1">
                  <Label className="text-xs">Motivo da devolução</Label>
                  <Textarea
                    rows={2}
                    value={motivo}
                    onChange={(e) => setMotivo(e.target.value)}
                    placeholder="Descreve o motivo da devolução ao fornecedor…"
                  />
                  {!motivo.trim() && (
                    <p className="text-xs text-destructive">Motivo obrigatório</p>
                  )}
                </div>

                <div className="space-y-1">
                  <Label className="text-xs">Data de devolução</Label>
                  <Input type="date" value={data} onChange={(e) => setData(e.target.value)} className="w-40" />
                </div>
              </div>
            )}
          </div>
        )}

        <DialogFooter>
          <Button type="button" variant="outline" onClick={handleClose}>Cancelar</Button>
          <Button
            type="button"
            disabled={saving || !acao || !podeSubmeter}
            variant={acao === "devolver" ? "default" : "default"}
            onClick={submeter}
          >
            {saving ? "A processar…" : acao === "devolver" ? "Criar devolução" : acao === "repor" ? "Repor no stock" : "Confirmar"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}