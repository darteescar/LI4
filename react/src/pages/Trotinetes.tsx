import { useEffect, useMemo, useState } from "react";
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
import { Textarea } from "@/components/ui/textarea";
import {
  Select, SelectContent, SelectItem, SelectTrigger, SelectValue,
} from "@/components/ui/select";

import { trotinetesService, clientesService } from "@/services/entities";
import { trotineteSchema } from "@/lib/validators";
import type { Cliente, Trotinete } from "@/lib/types";

type FormValues = z.infer<typeof trotineteSchema>;

export default function Trotinetes() {
  const [data, setData] = useState<Trotinete[]>([]);
  const [clientes, setClientes] = useState<Cliente[]>([]);
  const [editing, setEditing] = useState<Trotinete | null>(null);
  const [open, setOpen] = useState(false);

  const reload = async () => {
    const [t, c] = await Promise.all([trotinetesService.list(), clientesService.list()]);
    setData(t); setClientes(c);
  };
  useEffect(() => { reload(); }, []);

  const clienteMap = useMemo(() => new Map(clientes.map((c) => [c.id, c.nome])), [clientes]);

  const handleRemove = async (t: Trotinete) => {
    await trotinetesService.remove(t.id);
    toast.success("Trotinete eliminada");
    reload();
  };

  const columns: Column<Trotinete>[] = [
    { key: "marca", header: "Marca / Modelo", cell: (t) => (
      <div>
        <div className="font-medium">{t.marca} {t.modelo}</div>
        <div className="text-xs text-muted-foreground">{t.motor}</div>
      </div>
    ) },
    { key: "ns", header: "Nº de série", cell: (t) => <code className="text-xs">{t.numeroSerie}</code> },
    { key: "cliente", header: "Cliente", cell: (t) => clienteMap.get(t.clienteId) ?? "—" },
    { key: "motor", header: "Motor", cell: (t) => t.motor },
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
        data={data}
        columns={columns}
        searchKeys={["marca", "modelo", "numeroSerie", "motor"]}
        searchPlaceholder="Pesquisar por marca, modelo, nº de série…"
        rowActions={(t) => (
          <>
            <Button variant="ghost" size="icon" onClick={() => { setEditing(t); setOpen(true); }}>
              <Pencil className="h-4 w-4" />
            </Button>
            <ConfirmDialog
              trigger={<Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
              title="Eliminar trotinete?"
              destructive
              onConfirm={() => handleRemove(t)}
            />
          </>
        )}
      />
      <TrotineteForm
        open={open}
        onOpenChange={setOpen}
        initial={editing}
        clientes={clientes}
        onSaved={reload}
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
    defaultValues: initial ?? {
      clienteId: clientes[0]?.id ?? "", marca: "", modelo: "", numeroSerie: "",
      motor: "", notas: "",
    },
    values: initial ? {
      clienteId: initial.clienteId, marca: initial.marca, modelo: initial.modelo,
      numeroSerie: initial.numeroSerie, motor: initial.motor,
      notas: initial.notas ?? "",
    } : undefined,
  });

  const onSubmit = async (v: FormValues) => {
    try {
      if (initial) await trotinetesService.update(initial.id, v);
      else await trotinetesService.create(v);
      toast.success(initial ? "Trotinete atualizada" : "Trotinete criada");
      onOpenChange(false); onSaved();
    } catch (e) {
      toast.error(e instanceof Error ? e.message : "Erro");
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-xl">
        <DialogHeader>
          <DialogTitle>{initial ? "Editar trotinete" : "Nova trotinete"}</DialogTitle>
        </DialogHeader>
        <form onSubmit={form.handleSubmit(onSubmit)} className="grid grid-cols-1 gap-3 sm:grid-cols-2">
          <F label="Cliente" e={form.formState.errors.clienteId?.message} className="sm:col-span-2">
            <Select
              value={form.watch("clienteId")}
              onValueChange={(v) => form.setValue("clienteId", v, { shouldValidate: true })}
            >
              <SelectTrigger><SelectValue placeholder="Selecionar cliente" /></SelectTrigger>
              <SelectContent>
                {clientes.map((c) => (
                  <SelectItem key={c.id} value={c.id}>{c.nome} — {c.nif}</SelectItem>
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
          <F label="Nº de série" e={form.formState.errors.numeroSerie?.message}>
            <Input {...form.register("numeroSerie")} />
          </F>
          <F label="Motor" e={form.formState.errors.motor?.message}>
            <Input {...form.register("motor")} />
          </F>
          <F label="Notas" className="sm:col-span-2">
            <Textarea rows={2} {...form.register("notas")} />
          </F>
          <DialogFooter className="sm:col-span-2">
            <Button type="button" variant="outline" onClick={() => onOpenChange(false)}>Cancelar</Button>
            <Button type="submit">{initial ? "Guardar" : "Criar"}</Button>
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
