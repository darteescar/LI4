import { useState } from "react";
import { Link } from "react-router-dom";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Plus, Pencil, Trash2, Bike, FileText } from "lucide-react";
import { toast } from "sonner";

import { PageHeader } from "@/components/layout/PageHeader";
import { DataTable, type Column } from "@/components/data-table";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

import { api } from "@/services/api";

// campos devolvidos pelo backend
interface Cliente {
  id: number;
  nome: string;
  email: string;
  telemovel: string;
  nif: string;
  codsTrotinetes: number[];
}

const clienteSchema = z.object({
  nome:      z.string().trim().min(2, "Nome obrigatório"),
  nif:       z.string().regex(/^\d{9}$/, "NIF deve ter 9 dígitos"),
  telemovel: z.string().regex(/^9\d{8}$/, "Telemóvel inválido"),
  email:     z.string().trim().email("Email inválido"),
});

type FormValues = z.infer<typeof clienteSchema>;

export default function Clientes() {
  const qc = useQueryClient();
  const [editing, setEditing] = useState<Cliente | null>(null);
  const [open, setOpen] = useState(false);

  const { data = [], isLoading } = useQuery<Cliente[]>({
    queryKey: ["clientes"],
    queryFn: () => api.get<Cliente[]>("/clientes"),
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => api.delete(`/clientes/${id}`),
    onSuccess: () => {
      toast.success("Cliente eliminado");
      qc.invalidateQueries({ queryKey: ["clientes"] });
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const columns: Column<Cliente>[] = [
    { key: "nome",     header: "Nome",      cell: (c) => <span className="font-medium">{c.nome}</span> },
    { key: "NIF",      header: "NIF",       cell: (c) => c.nif },
    { key: "tel",      header: "Telemóvel", cell: (c) => c.telemovel },
    { key: "email",    header: "Email",     cell: (c) => c.email },
    { key: "trots",    header: "Trotinetes", cell: (c) => (
      <Badge variant="secondary" className="gap-1">
        <Bike className="h-3 w-3" />{c.codsTrotinetes.length}
      </Badge>
    ) },
  ];

  return (
    <div>
      <PageHeader
        title="Clientes"
        description="Clientes da oficina"
        actions={
          <Button onClick={() => { setEditing(null); setOpen(true); }}>
            <Plus className="h-4 w-4" /> Novo cliente
          </Button>
        }
      />
      <DataTable
        data={data}
        columns={columns}
        searchKeys={["nome", "email", "telemovel"]}
        searchPlaceholder="Pesquisar por nome, email ou telemóvel"
        isLoading={isLoading}
        rowActions={(c) => (
          <>
            <Button asChild variant="ghost" size="icon" title="Ver ordens de serviço">
              <Link to={`/os?cliente=${c.id}`}><FileText className="h-4 w-4" /></Link>
            </Button>
            <Button asChild variant="ghost" size="icon" title="Ver trotinetes">
              <Link to={`/trotinetes?cliente=${c.id}`}><Bike className="h-4 w-4" /></Link>
            </Button>
            <Button variant="ghost" size="icon" onClick={() => { setEditing(c); setOpen(true); }}>
              <Pencil className="h-4 w-4" />
            </Button>
            <ConfirmDialog
              trigger={<Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
              title="Eliminar cliente?"
              description={`Esta ação remove ${c.nome} e as suas trotinetes.`}
              destructive
              onConfirm={() => deleteMutation.mutate(c.id)}
            />
          </>
        )}
      />
      <ClienteForm
        key={editing?.id ?? "new"}
        open={open}
        onOpenChange={setOpen}
        initial={editing}
        onSaved={() => qc.invalidateQueries({ queryKey: ["clientes"] })}
      />
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
    values: initial
      ? { nome: initial.nome, nif: initial.nif, telemovel: initial.telemovel, email: initial.email }
      : { nome: "", nif: "", telemovel: "", email: "" },
  });

  const saveMutation = useMutation({
    mutationFn: (v: FormValues) =>
      initial
        ? api.patch<Cliente>(`/clientes/${initial.id}`, v)
        : api.post<Cliente>("/clientes", v),
    onSuccess: () => {
      toast.success(initial ? "Cliente atualizado" : "Cliente criado");
      onOpenChange(false);
      onSaved();
    },
    onError: (e: Error) => toast.error(e.message),
  });

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-xl">
        <DialogHeader>
          <DialogTitle>{initial ? "Editar cliente" : "Novo cliente"}</DialogTitle>
        </DialogHeader>
        <form onSubmit={form.handleSubmit((v) => saveMutation.mutate(v))} className="grid grid-cols-1 gap-3 sm:grid-cols-2">
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
