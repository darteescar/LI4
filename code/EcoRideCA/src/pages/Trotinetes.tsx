import { useMemo, useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Plus, Pencil, Trash2 } from "lucide-react";
import { toast } from "sonner";

import { PageHeader } from "@/components/layout/PageHeader";
import { DataTable, type Column } from "@/components/data-table";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { Button } from "@/components/ui/button";
import {
  Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select, SelectContent, SelectItem, SelectTrigger, SelectValue,
} from "@/components/ui/select";

import { api } from "@/services/api";

interface Trotinete {
  id: number;
  modelo: string;
  marca: string;
  num_serie: string;
  tipo_motor: string;
  cod_cliente: number;
}

interface Cliente {
  id: number;
  nome: string;
  nif: string;
}

const TIPOS_MOTOR = [
  "Hub Motor",
  "Mid-Drive",
  "Belt Drive",
  "Chain Drive",
] as const;

type TipoMotor = typeof TIPOS_MOTOR[number];

const trotineteSchema = z.object({
  cod_cliente: z.number().min(1, "Cliente obrigatório"),
  marca:       z.string().trim().min(1, "Marca obrigatória"),
  modelo:      z.string().trim().min(1, "Modelo obrigatório"),
  num_serie:   z.string().trim().min(1, "Nº de série obrigatório"),
  motor:       z.string().trim().min(1, "Motor obrigatório"),
});

type FormValues = z.infer<typeof trotineteSchema>;

// DataTable requires id: string | number — extend Trotinete with clienteNome for search
type TrotineteRow = Trotinete & { clienteNome: string };

export default function Trotinetes() {
  const qc = useQueryClient();
  const [editing, setEditing] = useState<Trotinete | null>(null);
  const [open, setOpen] = useState(false);

  const { data: trotinetes = [], isLoading } = useQuery<Trotinete[]>({
    queryKey: ["trotinetes"],
    queryFn: () => api.get<Trotinete[]>("/trotinetes"),
  });

  const { data: clientes = [] } = useQuery<Cliente[]>({
    queryKey: ["clientes"],
    queryFn: () => api.get<Cliente[]>("/clientes"),
  });

  const clienteMap = useMemo(() => new Map(clientes.map((c) => [c.id, c.nome])), [clientes]);

  const rows: TrotineteRow[] = useMemo(
    () => trotinetes.map((t) => ({ ...t, clienteNome: clienteMap.get(t.cod_cliente) ?? "" })),
    [trotinetes, clienteMap],
  );

  const deleteMutation = useMutation({
    mutationFn: (id: number) => api.delete(`/trotinetes/${id}`),
    onSuccess: () => {
      toast.success("Trotinete eliminada");
      qc.invalidateQueries({ queryKey: ["trotinetes"] });
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const columns: Column<TrotineteRow>[] = [
    { key: "marcaModelo", header: "Marca / Modelo", cell: (t) => (
      <div>
        <div className="font-medium">{t.marca} {t.modelo}</div>
        <div className="text-xs text-muted-foreground">{t.tipo_motor}</div>
      </div>
    ) },
    { key: "num_serie",   header: "Nº de série", cell: (t) => <code className="text-xs">{t.num_serie}</code> },
    { key: "cliente",     header: "Cliente",     cell: (t) => t.clienteNome || "—" },
    { key: "tipo_motor",  header: "Motor",       cell: (t) => t.tipo_motor },
  ];

  return (
    <div>
      <PageHeader
        title="Trotinetes"
        description="Equipamentos registados, associados a clientes"
        actions={
          <Button onClick={() => { setEditing(null); setOpen(true); }}>
            <Plus className="h-4 w-4" /> Nova trotinete
          </Button>
        }
      />
      <DataTable
        data={rows}
        columns={columns}
        searchKeys={["marca", "modelo", "num_serie", "tipo_motor", "clienteNome"]}
        searchPlaceholder="Pesquisar por marca, modelo, nº de série, cliente ou motor"
        searchClassName="max-w-lg"
        isLoading={isLoading}
        rowActions={(t) => (
          <>
            <Button variant="ghost" size="icon" onClick={() => { setEditing(t); setOpen(true); }}>
              <Pencil className="h-4 w-4" />
            </Button>
            <ConfirmDialog
              trigger={<Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
              title="Eliminar trotinete?"
              destructive
              onConfirm={() => deleteMutation.mutate(t.id)}
            />
          </>
        )}
      />
      <TrotineteForm
        open={open}
        onOpenChange={setOpen}
        initial={editing}
        clientes={clientes}
        onSaved={() => {
          qc.invalidateQueries({ queryKey: ["trotinetes"] });
          qc.invalidateQueries({ queryKey: ["clientes"] });
        }}
      />
    </div>
  );
}

function TrotineteForm({
  open, onOpenChange, initial, clientes, onSaved,
}: {
  open: boolean; onOpenChange: (o: boolean) => void;
  initial: Trotinete | null; clientes: Cliente[]; onSaved: () => void;
}) {
  const form = useForm<FormValues>({
    resolver: zodResolver(trotineteSchema),
    values: initial
      ? { cod_cliente: initial.cod_cliente, marca: initial.marca, modelo: initial.modelo, num_serie: initial.num_serie, motor: initial.tipo_motor }
      : { cod_cliente: clientes[0]?.id ?? 0, marca: "", modelo: "", num_serie: "", motor: "" },
  });

  const saveMutation = useMutation({
    mutationFn: (v: FormValues) =>
      initial
        ? api.patch<Trotinete>(`/trotinetes/${initial.id}`, v)
        : api.post<Trotinete>(`/clientes/${v.cod_cliente}/trotinetes`, v),
    onSuccess: () => {
      toast.success(initial ? "Trotinete atualizada" : "Trotinete criada");
      onOpenChange(false);
      onSaved();
    },
    onError: (e: Error) => toast.error(e.message),
  });

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-xl">
        <DialogHeader>
          <DialogTitle>{initial ? "Editar trotinete" : "Nova trotinete"}</DialogTitle>
        </DialogHeader>
        <form onSubmit={form.handleSubmit((v) => saveMutation.mutate(v))} className="grid grid-cols-1 gap-3 sm:grid-cols-2">
          <F label="Cliente" e={form.formState.errors.cod_cliente?.message} className="sm:col-span-2">
            <Select
              value={String(form.watch("cod_cliente"))}
              onValueChange={(v) => form.setValue("cod_cliente", Number(v), { shouldValidate: true })}
            >
              <SelectTrigger><SelectValue placeholder="Selecionar cliente" /></SelectTrigger>
              <SelectContent>
                {clientes.map((c) => (
                  <SelectItem key={c.id} value={String(c.id)}>{c.nome} — {c.nif}</SelectItem>
                ))}
              </SelectContent>
            </Select>
          </F>
          <F label="Marca" e={form.formState.errors.marca?.message}>
            <Input {...form.register("marca")} />
          </F>
          <F label="Modelo" e={form.formState.errors.modelo?.message}>
            <Input {...form.register("modelo")} />
          </F>
          <F label="Nº de série" e={form.formState.errors.num_serie?.message}>
            <Input {...form.register("num_serie")} />
          </F>
          <F label="Tipo de motor" e={form.formState.errors.motor?.message}>
            <Select
              value={form.watch("motor")}
              onValueChange={(v) => form.setValue("motor", v, { shouldValidate: true })}
            >
              <SelectTrigger><SelectValue placeholder="Selecionar tipo…" /></SelectTrigger>
              <SelectContent>
                {TIPOS_MOTOR.map((t) => (
                  <SelectItem key={t} value={t}>{t}</SelectItem>
                ))}
              </SelectContent>
            </Select>
          </F>
          <DialogFooter className="sm:col-span-2">
            <Button type="button" variant="outline" onClick={() => onOpenChange(false)}>Cancelar</Button>
            <Button type="submit" disabled={saveMutation.isPending}>
              {saveMutation.isPending ? "A guardar…" : initial ? "Guardar" : "Criar"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}

function F({ label, e, children, className }: { label: string; e?: string; children: React.ReactNode; className?: string }) {
  return (
    <div className={`space-y-1.5 ${className ?? ""}`}>
      <Label>{label}</Label>
      {children}
      {e && <p className="text-xs text-destructive">{e}</p>}
    </div>
  );
}
