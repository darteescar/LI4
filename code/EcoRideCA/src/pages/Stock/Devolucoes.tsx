import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { Trash2, CheckCircle2, Send, XCircle } from "lucide-react";
import { toast } from "sonner";

import { PageHeader } from "@/components/layout/PageHeader";
import { StockTabs } from "@/components/layout/StockTabs";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Table, TableBody, TableCell, TableHead, TableHeader, TableRow,
} from "@/components/ui/table";

import { api } from "@/services/api";
import { useAuth } from "@/context/AuthContext";

type EstadoDevolucao = "StockPendenteDeDevolucao" | "Enviada" | "Devolvida" | "Invalida";

interface Devolucao {
  id: number; data: string; motivo: string; estado: EstadoDevolucao; codStock: number;
}

interface StockEntry { id: number; codPeca: number; nr_serie?: string; }
interface Peca { id: number; referencia: string; nome: string; }

const ESTADO_LABELS: Record<EstadoDevolucao, string> = {
  StockPendenteDeDevolucao: "Pendente",
  Enviada: "Enviada",
  Devolvida: "Devolvida",
  Invalida: "Inválida",
};

const ESTADO_VARIANT: Record<EstadoDevolucao, "secondary" | "outline" | "destructive"> = {
  StockPendenteDeDevolucao: "outline",
  Enviada: "secondary",
  Devolvida: "secondary",
  Invalida: "destructive",
};

export default function StockDevolucoes() {
  const { role } = useAuth();
  const qc = useQueryClient();

  const canEdit = role === "GERENTE" || role === "GESTOR_STOCK";

  const { data: devolucoes = [], isLoading } = useQuery<Devolucao[]>({
    queryKey: ["devolucoes"],
    queryFn: () => api.get<Devolucao[]>("/devolucoes"),
  });

  const { data: stocks = [] } = useQuery<StockEntry[]>({
    queryKey: ["stocks"],
    queryFn: () => api.get<StockEntry[]>("/stocks"),
  });

  const { data: pecas = [] } = useQuery<Peca[]>({
    queryKey: ["pecas"],
    queryFn: () => api.get<Peca[]>("/pecas"),
  });

  const invalidate = () => qc.invalidateQueries({ queryKey: ["devolucoes"] });

  const enviadaMutation = useMutation({
    mutationFn: (id: number) => api.patch(`/devolucoes/${id}/enviada`, {}),
    onSuccess: () => { toast.success("Marcada como enviada"); invalidate(); },
    onError: (e: Error) => toast.error(e.message),
  });

  const devolvidaMutation = useMutation({
    mutationFn: (id: number) => api.patch(`/devolucoes/${id}/devolvida`, {}),
    onSuccess: () => { toast.success("Marcada como devolvida"); invalidate(); },
    onError: (e: Error) => toast.error(e.message),
  });

  const invalidaMutation = useMutation({
    mutationFn: (id: number) => api.patch(`/devolucoes/${id}/invalida`, {}),
    onSuccess: () => { toast.success("Marcada como inválida"); invalidate(); },
    onError: (e: Error) => toast.error(e.message),
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => api.delete(`/devolucoes/${id}`),
    onSuccess: () => { toast.success("Devolução removida"); invalidate(); },
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
              <TableHead>Data</TableHead>
              <TableHead>Motivo</TableHead>
              <TableHead>Estado</TableHead>
              {canEdit && <TableHead className="w-[1%] text-right">Ações</TableHead>}
            </TableRow>
          </TableHeader>
          <TableBody>
            {isLoading ? (
              <TableRow><TableCell colSpan={canEdit ? 6 : 5} className="h-24 text-center text-sm text-muted-foreground">A carregar…</TableCell></TableRow>
            ) : devolucoes.length === 0 ? (
              <TableRow><TableCell colSpan={canEdit ? 6 : 5} className="h-24 text-center text-sm text-muted-foreground">Sem devoluções registadas</TableCell></TableRow>
            ) : devolucoes.map((d) => (
              <TableRow key={d.id}>
                <TableCell className="font-mono text-xs">DEV-{d.id}</TableCell>
                <TableCell className="font-medium">{stockLabel(d.codStock)}</TableCell>
                <TableCell className="text-xs text-muted-foreground">{d.data}</TableCell>
                <TableCell className="max-w-[200px] truncate text-sm" title={d.motivo}>{d.motivo}</TableCell>
                <TableCell>
                  <Badge variant={ESTADO_VARIANT[d.estado]} className="text-xs">
                    {ESTADO_LABELS[d.estado] ?? d.estado}
                  </Badge>
                </TableCell>
                {canEdit && (
                  <TableCell className="text-right">
                    <div className="flex justify-end gap-1">
                      {d.estado === "StockPendenteDeDevolucao" && (
                        <ConfirmDialog
                          trigger={<Button variant="ghost" size="icon" title="Marcar como enviada"><Send className="h-4 w-4" /></Button>}
                          title="Marcar devolução como enviada?"
                          onConfirm={() => enviadaMutation.mutate(d.id)}
                        />
                      )}
                      {d.estado === "Enviada" && (
                        <ConfirmDialog
                          trigger={<Button variant="ghost" size="icon" title="Marcar como devolvida"><CheckCircle2 className="h-4 w-4 text-success" /></Button>}
                          title="Marcar devolução como devolvida?"
                          onConfirm={() => devolvidaMutation.mutate(d.id)}
                        />
                      )}
                      {(d.estado === "StockPendenteDeDevolucao" || d.estado === "Enviada") && (
                        <ConfirmDialog
                          trigger={<Button variant="ghost" size="icon" title="Marcar como inválida"><XCircle className="h-4 w-4 text-warning" /></Button>}
                          title="Marcar devolução como inválida?"
                          onConfirm={() => invalidaMutation.mutate(d.id)}
                        />
                      )}
                      <ConfirmDialog
                        trigger={<Button variant="ghost" size="icon" title="Remover"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
                        title="Remover esta devolução?"
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
      <div className="mt-2 text-xs text-muted-foreground">{devolucoes.length} devoluções</div>
    </div>
  );
}
