import { useEffect, useState } from "react";
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

import { fornecedoresService } from "@/services/entities";
import { fornecedorSchema } from "@/lib/validators";
import type { Fornecedor } from "@/lib/types";

type FormValues = z.infer<typeof fornecedorSchema>;

export default function Fornecedores() {
  const [data, setData] = useState<Fornecedor[]>([]);
  const [editing, setEditing] = useState<Fornecedor | null>(null);
  const [open, setOpen] = useState(false);

  const reload = () => fornecedoresService.list().then(setData);
  useEffect(() => { reload(); }, []);

  const handleRemove = async (f: Fornecedor) => {
    await fornecedoresService.remove(f.id);
    toast.success("Fornecedor eliminado");
    reload();
  };

  const columns: Column<Fornecedor>[] = [
    { key: "nome", header: "Nome", cell: (f) => <span className="font-medium">{f.nome}</span> },
    { key: "telemovel", header: "Telefone", cell: (f) => f.telemovel },
    { key: "email", header: "Email", cell: (f) => f.email },
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
        rowActions={(f) => (
          <>
            <Button variant="ghost" size="icon" onClick={() => { setEditing(f); setOpen(true); }}>
              <Pencil className="h-4 w-4" />
            </Button>
            <ConfirmDialog
              trigger={<Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
              title="Eliminar fornecedor?"
              destructive
              onConfirm={() => handleRemove(f)}
            />
          </>
        )}
      />
      <FornecedorForm open={open} onOpenChange={setOpen} initial={editing} onSaved={reload} />
    </div>
  );
}

function FornecedorForm({
  open, onOpenChange, initial, onSaved,
}: {
  open: boolean; onOpenChange: (o: boolean) => void;
  initial: Fornecedor | null; onSaved: () => void;
}) {
  const form = useForm<FormValues>({
    resolver: zodResolver(fornecedorSchema),
    defaultValues: initial ?? { nome: "", telemovel: "", email: "" },
    values: initial ?? undefined,
  });

  const onSubmit = async (v: FormValues) => {
    try {
      if (initial) await fornecedoresService.update(initial.id, v);
      else await fornecedoresService.create(v);
      toast.success(initial ? "Fornecedor atualizado" : "Fornecedor criado");
      onOpenChange(false); onSaved();
    } catch (e) {
      toast.error(e instanceof Error ? e.message : "Erro");
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{initial ? "Editar fornecedor" : "Novo fornecedor"}</DialogTitle>
        </DialogHeader>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-3">
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
            <Button type="submit">{initial ? "Guardar" : "Criar"}</Button>
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
