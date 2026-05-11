import { useState } from "react";
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

import { api } from "@/services/api";
import { fornecedorSchema } from "@/lib/validators";

// Tipo que espelha o JSON devolvido pelo servidor Java
interface Fornecedor {
  id: number;
  nome: string;
  telemovel: string;
  email: string;
}

type FormValues = z.infer<typeof fornecedorSchema>;

export default function Fornecedores() {
  const qc = useQueryClient();
  const [editing, setEditing] = useState<Fornecedor | null>(null);
  const [open, setOpen] = useState(false);

  // ── Leitura ──────────────────────────────────────────────────────────────
  // useQuery mantém os dados em cache e refaz o fetch quando necessário.
  // "fornecedores" é a chave de cache — qualquer invalidação com esta chave
  // vai forçar um novo GET /api/fornecedores.
  const { data = [], isLoading } = useQuery<Fornecedor[]>({
    queryKey: ["fornecedores"],
    queryFn: () => api.get<Fornecedor[]>("/fornecedores"),
  });

  // ── Escrita ───────────────────────────────────────────────────────────────
  // useMutation envolve operações que alteram dados. Após cada mutação com
  // sucesso, invalidamos a cache para que a tabela atualize automaticamente.

  const deleteMutation = useMutation({
    mutationFn: (id: number) => api.delete(`/fornecedores/${id}`),
    onSuccess: () => {
      toast.success("Fornecedor eliminado");
      qc.invalidateQueries({ queryKey: ["fornecedores"] });
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const columns: Column<Fornecedor>[] = [
    { key: "nome",      header: "Nome",     cell: (f) => <span className="font-medium">{f.nome}</span> },
    { key: "telemovel", header: "Telefone", cell: (f) => f.telemovel },
    { key: "email",     header: "Email",    cell: (f) => f.email },
  ];

  return (
    <div>
      <PageHeader
        title="Fornecedores"
        description="Parceiros e fornecedores de peças"
        actions={
          <Button onClick={() => { setEditing(null); setOpen(true); }}>
            <Plus className="h-4 w-4" /> Novo fornecedor
          </Button>
        }
      />
      <DataTable
        data={data}
        columns={columns}
        searchKeys={["nome", "telemovel", "email"]}
        searchPlaceholder="Pesquisar por nome, telefone ou email"
        isLoading={isLoading}
        rowActions={(f) => (
          <>
            <Button variant="ghost" size="icon" onClick={() => { setEditing(f); setOpen(true); }}>
              <Pencil className="h-4 w-4" />
            </Button>
            <ConfirmDialog
              trigger={<Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
              title="Eliminar fornecedor?"
              destructive
              onConfirm={() => deleteMutation.mutate(f.id)}
            />
          </>
        )}
      />
      <FornecedorForm
        open={open}
        onOpenChange={setOpen}
        initial={editing}
        onSaved={() => qc.invalidateQueries({ queryKey: ["fornecedores"] })}
      />
    </div>
  );
}

function FornecedorForm({
  open, onOpenChange, initial, onSaved,
}: {
  open: boolean;
  onOpenChange: (o: boolean) => void;
  initial: Fornecedor | null;
  onSaved: () => void;
}) {
  const form = useForm<FormValues>({
    resolver: zodResolver(fornecedorSchema),
    values: initial ?? { nome: "", telemovel: "", email: "" },
  });

  const saveMutation = useMutation({
    mutationFn: (v: FormValues) =>
      initial
        ? api.patch<Fornecedor>(`/fornecedores/${initial.id}`, v)
        : api.post<Fornecedor>("/fornecedores", v),
    onSuccess: () => {
      toast.success(initial ? "Fornecedor atualizado" : "Fornecedor criado");
      onOpenChange(false);
      onSaved();
    },
    onError: (e: Error) => toast.error(e.message),
  });

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{initial ? "Editar fornecedor" : "Novo fornecedor"}</DialogTitle>
        </DialogHeader>
        <form onSubmit={form.handleSubmit((v) => saveMutation.mutate(v))} className="space-y-3">
          <F label="Nome" e={form.formState.errors.nome?.message}>
            <Input {...form.register("nome")} />
          </F>
          <F label="Telefone" e={form.formState.errors.telemovel?.message}>
            <Input {...form.register("telemovel")} />
          </F>
          <F label="Email" e={form.formState.errors.email?.message}>
            <Input type="email" {...form.register("email")} />
          </F>
          <DialogFooter>
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

function F({ label, e, children }: { label: string; e?: string; children: React.ReactNode }) {
  return (
    <div className="space-y-1.5">
      <Label>{label}</Label>
      {children}
      {e && <p className="text-xs text-destructive">{e}</p>}
    </div>
  );
}
