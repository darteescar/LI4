import { useEffect, useState } from "react";
import { Plus, Trash2 } from "lucide-react";
import { toast } from "sonner";

import { PageHeader } from "@/components/layout/PageHeader";
import { StockTabs } from "@/components/layout/StockTabs";
import { DataTable, type Column } from "@/components/data-table";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { StateBadge } from "@/components/state-badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import {
  Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle,
} from "@/components/ui/dialog";
import {
  Select, SelectContent, SelectItem, SelectTrigger, SelectValue,
} from "@/components/ui/select";

import { devolucoesService } from "@/services/stock";
import { pecasService, fornecedoresService } from "@/services/entities";
import { useAuth } from "@/context/AuthContext";
import { formatDateTime } from "@/lib/format";
import type { Devolucao, Peca, Fornecedor, EstadoDevolucao } from "@/lib/types";

export default function StockDevolucoes() {
  const { role } = useAuth();
  const [devolucoes, setDevolucoes] = useState<Devolucao[]>([]);
  const [pecas, setPecas] = useState<Peca[]>([]);
  const [fornecedores, setFornecedores] = useState<Fornecedor[]>([]);
  const [open, setOpen] = useState(false);

  const reload = async () => {
    const [d, p, f] = await Promise.all([
      devolucoesService.list(), pecasService.list(), fornecedoresService.list(),
    ]);
    setDevolucoes(d); setPecas(p); setFornecedores(f);
  };
  useEffect(() => { reload(); }, []);

  const pecaNome = (id: string) => pecas.find((x) => x.id === id)?.nome ?? "—";
  const fornecedorNome = (id: string) => fornecedores.find((f) => f.id === id)?.nome ?? "—";

  const canEdit = role === "GERENTE" || role === "GESTOR_STOCK";

  const handleEstado = async (d: Devolucao, estado: EstadoDevolucao) => {
    try {
      await devolucoesService.setEstado(d.id, estado);
      toast.success(`Devolução marcada como ${estado.toLowerCase()}`);
      reload();
    } catch (e) {
      toast.error((e as Error).message);
    }
  };

  const handleRemove = async (d: Devolucao) => {
    await devolucoesService.remove(d.id);
    toast.success("Devolução eliminada");
    reload();
  };

  const columns: Column<Devolucao>[] = [
    { key: "data", header: "Data", cell: (d) => <span className="text-xs">{formatDateTime(d.data)}</span> },
    { key: "peca", header: "Peça", cell: (d) => <span className="font-medium">{pecaNome(d.pecaId)}</span> },
    { key: "qtd", header: "Qtd", cell: (d) => d.quantidade },
    { key: "fornecedor", header: "Fornecedor", cell: (d) => fornecedorNome(d.fornecedorId) },
    { key: "motivo", header: "Motivo", cell: (d) => <span className="text-xs">{d.motivo}</span> },
    { key: "estado", header: "Estado", cell: (d) => <StateBadge state={d.estado} /> },
  ];

  return (
    <div>
      <PageHeader
        title="Stock"
        description="Catálogo de peças, entradas, devoluções e encomendas"
        actions={
          canEdit && (
            <Button onClick={() => setOpen(true)}><Plus className="h-4 w-4" /> Nova devolução</Button>
          )
        }
      />
      <StockTabs />

      <DataTable
        data={devolucoes}
        columns={columns}
        searchKeys={["motivo"]}
        searchPlaceholder="Pesquisar por motivo…"
        emptyMessage="Sem devoluções"
        rowActions={(d) => (
          canEdit ? (
            <>
              {d.estado === "PENDENTE" && (
                <>
                  <Button variant="outline" size="sm" onClick={() => handleEstado(d, "DEVOLVIDA")}>
                    Marcar devolvida
                  </Button>
                  <Button variant="outline" size="sm" onClick={() => handleEstado(d, "INVALIDA")}>
                    Inválida
                  </Button>
                </>
              )}
              <ConfirmDialog
                trigger={<Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
                title="Eliminar devolução?"
                destructive
                onConfirm={() => handleRemove(d)}
              />
            </>
          ) : null
        )}
      />

      <DevolucaoDialog
        open={open}
        onOpenChange={setOpen}
        pecas={pecas}
        fornecedores={fornecedores}
        onSaved={() => { setOpen(false); reload(); }}
      />
    </div>
  );
}

function DevolucaoDialog({
  open, onOpenChange, pecas, fornecedores, onSaved,
}: {
  open: boolean;
  onOpenChange: (v: boolean) => void;
  pecas: Peca[];
  fornecedores: Fornecedor[];
  onSaved: () => void;
}) {
  const [pecaId, setPecaId] = useState("");
  const [fornecedorId, setFornecedorId] = useState("");
  const [quantidade, setQuantidade] = useState(1);
  const [motivo, setMotivo] = useState("");

  useEffect(() => {
    if (open) { setPecaId(""); setFornecedorId(""); setQuantidade(1); setMotivo(""); }
  }, [open]);

  // Pré-preencher fornecedor a partir da peça
  useEffect(() => {
    const p = pecas.find((x) => x.id === pecaId);
    if (p) setFornecedorId(p.fornecedorId);
  }, [pecaId, pecas]);

  const submit = async () => {
    if (!pecaId || !fornecedorId) { toast.error("Peça e fornecedor obrigatórios"); return; }
    if (motivo.trim().length < 3) { toast.error("Indica o motivo"); return; }
    try {
      await devolucoesService.create({ pecaId, fornecedorId, quantidade, motivo: motivo.trim() });
      toast.success("Devolução registada");
      onSaved();
    } catch (e) {
      toast.error((e as Error).message);
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader><DialogTitle>Nova devolução</DialogTitle></DialogHeader>
        <div className="grid gap-3">
          <div className="space-y-1">
            <Label className="text-xs">Peça</Label>
            <Select value={pecaId} onValueChange={setPecaId}>
              <SelectTrigger><SelectValue placeholder="Escolher peça…" /></SelectTrigger>
              <SelectContent>
                {pecas.map((p) => (
                  <SelectItem key={p.id} value={p.id}>{p.referencia} — {p.nome}</SelectItem>
                ))}
              </SelectContent>
            </Select>
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
            <Label className="text-xs">Quantidade</Label>
            <Input type="number" min={1} value={quantidade} onChange={(e) => setQuantidade(Number(e.target.value))} />
          </div>
          <div className="space-y-1">
            <Label className="text-xs">Motivo</Label>
            <Textarea rows={3} value={motivo} onChange={(e) => setMotivo(e.target.value)} placeholder="Defeito, peça errada, …" />
          </div>
        </div>
        <DialogFooter>
          <Button variant="outline" onClick={() => onOpenChange(false)}>Cancelar</Button>
          <Button onClick={submit}>Registar</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
