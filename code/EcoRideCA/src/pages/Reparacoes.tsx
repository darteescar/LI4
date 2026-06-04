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
import { Textarea } from "@/components/ui/textarea";
import {
  Select, SelectContent, SelectItem, SelectTrigger, SelectValue,
} from "@/components/ui/select";

import { api } from "@/services/api";
import { reparacaoSchema } from "@/lib/validators";
import { formatEUR } from "@/lib/format";

interface Reparacao {
  id: number;
  nomenclatura: string;
  descricao: string;
  preco: number;
  disponivel: boolean;
}

type FormValues = z.infer<typeof reparacaoSchema>;

export default function Reparacoes() {
  const qc = useQueryClient();
  const [editing, setEditing] = useState<Reparacao | null>(null);
  const [open, setOpen] = useState(false);

  const { data = [], isLoading } = useQuery<Reparacao[]>({
    queryKey: ["reparacoes"],
    queryFn: () => api.get<Reparacao[]>("/reparacoes"),
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => api.delete(`/reparacoes/${id}`),
    onSuccess: () => {
      toast.success("Reparação eliminada");
      qc.invalidateQueries({ queryKey: ["reparacoes"] });
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const columns: Column<Reparacao>[] = [
    { key: "nomenclatura", header: "Nomenclatura", cell: (r) => <span className="font-medium">{r.nomenclatura}</span> },
    { key: "descricao",    header: "Descrição",    cell: (r) => <span className="text-muted-foreground">{r.descricao}</span> },
    { key: "preco",        header: "Preço (mão de obra)", cell: (r) => formatEUR(r.preco), className: "text-right" },
    { key: "disponivel",   header: "Disponibilidade", cell: (r) => (
      r.disponivel
        ? <span className="inline-flex items-center rounded-md bg-success-soft px-2 py-1 text-xs font-medium text-success">Disponível</span>
        : <span className="inline-flex items-center rounded-md bg-destructive/10 px-2 py-1 text-xs font-medium text-destructive">Não disponível</span>
    ) },
  ];

  return (
    <div>
      <PageHeader
        title="Catálogo de Reparações"
        description="Tipos de reparação e respetivos preços de mão de obra"
        actions={
          <Button onClick={() => { setEditing(null); setOpen(true); }}>
            <Plus className="h-4 w-4" /> Nova reparação
          </Button>
        }
      />
      <DataTable
        data={data}
        columns={columns}
        searchKeys={["nomenclatura", "descricao"]}
        isLoading={isLoading}
        rowActions={(r) => (
          <>
            <Button variant="ghost" size="icon" onClick={() => { setEditing(r); setOpen(true); }}>
              <Pencil className="h-4 w-4" />
            </Button>
            <ConfirmDialog
              trigger={<Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
              title="Eliminar reparação?"
              destructive
              onConfirm={() => deleteMutation.mutate(r.id)}
            />
          </>
        )}
      />
      <ReparacaoForm
        open={open}
        onOpenChange={setOpen}
        initial={editing}
        onSaved={() => qc.invalidateQueries({ queryKey: ["reparacoes"] })}
      />
    </div>
  );
}

function ReparacaoForm({
  open, onOpenChange, initial, onSaved,
}: {
  open: boolean; onOpenChange: (o: boolean) => void;
  initial: Reparacao | null; onSaved: () => void;
}) {
  const form = useForm<FormValues>({
    resolver: zodResolver(reparacaoSchema) as any,
    values: initial ?? { nomenclatura: "", descricao: "", preco: 0, disponivel: true },
  });

  const saveMutation = useMutation({
    mutationFn: (v: FormValues) =>
      initial
        ? api.patch<Reparacao>(`/reparacoes/${initial.id}`, v)
        : api.post<Reparacao>("/reparacoes", v),
    onSuccess: () => {
      toast.success(initial ? "Reparação atualizada" : "Reparação criada");
      onOpenChange(false);
      onSaved();
    },
    onError: (e: Error) => toast.error(e.message),
  });

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{initial ? "Editar reparação" : "Nova reparação"}</DialogTitle>
        </DialogHeader>
        <form onSubmit={form.handleSubmit((v) => saveMutation.mutate(v))} className="space-y-3">
          <F label="Nomenclatura" e={form.formState.errors.nomenclatura?.message}>
            <Input {...form.register("nomenclatura")} />
          </F>
          <F label="Descrição" e={form.formState.errors.descricao?.message}>
            <Textarea rows={3} {...form.register("descricao")} />
          </F>
          <F label="Preço (€)" e={form.formState.errors.preco?.message}>
            <Input type="number" step="0.01" {...form.register("preco")} />
          </F>
          <F label="Disponibilidade">
            <Select
              value={form.watch("disponivel") ? "true" : "false"}
              onValueChange={(v) => form.setValue("disponivel", v === "true", { shouldValidate: true })}
            >
              <SelectTrigger><SelectValue /></SelectTrigger>
              <SelectContent>
                <SelectItem value="true">
                  <span className="inline-flex items-center rounded-md bg-success-soft px-2 py-0.5 text-xs font-medium text-success">Disponível</span>
                </SelectItem>
                <SelectItem value="false">
                  <span className="inline-flex items-center rounded-md bg-destructive/10 px-2 py-0.5 text-xs font-medium text-destructive">Não Disponível</span>
                </SelectItem>
              </SelectContent>
            </Select>
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
