import { useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { Trash2, Undo2, X } from "lucide-react";
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

interface Defeito {
  id: number; codStock: number; motivo: string; idFuncionario: number;
  estadoAnterior: string;
}

interface StockEntry { id: number; codPeca: number; quantidade: number; nr_serie?: string; }
interface Peca { id: number; referencia: string; nome: string; }

export default function StockDefeitos() {
  const { role } = useAuth();
  const qc = useQueryClient();
  const [devolverDefeito, setDevolverDefeito] = useState<Defeito | null>(null);

  const canEdit = role === "GERENTE" || role === "GESTOR_STOCK";

  const { data: defeitos = [], isLoading } = useQuery<Defeito[]>({
    queryKey: ["defeitos"],
    queryFn: () => api.get<Defeito[]>("/defeitos"),
  });

  const { data: stocks = [] } = useQuery<StockEntry[]>({
    queryKey: ["stocks"],
    queryFn: () => api.get<StockEntry[]>("/stocks"),
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
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const descartarMutation = useMutation({
    mutationFn: (id: number) => api.patch(`/defeitos/${id}/descartar`, {}),
    onSuccess: () => {
      toast.success("Defeito descartado");
      qc.invalidateQueries({ queryKey: ["defeitos"] });
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const stockLabel = (codStock: number) => {
    const s = stocks.find((x) => x.id === codStock);
    if (!s) return `Stock #${codStock}`;
    const p = pecas.find((x) => x.id === s.codPeca);
    const pLabel = p ? `${p.referencia} · ${p.nome}` : `Peça #${s.codPeca}`;
    return s.nr_serie ? `${pLabel} (${s.nr_serie})` : pLabel;
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
              <TableHead>Stock / Peça</TableHead>
              <TableHead>Motivo</TableHead>
              <TableHead>Estado anterior</TableHead>
              {canEdit && <TableHead className="w-[1%] text-right">Ações</TableHead>}
            </TableRow>
          </TableHeader>
          <TableBody>
            {isLoading ? (
              <TableRow><TableCell colSpan={canEdit ? 5 : 4} className="h-24 text-center text-sm text-muted-foreground">A carregar…</TableCell></TableRow>
            ) : defeitos.length === 0 ? (
              <TableRow><TableCell colSpan={canEdit ? 5 : 4} className="h-24 text-center text-sm text-muted-foreground">Sem defeitos registados</TableCell></TableRow>
            ) : defeitos.map((d) => (
              <TableRow key={d.id}>
                <TableCell className="font-mono text-xs">DEF-{d.id}</TableCell>
                <TableCell className="font-medium">{stockLabel(d.codStock)}</TableCell>
                <TableCell className="max-w-[240px] truncate text-sm" title={d.motivo}>{d.motivo}</TableCell>
                <TableCell>
                  <Badge variant="outline" className="text-xs">{d.estadoAnterior}</Badge>
                </TableCell>
                {canEdit && (
                  <TableCell className="text-right">
                    <div className="flex justify-end gap-1">
                      <Button variant="ghost" size="icon" title="Devolver ao fornecedor"
                        onClick={() => setDevolverDefeito(d)}>
                        <Undo2 className="h-4 w-4" />
                      </Button>
                      <ConfirmDialog
                        trigger={<Button variant="ghost" size="icon" title="Descartar"><X className="h-4 w-4 text-warning" /></Button>}
                        title="Descartar defeito?"
                        description="O item defeituoso será marcado como descartado."
                        destructive
                        onConfirm={() => descartarMutation.mutate(d.id)}
                      />
                      <ConfirmDialog
                        trigger={<Button variant="ghost" size="icon" title="Remover"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
                        title="Remover registo de defeito?"
                        destructive
                        onConfirm={() => deleteMutation.mutate(d.id)}
                      />
                    </div>
                  </TableCell>
                )}
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
      <div className="mt-2 text-xs text-muted-foreground">{defeitos.length} defeitos</div>

      <DevolverDialog
        defeito={devolverDefeito}
        onClose={() => setDevolverDefeito(null)}
        onSaved={() => {
          setDevolverDefeito(null);
          qc.invalidateQueries({ queryKey: ["defeitos"] });
          qc.invalidateQueries({ queryKey: ["devolucoes"] });
        }}
      />
    </div>
  );
}

function DevolverDialog({
  defeito, onClose, onSaved,
}: {
  defeito: Defeito | null; onClose: () => void; onSaved: () => void;
}) {
  const [motivo, setMotivo] = useState("");
  const [data, setData] = useState(new Date().toISOString().slice(0, 10));
  const [saving, setSaving] = useState(false);

  const submit = async () => {
    if (!defeito) return;
    if (!motivo.trim()) { toast.error("Indica o motivo da devolução"); return; }
    setSaving(true);
    try {
      await api.patch(`/defeitos/${defeito.id}/devolver`, { motivo: motivo.trim(), data });
      toast.success("Devolução criada");
      onSaved();
    } catch (e) { toast.error((e as Error).message); }
    finally { setSaving(false); }
  };

  return (
    <Dialog open={!!defeito} onOpenChange={(v) => !v && onClose()}>
      <DialogContent>
        <DialogHeader><DialogTitle>Devolver ao fornecedor</DialogTitle></DialogHeader>
        <div className="space-y-3">
          <div className="space-y-1">
            <Label className="text-xs">Data de devolução</Label>
            <Input type="date" value={data} onChange={(e) => setData(e.target.value)} />
          </div>
          <div className="space-y-1">
            <Label className="text-xs">Motivo</Label>
            <Textarea rows={3} value={motivo} onChange={(e) => setMotivo(e.target.value)}
              placeholder="Descreve o motivo da devolução…" />
          </div>
        </div>
        <DialogFooter>
          <Button variant="outline" onClick={onClose}>Cancelar</Button>
          <Button onClick={submit} disabled={saving}>
            {saving ? "A criar…" : "Criar devolução"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
