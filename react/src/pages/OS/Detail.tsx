import { useEffect, useMemo, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import {
  ArrowLeft, Plus, Trash2, AlertTriangle, CheckCircle2, ShieldCheck,
} from "lucide-react";
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
  Table, TableBody, TableCell, TableHead, TableHeader, TableRow,
} from "@/components/ui/table";
import {
  Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle, DialogTrigger,
} from "@/components/ui/dialog";

import { osService, calcOrcamento } from "@/services/os";
import {
  clientesService, trotinetesService, fornecedoresService,
  funcionariosService, reparacoesService, pecasService,
} from "@/services/entities";
import { useAuth } from "@/context/AuthContext";
import { formatDateTime, formatEUR } from "@/lib/format";
import type {
  OS, Cliente, Trotinete, Funcionario, Reparacao, Peca, Fornecedor,
  ItemReparacaoOS, ItemPecaOS, ChecklistSeguranca, MetodoPagamento,
} from "@/lib/types";
import { ESTADO_OS_LABELS } from "@/lib/types";

const CHECK_INIT: ChecklistSeguranca = {
  travoes: false, luzes: false, pneus: false, aceleracao: false,
  travagem: false, visor: false, testeConducao: false,
};

const CHECK_LABELS: Record<keyof ChecklistSeguranca, string> = {
  travoes: "Travões",
  luzes: "Luzes",
  pneus: "Pneus",
  aceleracao: "Aceleração",
  travagem: "Travagem",
  visor: "Visor / display",
  testeConducao: "Teste de condução",
};

export default function OSDetail() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { user, role } = useAuth();

  const [os, setOs] = useState<OS | null>(null);
  const [cliente, setCliente] = useState<Cliente | null>(null);
  const [trotinete, setTrotinete] = useState<Trotinete | null>(null);
  const [mecanicos, setMecanicos] = useState<Funcionario[]>([]);
  const [reparacoes, setReparacoes] = useState<Reparacao[]>([]);
  const [pecas, setPecas] = useState<Peca[]>([]);
  const [fornecedores, setFornecedores] = useState<Fornecedor[]>([]);

  const reload = async () => {
    if (!id) return;
    const o = await osService.get(id);
    if (!o) { toast.error("OS não encontrada"); navigate("/os"); return; }
    setOs(o);
    const [c, t, fs, rs, ps, fn] = await Promise.all([
      clientesService.get(o.clienteId),
      trotinetesService.list(),
      funcionariosService.list(),
      reparacoesService.list(),
      pecasService.list(),
      fornecedoresService.list(),
    ]);
    setCliente(c);
    setTrotinete(t.find((x) => x.id === o.trotineteId) ?? null);
    setMecanicos(fs.filter((f) => f.cargo === "MECANICO" && f.ativo));
    setReparacoes(rs);
    setPecas(ps);
    setFornecedores(fn);
  };
  useEffect(() => { reload(); /* eslint-disable-next-line */ }, [id]);

  if (!os) {
    return <div className="p-8 text-sm text-muted-foreground">A carregar OS…</div>;
  }

  const isMecanicoAtribuido = role === "MECANICO" && user?.id === os.mecanicoId;
  const canMecOps = role === "GERENTE" || isMecanicoAtribuido;
  const canSecOps = role === "GERENTE" || role === "SECRETARIA";

  // Mecânico pode "pegar" se a OS está REGISTADA e ainda não tem dono
  const canPegar =
    role === "MECANICO" && !!user && !os.mecanicoId && os.estado === "REGISTADA";
  // Bloqueado: outro mecânico já é dono
  const bloqueadoOutroMec =
    role === "MECANICO" && !!os.mecanicoId && os.mecanicoId !== user?.id;

  const handlePegar = async () => {
    if (!user) return;
    try {
      await osService.pegarOS(os.id, user.id);
      toast.success("OS atribuída a ti — começa o diagnóstico");
      reload();
    } catch (e) {
      toast.error((e as Error).message);
      reload();
    }
  };

  const handleCancelar = async () => {
    await osService.cancelar(os.id);
    toast.success("OS cancelada");
    reload();
  };

  return (
    <div>
      <PageHeader
        title={`OS ${os.numero}`}
        description={`Criada em ${formatDateTime(os.criadaEm)} · Última alteração ${formatDateTime(os.atualizadaEm)}`}
        actions={
          <div className="flex flex-wrap items-center gap-2">
            <StateBadge state={os.estado} />
            <Button variant="outline" asChild>
              <Link to="/os"><ArrowLeft className="h-4 w-4" /> Voltar</Link>
            </Button>
            {canSecOps && os.estado !== "PAGA" && os.estado !== "CANCELADA" && (
              <ConfirmDialog
                trigger={<Button variant="outline" className="text-destructive">Cancelar OS</Button>}
                title="Cancelar esta OS?"
                description="A OS deixa de poder ser editada."
                destructive
                onConfirm={handleCancelar}
              />
            )}
          </div>
        }
      />

      <Tabs defaultValue="resumo" className="space-y-4">
        <TabsList>
          <TabsTrigger value="resumo">Resumo</TabsTrigger>
          <TabsTrigger value="diagnostico">Diagnóstico</TabsTrigger>
          <TabsTrigger value="reparacao">Reparação</TabsTrigger>
          <TabsTrigger value="pagamento">Pagamento</TabsTrigger>
          <TabsTrigger value="historico-trotinete">Histórico da trotinete</TabsTrigger>
        </TabsList>

        <TabsContent value="resumo">
          <ResumoTab
            os={os} cliente={cliente} trotinete={trotinete}
            mecanicos={mecanicos}
            canPegar={canPegar}
            bloqueadoOutroMec={bloqueadoOutroMec}
            onPegar={handlePegar}
          />
        </TabsContent>

        <TabsContent value="diagnostico">
          <DiagnosticoTab
            os={os} reparacoes={reparacoes} pecas={pecas} fornecedores={fornecedores}
            canEdit={canMecOps && (os.estado === "EM_DIAGNOSTICO" || os.estado === "REGISTADA")}
            canApprove={canSecOps && os.estado === "AGUARDA_APROVACAO"}
            onChanged={reload}
          />
        </TabsContent>

        <TabsContent value="reparacao">
          <ReparacaoTab
            os={os} reparacoes={reparacoes} pecas={pecas} fornecedores={fornecedores}
            canEdit={canMecOps && (os.estado === "APROVADA" || os.estado === "EM_REPARACAO" || os.estado === "AGUARDA_PECAS")}
            onChanged={reload}
          />
        </TabsContent>

        <TabsContent value="pagamento">
          <PagamentoTab
            os={os} reparacoes={reparacoes} pecas={pecas}
            canSec={canSecOps}
            onChanged={reload}
          />
        </TabsContent>

        <TabsContent value="historico-trotinete">
          <HistoricoTrotineteTab os={os} trotinete={trotinete} />
        </TabsContent>
      </Tabs>
    </div>
  );
}

// ---------- Resumo ----------
function ResumoTab({
  os, cliente, trotinete, mecanicos, canPegar, bloqueadoOutroMec, onPegar,
}: {
  os: OS; cliente: Cliente | null; trotinete: Trotinete | null;
  mecanicos: Funcionario[];
  canPegar: boolean;
  bloqueadoOutroMec: boolean;
  onPegar: () => void;
}) {
  const mecanico = mecanicos.find((m) => m.id === os.mecanicoId);

  return (
    <div className="grid gap-4 md:grid-cols-2">
      <Card>
        <CardHeader><CardTitle className="text-base">Cliente & Trotinete</CardTitle></CardHeader>
        <CardContent className="space-y-2 text-sm">
          <Row label="Cliente" value={cliente?.nome ?? "—"} />
          <Row label="NIF" value={cliente?.nif ?? "—"} />
          <Row label="Telemóvel" value={cliente?.telemovel ?? "—"} />
          <Row label="Trotinete" value={trotinete ? `${trotinete.marca} ${trotinete.modelo}` : "—"} />
          <Row label="Nº de série" value={trotinete?.numeroSerie ?? "—"} />
          <Row label="Motor" value={trotinete ? trotinete.motor : "—"} />
        </CardContent>
      </Card>

      <Card>
        <CardHeader><CardTitle className="text-base">Detalhes da OS</CardTitle></CardHeader>
        <CardContent className="space-y-3 text-sm">
          <div>
            <div className="text-xs font-medium text-muted-foreground">Problema reportado</div>
            <p className="mt-1 whitespace-pre-wrap">{os.descricaoProblema}</p>
          </div>
          <div>
            <div className="text-xs font-medium text-muted-foreground">Acessórios</div>
            <div className="mt-1 flex flex-wrap gap-1">
              {os.acessorios.length === 0 && <span className="text-muted-foreground">Nenhum</span>}
              {os.acessorios.map((a, i) => <Badge key={i} variant="secondary">{a}</Badge>)}
            </div>
          </div>
          <div>
            <div className="text-xs font-medium text-muted-foreground">Mecânico responsável</div>
            <div className="mt-1">
              {mecanico ? (
                <div className="flex items-center gap-2">
                  <span className="font-medium">{mecanico.nome}</span>
                  {bloqueadoOutroMec && (
                    <Badge variant="outline" className="text-xs">
                      Atribuída a outro mecânico
                    </Badge>
                  )}
                </div>
              ) : canPegar ? (
                <div className="space-y-2">
                  <p className="text-xs text-muted-foreground">
                    Esta OS está disponível. Ao "pegar", ficas responsável pelo diagnóstico e reparação.
                  </p>
                  <Button onClick={onPegar}>
                    <ShieldCheck className="h-4 w-4" /> Pegar nesta OS
                  </Button>
                </div>
              ) : (
                <span className="text-muted-foreground">
                  {os.estado === "REGISTADA"
                    ? "Aguarda que um mecânico pegue na OS"
                    : "Sem mecânico atribuído"}
                </span>
              )}
            </div>
          </div>
          {os.fotos.length > 0 && (
            <div>
              <div className="text-xs font-medium text-muted-foreground">Fotos</div>
              <div className="mt-1 grid grid-cols-3 gap-2">
                {os.fotos.map((src, i) => (
                  <a key={i} href={src} target="_blank" rel="noreferrer" className="block aspect-square overflow-hidden rounded border">
                    <img src={src} alt={`Foto ${i + 1}`} className="h-full w-full object-cover" />
                  </a>
                ))}
              </div>
            </div>
          )}
        </CardContent>
      </Card>
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

// ---------- Diagnóstico ----------
function DiagnosticoTab({
  os, reparacoes, pecas, fornecedores, canEdit, canApprove, onChanged,
}: {
  os: OS; reparacoes: Reparacao[]; pecas: Peca[]; fornecedores: Fornecedor[];
  canEdit: boolean; canApprove: boolean; onChanged: () => void;
}) {
  const [reps, setReps] = useState<ItemReparacaoOS[]>(os.reparacoes);
  const [pcs, setPcs] = useState<ItemPecaOS[]>(os.pecas);
  const [notas, setNotas] = useState(os.notasMecanico ?? "");

  useEffect(() => { setReps(os.reparacoes); setPcs(os.pecas); setNotas(os.notasMecanico ?? ""); }, [os]);

  const orcamento = useMemo(() => calcOrcamento(reps, pcs), [reps, pcs]);

  const addReparacao = (rid: string) => {
    if (!rid || reps.some((r) => r.reparacaoId === rid)) return;
    setReps((p) => [...p, { reparacaoId: rid }]);
  };
  const addPeca = (pid: string) => {
    if (!pid || pcs.some((x) => x.pecaId === pid)) return;
    setPcs((p) => [...p, { pecaId: pid, quantidade: 1 }]);
  };
  const removeRep = (rid: string) => setReps((p) => p.filter((x) => x.reparacaoId !== rid));
  const removePeca = (pid: string) => setPcs((p) => p.filter((x) => x.pecaId !== pid));
  const setQtd = (pid: string, q: number) =>
    setPcs((p) => p.map((x) => x.pecaId === pid ? { ...x, quantidade: Math.max(1, q) } : x));

  const submit = async () => {
    if (reps.length === 0) {
      toast.error("Adiciona pelo menos uma reparação");
      return;
    }
    await osService.submeterDiagnostico(os.id, reps, pcs, notas.trim() || undefined);
    toast.success("Orçamento submetido — aguarda aprovação");
    onChanged();
  };

  const aprovar = async () => {
    await osService.aprovarOrcamento(os.id);
    toast.success("Orçamento aprovado");
    onChanged();
  };

  return (
    <div className="grid gap-4 lg:grid-cols-3">
      <Card className="lg:col-span-2">
        <CardHeader><CardTitle className="text-base">Reparações & Peças propostas</CardTitle></CardHeader>
        <CardContent className="space-y-4">
          <Section title="Reparações">
            {canEdit && (
              <AddSelect
                placeholder="Adicionar reparação…"
                options={reparacoes.map((r) => ({ value: r.id, label: `${r.nomenclatura} — ${formatEUR(r.preco)}` }))}
                onAdd={addReparacao}
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
                {reps.length === 0 && (
                  <TableRow><TableCell colSpan={3} className="text-center text-sm text-muted-foreground">Sem reparações</TableCell></TableRow>
                )}
                {reps.map((ir) => {
                  const r = reparacoes.find((x) => x.id === ir.reparacaoId);
                  return (
                    <TableRow key={ir.reparacaoId}>
                      <TableCell className="font-medium">{r?.nomenclatura ?? "—"}</TableCell>
                      <TableCell className="text-right">{r ? formatEUR(r.preco) : "—"}</TableCell>
                      {canEdit && (
                        <TableCell className="w-[1%]">
                          <Button variant="ghost" size="icon" onClick={() => removeRep(ir.reparacaoId)}>
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

          <Section title="Peças">
            {canEdit && (
              <AddPecaSelect
                pecas={pecas}
                fornecedores={fornecedores}
                excludeIds={pcs.map((x) => x.pecaId)}
                onAdd={addPeca}
              />
            )}
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
                {pcs.length === 0 && (
                  <TableRow><TableCell colSpan={4} className="text-center text-sm text-muted-foreground">Sem peças</TableCell></TableRow>
                )}
                {pcs.map((ip) => {
                  const p = pecas.find((x) => x.id === ip.pecaId);
                  return (
                    <TableRow key={ip.pecaId}>
                      <TableCell className="font-medium">{p?.nome ?? "—"}</TableCell>
                      <TableCell>
                        {canEdit ? (
                          <Input
                            type="number" min={1} value={ip.quantidade}
                            onChange={(e) => setQtd(ip.pecaId, Number(e.target.value))}
                            className="h-8 w-20"
                          />
                        ) : ip.quantidade}
                      </TableCell>
                      <TableCell className="text-right">
                        {p ? formatEUR(p.precoVenda * ip.quantidade) : "—"}
                      </TableCell>
                      {canEdit && (
                        <TableCell className="w-[1%]">
                          <Button variant="ghost" size="icon" onClick={() => removePeca(ip.pecaId)}>
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

          {canEdit && (
            <div className="space-y-2">
              <Label>Notas do mecânico</Label>
              <Textarea rows={3} value={notas} onChange={(e) => setNotas(e.target.value)} />
            </div>
          )}
        </CardContent>
      </Card>

      <Card>
        <CardHeader><CardTitle className="text-base">Orçamento</CardTitle></CardHeader>
        <CardContent className="space-y-3 text-sm">
          <Row label="Mão de obra" value={formatEUR(orcamento.maoObra)} />
          <Row label="Peças" value={formatEUR(orcamento.pecas)} />
          <div className="flex justify-between border-t pt-2 text-base font-semibold">
            <span>Total</span><span>{formatEUR(orcamento.total)}</span>
          </div>
          {canEdit && (
            <Button className="w-full" onClick={submit}>Submeter para aprovação</Button>
          )}
          {canApprove && (
            <Button className="w-full" onClick={aprovar}>
              <CheckCircle2 className="h-4 w-4" /> Aprovar orçamento
            </Button>
          )}
          {!canEdit && !canApprove && (
            <p className="text-xs text-muted-foreground">
              Estado actual: <strong>{ESTADO_OS_LABELS[os.estado]}</strong>
            </p>
          )}
        </CardContent>
      </Card>
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

// ---------- Reparação ----------
function ReparacaoTab({
  os, reparacoes, pecas, fornecedores, canEdit, onChanged,
}: {
  os: OS; reparacoes: Reparacao[]; pecas: Peca[]; fornecedores: Fornecedor[];
  canEdit: boolean; onChanged: () => void;
}) {
  const { user } = useAuth();
  const [reps, setReps] = useState<ItemReparacaoOS[]>(os.reparacoes);
  const [pcs, setPcs] = useState<ItemPecaOS[]>(os.pecas);
  const [check, setCheck] = useState<ChecklistSeguranca>(os.checklist ?? CHECK_INIT);
  const [defeitoOpen, setDefeitoOpen] = useState(false);
  const [defPecaId, setDefPecaId] = useState("");
  const [defQtd, setDefQtd] = useState(1);
  const [defNumeroSerie, setDefNumeroSerie] = useState("");
  const [defMotivo, setDefMotivo] = useState("");

  // Extras (peças/reparações detectadas durante a reparação)
  const [extrasOpen, setExtrasOpen] = useState(false);
  const [extraReps, setExtraReps] = useState<ItemReparacaoOS[]>([]);
  const [extraPcs, setExtraPcs] = useState<ItemPecaOS[]>([]);
  const [extraNotas, setExtraNotas] = useState("");

  useEffect(() => {
    setReps(os.reparacoes);
    setPcs(os.pecas);
    setCheck(os.checklist ?? CHECK_INIT);
  }, [os]);

  const orcamentoExtra = useMemo(
    () => calcOrcamento(extraReps, extraPcs),
    [extraReps, extraPcs],
  );

  if (os.estado === "REGISTADA" || os.estado === "EM_DIAGNOSTICO" || os.estado === "AGUARDA_APROVACAO") {
    return (
      <Card><CardContent className="py-10 text-center text-sm text-muted-foreground">
        A reparação só pode ser executada após a aprovação do orçamento.
      </CardContent></Card>
    );
  }

  const todasFeitas = reps.every((r) => r.feita) && pcs.every((p) => p.usada);
  const todasChecks = Object.values(check).every(Boolean);

  const concluir = async () => {
    if (!todasFeitas) {
      toast.error("Marca todas as reparações e peças como executadas");
      return;
    }
    if (!todasChecks) {
      toast.error("Completa o checklist de segurança");
      return;
    }
    await osService.concluirReparacao(os.id, reps, pcs, check);
    toast.success("Reparação concluída — cliente será notificado");
    onChanged();
  };

  const iniciar = async () => {
    await osService.iniciarReparacao(os.id);
    toast.success("Reparação iniciada");
    onChanged();
  };

  const requisitarPecas = async () => {
    await osService.requisitarPecas(os.id);
    toast.success("Peças requisitadas — alerta enviado ao gestor de stock");
    onChanged();
  };

  const openDefeito = () => {
    setDefPecaId(pcs[0]?.pecaId ?? "");
    setDefQtd(1);
    setDefNumeroSerie("");
    setDefMotivo("");
    setDefeitoOpen(true);
  };

  const submeterDefeito = async () => {
    if (!user) return;
    if (!defPecaId) { toast.error("Escolhe a peça com defeito"); return; }
    const escolhida = pcs.find((x) => x.pecaId === defPecaId);
    if (!escolhida) { toast.error("Peça inválida"); return; }
    if (defQtd < 1 || defQtd > escolhida.quantidade) {
      toast.error(`Quantidade entre 1 e ${escolhida.quantidade}`); return;
    }
    if (defMotivo.trim().length < 3) { toast.error("Indica o motivo do defeito"); return; }
    try {
      await osService.reportarDefeito(os.id, {
        pecaId: defPecaId,
        quantidade: defQtd,
        motivo: defMotivo.trim(),
        numeroSerie: defNumeroSerie.trim() || undefined,
        reportadaPor: user.id,
      });
      toast.success("Peça defeituosa enviada para o gestor de stock");
      setDefeitoOpen(false);
      onChanged();
    } catch (e) {
      toast.error((e as Error).message);
    }
  };

  const openExtras = () => {
    setExtraReps([]);
    setExtraPcs([]);
    setExtraNotas("");
    setExtrasOpen(true);
  };
  const addExtraRep = (rid: string) => {
    if (!rid) return;
    if (reps.some((r) => r.reparacaoId === rid) || extraReps.some((r) => r.reparacaoId === rid)) return;
    setExtraReps((p) => [...p, { reparacaoId: rid }]);
  };
  const addExtraPeca = (pid: string) => {
    if (!pid) return;
    if (pcs.some((x) => x.pecaId === pid) || extraPcs.some((x) => x.pecaId === pid)) return;
    setExtraPcs((p) => [...p, { pecaId: pid, quantidade: 1 }]);
  };
  const removeExtraRep = (rid: string) =>
    setExtraReps((p) => p.filter((x) => x.reparacaoId !== rid));
  const removeExtraPeca = (pid: string) =>
    setExtraPcs((p) => p.filter((x) => x.pecaId !== pid));
  const setExtraQtd = (pid: string, q: number) =>
    setExtraPcs((p) => p.map((x) => x.pecaId === pid ? { ...x, quantidade: Math.max(1, q) } : x));

  const submeterExtras = async () => {
    if (extraReps.length === 0 && extraPcs.length === 0) {
      toast.error("Adiciona pelo menos uma reparação ou peça extra");
      return;
    }
    const novasReps = [...reps, ...extraReps];
    const novasPcs = [...pcs, ...extraPcs];
    const notasFinais = [os.notasMecanico, extraNotas.trim()].filter(Boolean).join("\n---\n");
    try {
      await osService.adicionarExtras(os.id, novasReps, novasPcs, notasFinais || undefined);
      toast.success("Orçamento revisto submetido — aguarda nova aprovação");
      setExtrasOpen(false);
      onChanged();
    } catch (e) {
      toast.error((e as Error).message);
    }
  };

  return (
    <div className="grid gap-4 lg:grid-cols-3">
      <Card className="lg:col-span-2">
        <CardHeader><CardTitle className="text-base">Execução</CardTitle></CardHeader>
        <CardContent className="space-y-5">
          <Section title="Reparações">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead className="w-12">Feita</TableHead>
                  <TableHead>Nomenclatura</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {reps.map((ir) => {
                  const r = reparacoes.find((x) => x.id === ir.reparacaoId);
                  return (
                    <TableRow key={ir.reparacaoId}>
                      <TableCell>
                        <Checkbox
                          checked={!!ir.feita}
                          disabled={!canEdit}
                          onCheckedChange={(v) =>
                            setReps((p) => p.map((x) =>
                              x.reparacaoId === ir.reparacaoId ? { ...x, feita: !!v } : x))}
                        />
                      </TableCell>
                      <TableCell>{r?.nomenclatura ?? "—"}</TableCell>
                    </TableRow>
                  );
                })}
              </TableBody>
            </Table>
          </Section>

          <Section title="Peças usadas">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead className="w-12">Usada</TableHead>
                  <TableHead>Peça</TableHead>
                  <TableHead>Qtd</TableHead>
                  <TableHead>Stock</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {pcs.map((ip) => {
                  const p = pecas.find((x) => x.id === ip.pecaId);
                  const semStock = p ? p.stockAtual < ip.quantidade : false;
                  return (
                    <TableRow key={ip.pecaId}>
                      <TableCell>
                        <Checkbox
                          checked={!!ip.usada}
                          disabled={!canEdit || semStock}
                          onCheckedChange={(v) =>
                            setPcs((arr) => arr.map((x) =>
                              x.pecaId === ip.pecaId ? { ...x, usada: !!v } : x))}
                        />
                      </TableCell>
                      <TableCell>{p?.nome ?? "—"}</TableCell>
                      <TableCell>{ip.quantidade}</TableCell>
                      <TableCell>
                        {p ? (
                          <Badge variant={semStock ? "destructive" : "secondary"}>
                            {p.stockAtual}
                          </Badge>
                        ) : "—"}
                      </TableCell>
                    </TableRow>
                  );
                })}
              </TableBody>
            </Table>
          </Section>

          <Section title="Checklist de segurança">
            <div className="grid grid-cols-2 gap-2 sm:grid-cols-3">
              {(Object.keys(CHECK_LABELS) as (keyof ChecklistSeguranca)[]).map((k) => (
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
        <CardHeader><CardTitle className="text-base">Ações</CardTitle></CardHeader>
        <CardContent className="space-y-3">
          {os.estado === "APROVADA" && canEdit && (
            <Button className="w-full" onClick={iniciar}>
              <ShieldCheck className="h-4 w-4" /> Iniciar reparação
            </Button>
          )}
          {(os.estado === "EM_REPARACAO" || os.estado === "AGUARDA_PECAS") && canEdit && (
            <>
              <Button className="w-full" disabled={!todasFeitas || !todasChecks} onClick={concluir}>
                Marcar reparação concluída
              </Button>
              {os.estado === "EM_REPARACAO" && (
                <Button variant="outline" className="w-full" onClick={requisitarPecas}>
                  Requisitar peças em falta
                </Button>
              )}
              <Button
                variant="outline"
                className="w-full"
                onClick={openExtras}
              >
                <Plus className="h-4 w-4" /> Adicionar peças/reparações
              </Button>
              <Button
                variant="outline"
                className="w-full text-destructive"
                disabled={pcs.length === 0}
                onClick={openDefeito}
              >
                <AlertTriangle className="h-4 w-4" /> Reportar peça com defeito
              </Button>
            </>
          )}
          {os.defeitoReportado && (
            <div className="rounded border border-destructive/30 bg-destructive/5 p-2 text-xs whitespace-pre-wrap">
              <strong>Defeitos reportados:</strong>
              {"\n"}{os.defeitoReportado}
            </div>
          )}
          {!canEdit && (
            <p className="text-xs text-muted-foreground">
              Apenas o mecânico atribuído ou o gerente podem editar a reparação.
            </p>
          )}
        </CardContent>
      </Card>

      <Dialog open={defeitoOpen} onOpenChange={setDefeitoOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Reportar peça com defeito</DialogTitle>
          </DialogHeader>
          <div className="space-y-3">
            <p className="text-xs text-muted-foreground">
              A peça será removida do stock e enviada para o gestor de stock processar a devolução ao fornecedor.
            </p>
            <div className="space-y-1">
              <Label className="text-xs">Peça com defeito</Label>
              <Select value={defPecaId} onValueChange={setDefPecaId}>
                <SelectTrigger><SelectValue placeholder="Escolher peça…" /></SelectTrigger>
                <SelectContent>
                  {pcs.map((ip) => {
                    const p = pecas.find((x) => x.id === ip.pecaId);
                    return (
                      <SelectItem key={ip.pecaId} value={ip.pecaId}>
                        {p ? `${p.referencia} — ${p.nome}` : ip.pecaId} (na OS: {ip.quantidade})
                      </SelectItem>
                    );
                  })}
                </SelectContent>
              </Select>
            </div>
            <div className="grid gap-3 sm:grid-cols-2">
              <div className="space-y-1">
                <Label className="text-xs">Quantidade defeituosa</Label>
                <Input type="number" min={1}
                  value={defQtd}
                  onChange={(e) => setDefQtd(Math.max(1, Number(e.target.value)))} />
              </div>
              <div className="space-y-1">
                <Label className="text-xs">Nº de série (se aplicável)</Label>
                <Input value={defNumeroSerie} onChange={(e) => setDefNumeroSerie(e.target.value)} placeholder="Opcional" />
              </div>
            </div>
            <div className="space-y-1">
              <Label className="text-xs">Motivo / descrição do defeito</Label>
              <Textarea rows={3} value={defMotivo} onChange={(e) => setDefMotivo(e.target.value)} />
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setDefeitoOpen(false)}>Cancelar</Button>
            <Button onClick={submeterDefeito}>Reportar defeito</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <Dialog open={extrasOpen} onOpenChange={setExtrasOpen}>
        <DialogContent className="max-w-2xl">
          <DialogHeader>
            <DialogTitle>Adicionar peças / reparações extra</DialogTitle>
          </DialogHeader>
          <div className="space-y-4">
            <p className="text-xs text-muted-foreground">
              Itens detectados durante a reparação que não estavam no diagnóstico inicial.
              Ao submeter, a OS volta ao estado <strong>Aguarda aprovação</strong> para o cliente
              aprovar o orçamento revisto.
            </p>

            <Section title="Reparações extra">
              <AddSelect
                placeholder="Adicionar reparação…"
                options={reparacoes
                  .filter((r) => !reps.some((x) => x.reparacaoId === r.id))
                  .map((r) => ({ value: r.id, label: `${r.nomenclatura} — ${formatEUR(r.preco)}` }))}
                onAdd={addExtraRep}
              />
              {extraReps.length > 0 && (
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>Nomenclatura</TableHead>
                      <TableHead className="text-right">Preço</TableHead>
                      <TableHead />
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {extraReps.map((ir) => {
                      const r = reparacoes.find((x) => x.id === ir.reparacaoId);
                      return (
                        <TableRow key={ir.reparacaoId}>
                          <TableCell className="font-medium">{r?.nomenclatura ?? "—"}</TableCell>
                          <TableCell className="text-right">{r ? formatEUR(r.preco) : "—"}</TableCell>
                          <TableCell className="w-[1%]">
                            <Button variant="ghost" size="icon" onClick={() => removeExtraRep(ir.reparacaoId)}>
                              <Trash2 className="h-4 w-4 text-destructive" />
                            </Button>
                          </TableCell>
                        </TableRow>
                      );
                    })}
                  </TableBody>
                </Table>
              )}
            </Section>

            <Section title="Peças extra">
              <AddPecaSelect
                pecas={pecas}
                fornecedores={fornecedores}
                excludeIds={[...pcs.map((x) => x.pecaId), ...extraPcs.map((x) => x.pecaId)]}
                onAdd={addExtraPeca}
              />
              {extraPcs.length > 0 && (
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>Peça</TableHead>
                      <TableHead className="w-24">Qtd</TableHead>
                      <TableHead className="text-right">Subtotal</TableHead>
                      <TableHead />
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {extraPcs.map((ip) => {
                      const p = pecas.find((x) => x.id === ip.pecaId);
                      return (
                        <TableRow key={ip.pecaId}>
                          <TableCell className="font-medium">{p?.nome ?? "—"}</TableCell>
                          <TableCell>
                            <Input
                              type="number" min={1} value={ip.quantidade}
                              onChange={(e) => setExtraQtd(ip.pecaId, Number(e.target.value))}
                              className="h-8 w-20"
                            />
                          </TableCell>
                          <TableCell className="text-right">
                            {p ? formatEUR(p.precoVenda * ip.quantidade) : "—"}
                          </TableCell>
                          <TableCell className="w-[1%]">
                            <Button variant="ghost" size="icon" onClick={() => removeExtraPeca(ip.pecaId)}>
                              <Trash2 className="h-4 w-4 text-destructive" />
                            </Button>
                          </TableCell>
                        </TableRow>
                      );
                    })}
                  </TableBody>
                </Table>
              )}
            </Section>

            <div className="space-y-1">
              <Label className="text-xs">Notas adicionais (opcional)</Label>
              <Textarea rows={2} value={extraNotas} onChange={(e) => setExtraNotas(e.target.value)}
                placeholder="Justificação para o cliente…" />
            </div>

            <div className="rounded-md border bg-muted/30 p-3 text-sm">
              <div className="flex justify-between">
                <span className="text-muted-foreground">Mão de obra extra</span>
                <span>{formatEUR(orcamentoExtra.maoObra)}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-muted-foreground">Peças extra</span>
                <span>{formatEUR(orcamentoExtra.pecas)}</span>
              </div>
              <div className="mt-1 flex justify-between border-t pt-1 font-semibold">
                <span>Total extra</span>
                <span>{formatEUR(orcamentoExtra.total)}</span>
              </div>
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setExtrasOpen(false)}>Cancelar</Button>
            <Button onClick={submeterExtras}>Submeter para nova aprovação</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
}

// ---------- Pagamento ----------
function PagamentoTab({
  os, reparacoes, pecas, canSec, onChanged,
}: {
  os: OS; reparacoes: Reparacao[]; pecas: Peca[];
  canSec: boolean; onChanged: () => void;
}) {
  const orc = useMemo(() => calcOrcamento(os.reparacoes, os.pecas), [os.reparacoes, os.pecas]);
  const [metodo, setMetodo] = useState<MetodoPagamento>("MULTIBANCO");
  const [atraso, setAtraso] = useState<OS[]>([]);

  useEffect(() => {
    osService.pagamentosEmAtraso(os.clienteId, os.id).then(setAtraso);
  }, [os.clienteId, os.id, os.estado]);

  const notificarLevantamento = async () => {
    await osService.marcarAguardaPagamento(os.id);
    toast.success("Cliente notificado para levantamento");
    onChanged();
  };

  const registar = async () => {
    try {
      await osService.registarPagamento(os.id, metodo);
      toast.success("Pagamento registado");
      onChanged();
    } catch (e) {
      toast.error((e as Error).message);
    }
  };

  const bloqueado = atraso.length > 0;

  return (
    <Card>
      <CardHeader><CardTitle className="text-base">Pagamento</CardTitle></CardHeader>
      <CardContent className="space-y-4 text-sm">
        <div className="grid gap-2 sm:grid-cols-3">
          <Stat label="Mão de obra" value={formatEUR(orc.maoObra)} />
          <Stat label="Peças" value={formatEUR(orc.pecas)} />
          <Stat label="Total" value={formatEUR(orc.total)} highlight />
        </div>

        {bloqueado && os.estado === "AGUARDA_PAGAMENTO" && (
          <div className="rounded-md border border-destructive/30 bg-destructive/5 p-3">
            <div className="flex items-start gap-2">
              <AlertTriangle className="mt-0.5 h-4 w-4 text-destructive" />
              <div className="space-y-1">
                <div className="text-sm font-medium text-destructive">
                  Cliente tem pagamentos em atraso
                </div>
                <p className="text-xs text-muted-foreground">
                  Antes de cobrar esta OS, é necessário liquidar:
                </p>
                <ul className="text-xs">
                  {atraso.map((o) => (
                    <li key={o.id}>
                      <Link to={`/os/${o.id}`} className="font-medium text-primary hover:underline">
                        {o.numero}
                      </Link>{" "}
                      — criada em {formatDateTime(o.criadaEm)}
                    </li>
                  ))}
                </ul>
              </div>
            </div>
          </div>
        )}

        {os.estado === "CONCLUIDA" && canSec && (
          <Button onClick={notificarLevantamento}>Cliente notificado para levantamento</Button>
        )}

        {os.estado === "AGUARDA_PAGAMENTO" && canSec && (
          <div className="flex flex-wrap items-end gap-3">
            <div className="space-y-1">
              <Label>Método</Label>
              <Select value={metodo} onValueChange={(v) => setMetodo(v as MetodoPagamento)}>
                <SelectTrigger className="w-[180px]"><SelectValue /></SelectTrigger>
                <SelectContent>
                  <SelectItem value="NUMERARIO">Numerário</SelectItem>
                  <SelectItem value="MULTIBANCO">Multibanco</SelectItem>
                  <SelectItem value="MBWAY">MB Way</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <Button onClick={registar} disabled={bloqueado}>
              <CheckCircle2 className="h-4 w-4" /> Registar pagamento
            </Button>
          </div>
        )}

        {os.estado === "PAGA" && (
          <div className="rounded border border-success/30 bg-success-soft p-3 text-success">
            <strong>OS paga</strong> via {os.metodoPagamento ?? "—"}.
          </div>
        )}

        {os.estado !== "CONCLUIDA" && os.estado !== "AGUARDA_PAGAMENTO" && os.estado !== "PAGA" && (
          <p className="text-xs text-muted-foreground">
            O pagamento estará disponível quando a reparação for concluída.
          </p>
        )}
      </CardContent>
    </Card>
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

// ---------- Histórico da trotinete (apenas consulta) ----------
function HistoricoTrotineteTab({ os, trotinete }: { os: OS; trotinete: Trotinete | null }) {
  const [historico, setHistorico] = useState<OS[]>([]);

  useEffect(() => {
    osService.listByTrotinete(os.trotineteId, os.id).then(setHistorico);
  }, [os.trotineteId, os.id]);

  return (
    <Card>
      <CardHeader>
        <CardTitle className="text-base">
          Histórico da trotinete
          {trotinete && (
            <span className="ml-2 text-sm font-normal text-muted-foreground">
              {trotinete.marca} {trotinete.modelo} · {trotinete.numeroSerie}
            </span>
          )}
        </CardTitle>
      </CardHeader>
      <CardContent>
        {historico.length === 0 ? (
          <p className="py-6 text-center text-sm text-muted-foreground">
            Esta trotinete não tem outras ordens de serviço registadas.
          </p>
        ) : (
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Nº OS</TableHead>
                <TableHead>Data</TableHead>
                <TableHead>Problema</TableHead>
                <TableHead>Estado</TableHead>
                <TableHead className="text-right">Detalhe</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {historico.map((o) => (
                <TableRow key={o.id}>
                  <TableCell className="font-medium">{o.numero}</TableCell>
                  <TableCell className="text-xs text-muted-foreground">
                    {formatDateTime(o.criadaEm)}
                  </TableCell>
                  <TableCell className="max-w-[280px] truncate text-sm" title={o.descricaoProblema}>
                    {o.descricaoProblema}
                  </TableCell>
                  <TableCell><StateBadge state={o.estado} /></TableCell>
                  <TableCell className="text-right">
                    <Button variant="ghost" size="sm" asChild>
                      <Link to={`/os/${o.id}`}>Consultar</Link>
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        )}
        <p className="mt-3 text-xs text-muted-foreground">
          Apenas consulta — para alterar uma OS antiga abre-a directamente.
        </p>
      </CardContent>
    </Card>
  );
}

// ---------- Adicionar peça com filtros (catálogo)
function AddPecaSelect({
  pecas, fornecedores, excludeIds, onAdd,
}: {
  pecas: Peca[];
  fornecedores: Fornecedor[];
  excludeIds: string[];
  onAdd: (id: string) => void;
}) {
  const [fornecedorId, setFornecedorId] = useState<string>("ALL");
  const [referencia, setReferencia] = useState<string>("");
  const [pecaId, setPecaId] = useState<string>("");

  const fornecedorNome = (id: string) => fornecedores.find((f) => f.id === id)?.nome ?? "—";

  const filtradas = useMemo(() => pecas.filter((p) => {
    if (excludeIds.includes(p.id)) return false;
    if (fornecedorId !== "ALL" && p.fornecedorId !== fornecedorId) return false;
    if (referencia.trim() &&
        !p.referencia.toLowerCase().includes(referencia.toLowerCase()) &&
        !p.nome.toLowerCase().includes(referencia.toLowerCase())) return false;
    return true;
  }), [pecas, fornecedorId, referencia, excludeIds]);

  useEffect(() => {
    if (pecaId && !filtradas.some((p) => p.id === pecaId)) setPecaId("");
  }, [filtradas, pecaId]);

  return (
    <div className="space-y-2 rounded-md border bg-muted/30 p-2">
      <div className="grid gap-2 sm:grid-cols-2">
        <div className="space-y-1">
          <Label className="text-xs">Filtrar por fornecedor</Label>
          <Select value={fornecedorId} onValueChange={setFornecedorId}>
            <SelectTrigger className="h-8"><SelectValue /></SelectTrigger>
            <SelectContent>
              <SelectItem value="ALL">Todos</SelectItem>
              {fornecedores.map((f) => (
                <SelectItem key={f.id} value={f.id}>{f.nome}</SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
        <div className="space-y-1">
          <Label className="text-xs">Referência ou nome</Label>
          <Input value={referencia} onChange={(e) => setReferencia(e.target.value)}
            placeholder="Ex: BAT-36V" className="h-8" />
        </div>
      </div>
      <div className="flex gap-2">
        <Select value={pecaId} onValueChange={setPecaId}>
          <SelectTrigger className="flex-1">
            <SelectValue placeholder={`Adicionar peça… (${filtradas.length} disponíveis)`} />
          </SelectTrigger>
          <SelectContent>
            {filtradas.map((p) => (
              <SelectItem key={p.id} value={p.id}>
                {p.referencia} — {p.nome} · {fornecedorNome(p.fornecedorId)} · {formatEUR(p.precoVenda)} · stock: {p.stockAtual}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
        <Button type="button" disabled={!pecaId} onClick={() => { onAdd(pecaId); setPecaId(""); }}>
          <Plus className="h-4 w-4" />
        </Button>
      </div>
    </div>
  );
}
