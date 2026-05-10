import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { PackageX, Send, Trash2, ExternalLink } from "lucide-react";
import { toast } from "sonner";

import { PageHeader } from "@/components/layout/PageHeader";
import { StockTabs } from "@/components/layout/StockTabs";
import { DataTable, type Column } from "@/components/data-table";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle,
} from "@/components/ui/dialog";
import { Label } from "@/components/ui/label";
import {
  Select, SelectContent, SelectItem, SelectTrigger, SelectValue,
} from "@/components/ui/select";

import { pecasDefeituosasService } from "@/services/stock";
import { pecasService, fornecedoresService, funcionariosService } from "@/services/entities";
import { osService } from "@/services/os";
import { useAuth } from "@/context/AuthContext";
import { formatDateTime } from "@/lib/format";
import type { PecaDefeituosa, Peca, Fornecedor, Funcionario, OS } from "@/lib/types";

export default function StockDefeitos() {
  const { role } = useAuth();
  const [defeitos, setDefeitos] = useState<PecaDefeituosa[]>([]);
  const [pecas, setPecas] = useState<Peca[]>([]);
  const [fornecedores, setFornecedores] = useState<Fornecedor[]>([]);
  const [funcionarios, setFuncionarios] = useState<Funcionario[]>([]);
  const [ordens, setOrdens] = useState<OS[]>([]);
  const [openDev, setOpenDev] = useState<PecaDefeituosa | null>(null);
  const [fornecedorEscolhido, setFornecedorEscolhido] = useState("");

  const reload = async () => {
    const [d, p, f, fn, o] = await Promise.all([
      pecasDefeituosasService.list(),
      pecasService.list(),
      fornecedoresService.list(),
      funcionariosService.list(),
      osService.list(),
    ]);
    setDefeitos(d); setPecas(p); setFornecedores(f); setFuncionarios(fn); setOrdens(o);
  };
  useEffect(() => { reload(); }, []);

  const canEdit = role === "GERENTE" || role === "GESTOR_STOCK";

  const pecaById = (id: string) => pecas.find((x) => x.id === id);
  const funcionarioNome = (id: string) => funcionarios.find((x) => x.id === id)?.nome ?? "—";
  const osNumero = (id: string) => ordens.find((x) => x.id === id)?.numero ?? "—";

  const abrirDevolucao = (d: PecaDefeituosa) => {
    const peca = pecaById(d.pecaId);
    setFornecedorEscolhido(peca?.fornecedorId ?? "");
    setOpenDev(d);
  };

  const submeterDevolucao = async () => {
    if (!openDev) return;
    if (!fornecedorEscolhido) { toast.error("Escolhe o fornecedor"); return; }
    try {
      await pecasDefeituosasService.submeterDevolucao(openDev.id, fornecedorEscolhido);
      toast.success("Devolução criada — disponível em 'Devoluções'");
      setOpenDev(null);
      reload();
    } catch (e) {
      toast.error((e as Error).message);
    }
  };

  const remover = async (d: PecaDefeituosa) => {
    await pecasDefeituosasService.remove(d.id);
    toast.success("Registo removido");
    reload();
  };

  const columns: Column<PecaDefeituosa>[] = [
    { key: "data", header: "Reportado em", cell: (d) => <span className="text-xs">{formatDateTime(d.data)}</span> },
    {
      key: "peca", header: "Peça",
      cell: (d) => {
        const p = pecaById(d.pecaId);
        return (
          <div>
            <div className="font-medium">{p ? `${p.referencia} — ${p.nome}` : d.pecaId}</div>
            {d.numeroSerie && (
              <div className="text-[10px] font-mono text-muted-foreground">S/N {d.numeroSerie}</div>
            )}
          </div>
        );
      },
    },
    { key: "qtd", header: "Qtd", cell: (d) => d.quantidade },
    { key: "motivo", header: "Motivo", cell: (d) => <span className="text-xs">{d.motivo}</span> },
    {
      key: "os", header: "OS",
      cell: (d) => (
        <Link to={`/os/${d.osId}`} className="inline-flex items-center gap-1 text-xs text-primary hover:underline">
          {osNumero(d.osId)} <ExternalLink className="h-3 w-3" />
        </Link>
      ),
    },
    { key: "rep", header: "Mecânico", cell: (d) => <span className="text-xs">{funcionarioNome(d.reportadaPor)}</span> },
    {
      key: "estado", header: "Estado",
      cell: (d) => d.estado === "POR_TRATAR"
        ? <Badge variant="destructive">Por tratar</Badge>
        : <Badge variant="secondary">Enviada para devolução</Badge>,
    },
  ];

  return (
    <div>
      <PageHeader
        title="Stock"
        description="Peças reportadas como defeituosas pelos mecânicos durante reparações"
      />
      <StockTabs />

      <DataTable
        data={defeitos}
        columns={columns}
        emptyMessage="Sem peças defeituosas reportadas"
        searchPlaceholder="Pesquisar…"
        rowActions={(d) => (
          <>
            {canEdit && d.estado === "POR_TRATAR" && (
              <Button variant="ghost" size="sm" onClick={() => abrirDevolucao(d)}>
                <Send className="h-4 w-4" /> Devolver
              </Button>
            )}
            {canEdit && (
              <ConfirmDialog
                trigger={<Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
                title="Remover registo?"
                description="O registo de defeito será apagado. O stock já não é alterado."
                destructive
                onConfirm={() => remover(d)}
              />
            )}
          </>
        )}
      />

      <Dialog open={!!openDev} onOpenChange={(v) => !v && setOpenDev(null)}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle className="flex items-center gap-2">
              <PackageX className="h-4 w-4" /> Submeter para devolução
            </DialogTitle>
          </DialogHeader>
          {openDev && (
            <div className="space-y-3 text-sm">
              <p>
                <strong>{pecaById(openDev.pecaId)?.nome ?? openDev.pecaId}</strong>
                {" · "}{openDev.quantidade}× unidade(s)
                {openDev.numeroSerie && <> · S/N <code>{openDev.numeroSerie}</code></>}
              </p>
              <div className="space-y-1">
                <Label className="text-xs">Fornecedor</Label>
                <Select value={fornecedorEscolhido} onValueChange={setFornecedorEscolhido}>
                  <SelectTrigger><SelectValue placeholder="Escolher fornecedor…" /></SelectTrigger>
                  <SelectContent>
                    {fornecedores.map((f) => (
                      <SelectItem key={f.id} value={f.id}>{f.nome}</SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <p className="text-xs text-muted-foreground">
                A devolução será criada no estado <em>Pendente</em>. O stock não é alterado de novo (já saiu quando o defeito foi reportado).
              </p>
            </div>
          )}
          <DialogFooter>
            <Button variant="outline" onClick={() => setOpenDev(null)}>Cancelar</Button>
            <Button onClick={submeterDevolucao}>Criar devolução</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}
