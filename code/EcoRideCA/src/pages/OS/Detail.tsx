import { useEffect, useMemo, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { ArrowLeft, Plus, Trash2, CheckCircle2, ShieldCheck, AlertTriangle } from "lucide-react";
import { toast } from "sonner";

import { PageHeader } from "@/components/layout/PageHeader";
import { StateBadge } from "@/components/state-badge";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { Checkbox } from "@/components/ui/checkbox";
import { Badge } from "@/components/ui/badge";
import {
  Select, SelectContent, SelectItem, SelectTrigger, SelectValue,
} from "@/components/ui/select";
import {
  Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter,
} from "@/components/ui/dialog";
import {
  Table, TableBody, TableCell, TableHead, TableHeader, TableRow,
} from "@/components/ui/table";

import { api } from "@/services/api";
import { useAuth } from "@/context/AuthContext";
import { formatDateTime, formatEUR } from "@/lib/format";

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

type MetodoPagamento = "NUMERARIO" | "MULTIBANCO" | "MBWAY";

interface CheckList {
  luzes: boolean; pneus: boolean; aceleracao: boolean;
  travagem: boolean; visor: boolean; teste_pratico: boolean;
}

const CHECK_INIT: CheckList = {
  luzes: false, pneus: false, aceleracao: false,
  travagem: false, visor: false, teste_pratico: false,
};

const CHECK_LABELS: Record<keyof CheckList, string> = {
  luzes: "Luzes", pneus: "Pneus", aceleracao: "Aceleração",
  travagem: "Travagem", visor: "Visor / display", teste_pratico: "Teste prático",
};

interface Diagnostico {
  descricao: string;
  orcamento: number;
  pecasOrcamento: Record<string, number>;
  cod_reparacoes: number[];
}

interface Conserto {
  preco_total: number;
  cod_reparacoes: number[];
  checkList: CheckList;
}

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
  conserto: Conserto | null;
  diagnostico: Diagnostico | null;
  metodo_pagamento: MetodoPagamento | null;
}

interface Reparacao { id: number; nomenclatura: string; descricao: string; preco: number; disponivel: boolean; }
interface Peca { id: number; referencia: string; nome: string; preco_venda: number; codFornecedor: number; ativa: boolean; }
interface Funcionario { id: number; nome: string; }
interface Cliente { id: number; nome: string; nif: string; telemovel: string; email: string; }
interface Trotinete { id: number; marca: string; modelo: string; num_serie: string; tipo_motor: string; cod_cliente: number; }


export default function OSDetail() {
  const { id } = useParams<{ id: string }>();
  const { user, role } = useAuth();
  const qc = useQueryClient();
  const navigate = useNavigate();

  const isMec = role === "MECANICO";
  const isGerente = role === "GERENTE";
  const canSecOps = isGerente || role === "SECRETARIA";

  const { data: os, isLoading } = useQuery<OrdemServico>({
    queryKey: ["ordensservicos", id],
    queryFn: () => api.get<OrdemServico>(`/ordensservicos/${id}`),
    enabled: !!id,
  });

  const { data: cliente } = useQuery<Cliente>({
    queryKey: ["clientes", os?.codCliente],
    queryFn: () => api.get<Cliente>(`/clientes/${os!.codCliente}`),
    enabled: !!os && !isMec,
  });

  const { data: trotinetes = [] } = useQuery<Trotinete[]>({
    queryKey: ["trotinetes"],
    queryFn: () => api.get<Trotinete[]>("/trotinetes"),
    enabled: !isMec,
  });

  const { data: funcionarios = [] } = useQuery<Funcionario[]>({
    queryKey: ["funcionarios"],
    queryFn: () => api.get<Funcionario[]>("/funcionarios"),
    enabled: isGerente,
  });

  const { data: mecIdUtilizador } = useQuery<{ idUtilizador: number }>({
    queryKey: ["utilizadores", "por-funcionario", os?.codMecanico],
    queryFn: () => api.get<{ idUtilizador: number }>(`/utilizadores/por-funcionario/${os!.codMecanico}`),
    enabled: !!os?.codMecanico && isMec,
  });

  const { data: reparacoes = [] } = useQuery<Reparacao[]>({
    queryKey: ["reparacoes"],
    queryFn: () => api.get<Reparacao[]>("/reparacoes"),
  });

  const { data: pecas = [] } = useQuery<Peca[]>({
    queryKey: ["pecas"],
    queryFn: () => api.get<Peca[]>("/pecas"),
  });

  const trotinete = useMemo(
    () => trotinetes.find((t) => t.id === os?.codTrotinete) ?? null,
    [trotinetes, os],
  );

  const reload = () => {
    qc.invalidateQueries({ queryKey: ["ordensservicos", id] });
  };

  const pegarMutation = useMutation({
    mutationFn: () => api.patch(`/ordensservicos/${id}/atribuir`, { id_funcionario: user!.id }),
    onSuccess: () => { toast.success("OS atribuída — começa o diagnóstico"); reload(); },
    onError: (e: Error) => toast.error(e.message),
  });

  const cancelarMutation = useMutation({
    mutationFn: () => api.patch(`/ordensservicos/${id}/cancelar`, {}),
    onSuccess: () => { toast.success("OS eliminada"); reload(); },
    onError: (e: Error) => toast.error(e.message),
  });

  if (isLoading || !os) {
    return <div className="p-8 text-sm text-muted-foreground">A carregar OS…</div>;
  }

  const isMecAtribuido = isMec && !!mecIdUtilizador && user?.idUtilizador === mecIdUtilizador.idUtilizador;
  const canMecOps = isMecAtribuido;
  const canPegar = isMec && !os.codMecanico && os.estado === "PendenteDiagnostico";
  const bloqueadoOutroMec = isMec && !!os.codMecanico && !isMecAtribuido;

  const mecNome = funcionarios.find((f) => f.id === os.codMecanico)?.nome ?? (os.codMecanico ? `#${os.codMecanico}` : null);
  const criadorNome = funcionarios.find((f) => f.id === os.codCriador)?.nome ?? `#${os.codCriador}`;

  return (
    <div>
      <PageHeader
        title={`OS-${os.id}`}
        description={`Criada em ${formatDateTime(os.dataCriacao)}`}
        actions={
          <div className="flex flex-wrap items-center gap-2">
            <StateBadge state={os.estado} />
            <Button variant="outline" asChild>
              <Link to="/os"><ArrowLeft className="h-4 w-4" /> Voltar</Link>
            </Button>
            {canSecOps && os.estado !== "Paga" && os.estado !== "Eliminada" && (
              <ConfirmDialog
                trigger={<Button variant="outline" className="text-destructive">Eliminar OS</Button>}
                title="Eliminar esta OS?"
                description="A OS deixa de poder ser editada."
                destructive
                onConfirm={() => cancelarMutation.mutate()}
              />
            )}
          </div>
        }
      />

      <Tabs defaultValue="resumo" className="space-y-4">
        <TabsList>
          <TabsTrigger value="resumo">Resumo</TabsTrigger>
          <TabsTrigger value="diagnostico">Diagnóstico</TabsTrigger>
          <TabsTrigger value="conserto">Conserto</TabsTrigger>
          <TabsTrigger value="pagamento">Pagamento</TabsTrigger>
        </TabsList>

        <TabsContent value="resumo">
          <div className="grid gap-4 md:grid-cols-2">
            {!isMec && (
              <Card>
                <CardHeader><CardTitle className="text-base">Cliente & Trotinete</CardTitle></CardHeader>
                <CardContent className="space-y-2 text-sm">
                  <Row label="Cliente" value={cliente?.nome ?? "—"} />
                  <Row label="NIF" value={cliente?.nif ?? "—"} />
                  <Row label="Telemóvel" value={cliente?.telemovel ?? "—"} />
                  <Row label="Trotinete" value={trotinete ? `${trotinete.marca} ${trotinete.modelo}` : "—"} />
                  <Row label="Nº de série" value={trotinete?.num_serie ?? "—"} />
                  <Row label="Motor" value={trotinete?.tipo_motor ?? "—"} />
                </CardContent>
              </Card>
            )}
            <Card>
              <CardHeader><CardTitle className="text-base">Detalhes da OS</CardTitle></CardHeader>
              <CardContent className="space-y-3 text-sm">
                {isGerente && (
                  <div>
                    <div className="text-xs font-medium text-muted-foreground">Registada por</div>
                    <p className="mt-1">{criadorNome}</p>
                  </div>
                )}
                <div>
                  <div className="text-xs font-medium text-muted-foreground">Problema reportado</div>
                  <p className="mt-1 whitespace-pre-wrap">{os.descricao}</p>
                </div>
                <div>
                  <div className="text-xs font-medium text-muted-foreground">Acessórios</div>
                  <div className="mt-1 flex flex-wrap gap-1">
                    {os.acessorios.length === 0
                      ? <span className="text-muted-foreground">Nenhum</span>
                      : os.acessorios.map((a, i) => <Badge key={i} variant="secondary">{a}</Badge>)}
                  </div>
                </div>
                <div>
                  <div className="text-xs font-medium text-muted-foreground">Mecânico responsável</div>
                  <div className="mt-1">
                    {os.codMecanico ? (
                      <div className="flex items-center gap-2">
                        <span className="font-medium">{mecNome}</span>
                        {bloqueadoOutroMec && (
                          <Badge variant="outline" className="text-xs">Atribuída a outro mecânico</Badge>
                        )}
                      </div>
                    ) : canPegar ? (
                      <div className="space-y-2">
                        <p className="text-xs text-muted-foreground">
                          Esta OS está disponível.
                        </p>
                        <Button onClick={() => pegarMutation.mutate()} disabled={pegarMutation.isPending}>
                          <ShieldCheck className="h-4 w-4" /> Pegar nesta OS
                        </Button>
                      </div>
                    ) : isGerente && os.estado === "PendenteDiagnostico" && funcionarios.length > 0 ? (
                      <AtribuirDialog
                        funcionarios={funcionarios}
                        onAtribuir={(funcId) =>
                          api.patch(`/ordensservicos/${id}/atribuir`, { id_funcionario: funcId })
                            .then(() => { toast.success("Mecânico atribuído"); reload(); })
                            .catch((e: Error) => toast.error(e.message))
                        }
                      />
                    ) : (
                      <span className="text-muted-foreground">
                        {os.estado === "PendenteDiagnostico" ? "Aguarda que um mecânico pegue na OS" : "—"}
                      </span>
                    )}
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>
        </TabsContent>

        <TabsContent value="diagnostico">
          <DiagnosticoTab
            os={os}
            reparacoes={reparacoes}
            pecas={pecas}
            canEdit={canMecOps && os.estado === "PendenteDiagnostico" && !!os.codMecanico}
            canApprove={canSecOps && os.estado === "PendenteAprovacaoOrcamento"}
            onChanged={reload}
          />
        </TabsContent>

        <TabsContent value="conserto">
          <ConsertoTab
            os={os}
            reparacoes={reparacoes}
            pecas={pecas}
            canEdit={canMecOps && (os.estado === "PendenteReparacao" || os.estado === "AguardarPecas")}
            canReportDefeito={isGerente || isMecAtribuido}
            onChanged={reload}
          />
        </TabsContent>

        <TabsContent value="pagamento">
          <PagamentoTab
            os={os}
            reparacoes={reparacoes}
            pecas={pecas}
            canSec={canSecOps}
            onChanged={reload}
          />
        </TabsContent>
      </Tabs>
    </div>
  );
}

function Row({ label, value }: { label: string; value: string }) {
  return (
    <div className="flex justify-between gap-4 border-b py-1.5 last:border-0">
      <span className="text-xs font-medium text-muted-foreground">{label}</span>
      <span className="text-right">{value}</span>
    </div>
  );
}

function Section({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div className="space-y-2">
      <h3 className="text-sm font-semibold">{title}</h3>
      {children}
    </div>
  );
}

function AtribuirDialog({ funcionarios, onAtribuir }: {
  funcionarios: Funcionario[];
  onAtribuir: (id: number) => void;
}) {
  const [open, setOpen] = useState(false);
  const [funcId, setFuncId] = useState(String(funcionarios[0]?.id ?? ""));

  if (!open) {
    return (
      <Button variant="outline" size="sm" onClick={() => setOpen(true)}>
        Atribuir mecânico
      </Button>
    );
  }

  return (
    <div className="flex items-center gap-2">
      <Select value={funcId} onValueChange={setFuncId}>
        <SelectTrigger className="w-[200px]"><SelectValue /></SelectTrigger>
        <SelectContent>
          {funcionarios.map((f) => (
            <SelectItem key={f.id} value={String(f.id)}>{f.nome}</SelectItem>
          ))}
        </SelectContent>
      </Select>
      <Button size="sm" onClick={() => { onAtribuir(Number(funcId)); setOpen(false); }}>
        Confirmar
      </Button>
      <Button size="sm" variant="outline" onClick={() => setOpen(false)}>
        Cancelar
      </Button>
    </div>
  );
}

// ---------- Diagnóstico ----------
function DiagnosticoTab({
  os, reparacoes, pecas, canEdit, canApprove, onChanged,
}: {
  os: OrdemServico; reparacoes: Reparacao[]; pecas: Peca[];
  canEdit: boolean; canApprove: boolean; onChanged: () => void;
}) {
  const existing = os.diagnostico;

  const initReps = existing?.cod_reparacoes ?? [];
  const initPecs: Record<number, number> = existing
    ? Object.fromEntries(Object.entries(existing.pecasOrcamento).map(([k, v]) => [Number(k), v]))
    : {};

  const [selectedReps, setSelectedReps] = useState<number[]>(initReps);
  const [pecasQtd, setPecasQtd] = useState<Record<number, number>>(initPecs);
  const [notas, setNotas] = useState(existing?.descricao ?? "");
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    setSelectedReps(existing?.cod_reparacoes ?? []);
    setPecasQtd(existing
      ? Object.fromEntries(Object.entries(existing.pecasOrcamento).map(([k, v]) => [Number(k), v]))
      : {});
    setNotas(existing?.descricao ?? "");
  }, [os.id, existing]);

  const addRep = (idStr: string) => {
    const id = Number(idStr);
    if (!id || selectedReps.includes(id)) return;
    setSelectedReps((p) => [...p, id]);
  };
  const removeRep = (id: number) => setSelectedReps((p) => p.filter((x) => x !== id));

  const addPeca = (idStr: string) => {
    const id = Number(idStr);
    if (!id || id in pecasQtd) return;
    setPecasQtd((p) => ({ ...p, [id]: 1 }));
  };
  const removePeca = (id: number) =>
    setPecasQtd((p) => { const n = { ...p }; delete n[id]; return n; });
  const setQtd = (id: number, q: number) =>
    setPecasQtd((p) => ({ ...p, [id]: Math.max(1, q) }));

  const maoObra = useMemo(
    () => selectedReps.reduce((s, rid) => s + (reparacoes.find((r) => r.id === rid)?.preco ?? 0), 0),
    [selectedReps, reparacoes],
  );
  const totalPecas = useMemo(
    () => Object.entries(pecasQtd).reduce((s, [pid, q]) => s + (pecas.find((p) => p.id === Number(pid))?.preco_venda ?? 0) * q, 0),
    [pecasQtd, pecas],
  );

  const submit = async () => {
    if (selectedReps.length === 0) { toast.error("Adiciona pelo menos uma reparação"); return; }
    if (!notas.trim()) { toast.error("Adiciona uma descrição ao Diagnóstico"); return; }
    setSaving(true);
    try {
      await api.patch(`/ordensservicos/${os.id}/diagnostico`, {
        pecasQuantidades: pecasQtd,
        reparacoes: selectedReps.map((rid) => reparacoes.find((r) => r.id === rid)!).filter(Boolean),
        descricao: notas.trim(),
      });
      toast.success("Diagnóstico submetido — aguarda aprovação");
      onChanged();
    } catch (e) { toast.error((e as Error).message); }
    finally { setSaving(false); }
  };

  const aprovar = async () => {
    try {
      await api.patch(`/ordensservicos/${os.id}/aprovarOrcamento`, {});
      toast.success("Orçamento aprovado");
      onChanged();
    } catch (e) { toast.error((e as Error).message); }
  };

  const rejeitar = async () => {
    try {
      await api.patch(`/ordensservicos/${os.id}/rejeitarOrcamento`, {});
      toast.success("Orçamento rejeitado");
      onChanged();
    } catch (e) { toast.error((e as Error).message); }
  };

  if (!canEdit && !canApprove && !existing) {
    return (
      <Card><CardContent className="py-10 text-center text-sm text-muted-foreground">
        O Diagnóstico ainda não foi feito.
      </CardContent></Card>
    );
  }

  return (
    <div className="grid gap-4 lg:grid-cols-3">
      <Card className="lg:col-span-2">
        <CardHeader><CardTitle className="text-base">Reparações & Peças do diagnóstico</CardTitle></CardHeader>
        <CardContent className="space-y-4">
          <Section title="Reparações">
            {canEdit && (
              <AddSelect
                placeholder="Adicionar reparação…"
                options={reparacoes.filter((r) => r.disponivel && !selectedReps.includes(r.id))
                  .map((r) => ({ value: String(r.id), label: `${r.nomenclatura} — ${formatEUR(r.preco)}` }))}
                onAdd={addRep}
              />
            )}
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Nomenclatura</TableHead>
                  <TableHead className="text-right">Preço</TableHead>
                  {canEdit && <TableHead />}
                </TableRow>
              </TableHeader>
              <TableBody>
                {selectedReps.length === 0 ? (
                  <TableRow><TableCell colSpan={3} className="text-center text-sm text-muted-foreground">Sem reparações</TableCell></TableRow>
                ) : selectedReps.map((rid) => {
                  const r = reparacoes.find((x) => x.id === rid);
                  return (
                    <TableRow key={rid}>
                      <TableCell className="font-medium">{r?.nomenclatura ?? `#${rid}`}</TableCell>
                      <TableCell className="text-right">{r ? formatEUR(r.preco) : "—"}</TableCell>
                      {canEdit && (
                        <TableCell className="w-[1%]">
                          <Button variant="ghost" size="icon" onClick={() => removeRep(rid)}>
                            <Trash2 className="h-4 w-4 text-destructive" />
                          </Button>
                        </TableCell>
                      )}
                    </TableRow>
                  );
                })}
              </TableBody>
            </Table>
          </Section>

          {pecas.length > 0 && (
            <Section title="Peças">
              {canEdit && (
                <AddSelect
                  placeholder="Adicionar peça…"
                  options={pecas.filter((p) => p.ativa && !(p.id in pecasQtd))
                    .map((p) => ({ value: String(p.id), label: `${p.referencia} — ${p.nome} · ${formatEUR(p.preco_venda)}` }))}
                  onAdd={addPeca}
                />
              )}
              {Object.keys(pecasQtd).length > 0 && (
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>Peça</TableHead>
                      <TableHead className="w-24">Qtd</TableHead>
                      <TableHead className="text-right">Subtotal</TableHead>
                      {canEdit && <TableHead />}
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {Object.entries(pecasQtd).map(([pidStr, q]) => {
                      const pid = Number(pidStr);
                      const p = pecas.find((x) => x.id === pid);
                      return (
                        <TableRow key={pid}>
                          <TableCell className="font-medium">{p ? `${p.referencia} — ${p.nome}` : `#${pid}`}</TableCell>
                          <TableCell>
                            {canEdit ? (
                              <Input type="number" min={1} value={q} className="h-8 w-20"
                                onChange={(e) => setQtd(pid, Number(e.target.value))} />
                            ) : q}
                          </TableCell>
                          <TableCell className="text-right">{p ? formatEUR(p.preco_venda * q) : "—"}</TableCell>
                          {canEdit && (
                            <TableCell className="w-[1%]">
                              <Button variant="ghost" size="icon" onClick={() => removePeca(pid)}>
                                <Trash2 className="h-4 w-4 text-destructive" />
                              </Button>
                            </TableCell>
                          )}
                        </TableRow>
                      );
                    })}
                  </TableBody>
                </Table>
              )}
            </Section>
          )}

          {canEdit && (
            <div className="space-y-2">
              <Label>Notas / descrição do diagnóstico</Label>
              <Textarea rows={3} value={notas} onChange={(e) => setNotas(e.target.value)}
                placeholder="Descreve o problema diagnosticado…" />
            </div>
          )}
          {!canEdit && existing?.descricao && (
            <div>
              <div className="text-xs font-medium text-muted-foreground mb-1">Notas do mecânico</div>
              <p className="text-sm whitespace-pre-wrap">{existing.descricao}</p>
            </div>
          )}
        </CardContent>
      </Card>

      <Card>
        <CardHeader><CardTitle className="text-base">Orçamento</CardTitle></CardHeader>
        <CardContent className="space-y-3 text-sm">
          {canEdit ? (
            <>
              <Row label="Mão de obra" value={formatEUR(maoObra)} />
              <Row label="Peças" value={formatEUR(totalPecas)} />
              <div className="flex justify-between border-t pt-2 text-base font-semibold">
                <span>Total</span><span>{formatEUR(maoObra + totalPecas)}</span>
              </div>
              <Button className="w-full" onClick={submit} disabled={saving}>
                {saving ? "A submeter…" : "Submeter para aprovação"}
              </Button>
            </>
          ) : existing ? (
            <>
              <Row label="Orçamento" value={formatEUR(existing.orcamento)} />
              {canApprove && (
                <div className="flex flex-col gap-2 pt-2">
                  <Button className="w-full" onClick={aprovar}>
                    <CheckCircle2 className="h-4 w-4" /> Aprovar orçamento
                  </Button>
                  <ConfirmDialog
                    trigger={<Button variant="outline" className="w-full text-destructive">Rejeitar orçamento</Button>}
                    title="Rejeitar orçamento?"
                    description="Apenas rejeite o orçamento se o cliente não aceitar o valor. Esta ação não pode ser revertida."
                    destructive
                    onConfirm={rejeitar}
                  />
                </div>
              )}
            </>
          ) : (
            <p className="text-xs text-muted-foreground">Sem diagnóstico ainda.</p>
          )}
        </CardContent>
      </Card>
    </div>
  );
}

// ---------- Conserto ----------
function ConsertoTab({
  os, reparacoes, pecas, canEdit, canReportDefeito, onChanged,
}: {
  os: OrdemServico; reparacoes: Reparacao[]; pecas: Peca[];
  canEdit: boolean; canReportDefeito: boolean; onChanged: () => void;
}) {
  const [defeitoOpen, setDefeitoOpen] = useState(false);
  const diag = os.diagnostico;
  const existing = os.conserto;

  const { data: pecasConserto } = useQuery<Record<number, number>>({
    queryKey: ["ordensservicos", os.id, "conserto", "pecas"],
    queryFn: () => api.get<Record<number, number>>(`/ordensservicos/${os.id}/conserto/pecas`),
    enabled: !!existing,
  });

  const initReps = existing?.cod_reparacoes ?? diag?.cod_reparacoes ?? [];
  const initPecs: Record<number, number> = !existing && diag
    ? Object.fromEntries(Object.entries(diag.pecasOrcamento).map(([k, v]) => [Number(k), v]))
    : {};

  const [selectedReps, setSelectedReps] = useState<number[]>(initReps);
  const [pecasQtd, setPecasQtd] = useState<Record<number, number>>(initPecs);
  const [check, setCheck] = useState<CheckList>(existing?.checkList ?? CHECK_INIT);
  const [saving, setSaving] = useState(false);
  const [novoDiagOpen, setNovoDiagOpen] = useState(false);
  const [novoDiagDesc, setNovoDiagDesc] = useState("");
  const [aguardandoPecas, setAguardandoPecas] = useState(false);

  const maoObra = useMemo(
    () => selectedReps.reduce((s, rid) => s + (reparacoes.find((r) => r.id === rid)?.preco ?? 0), 0),
    [selectedReps, reparacoes],
  );
  const totalPecas = useMemo(
    () => Object.entries(pecasQtd).reduce((s, [pid, q]) => s + (pecas.find((p) => p.id === Number(pid))?.preco_venda ?? 0) * q, 0),
    [pecasQtd, pecas],
  );
  const total = maoObra + totalPecas;
  const orcamentoAprovado = os.diagnostico?.orcamento ?? null;
  const exceedsOrcamento = orcamentoAprovado !== null && total > orcamentoAprovado+0.01;

  // Reset quando muda de OS
  useEffect(() => {
    const e = os.conserto;
    const d = os.diagnostico;
    setSelectedReps(e?.cod_reparacoes ?? d?.cod_reparacoes ?? []);
    setPecasQtd(!e && d
      ? Object.fromEntries(Object.entries(d.pecasOrcamento).map(([k, v]) => [Number(k), v]))
      : {});
    setCheck(e?.checkList ?? CHECK_INIT);
  }, [os.id, os.conserto, os.diagnostico]);

  // Aplica pecasConserto quando os dados chegam do servidor (só uma vez por query)
  useEffect(() => {
    if (pecasConserto && Object.keys(pecasConserto).length > 0) {
      setPecasQtd(pecasConserto);
    }
  }, [pecasConserto]);

  const addRep = (idStr: string) => {
    const id = Number(idStr);
    if (!id || selectedReps.includes(id)) return;
    setSelectedReps((p) => [...p, id]);
  };
  const removeRep = (id: number) => setSelectedReps((p) => p.filter((x) => x !== id));
  const addPeca = (idStr: string) => {
    const id = Number(idStr);
    if (!id || id in pecasQtd) return;
    setPecasQtd((p) => ({ ...p, [id]: 1 }));
  };
  const removePeca = (id: number) =>
    setPecasQtd((p) => { const n = { ...p }; delete n[id]; return n; });
  const setQtd = (id: number, q: number) =>
    setPecasQtd((p) => ({ ...p, [id]: Math.max(1, q) }));

  const allChecked = Object.values(check).every(Boolean);

  const concluir = async () => {
    if (selectedReps.length === 0) { toast.error("Adiciona pelo menos uma reparação"); return; }
    if (!allChecked) { toast.error("Completa o checklist de segurança"); return; }
    setSaving(true);
    try {
      await api.patch(`/ordensservicos/${os.id}/conserto`, {
        pecasQuantidades: pecasQtd,
        reparacoes: selectedReps.map((rid) => reparacoes.find((r) => r.id === rid)!).filter(Boolean),
        checklist: check,
      });
      toast.success("Reparação concluída");
      onChanged();
    } catch (e) { toast.error((e as Error).message); }
    finally { setSaving(false); }
  };

  const submitNovoDiag = async () => {
    if (selectedReps.length === 0) { toast.error("Adiciona pelo menos uma reparação"); return; }
    if (!novoDiagDesc.trim()) { toast.error("Adiciona uma descrição para o novo diagnóstico"); return; }
    setSaving(true);
    try {
      await api.patch(`/ordensservicos/${os.id}/diagnostico`, {
        pecasQuantidades: pecasQtd,
        reparacoes: selectedReps.map((rid) => reparacoes.find((r) => r.id === rid)!).filter(Boolean),
        descricao: novoDiagDesc.trim(),
      });
      toast.success("Novo diagnóstico submetido — aguarda aprovação do orçamento");
      onChanged();
    } catch (e) { toast.error((e as Error).message); }
    finally { setSaving(false); }
  };

  const aguardarPecas = async () => {
    setSaving(true);
    setAguardandoPecas(true);
    try {
      await api.patch(`/ordensservicos/${os.id}/aguardarPecas`, {});
      toast.success("OS colocada em espera por peças");
      onChanged();
    } catch (e) { 
      toast.error((e as Error).message);
      setAguardandoPecas(false);
    }
    finally { setSaving(false); }
  };

  const blockedStates: EstadoOS[] = ["PendenteDiagnostico", "PendenteAprovacaoOrcamento", "OrcamentoNaoAprovado"];
  if (blockedStates.includes(os.estado) || (!canEdit && !existing)) {
    return (
      <Card><CardContent className="py-10 text-center text-sm text-muted-foreground">
        O Conserto só pode ser registado após a aprovação do orçamento.
      </CardContent></Card>
    );
  }

  return (
    <>
    <div className="grid gap-4 lg:grid-cols-3">
      <Card className="lg:col-span-2">
        <CardHeader><CardTitle className="text-base">Execução do conserto</CardTitle></CardHeader>
        <CardContent className="space-y-4">
          <Section title="Reparações">
            {canEdit && (
              <AddSelect
                placeholder="Adicionar reparação…"
                options={reparacoes.filter((r) => r.disponivel && !selectedReps.includes(r.id))
                  .map((r) => ({ value: String(r.id), label: `${r.nomenclatura} — ${formatEUR(r.preco)}` }))}
                onAdd={addRep}
              />
            )}
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Nomenclatura</TableHead>
                  <TableHead className="text-right">Preço</TableHead>
                  {canEdit && <TableHead />}
                </TableRow>
              </TableHeader>
              <TableBody>
                {selectedReps.length === 0 ? (
                  <TableRow><TableCell colSpan={canEdit ? 3 : 2} className="text-center text-sm text-muted-foreground">Sem reparações</TableCell></TableRow>
                ) : selectedReps.map((rid) => {
                  const r = reparacoes.find((x) => x.id === rid);
                  return (
                    <TableRow key={rid}>
                      <TableCell className="font-medium">{r?.nomenclatura ?? `#${rid}`}</TableCell>
                      <TableCell className="text-right">{r ? formatEUR(r.preco) : "—"}</TableCell>
                      {canEdit && (
                        <TableCell className="w-[1%]">
                          <Button variant="ghost" size="icon" onClick={() => removeRep(rid)}>
                            <Trash2 className="h-4 w-4 text-destructive" />
                          </Button>
                        </TableCell>
                      )}
                    </TableRow>
                  );
                })}
              </TableBody>
            </Table>
          </Section>

          {(pecas.length > 0 || Object.keys(pecasQtd).length > 0) && (
            <Section title="Peças usadas">
              {canEdit && pecas.length > 0 && (
                <AddSelect
                  placeholder="Adicionar peça…"
                  options={pecas.filter((p) => p.ativa && !(p.id in pecasQtd))
                    .map((p) => ({ value: String(p.id), label: `${p.referencia} — ${p.nome} · ${formatEUR(p.preco_venda)}` }))}
                  onAdd={addPeca}
                />
              )}
              {Object.keys(pecasQtd).length > 0 && (
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>Peça</TableHead>
                      <TableHead className="w-24">Qtd</TableHead>
                      <TableHead className="text-right">Preço unit.</TableHead>
                      <TableHead className="text-right">Subtotal</TableHead>
                      {canEdit && <TableHead />}
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {Object.entries(pecasQtd).map(([pidStr, q]) => {
                      const pid = Number(pidStr);
                      const p = pecas.find((x) => x.id === pid);
                      return (
                        <TableRow key={pid}>
                          <TableCell className="font-medium">{p ? `${p.referencia} — ${p.nome}` : `#${pid}`}</TableCell>
                          <TableCell>
                            {canEdit ? (
                              <Input type="number" min={1} value={q} className="h-8 w-20"
                                onChange={(e) => setQtd(pid, Number(e.target.value))} />
                            ) : q}
                          </TableCell>
                          <TableCell className="text-right">{p ? formatEUR(p.preco_venda) : "—"}</TableCell>
                          <TableCell className="text-right">{p ? formatEUR(p.preco_venda * Number(q)) : "—"}</TableCell>
                          {canEdit && (
                            <TableCell className="w-[1%]">
                              <Button variant="ghost" size="icon" onClick={() => removePeca(pid)}>
                                <Trash2 className="h-4 w-4 text-destructive" />
                              </Button>
                            </TableCell>
                          )}
                        </TableRow>
                      );
                    })}
                  </TableBody>
                </Table>
              )}
            </Section>
          )}

          <Section title="Checklist de segurança">
            <div className="grid grid-cols-2 gap-2 sm:grid-cols-3">
              {(Object.keys(CHECK_LABELS) as (keyof CheckList)[]).map((k) => (
                <label key={k} className="flex items-center gap-2 rounded border p-2 text-sm">
                  <Checkbox
                    checked={check[k]}
                    disabled={!canEdit}
                    onCheckedChange={(v) => setCheck((c) => ({ ...c, [k]: !!v }))}
                  />
                  {CHECK_LABELS[k]}
                </label>
              ))}
            </div>
          </Section>
        </CardContent>
      </Card>

      <Card>
        <CardHeader><CardTitle className="text-base">Totais</CardTitle></CardHeader>
        <CardContent className="space-y-3 text-sm">
          <Row label="Mão de obra" value={formatEUR(maoObra)} />
          <Row label="Peças" value={formatEUR(totalPecas)} />
          <div className="flex justify-between border-t pt-2 text-base font-semibold">
            <span>Total</span><span>{formatEUR(total)}</span>
          </div>
          {orcamentoAprovado !== null && (
            <div className="flex justify-between text-xs text-muted-foreground">
              <span>Orçamento aprovado</span>
              <span>{formatEUR(orcamentoAprovado)}</span>
            </div>
          )}
          {exceedsOrcamento && (
            <div className="rounded border border-destructive/30 bg-destructive/10 p-2 text-xs text-destructive">
              O total excede o orçamento aprovado. Submete um novo diagnóstico.
            </div>
          )}

          {existing ? (
            <div className="rounded border border-success/30 bg-success-soft p-3 text-sm text-success">
              Conserto registado — preço total: <strong>{formatEUR(existing.preco_total)}</strong>
            </div>
          ) : canEdit ? (
            <div className="space-y-2 pt-1">
              <Button
                className="w-full"
                disabled={saving || !allChecked || selectedReps.length === 0 || exceedsOrcamento}
                onClick={concluir}
              >
                {saving ? "A registar…" : "Concluir reparação"}
              </Button>
              {!allChecked && (
                <p className="text-xs text-muted-foreground">Completa o checklist para poder concluir.</p>
              )}
              {exceedsOrcamento && (
                <p className="text-xs text-destructive">O total excede o orçamento — conclui um novo diagnóstico primeiro.</p>
              )}
              <hr />
              {!novoDiagOpen ? (
                <Button variant="outline" className="w-full" onClick={() => setNovoDiagOpen(true)}>
                  Submeter novo Diagnóstico
                </Button>
              ) : (
                <div className="space-y-2">
                  <Label>Descrição do novo diagnóstico</Label>
                  <Textarea
                    rows={3}
                    value={novoDiagDesc}
                    onChange={(e) => setNovoDiagDesc(e.target.value)}
                    placeholder="Descreve as alterações ao diagnóstico…"
                  />
                  <div className="flex gap-2">
                    <Button className="flex-1" onClick={submitNovoDiag} disabled={saving || !novoDiagDesc.trim()}>
                      Confirmar
                    </Button>
                    <Button variant="outline" className="flex-1" onClick={() => { setNovoDiagOpen(false); setNovoDiagDesc(""); }}>
                      Cancelar
                    </Button>
                  </div>
                </div>
              )}
              <hr />
              <Button className="w-full bg-orange-500 text-white hover:bg-orange-600" disabled={saving || aguardandoPecas} onClick={aguardarPecas}>
                {aguardandoPecas ? "Aguardando peças…" : "Aguardar Peças"}
              </Button>
            </div>
          ) : null}

          {canReportDefeito && Object.keys(pecasQtd).length > 0 && (
            <>
              <hr />
              <Button variant="outline" className="w-full text-destructive" onClick={() => setDefeitoOpen(true)}>
                <AlertTriangle className="h-4 w-4" /> Reportar peça com defeito
              </Button>
            </>
          )}
        </CardContent>
      </Card>
    </div>

    {canReportDefeito && Object.keys(pecasQtd).length > 0 && (
      <DefeitoDialog
        open={defeitoOpen}
        onOpenChange={setDefeitoOpen}
        osId={os.id}
        pecas={pecas}
        pecasUsadas={pecasQtd}
        idFuncionario={os.codMecanico!}
        onSaved={() => { setDefeitoOpen(false); onChanged(); }}
      />
    )}
    </>
  );
}

// ---------- Pagamento ----------
function PagamentoTab({
  os, reparacoes, pecas, canSec, onChanged,
}: {
  os: OrdemServico; reparacoes: Reparacao[]; pecas: Peca[];
  canSec: boolean; onChanged: () => void;
}) {
  const [metodo, setMetodo] = useState<MetodoPagamento>("MULTIBANCO");
  const [saving, setSaving] = useState(false);

  const maoObra = (os.conserto?.cod_reparacoes ?? os.diagnostico?.cod_reparacoes ?? []).reduce(
    (s, rid) => s + (reparacoes.find((r) => r.id === rid)?.preco ?? 0), 0
  );

  const { data: pecasConserto = {} } = useQuery<Record<number, number>>({
    queryKey: ["ordensservicos", os.id, "conserto", "pecas"],
    queryFn: () => api.get<Record<number, number>>(`/ordensservicos/${os.id}/conserto/pecas`),
    enabled: !!os.conserto,
  });
  const pecasParaCalculo = os.conserto ? pecasConserto : os.diagnostico?.pecasOrcamento ?? {};
  const totalPecas = Object.entries(pecasParaCalculo).reduce(
    (s, [pid, q]) => s + (pecas.find((p) => p.id === Number(pid))?.preco_venda ?? 0) * q, 0
  );
  const total = os.conserto?.preco_total ?? (maoObra + totalPecas);

  const notificar = async () => {
    setSaving(true);
    try {
      await api.patch(`/ordensservicos/${os.id}/notificarCliente`, {});
      toast.success("Cliente notificado");
      onChanged();
    } catch (e) { toast.error((e as Error).message); }
    finally { setSaving(false); }
  };

  const pagar = async () => {
    setSaving(true);
    try {
      await api.patch(`/ordensservicos/${os.id}/pagar`, { metodo_pagamento: metodo });
      toast.success("Pagamento registado");
      onChanged();
    } catch (e) { toast.error((e as Error).message); }
    finally { setSaving(false); }
  };

  const blockedStates: EstadoOS[] = [
    "PendenteDiagnostico", "PendenteAprovacaoOrcamento", "OrcamentoNaoAprovado",
    "PendenteReparacao", "AguardarPecas",
  ];

  if (blockedStates.includes(os.estado)) {
    return (
      <Card><CardContent className="py-10 text-center text-sm text-muted-foreground">
        O pagamento estará disponível após a conclusão do conserto.
      </CardContent></Card>
    );
  }

  return (
    <Card>
      <CardHeader><CardTitle className="text-base">Pagamento</CardTitle></CardHeader>
      <CardContent className="space-y-4 text-sm">
        <div className="grid gap-2 sm:grid-cols-3">
          <Stat label="Mão de obra" value={formatEUR(maoObra)} />
          <Stat label="Peças" value={formatEUR(totalPecas)} />
          <Stat label="Total" value={formatEUR(total)} highlight />
        </div>

        {os.estado === "ClienteNotificado" && canSec && (
          <Button onClick={notificar} disabled={saving}>
            {saving ? "A notificar…" : "Confirmar disponibilidade para levantamento"}
          </Button>
        )}

        {os.estado === "PendentePagamento" && canSec && (
          <div className="flex flex-wrap items-end gap-3">
            <div className="space-y-1">
              <Label>Método de pagamento</Label>
              <Select value={metodo} onValueChange={(v) => setMetodo(v as MetodoPagamento)}>
                <SelectTrigger className="w-[180px]"><SelectValue /></SelectTrigger>
                <SelectContent>
                  <SelectItem value="NUMERARIO">Numerário</SelectItem>
                  <SelectItem value="MULTIBANCO">Multibanco</SelectItem>
                  <SelectItem value="MBWAY">MB Way</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <Button onClick={pagar} disabled={saving}>
              <CheckCircle2 className="h-4 w-4" />
              {saving ? "A registar…" : "Registar pagamento"}
            </Button>
          </div>
        )}

        {os.estado === "Paga" && (
          <div className="rounded border border-success/30 bg-success-soft p-3 text-success">
            <strong>OS paga</strong>{os.metodo_pagamento ? ` via ${os.metodo_pagamento}` : ""}.
          </div>
        )}
      </CardContent>
    </Card>
  );
}

// ---------- Defeito Dialog ----------
function DefeitoDialog({
  open, onOpenChange, osId, pecas, pecasUsadas, idFuncionario, onSaved,
}: {
  open: boolean; onOpenChange: (v: boolean) => void;
  osId: number; pecas: Peca[]; pecasUsadas: Record<number, number>;
  idFuncionario: number; onSaved: () => void;
}) {
  const [selecionadas, setSelecionadas] = useState<Set<number>>(new Set());
  const [motivo, setMotivo] = useState("");
  const [saving, setSaving] = useState(false);

  const pecasDoConserto = useMemo(
    () => Object.entries(pecasUsadas)
      .map(([pidStr, qtd]) => ({ peca: pecas.find((p) => p.id === Number(pidStr)) ?? null, id: Number(pidStr), qtd }))
      .filter((x) => x.peca !== null),
    [pecasUsadas, pecas],
  );

  const reset = () => { setSelecionadas(new Set()); setMotivo(""); setSaving(false); };

  const togglePeca = (id: number) =>
    setSelecionadas((prev) => {
      const next = new Set(prev);
      next.has(id) ? next.delete(id) : next.add(id);
      return next;
    });

  const submit = async () => {
    if (selecionadas.size === 0 || !motivo.trim()) return;
    setSaving(true);
    try {
      // 1. Reportar defeito para cada peça selecionada — bloqueia todos os stocks dessa peça
      await Promise.all(
        [...selecionadas].map((codPeca) =>
          api.post(`/defeitos`, { codPeca, motivo: motivo.trim(), idFuncionario })
        )
      );
      // 2. Colocar a OS em AguardarPeças automaticamente
      await api.patch(`/ordensservicos/${osId}/aguardarPecas`, {});

      toast.success(
        selecionadas.size === 1
          ? "Defeito reportado — OS colocada em aguarda de peças"
          : `${selecionadas.size} defeitos reportados — OS colocada em aguarda de peças`
      );
      reset();
      onSaved();
    } catch (e) { toast.error((e as Error).message); }
    finally { setSaving(false); }
  };

  return (
    <Dialog open={open} onOpenChange={(v) => { if (!v) reset(); onOpenChange(v); }}>
      <DialogContent className="max-w-lg">
        <DialogHeader>
          <DialogTitle>Reportar peça(s) com defeito</DialogTitle>
        </DialogHeader>
        <div className="space-y-4">
          <div className="rounded-md border border-destructive/30 bg-destructive/5 px-3 py-2 text-xs text-destructive">
            Todos os stocks em armazém das peças selecionadas serão bloqueados para inspeção pelo Gestor de Stock.
            A OS passará automaticamente para <strong>Aguarda Peças</strong>.
          </div>

          <div className="space-y-1">
            <Label className="text-xs">Peças usadas neste conserto</Label>
            <div className="rounded-md border divide-y">
              {pecasDoConserto.map(({ peca, id, qtd }) => (
                <label
                  key={id}
                  className={`flex cursor-pointer items-center gap-3 px-3 py-2.5 text-sm transition-colors ${
                    selecionadas.has(id) ? "bg-destructive/5" : "hover:bg-muted/40"
                  }`}
                >
                  <Checkbox
                    checked={selecionadas.has(id)}
                    onCheckedChange={() => togglePeca(id)}
                  />
                  <span className="flex-1 font-medium">
                    <span className="font-mono text-xs text-muted-foreground">{peca!.referencia}</span>
                    {" — "}{peca!.nome}
                  </span>
                  <Badge variant="secondary" className="shrink-0 font-mono text-xs">
                    Qtd: {qtd}
                  </Badge>
                </label>
              ))}
            </div>
            {selecionadas.size === 0 && (
              <p className="text-xs text-destructive">Seleciona pelo menos uma peça</p>
            )}
          </div>

          <div className="space-y-1">
            <Label className="text-xs">Motivo</Label>
            <Textarea
              rows={3}
              value={motivo}
              onChange={(e) => setMotivo(e.target.value)}
              placeholder="Descreve o defeito observado…"
            />
            {!motivo.trim() && <p className="text-xs text-destructive">Motivo obrigatório</p>}
          </div>
        </div>
        <DialogFooter>
          <Button type="button" variant="outline" onClick={() => { reset(); onOpenChange(false); }}>
            Cancelar
          </Button>
          <Button
            type="button"
            variant="destructive"
            disabled={saving || selecionadas.size === 0 || !motivo.trim()}
            onClick={submit}
          >
            {saving ? "A reportar…" : "Reportar defeito"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

function Stat({ label, value, highlight }: { label: string; value: string; highlight?: boolean }) {
  return (
    <div className={`rounded-md border p-3 ${highlight ? "bg-primary-soft" : ""}`}>
      <div className="text-xs text-muted-foreground">{label}</div>
      <div className={`text-lg font-semibold ${highlight ? "text-primary" : ""}`}>{value}</div>
    </div>
  );
}

function AddSelect({
  placeholder, options, onAdd,
}: { placeholder: string; options: { value: string; label: string }[]; onAdd: (v: string) => void }) {
  const [v, setV] = useState("");
  return (
    <div className="flex gap-2">
      <Select value={v} onValueChange={setV}>
        <SelectTrigger className="flex-1"><SelectValue placeholder={placeholder} /></SelectTrigger>
        <SelectContent>
          {options.map((o) => <SelectItem key={o.value} value={o.value}>{o.label}</SelectItem>)}
        </SelectContent>
      </Select>
      <Button type="button" disabled={!v} onClick={() => { onAdd(v); setV(""); }}>
        <Plus className="h-4 w-4" />
      </Button>
    </div>
  );
}