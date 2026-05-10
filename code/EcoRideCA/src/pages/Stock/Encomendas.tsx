import { useEffect, useMemo, useState } from "react";
import { Plus, Trash2, Sparkles, Send, PackageCheck } from "lucide-react";
import { toast } from "sonner";

import { PageHeader } from "@/components/layout/PageHeader";
import { StockTabs } from "@/components/layout/StockTabs";
import { DataTable, type Column } from "@/components/data-table";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { StateBadge } from "@/components/state-badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle,
} from "@/components/ui/dialog";
import {
  Select, SelectContent, SelectItem, SelectTrigger, SelectValue,
} from "@/components/ui/select";
import {
  Table, TableBody, TableCell, TableHead, TableHeader, TableRow,
} from "@/components/ui/table";

import { encomendasService, type SugestaoEncomenda } from "@/services/stock";
import { pecasService, fornecedoresService } from "@/services/entities";
import { useAuth } from "@/context/AuthContext";
import { formatDateTime, formatEUR } from "@/lib/format";
import type { Encomenda, Peca, Fornecedor, ItemEncomenda, UnidadeSerie } from "@/lib/types";

export default function StockEncomendas() {
  const { user, role } = useAuth();
  const [encomendas, setEncomendas] = useState<Encomenda[]>([]);
  const [pecas, setPecas] = useState<Peca[]>([]);
  const [fornecedores, setFornecedores] = useState<Fornecedor[]>([]);
  const [openNew, setOpenNew] = useState(false);
  const [openDetail, setOpenDetail] = useState<Encomenda | null>(null);
  const [openSugestoes, setOpenSugestoes] = useState(false);
  const [openReceber, setOpenReceber] = useState<Encomenda | null>(null);
  const [sugestoes, setSugestoes] = useState<SugestaoEncomenda[]>([]);

  const reload = async () => {
    const [e, p, f] = await Promise.all([
      encomendasService.list(), pecasService.list(), fornecedoresService.list(),
    ]);
    setEncomendas(e); setPecas(p); setFornecedores(f);
  };
  useEffect(() => { reload(); }, []);

  const fornecedorNome = (id: string) => fornecedores.find((f) => f.id === id)?.nome ?? "—";
  const pecaById = (id: string) => pecas.find((x) => x.id === id);

  const canEdit = role === "GERENTE" || role === "GESTOR_STOCK";

  const totalEnc = (e: Encomenda) =>
    e.itens.reduce((s, it) => s + it.quantidade * it.precoUnitario, 0);

  const handleGerar = async () => {
    const s = await encomendasService.gerarSugestoes();
    setSugestoes(s);
    setOpenSugestoes(true);
    if (s.length === 0) toast.info("Sem peças abaixo do stock mínimo");
  };

  const handleEstado = async (
    e: Encomenda,
    estado: Encomenda["estado"],
    series?: Record<string, UnidadeSerie[]>,
  ) => {
    try {
      await encomendasService.setEstado(e.id, estado, series);
      toast.success(
        estado === "ENVIADA" ? "Encomenda marcada como enviada" :
        estado === "RECEBIDA" ? "Encomenda recebida — stock atualizado" :
        "Encomenda atualizada",
      );
      reload();
      setOpenDetail(null);
      setOpenReceber(null);
    } catch (err) {
      toast.error((err as Error).message);
    }
  };

  const tentarReceber = (e: Encomenda) => {
    const precisaSeries = e.itens.some((it) => it.precoUnitario > 70);
    if (precisaSeries) {
      setOpenReceber(e);
    } else {
      handleEstado(e, "RECEBIDA");
    }
  };

  const handleRemove = async (e: Encomenda) => {
    try {
      await encomendasService.remove(e.id);
      toast.success("Encomenda eliminada");
      reload();
    } catch (err) {
      toast.error((err as Error).message);
    }
  };

  const columns: Column<Encomenda>[] = [
    { key: "data", header: "Criada", cell: (e) => <span className="text-xs">{formatDateTime(e.criadaEm)}</span> },
    { key: "fornecedor", header: "Fornecedor", cell: (e) => <span className="font-medium">{fornecedorNome(e.fornecedorId)}</span> },
    { key: "itens", header: "Itens", cell: (e) => `${e.itens.length} · ${e.itens.reduce((s, i) => s + i.quantidade, 0)} unid.` },
    { key: "total", header: "Total", cell: (e) => formatEUR(totalEnc(e)) },
    { key: "estado", header: "Estado", cell: (e) => <StateBadge state={e.estado} /> },
  ];

  return (
    <div>
      <PageHeader
        title="Stock"
        description="Catálogo de peças, entradas, devoluções e encomendas"
        actions={
          canEdit && (
            <div className="flex flex-wrap gap-2">
              <Button variant="outline" onClick={handleGerar}>
                <Sparkles className="h-4 w-4" /> Gerar lista automática
              </Button>
              <Button onClick={() => setOpenNew(true)}>
                <Plus className="h-4 w-4" /> Nova encomenda
              </Button>
            </div>
          )
        }
      />
      <StockTabs />

      <DataTable
        data={encomendas}
        columns={columns}
        emptyMessage="Sem encomendas"
        searchPlaceholder="Pesquisar…"
        rowActions={(e) => (
          <>
            <Button variant="ghost" size="sm" onClick={() => setOpenDetail(e)}>Ver</Button>
            {canEdit && e.estado === "RASCUNHO" && (
              <ConfirmDialog
                trigger={<Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
                title="Eliminar encomenda?"
                description="A encomenda em rascunho será removida."
                destructive
                onConfirm={() => handleRemove(e)}
              />
            )}
          </>
        )}
      />

      {/* Detalhe */}
      <Dialog open={!!openDetail} onOpenChange={(v) => !v && setOpenDetail(null)}>
        <DialogContent className="max-w-3xl">
          <DialogHeader>
            <DialogTitle className="flex items-center gap-2">
              Encomenda · {openDetail && fornecedorNome(openDetail.fornecedorId)}
              {openDetail && <StateBadge state={openDetail.estado} />}
            </DialogTitle>
          </DialogHeader>
          {openDetail && (
            <>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Peça</TableHead>
                    <TableHead className="w-20">Qtd</TableHead>
                    <TableHead className="w-32">Preço unit.</TableHead>
                    <TableHead className="w-32 text-right">Subtotal</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {openDetail.itens.map((it) => {
                    const p = pecaById(it.pecaId);
                    return (
                      <TableRow key={it.pecaId}>
                        <TableCell className="font-medium">
                          {p ? `${p.referencia} — ${p.nome}` : it.pecaId}
                        </TableCell>
                        <TableCell>{it.quantidade}</TableCell>
                        <TableCell>{formatEUR(it.precoUnitario)}</TableCell>
                        <TableCell className="text-right">{formatEUR(it.quantidade * it.precoUnitario)}</TableCell>
                      </TableRow>
                    );
                  })}
                </TableBody>
              </Table>
              <div className="flex items-center justify-between border-t pt-3 text-sm">
                <span className="text-muted-foreground">
                  Criada em {formatDateTime(openDetail.criadaEm)}
                  {openDetail.enviadaEm && ` · Enviada em ${formatDateTime(openDetail.enviadaEm)}`}
                  {openDetail.recebidaEm && ` · Recebida em ${formatDateTime(openDetail.recebidaEm)}`}
                </span>
                <span className="font-semibold">Total: {formatEUR(totalEnc(openDetail))}</span>
              </div>
            </>
          )}
          <DialogFooter>
            {openDetail && canEdit && openDetail.estado === "RASCUNHO" && (
              <Button onClick={() => handleEstado(openDetail, "ENVIADA")}>
                <Send className="h-4 w-4" /> Marcar enviada
              </Button>
            )}
            {openDetail && canEdit && openDetail.estado === "ENVIADA" && (
              <Button onClick={() => tentarReceber(openDetail)}>
                <PackageCheck className="h-4 w-4" /> Marcar recebida
              </Button>
            )}
            <Button variant="outline" onClick={() => setOpenDetail(null)}>Fechar</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <ReceberEncomendaDialog
        encomenda={openReceber}
        pecas={pecas}
        onClose={() => setOpenReceber(null)}
        onConfirm={(series) => openReceber && handleEstado(openReceber, "RECEBIDA", series)}
      />

      <NovaEncomendaDialog
        open={openNew}
        onOpenChange={setOpenNew}
        pecas={pecas}
        fornecedores={fornecedores}
        userId={user?.id ?? ""}
        onSaved={() => { setOpenNew(false); reload(); }}
      />

      <SugestoesDialog
        open={openSugestoes}
        onOpenChange={setOpenSugestoes}
        sugestoes={sugestoes}
        pecas={pecas}
        fornecedores={fornecedores}
        userId={user?.id ?? ""}
        onCreated={() => { setOpenSugestoes(false); reload(); }}
      />
    </div>
  );
}

// ---------- Nova encomenda manual
function NovaEncomendaDialog({
  open, onOpenChange, pecas, fornecedores, userId, onSaved,
}: {
  open: boolean;
  onOpenChange: (v: boolean) => void;
  pecas: Peca[];
  fornecedores: Fornecedor[];
  userId: string;
  onSaved: () => void;
}) {
  const [fornecedorId, setFornecedorId] = useState("");
  const [itens, setItens] = useState<ItemEncomenda[]>([]);
  const [pecaSel, setPecaSel] = useState("");

  useEffect(() => {
    if (open) { setFornecedorId(""); setItens([]); setPecaSel(""); }
  }, [open]);

  const pecasFornecedor = useMemo(
    () => pecas.filter((p) => !fornecedorId || p.fornecedorId === fornecedorId),
    [pecas, fornecedorId],
  );

  const addItem = () => {
    const p = pecas.find((x) => x.id === pecaSel);
    if (!p || itens.some((i) => i.pecaId === p.id)) return;
    setItens((arr) => [...arr, { pecaId: p.id, quantidade: 1, precoUnitario: 0 }]);
    setPecaSel("");
  };

  const setItem = (pid: string, patch: Partial<ItemEncomenda>) =>
    setItens((arr) => arr.map((i) => i.pecaId === pid ? { ...i, ...patch } : i));

  const remItem = (pid: string) => setItens((arr) => arr.filter((i) => i.pecaId !== pid));

  const submit = async () => {
    if (!fornecedorId) { toast.error("Escolhe fornecedor"); return; }
    if (itens.length === 0) { toast.error("Adiciona pelo menos uma peça"); return; }
    try {
      await encomendasService.create({ fornecedorId, itens, criadaPor: userId });
      toast.success("Encomenda criada (rascunho)");
      onSaved();
    } catch (e) {
      toast.error((e as Error).message);
    }
  };

  const total = itens.reduce((s, i) => s + i.quantidade * i.precoUnitario, 0);

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-3xl">
        <DialogHeader><DialogTitle>Nova encomenda</DialogTitle></DialogHeader>
        <div className="space-y-3">
          <div className="space-y-1">
            <Label className="text-xs">Fornecedor</Label>
            <Select value={fornecedorId} onValueChange={(v) => { setFornecedorId(v); setItens([]); }}>
              <SelectTrigger><SelectValue placeholder="Escolher fornecedor…" /></SelectTrigger>
              <SelectContent>
                {fornecedores.map((f) => (
                  <SelectItem key={f.id} value={f.id}>{f.nome}</SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          {fornecedorId && (
            <div className="flex gap-2">
              <Select value={pecaSel} onValueChange={setPecaSel}>
                <SelectTrigger className="flex-1">
                  <SelectValue placeholder="Adicionar peça…" />
                </SelectTrigger>
                <SelectContent>
                  {pecasFornecedor
                    .filter((p) => !itens.some((i) => i.pecaId === p.id))
                    .map((p) => (
                      <SelectItem key={p.id} value={p.id}>
                        {p.referencia} — {p.nome}
                      </SelectItem>
                    ))}
                </SelectContent>
              </Select>
              <Button type="button" disabled={!pecaSel} onClick={addItem}>
                <Plus className="h-4 w-4" />
              </Button>
            </div>
          )}

          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Peça</TableHead>
                <TableHead className="w-24">Qtd</TableHead>
                <TableHead className="w-32">Preço unit.</TableHead>
                <TableHead className="w-32 text-right">Subtotal</TableHead>
                <TableHead />
              </TableRow>
            </TableHeader>
            <TableBody>
              {itens.length === 0 && (
                <TableRow><TableCell colSpan={5} className="text-center text-sm text-muted-foreground">Sem peças</TableCell></TableRow>
              )}
              {itens.map((it) => {
                const p = pecas.find((x) => x.id === it.pecaId);
                return (
                  <TableRow key={it.pecaId}>
                    <TableCell className="font-medium">{p?.nome ?? it.pecaId}</TableCell>
                    <TableCell>
                      <Input type="number" min={1} value={it.quantidade}
                        onChange={(e) => setItem(it.pecaId, { quantidade: Math.max(1, Number(e.target.value)) })}
                        className="h-8 w-20" />
                    </TableCell>
                    <TableCell>
                      <Input type="number" step="0.01" min={0} value={it.precoUnitario}
                        onChange={(e) => setItem(it.pecaId, { precoUnitario: Math.max(0, Number(e.target.value)) })}
                        className="h-8 w-28"
                        title="Pré-preenchido com o preço de compra. Alterar aqui não modifica o preço de catálogo." />
                    </TableCell>
                    <TableCell className="text-right">{formatEUR(it.quantidade * it.precoUnitario)}</TableCell>
                    <TableCell>
                      <Button variant="ghost" size="icon" onClick={() => remItem(it.pecaId)}>
                        <Trash2 className="h-4 w-4 text-destructive" />
                      </Button>
                    </TableCell>
                  </TableRow>
                );
              })}
            </TableBody>
          </Table>

          <div className="flex justify-end text-sm font-semibold">Total: {formatEUR(total)}</div>
        </div>
        <DialogFooter>
          <Button variant="outline" onClick={() => onOpenChange(false)}>Cancelar</Button>
          <Button onClick={submit}>Criar rascunho</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

// ---------- Sugestões automáticas
function SugestoesDialog({
  open, onOpenChange, sugestoes, pecas, fornecedores, userId, onCreated,
}: {
  open: boolean;
  onOpenChange: (v: boolean) => void;
  sugestoes: SugestaoEncomenda[];
  pecas: Peca[];
  fornecedores: Fornecedor[];
  userId: string;
  onCreated: () => void;
}) {
  const [editable, setEditable] = useState<SugestaoEncomenda[]>([]);

  useEffect(() => {
    if (open) setEditable(sugestoes.map((s) => ({ ...s, itens: s.itens.map((i) => ({ ...i })) })));
  }, [open, sugestoes]);

  const fornecedorNome = (id: string) => fornecedores.find((f) => f.id === id)?.nome ?? "—";
  const pecaNome = (id: string) => {
    const p = pecas.find((x) => x.id === id);
    return p ? `${p.referencia} — ${p.nome}` : id;
  };

  const setItem = (fid: string, pid: string, patch: Partial<ItemEncomenda>) =>
    setEditable((arr) => arr.map((s) =>
      s.fornecedorId === fid
        ? { ...s, itens: s.itens.map((i) => i.pecaId === pid ? { ...i, ...patch } : i) }
        : s,
    ));

  const remItem = (fid: string, pid: string) =>
    setEditable((arr) => arr.map((s) =>
      s.fornecedorId === fid
        ? { ...s, itens: s.itens.filter((i) => i.pecaId !== pid) }
        : s,
    ).filter((s) => s.itens.length > 0));

  const criarTodas = async () => {
    if (editable.length === 0) { toast.error("Sem encomendas para criar"); return; }
    try {
      for (const s of editable) {
        await encomendasService.create({
          fornecedorId: s.fornecedorId,
          itens: s.itens,
          criadaPor: userId,
        });
      }
      toast.success(`${editable.length} encomenda(s) criada(s) em rascunho`);
      onCreated();
    } catch (e) {
      toast.error((e as Error).message);
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-3xl">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <Sparkles className="h-4 w-4" /> Sugestões automáticas de encomenda
          </DialogTitle>
        </DialogHeader>
        {editable.length === 0 ? (
          <div className="py-10 text-center text-sm text-muted-foreground">
            Sem peças abaixo do stock mínimo.
          </div>
        ) : (
          <div className="space-y-4 max-h-[60vh] overflow-y-auto pr-1">
            {editable.map((s) => (
              <Card key={s.fornecedorId}>
                <CardHeader className="py-3">
                  <CardTitle className="text-sm">{fornecedorNome(s.fornecedorId)}</CardTitle>
                </CardHeader>
                <CardContent className="pt-0">
                  <Table>
                    <TableHeader>
                      <TableRow>
                        <TableHead>Peça</TableHead>
                        <TableHead className="w-20">Qtd</TableHead>
                        <TableHead className="w-28">Preço unit.</TableHead>
                        <TableHead />
                      </TableRow>
                    </TableHeader>
                    <TableBody>
                      {s.itens.map((it) => (
                        <TableRow key={it.pecaId}>
                          <TableCell>{pecaNome(it.pecaId)}</TableCell>
                          <TableCell>
                            <Input type="number" min={1} value={it.quantidade}
                              onChange={(e) => setItem(s.fornecedorId, it.pecaId, { quantidade: Math.max(1, Number(e.target.value)) })}
                              className="h-8 w-16" />
                          </TableCell>
                          <TableCell>
                            <Input type="number" step="0.01" min={0} value={it.precoUnitario}
                              onChange={(e) => setItem(s.fornecedorId, it.pecaId, { precoUnitario: Math.max(0, Number(e.target.value)) })}
                              className="h-8 w-24" />
                          </TableCell>
                          <TableCell>
                            <Button variant="ghost" size="icon" onClick={() => remItem(s.fornecedorId, it.pecaId)}>
                              <Trash2 className="h-4 w-4 text-destructive" />
                            </Button>
                          </TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </CardContent>
              </Card>
            ))}
          </div>
        )}
        <DialogFooter>
          <Button variant="outline" onClick={() => onOpenChange(false)}>Cancelar</Button>
          <Button onClick={criarTodas} disabled={editable.length === 0}>
            Criar {editable.length} rascunho{editable.length === 1 ? "" : "s"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

// ---------- Receber encomenda — captura nº de série para itens > 70€
function ReceberEncomendaDialog({
  encomenda, pecas, onClose, onConfirm,
}: {
  encomenda: Encomenda | null;
  pecas: Peca[];
  onClose: () => void;
  onConfirm: (series: Record<string, UnidadeSerie[]>) => void;
}) {
  const [series, setSeries] = useState<Record<string, UnidadeSerie[]>>({});

  useEffect(() => {
    if (!encomenda) return;
    const init: Record<string, UnidadeSerie[]> = {};
    encomenda.itens.forEach((it) => {
      if (it.precoUnitario > 70) {
        init[it.pecaId] = Array.from({ length: it.quantidade }, () => ({
          numeroSerie: "", garantiaMeses: 12,
        }));
      }
    });
    setSeries(init);
  }, [encomenda]);

  if (!encomenda) return null;

  const itensComSerie = encomenda.itens.filter((it) => it.precoUnitario > 70);

  const setUnidade = (pecaId: string, idx: number, patch: Partial<UnidadeSerie>) =>
    setSeries((cur) => ({
      ...cur,
      [pecaId]: cur[pecaId].map((u, i) => i === idx ? { ...u, ...patch } : u),
    }));

  const pecaLabel = (id: string) => {
    const p = pecas.find((x) => x.id === id);
    return p ? `${p.referencia} — ${p.nome}` : id;
  };

  const submit = () => {
    for (const it of itensComSerie) {
      const arr = series[it.pecaId] ?? [];
      if (arr.length !== it.quantidade) {
        toast.error(`Faltam nºs de série para ${pecaLabel(it.pecaId)}`);
        return;
      }
      const seen = new Set<string>();
      for (const u of arr) {
        if (!u.numeroSerie.trim()) {
          toast.error(`Nº de série em falta para ${pecaLabel(it.pecaId)}`);
          return;
        }
        if (seen.has(u.numeroSerie)) {
          toast.error(`Nº de série duplicado: ${u.numeroSerie}`);
          return;
        }
        seen.add(u.numeroSerie);
      }
    }
    onConfirm(series);
  };

  return (
    <Dialog open={!!encomenda} onOpenChange={(v) => !v && onClose()}>
      <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle>Receber encomenda — registar nºs de série</DialogTitle>
        </DialogHeader>
        <p className="text-xs text-muted-foreground">
          As peças abaixo custaram mais de 70€ por unidade. Antes de marcar como recebida,
          é necessário indicar o nº de série e a garantia para cada uma.
        </p>
        <div className="space-y-4">
          {itensComSerie.map((it) => (
            <Card key={it.pecaId}>
              <CardHeader className="py-3">
                <CardTitle className="text-sm">
                  {pecaLabel(it.pecaId)} — {it.quantidade} unidades · {formatEUR(it.precoUnitario)}/un.
                </CardTitle>
              </CardHeader>
              <CardContent className="pt-0">
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead className="w-12">#</TableHead>
                      <TableHead>Nº de série</TableHead>
                      <TableHead className="w-32">Garantia (meses)</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {(series[it.pecaId] ?? []).map((u, i) => (
                      <TableRow key={i}>
                        <TableCell>{i + 1}</TableCell>
                        <TableCell>
                          <Input value={u.numeroSerie}
                            onChange={(e) => setUnidade(it.pecaId, i, { numeroSerie: e.target.value })}
                            placeholder="Nº de série"
                            className="h-8" />
                        </TableCell>
                        <TableCell>
                          <Input type="number" min={0} max={120}
                            value={u.garantiaMeses}
                            onChange={(e) => setUnidade(it.pecaId, i, { garantiaMeses: Number(e.target.value) })}
                            className="h-8" />
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </CardContent>
            </Card>
          ))}
        </div>
        <DialogFooter>
          <Button variant="outline" onClick={onClose}>Cancelar</Button>
          <Button onClick={submit}>Confirmar recebida</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
