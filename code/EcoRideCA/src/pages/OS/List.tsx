import { useEffect, useMemo, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { Plus, Eye, Trash2 } from "lucide-react";
import { toast } from "sonner";

import { PageHeader } from "@/components/layout/PageHeader";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { StateBadge } from "@/components/state-badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select, SelectContent, SelectItem, SelectTrigger, SelectValue,
} from "@/components/ui/select";
import {
  Table, TableBody, TableCell, TableHead, TableHeader, TableRow,
} from "@/components/ui/table";

import { osService } from "@/services/os";
import { clientesService, trotinetesService, funcionariosService } from "@/services/entities";
import { useAuth } from "@/context/AuthContext";
import { formatDateTime } from "@/lib/format";
import type { OS, Cliente, Trotinete, Funcionario, EstadoOS } from "@/lib/types";
import { ESTADO_OS_LABELS } from "@/lib/types";

const ESTADOS_TODOS: EstadoOS[] = [
  "REGISTADA", "EM_DIAGNOSTICO", "AGUARDA_APROVACAO", "APROVADA",
  "EM_REPARACAO", "AGUARDA_PECAS", "CONCLUIDA", "AGUARDA_PAGAMENTO",
  "PAGA", "CANCELADA",
];

// Mecânicos não vêem AGUARDA_PAGAMENTO nem PAGA
const ESTADOS_MEC: EstadoOS[] = ESTADOS_TODOS.filter(
  (e) => e !== "AGUARDA_PAGAMENTO" && e !== "PAGA",
);

export default function OSList() {
  const { user, role } = useAuth();
  const [params] = useSearchParams();
  const meu = params.get("meu") === "true";

  const [ordens, setOrdens] = useState<OS[]>([]);
  const [clientes, setClientes] = useState<Cliente[]>([]);
  const [trotinetes, setTrotinetes] = useState<Trotinete[]>([]);
  const [mecanicos, setMecanicos] = useState<Funcionario[]>([]);

  // Filtros
  const [estado, setEstado] = useState<string>("ALL");
  const [clienteFiltro, setClienteFiltro] = useState<string>("ALL");
  const [mecanicoFiltro, setMecanicoFiltro] = useState<string>("ALL");
  const [dataDesde, setDataDesde] = useState<string>("");
  const [dataAte, setDataAte] = useState<string>("");

  const reload = async () => {
    const [o, c, t, fs] = await Promise.all([
      osService.list(),
      clientesService.list(),
      trotinetesService.list(),
      funcionariosService.list(),
    ]);
    setOrdens(o);
    setClientes(c);
    setTrotinetes(t);
    setMecanicos(fs.filter((f) => f.cargo === "MECANICO"));
  };
  useEffect(() => { reload(); }, []);

  const clienteName = (id: string) => clientes.find((c) => c.id === id)?.nome ?? "—";
  const trotineteLabel = (id: string) => {
    const t = trotinetes.find((x) => x.id === id);
    return t ? `${t.marca} ${t.modelo}` : "—";
  };
  const mecanicoNome = (id?: string) =>
    id ? mecanicos.find((m) => m.id === id)?.nome ?? "—" : "—";

  const isMec = role === "MECANICO";
  const estadosDisponiveis = isMec ? ESTADOS_MEC : ESTADOS_TODOS;

  const filtered = useMemo(() => {
    let r = ordens;

    // Mecânico: esconder OS pagas/aguarda pagamento e aplicar regra "minhas/disponíveis"
    if (isMec && user) {
      r = r.filter((o) => o.estado !== "PAGA" && o.estado !== "AGUARDA_PAGAMENTO");
      if (meu) {
        r = r.filter((o) => o.mecanicoId === user.id);
      } else {
        r = r.filter(
          (o) =>
            o.mecanicoId === user.id ||
            (!o.mecanicoId && o.estado === "REGISTADA"),
        );
      }
    }

    if (estado !== "ALL") r = r.filter((o) => o.estado === estado);

    // Filtros gerente/secretária
    if (!isMec) {
      if (clienteFiltro !== "ALL") r = r.filter((o) => o.clienteId === clienteFiltro);
      if (mecanicoFiltro !== "ALL") {
        if (mecanicoFiltro === "SEM_MEC") {
          r = r.filter((o) => !o.mecanicoId);
        } else {
          r = r.filter((o) => o.mecanicoId === mecanicoFiltro);
        }
      }
      if (dataDesde) r = r.filter((o) => o.criadaEm >= dataDesde);
      if (dataAte) r = r.filter((o) => o.criadaEm <= `${dataAte}T23:59:59`);
    }

    return r;
  }, [ordens, estado, clienteFiltro, mecanicoFiltro, dataDesde, dataAte, meu, isMec, user]);

  const handleRemove = async (o: OS) => {
    await osService.remove(o.id);
    toast.success("OS eliminada");
    reload();
  };

  const limparFiltros = () => {
    setEstado("ALL");
    setClienteFiltro("ALL");
    setMecanicoFiltro("ALL");
    setDataDesde("");
    setDataAte("");
  };

  const canCreate = role === "GERENTE" || role === "SECRETARIA";
  const canDelete = role === "GERENTE" || role === "SECRETARIA";

  return (
    <div>
      <PageHeader
        title={meu ? "Minhas Ordens de Serviço" : "Ordens de Serviço"}
        description="Lista de OS — diagnóstico, reparação e pagamento"
        actions={
          canCreate ? (
            <Button asChild>
              <Link to="/os/nova"><Plus className="h-4 w-4" /> Nova OS</Link>
            </Button>
          ) : null
        }
      />

      {/* Filtros */}
      <div className="mb-4 grid gap-3 rounded-lg border bg-card p-3 shadow-sm sm:grid-cols-2 lg:grid-cols-4">
        <div className="space-y-1">
          <Label className="text-xs">Estado</Label>
          <Select value={estado} onValueChange={setEstado}>
            <SelectTrigger><SelectValue /></SelectTrigger>
            <SelectContent>
              <SelectItem value="ALL">Todos os estados</SelectItem>
              {estadosDisponiveis.map((e) => (
                <SelectItem key={e} value={e}>{ESTADO_OS_LABELS[e]}</SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>

        {!isMec && (
          <>
            <div className="space-y-1">
              <Label className="text-xs">Cliente</Label>
              <Select value={clienteFiltro} onValueChange={setClienteFiltro}>
                <SelectTrigger><SelectValue /></SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">Todos</SelectItem>
                  {clientes.map((c) => (
                    <SelectItem key={c.id} value={c.id}>{c.nome}</SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-1">
              <Label className="text-xs">Mecânico</Label>
              <Select value={mecanicoFiltro} onValueChange={setMecanicoFiltro}>
                <SelectTrigger><SelectValue /></SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">Todos</SelectItem>
                  <SelectItem value="SEM_MEC">Sem mecânico atribuído</SelectItem>
                  {mecanicos.map((m) => (
                    <SelectItem key={m.id} value={m.id}>{m.nome}</SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-1">
              <Label className="text-xs">Criada desde</Label>
              <Input type="date" value={dataDesde} onChange={(e) => setDataDesde(e.target.value)} />
            </div>
            <div className="space-y-1">
              <Label className="text-xs">Criada até</Label>
              <Input type="date" value={dataAte} onChange={(e) => setDataAte(e.target.value)} />
            </div>
            <div className="flex items-end justify-end sm:col-span-2 lg:col-span-4">
              <Button variant="outline" size="sm" onClick={limparFiltros}>Limpar filtros</Button>
            </div>
          </>
        )}

        {isMec && (
          <div className="space-y-1 sm:col-span-3">
            <Label className="text-xs">Vista</Label>
            <div className="flex gap-2">
              <Button asChild variant={!meu ? "default" : "outline"} size="sm">
                <Link to="/os">Disponíveis + Minhas</Link>
              </Button>
              <Button asChild variant={meu ? "default" : "outline"} size="sm">
                <Link to="/os?meu=true">Apenas minhas</Link>
              </Button>
            </div>
          </div>
        )}
      </div>

      <div className="rounded-lg border bg-card shadow-sm">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Nº</TableHead>
              <TableHead>Cliente</TableHead>
              <TableHead>Trotinete</TableHead>
              {!isMec && <TableHead>Mecânico</TableHead>}
              <TableHead>Estado</TableHead>
              <TableHead>Criada</TableHead>
              <TableHead className="w-[1%] text-right">Ações</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {filtered.length === 0 ? (
              <TableRow>
                <TableCell colSpan={isMec ? 6 : 7} className="h-24 text-center text-sm text-muted-foreground">
                  Sem ordens de serviço
                </TableCell>
              </TableRow>
            ) : filtered.map((o) => (
              <TableRow key={o.id}>
                <TableCell><span className="font-mono text-xs">{o.numero}</span></TableCell>
                <TableCell><span className="font-medium">{clienteName(o.clienteId)}</span></TableCell>
                <TableCell>{trotineteLabel(o.trotineteId)}</TableCell>
                {!isMec && (
                  <TableCell className="text-sm">
                    {o.mecanicoId
                      ? mecanicoNome(o.mecanicoId)
                      : <span className="text-xs text-muted-foreground">—</span>}
                  </TableCell>
                )}
                <TableCell><StateBadge state={o.estado} /></TableCell>
                <TableCell><span className="text-xs text-muted-foreground">{formatDateTime(o.criadaEm)}</span></TableCell>
                <TableCell className="text-right">
                  <div className="flex justify-end gap-1">
                    <Button asChild variant="ghost" size="icon">
                      <Link to={`/os/${o.id}`}><Eye className="h-4 w-4" /></Link>
                    </Button>
                    {canDelete && o.estado !== "PAGA" && (
                      <ConfirmDialog
                        trigger={<Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
                        title="Eliminar OS?"
                        description={`A OS ${o.numero} será removida.`}
                        destructive
                        onConfirm={() => handleRemove(o)}
                      />
                    )}
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
      <div className="mt-2 text-xs text-muted-foreground">
        {filtered.length} ordens de serviço
      </div>
    </div>
  );
}
