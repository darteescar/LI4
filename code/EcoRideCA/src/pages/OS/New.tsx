import { useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { ArrowLeft, ArrowRight, Check, X, Upload, Image as ImageIcon, Pencil, Trash2 } from "lucide-react";
import { toast } from "sonner";

import { PageHeader } from "@/components/layout/PageHeader";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Badge } from "@/components/ui/badge";
import { DataTable } from "@/components/data-table";

import { api } from "@/services/api";

const MAX_FOTO_BYTES = 5 * 1024 * 1024;
const FOTO_TYPES = ["image/png", "image/jpeg"];

interface Cliente { id: number; nome: string; NIF: string; telemovel: string; email: string; }
interface Trotinete { id: number; marca: string; modelo: string; num_serie: string; tipo_motor: string; cod_cliente: number; }
interface OrdemServico { id: number; }

export default function NewOS() {
  const navigate = useNavigate();

  const [step, setStep] = useState(1);
  const [clienteId, setClienteId] = useState<number | null>(null);
  const [trotineteId, setTrotineteId] = useState<number | null>(null);
  const [acessorios, setAcessorios] = useState<string[]>([]);
  const [novoAcessorio, setNovoAcessorio] = useState("");
  const [editIndex, setEditIndex] = useState<number | null>(null);
  const [fotos, setFotos] = useState<string[]>([]);
  const [descricao, setDescricao] = useState("");
  const [submitting, setSubmitting] = useState(false);

  const { data: clientes = [], isLoading: loadingClientes } = useQuery<Cliente[]>({
    queryKey: ["clientes"],
    queryFn: () => api.get<Cliente[]>("/clientes"),
  });

  const { data: trotinetes = [], isLoading: loadingTrotinetes } = useQuery<Trotinete[]>({
    queryKey: ["trotinetes"],
    queryFn: () => api.get<Trotinete[]>("/trotinetes"),
  });

  const trotinetesCliente = useMemo(
    () => trotinetes.filter((t) => t.cod_cliente === clienteId),
    [trotinetes, clienteId],
  );

  const addAcessorio = () => {
    const v = novoAcessorio.trim();
    if (!v) return;
    if (editIndex !== null) {
      setAcessorios((prev) => prev.map((a, i) => (i === editIndex ? v : a)));
      toast.success(`Acessório atualizado: ${v}`);
      setEditIndex(null);
    } else {
      setAcessorios((prev) => [...prev, v]);
      toast.success(`Acessório adicionado: ${v}`);
    }
    setNovoAcessorio("");
  };

  const removeAcessorio = (i: number) => {
    const removed = acessorios[i];
    setAcessorios((prev) => prev.filter((_, idx) => idx !== i));
    if (editIndex === i) { setEditIndex(null); setNovoAcessorio(""); }
    toast.message(`Acessório removido: ${removed}`);
  };

  const editAcessorio = (i: number) => { setEditIndex(i); setNovoAcessorio(acessorios[i]); };
  const cancelEdit = () => { setEditIndex(null); setNovoAcessorio(""); };

  const handleFiles = async (files: FileList | null) => {
    if (!files) return;
    const novos: string[] = [];
    for (const file of Array.from(files)) {
      if (!FOTO_TYPES.includes(file.type)) { toast.error(`${file.name}: formato inválido (apenas PNG/JPEG)`); continue; }
      if (file.size > MAX_FOTO_BYTES) { toast.error(`${file.name}: excede 5MB`); continue; }
      const dataUrl = await new Promise<string>((res, rej) => {
        const r = new FileReader();
        r.onload = () => res(r.result as string);
        r.onerror = rej;
        r.readAsDataURL(file);
      });
      novos.push(dataUrl);
    }
    setFotos((prev) => [...prev, ...novos]);
  };

  const removeFoto = (i: number) => setFotos((prev) => prev.filter((_, idx) => idx !== i));

  const canNext = () => {
    if (step === 1) return clienteId !== null;
    if (step === 2) return trotineteId !== null;
    if (step === 3) return true;
    if (step === 4) return descricao.trim().length >= 5;
    return false;
  };

  const handleSubmit = async () => {
    if (clienteId === null || trotineteId === null) return;
    if (descricao.trim().length < 5) {
      toast.error("Descrição do problema é obrigatória (mín. 5 caracteres)");
      return;
    }
    setSubmitting(true);
    try {
      const os = await api.post<OrdemServico>("/ordensservicos", {
        id_cliente: clienteId,
        id_trotinete: trotineteId,
        descricao: descricao.trim(),
        acessorios,
        fotografias: [],
      });
      toast.success(`OS-${os.id} criada`);
      navigate(`/os/${os.id}`);
    } catch (e) {
      toast.error(e instanceof Error ? e.message : "Erro ao criar OS");
    } finally {
      setSubmitting(false);
    }
  };

  const Steps = (
    <div className="mb-6 flex items-center gap-2 text-xs">
      {["Cliente", "Trotinete", "Acessórios e fotos", "Problema"].map((s, i) => {
        const n = i + 1;
        const active = n === step;
        const done = n < step;
        return (
          <div key={s} className="flex items-center gap-2">
            <div className={`flex h-6 w-6 items-center justify-center rounded-full text-[11px] font-semibold ${
              done ? "bg-success text-success-foreground"
                : active ? "bg-primary text-primary-foreground"
                : "bg-muted text-muted-foreground"
            }`}>
              {done ? <Check className="h-3 w-3" /> : n}
            </div>
            <span className={active ? "font-medium" : "text-muted-foreground"}>{s}</span>
            {n < 4 && <span className="text-muted-foreground">/</span>}
          </div>
        );
      })}
    </div>
  );

  return (
    <div>
      <PageHeader
        title="Nova Ordem de Serviço"
        description="Registo passo a passo: cliente, trotinete, acessórios e descrição do problema"
        actions={
          <Button variant="outline" onClick={() => navigate("/os")}>
            <ArrowLeft className="h-4 w-4" /> Voltar
          </Button>
        }
      />

      <Card className="shadow-sm">
        <CardContent className="p-6">
          {Steps}

          {step === 1 && (
            <div className="space-y-3">
              <Label>Cliente</Label>
              {loadingClientes ? (
                <p className="text-xs text-muted-foreground">A carregar…</p>
              ) : clientes.length === 0 ? (
                <p className="text-xs text-muted-foreground">
                  Não existem clientes. Cria um cliente em <strong>Clientes</strong> primeiro.
                </p>
              ) : (
                <DataTable<Cliente>
                  data={clientes}
                  onRowClick={(c) => { setClienteId(c.id); setTrotineteId(null); }}
                  isRowSelected={(c) => clienteId === c.id}
                  columns={[
                    { key: "nome", header: "Nome", cell: (c) => <span className="font-medium">{c.nome}</span> },
                    { key: "NIF", header: "NIF", cell: (c) => c.NIF },
                    { key: "telemovel", header: "Telemóvel", cell: (c) => c.telemovel },
                    { key: "email", header: "Email", cell: (c) => c.email },
                    { key: "selecionado", header: "", className: "w-[1%]", cell: (c) => (
                      clienteId === c.id
                        ? <Badge className="gap-1"><Check className="h-3 w-3" />Selecionado</Badge>
                        : null
                    ) },
                  ]}
                  searchKeys={["nome", "email", "NIF", "telemovel"]}
                  searchPlaceholder="Pesquisar por nome, email, NIF ou telemóvel"
                  searchClassName="max-w-md"
                />
              )}
            </div>
          )}

          {step === 2 && (
            <div className="space-y-3">
              <Label>Trotinete</Label>
              {loadingTrotinetes ? (
                <p className="text-xs text-muted-foreground">A carregar…</p>
              ) : trotinetesCliente.length === 0 ? (
                <p className="text-xs text-muted-foreground">
                  Este cliente ainda não tem trotinetes registadas. Adiciona em <strong>Trotinetes</strong>.
                </p>
              ) : (
                <DataTable<Trotinete>
                  data={trotinetesCliente}
                  onRowClick={(t) => setTrotineteId(t.id)}
                  isRowSelected={(t) => trotineteId === t.id}
                  columns={[
                    { key: "marcaModelo", header: "Marca / Modelo", cell: (t) => (
                      <div>
                        <div className="font-medium">{t.marca} {t.modelo}</div>
                        <div className="text-xs text-muted-foreground">{t.tipo_motor}</div>
                      </div>
                    ) },
                    { key: "num_serie", header: "Nº de série", cell: (t) => <code className="text-xs">{t.num_serie}</code> },
                    { key: "motor", header: "Motor", cell: (t) => t.tipo_motor },
                    { key: "selecionado", header: "", className: "w-[1%]", cell: (t) => (
                      trotineteId === t.id
                        ? <Badge className="gap-1"><Check className="h-3 w-3" />Selecionada</Badge>
                        : null
                    ) },
                  ]}
                  searchKeys={["marca", "modelo", "num_serie", "tipo_motor"]}
                  searchPlaceholder="Pesquisar por marca, modelo, nº de série ou motor"
                  searchClassName="max-w-lg"
                />
              )}
            </div>
          )}

          {step === 3 && (
            <div className="space-y-5">
              <div className="space-y-2">
                <Label>Acessórios entregues com a trotinete</Label>
                <div className="flex gap-2">
                  <Input
                    value={novoAcessorio}
                    onChange={(e) => setNovoAcessorio(e.target.value)}
                    placeholder="Ex: carregador, capacete…"
                    onKeyDown={(e) => { if (e.key === "Enter") { e.preventDefault(); addAcessorio(); } }}
                  />
                  <Button type="button" onClick={addAcessorio}>
                    {editIndex !== null ? "Guardar" : "Adicionar"}
                  </Button>
                  {editIndex !== null && (
                    <Button type="button" variant="outline" onClick={cancelEdit}>Cancelar</Button>
                  )}
                </div>
                {acessorios.length === 0 ? (
                  <p className="text-xs text-muted-foreground">Nenhum acessório adicionado</p>
                ) : (
                  <ul className="divide-y rounded-md border">
                    {acessorios.map((a, i) => (
                      <li key={i} className={`flex items-center justify-between gap-2 px-3 py-2 text-sm ${editIndex === i ? "bg-muted/60" : ""}`}>
                        <span className="truncate">
                          <span className="mr-2 text-xs text-muted-foreground">#{i + 1}</span>
                          {a}
                        </span>
                        <div className="flex gap-1">
                          <Button type="button" size="icon" variant="ghost" onClick={() => editAcessorio(i)} aria-label="Editar">
                            <Pencil className="h-4 w-4" />
                          </Button>
                          <Button type="button" size="icon" variant="ghost" onClick={() => removeAcessorio(i)} aria-label="Remover">
                            <Trash2 className="h-4 w-4" />
                          </Button>
                        </div>
                      </li>
                    ))}
                  </ul>
                )}
              </div>

              <div className="space-y-2">
                <Label>Fotos da trotinete (PNG/JPEG, máx. 5MB cada)</Label>
                <label className="flex cursor-pointer items-center justify-center gap-2 rounded-md border border-dashed bg-muted/40 px-3 py-6 text-sm text-muted-foreground hover:bg-muted">
                  <Upload className="h-4 w-4" />
                  Carregar imagens
                  <input type="file" accept="image/png,image/jpeg" multiple className="hidden" onChange={(e) => handleFiles(e.target.files)} />
                </label>
                {fotos.length > 0 && (
                  <div className="grid grid-cols-3 gap-2 sm:grid-cols-4">
                    {fotos.map((src, i) => (
                      <div key={i} className="group relative aspect-square overflow-hidden rounded border">
                        <img src={src} alt={`Foto ${i + 1}`} className="h-full w-full object-cover" />
                        <button
                          type="button"
                          onClick={() => removeFoto(i)}
                          className="absolute right-1 top-1 rounded bg-destructive p-1 text-destructive-foreground opacity-0 transition-opacity group-hover:opacity-100"
                          aria-label="Remover foto"
                        >
                          <X className="h-3 w-3" />
                        </button>
                      </div>
                    ))}
                  </div>
                )}
                {fotos.length === 0 && (
                  <p className="flex items-center gap-1 text-xs text-muted-foreground">
                    <ImageIcon className="h-3 w-3" /> Nenhuma foto carregada
                  </p>
                )}
              </div>
            </div>
          )}

          {step === 4 && (
            <div className="space-y-2">
              <Label>Descrição do problema reportado pelo cliente</Label>
              <Textarea
                rows={6}
                value={descricao}
                onChange={(e) => setDescricao(e.target.value)}
                placeholder="Ex: a trotinete não liga, ouve-se um clique no motor…"
              />
              <p className="text-xs text-muted-foreground">{descricao.trim().length} caracteres</p>
            </div>
          )}

          <div className="mt-6 flex justify-between">
            <Button variant="outline" disabled={step === 1} onClick={() => setStep((s) => s - 1)}>
              <ArrowLeft className="h-4 w-4" /> Anterior
            </Button>
            {step < 4 ? (
              <Button disabled={!canNext()} onClick={() => setStep((s) => s + 1)}>
                Próximo <ArrowRight className="h-4 w-4" />
              </Button>
            ) : (
              <Button disabled={!canNext() || submitting} onClick={handleSubmit}>
                {submitting ? "A criar…" : "Criar OS"}
              </Button>
            )}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
