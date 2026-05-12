import { useMemo, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
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

import { api } from "@/services/api";
import { useAuth } from "@/context/AuthContext";
import { formatDateTime } from "@/lib/format";

type EstadoOS =
  | "PendenteDiagnostico"
  | "PendenteAprovacaoOrcamento"
  | "OrcamentoNaoAprovado"
  | "PendenteReparacao"
  | "AguardarPecas"
  | "ClienteNotificado"
  | "PendentePagamento"
  | "Paga"
  | "Eliminada";

const ESTADO_LABELS: Record<EstadoOS, string> = {
  PendenteDiagnostico:       "Pendente diagnóstico",
  PendenteAprovacaoOrcamento: "Aguarda aprovação",
  OrcamentoNaoAprovado:      "Orçamento rejeitado",
  PendenteReparacao:         "Em reparação",
  AguardarPecas:             "Aguarda peças",
  ClienteNotificado:         "Cliente notificado",
  PendentePagamento:         "Aguarda pagamento",
  Paga:                      "Paga",
  Eliminada:                 "Eliminada",
};

const TODOS_ESTADOS = Object.keys(ESTADO_LABELS) as EstadoOS[];
const ESTADOS_MEC = TODOS_ESTADOS.filter((e) => e !== "PendentePagamento" && e !== "Paga");
const TERMINAL = ["Paga", "Eliminada"] as EstadoOS[];

interface OrdemServico {
  id: number;
  descricao: string;
  dataCriacao: string;
  codTrotinete: number;
  codCliente: number;
  codCriador: number;
  codMecanico: number | null;
  estado: EstadoOS;
  acessorios: string[];
}

interface Cliente { id: number; nome: string; }
interface Trotinete { id: number; modelo: string; marca: string; }
interface Funcionario { id: number; nome: string; }

export default function OSList() {
  const { user, role } = useAuth();
  const qc = useQueryClient();
  const [params] = useSearchParams();
  const meu = params.get("meu") === "true";

  const [estado, setEstado] = useState("ALL");
  const [clienteFiltro, setClienteFiltro] = useState("ALL");
  const [mecanicoFiltro, setMecanicoFiltro] = useState("ALL");
  const [dataDesde, setDataDesde] = useState("");
  const [dataAte, setDataAte] = useState("");

  const { data: ordens = [], isLoading } = useQuery<OrdemServico[]>({
    queryKey: ["ordensservicos"],
    queryFn: () => api.get<OrdemServico[]>("/ordensservicos"),
  });

  const { data: clientes = [] } = useQuery<Cliente[]>({
    queryKey: ["clientes"],
    queryFn: () => api.get<Cliente[]>("/clientes"),
    enabled: role === "GERENTE" || role === "SECRETARIA",
  });

  const { data: trotinetes = [] } = useQuery<Trotinete[]>({
    queryKey: ["trotinetes"],
    queryFn: () => api.get<Trotinete[]>("/trotinetes"),
  });

  const { data: funcionarios = [] } = useQuery<Funcionario[]>({
    queryKey: ["funcionarios"],
    queryFn: () => api.get<Funcionario[]>("/funcionarios"),
    enabled: role === "GERENTE",
  });

  const mecanicos = funcionarios;

  const clienteNome = (id: number) => clientes.find((c) => c.id === id)?.nome ?? "—";
  const trotineteLabel = (id: number) => {
    const t = trotinetes.find((x) => x.id === id);
    return t ? `${t.marca} ${t.modelo}` : "—";
  };
  const mecanicoNome = (id: number | null) =>
    id ? (mecanicos.find((m) => m.id === id)?.nome ?? "—") : "—";

  const isMec = role === "MECANICO";
  const estadosDisponiveis = isMec ? ESTADOS_MEC : TODOS_ESTADOS;

  const filtered = useMemo(() => {
    let r = ordens;

    if (isMec && user) {
      r = r.filter((o) => o.estado !== "Paga" && o.estado !== "PendentePagamento");
      if (meu) {
        r = r.filter((o) => o.codMecanico === user.id);
      } else {
        r = r.filter((o) =>
          o.codMecanico === user.id || (!o.codMecanico && o.estado === "PendenteDiagnostico")
        );
      }
    }

    if (estado !== "ALL") r = r.filter((o) => o.estado === estado);

    if (!isMec) {
      if (clienteFiltro !== "ALL") r = r.filter((o) => o.codCliente === Number(clienteFiltro));
      if (mecanicoFiltro !== "ALL") {
        if (mecanicoFiltro === "SEM_MEC") r = r.filter((o) => !o.codMecanico);
        else r = r.filter((o) => o.codMecanico === Number(mecanicoFiltro));
      }
      if (dataDesde) r = r.filter((o) => o.dataCriacao >= dataDesde);
      if (dataAte) r = r.filter((o) => o.dataCriacao <= `${dataAte}T23:59:59`);
    }

    return r;
  }, [ordens, estado, clienteFiltro, mecanicoFiltro, dataDesde, dataAte, meu, isMec, user]);

  const deleteMutation = useMutation({
    mutationFn: (id: number) => api.delete(`/ordensservicos/${id}`),
    onSuccess: () => {
      toast.success("OS eliminada");
      qc.invalidateQueries({ queryKey: ["ordensservicos"] });
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const canCreate = role === "GERENTE" || role === "SECRETARIA";
  const canDelete = role === "GERENTE" || role === "SECRETARIA";

  return (
    <div>
      <PageHeader
        title={meu ? "Minhas Ordens de Serviço" : "Ordens de Serviço"}
        description="Lista de Ordens de Serviço"
        actions={
          canCreate ? (
            <Button asChild>
              <Link to="/os/nova"><Plus className="h-4 w-4" /> Nova OS</Link>
            </Button>
          ) : null
        }
      />

      <div className="mb-4 grid gap-3 rounded-lg border bg-card p-3 shadow-sm sm:grid-cols-2 lg:grid-cols-4">
        <div className="space-y-1">
          <Label className="text-xs">Estado</Label>
          <Select value={estado} onValueChange={setEstado}>
            <SelectTrigger><SelectValue /></SelectTrigger>
            <SelectContent>
              <SelectItem value="ALL">Todos os estados</SelectItem>
              {estadosDisponiveis.map((e) => (
                <SelectItem key={e} value={e}>{ESTADO_LABELS[e]}</SelectItem>
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
                  {clientes.map((c) => <SelectItem key={c.id} value={String(c.id)}>{c.nome}</SelectItem>)}
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
                  {mecanicos.map((m) => <SelectItem key={m.id} value={String(m.id)}>{m.nome}</SelectItem>)}
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
              <Button variant="outline" size="sm" onClick={() => { setEstado("ALL"); setClienteFiltro("ALL"); setMecanicoFiltro("ALL"); setDataDesde(""); setDataAte(""); }}>
                Limpar filtros
              </Button>
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
            {isLoading ? (
              <TableRow>
                <TableCell colSpan={isMec ? 6 : 7} className="h-24 text-center text-sm text-muted-foreground">
                  A carregar…
                </TableCell>
              </TableRow>
            ) : filtered.length === 0 ? (
              <TableRow>
                <TableCell colSpan={isMec ? 6 : 7} className="h-24 text-center text-sm text-muted-foreground">
                  Sem ordens de serviço
                </TableCell>
              </TableRow>
            ) : filtered.map((o) => (
              <TableRow key={o.id}>
                <TableCell><span className="font-mono text-xs">OS-{o.id}</span></TableCell>
                <TableCell><span className="font-medium">{clienteNome(o.codCliente)}</span></TableCell>
                <TableCell>{trotineteLabel(o.codTrotinete)}</TableCell>
                {!isMec && (
                  <TableCell className="text-sm">
                    {o.codMecanico ? mecanicoNome(o.codMecanico) : <span className="text-xs text-muted-foreground">—</span>}
                  </TableCell>
                )}
                <TableCell><StateBadge state={o.estado} /></TableCell>
                <TableCell><span className="text-xs text-muted-foreground">{formatDateTime(o.dataCriacao)}</span></TableCell>
                <TableCell className="text-right">
                  <div className="flex justify-end gap-1">
                    <Button asChild variant="ghost" size="icon">
                      <Link to={`/os/${o.id}`}><Eye className="h-4 w-4" /></Link>
                    </Button>
                    {canDelete && !TERMINAL.includes(o.estado) && (
                      <ConfirmDialog
                        trigger={<Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
                        title="Eliminar OS?"
                        description={`A OS OS-${o.id} será removida.`}
                        destructive
                        onConfirm={() => deleteMutation.mutate(o.id)}
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
