import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Plus, Pencil, Trash2, Bike } from "lucide-react";
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

import { Badge } from "@/components/ui/badge";

import { clientesService, trotinetesService } from "@/services/entities";
import { clienteSchema } from "@/lib/validators";
import type { Cliente } from "@/lib/types";

type FormValues = z.infer<typeof clienteSchema>;

export default function Clientes() {
  const [data, setData] = useState<Cliente[]>([]);
  const [counts, setCounts] = useState<Record<string, number>>({});
  const [editing, setEditing] = useState<Cliente | null>(null);
  const [open, setOpen] = useState(false);

  const reload = async () => {
    const [c, t] = await Promise.all([clientesService.list(), trotinetesService.list()]);
    setData(c);
    const map: Record<string, number> = {};
    t.forEach((x) => { map[x.clienteId] = (map[x.clienteId] ?? 0) + 1; });
    setCounts(map);
  };
  useEffect(() => { reload(); }, []);

  const handleRemove = async (c: Cliente) => {
    await clientesService.remove(c.id);
    toast.success("Cliente eliminado");
    reload();
  };

  const columns: Column<Cliente>[] = [
    { key: "nome", header: "Nome", cell: (c) => <span className="font-medium">{c.nome}</span> },
    { key: "nif", header: "NIF", cell: (c) => c.nif },
    { key: "telemovel", header: "Telemóvel", cell: (c) => c.telemovel },
    { key: "email", header: "Email", cell: (c) => c.email },
    { key: "trotinetes", header: "Trotinetes", cell: (c) => (
      <Badge variant="secondary" className="gap-1">
        <Bike className="h-3 w-3" />{counts[c.id] ?? 0}
      </Badge>
    ) },
  ];

  return (
    <div>
      <PageHeader
        title="Clientes"
        description="Base de dados de clientes da oficina"
        actions={
          <Button onClick={() => { setEditing(null); setOpen(true); }}>
            <Plus className="h-4 w-4" /> Novo cliente
          </Button>
        }
      />
      <DataTable
        data={data}
        columns={columns}
        searchKeys={["nome", "nif", "email", "telemovel"]}
        searchPlaceholder="Pesquisar por nome, email, NIF ou telemóvel"
        rowActions={(c) => (
          <>
            <Button variant="ghost" size="icon" onClick={() => { setEditing(c); setOpen(true); }}>
              <Pencil className="h-4 w-4" />
            </Button>
            <ConfirmDialog
              trigger={<Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
              title="Eliminar cliente?"
              description={`Esta ação remove ${c.nome} e as suas trotinetes.`}
              destructive
              onConfirm={() => handleRemove(c)}
            />
          </>
        )}
      />
      <ClienteForm open={open} onOpenChange={setOpen} initial={editing} onSaved={reload} />
    </div>
  );
}

function ClienteForm({
  open, onOpenChange, initial, onSaved,
}: {
  open: boolean; onOpenChange: (o: boolean) => void;
  initial: Cliente | null; onSaved: () => void;
}) {
  const form = useForm<FormValues>({
    resolver: zodResolver(clienteSchema),
    defaultValues: initial ?? {
      nome: "", nif: "", telemovel: "", email: "", morada: "", codigoPostal: "",
    },
    values: initial ? {
      nome: initial.nome, nif: initial.nif, telemovel: initial.telemovel,
      email: initial.email, morada: initial.morada, codigoPostal: initial.codigoPostal,
    } : undefined,
  });

  const onSubmit = async (v: FormValues) => {
    try {
      if (initial) await clientesService.update(initial.id, v);
      else await clientesService.create(v);
      toast.success(initial ? "Cliente atualizado" : "Cliente criado");
      onOpenChange(false);
      onSaved();
    } catch (e) {
      toast.error(e instanceof Error ? e.message : "Erro");
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-xl">
        <DialogHeader>
          <DialogTitle>{initial ? "Editar cliente" : "Novo cliente"}</DialogTitle>
        </DialogHeader>
        <form onSubmit={form.handleSubmit(onSubmit)} className="grid grid-cols-1 gap-3 sm:grid-cols-2">
          <F label="Nome" e={form.formState.errors.nome?.message} className="sm:col-span-2">
            <Input {...form.register("nome")} />
          </F>
          <F label="NIF" e={form.formState.errors.nif?.message}>
            <Input {...form.register("nif")} maxLength={9} />
          </F>
          <F label="Telemóvel" e={form.formState.errors.telemovel?.message}>
            <Input {...form.register("telemovel")} />
          </F>
          <F label="Email" e={form.formState.errors.email?.message} className="sm:col-span-2">
            <Input type="email" {...form.register("email")} />
          </F>
          <F label="Morada" e={form.formState.errors.morada?.message} className="sm:col-span-2">
            <Input {...form.register("morada")} />
          </F>
          <F label="Código postal" e={form.formState.errors.codigoPostal?.message}>
            <Input {...form.register("codigoPostal")} placeholder="1234-567" />
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
