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
import { Textarea } from "@/components/ui/textarea";
import {
  Select, SelectContent, SelectItem, SelectTrigger, SelectValue,
} from "@/components/ui/select";

import { reparacoesService } from "@/services/entities";
import { reparacaoSchema } from "@/lib/validators";
import type { Reparacao } from "@/lib/types";
import { formatEUR } from "@/lib/format";

type FormValues = z.infer<typeof reparacaoSchema>;

export default function Reparacoes() {
  const [data, setData] = useState<Reparacao[]>([]);
  const [editing, setEditing] = useState<Reparacao | null>(null);
  const [open, setOpen] = useState(false);

  const reload = () => reparacoesService.list().then(setData);
  useEffect(() => { reload(); }, []);

  const handleRemove = async (r: Reparacao) => {
    await reparacoesService.remove(r.id);
    toast.success("Reparação eliminada");
    reload();
  };

  const columns: Column<Reparacao>[] = [
    { key: "nomenclatura", header: "Nomenclatura", cell: (r) => <span className="font-medium">{r.nomenclatura}</span> },
    { key: "descricao", header: "Descrição", cell: (r) => <span className="text-muted-foreground">{r.descricao}</span> },
    { key: "preco", header: "Preço (mão de obra)", cell: (r) => formatEUR(r.preco), className: "text-right" },
    { key: "disponivel", header: "Disponibilidade", cell: (r) => (
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
        rowActions={(r) => (
          <>
            <Button variant="ghost" size="icon" onClick={() => { setEditing(r); setOpen(true); }}>
              <Pencil className="h-4 w-4" />
            </Button>
            <ConfirmDialog
              trigger={<Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
              title="Eliminar reparação?"
              destructive
              onConfirm={() => handleRemove(r)}
            />
          </>
        )}
      />
      <ReparacaoForm open={open} onOpenChange={setOpen} initial={editing} onSaved={reload} />
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
    defaultValues: initial ?? { nomenclatura: "", descricao: "", preco: 0, disponivel: true },
    values: initial ?? undefined,
  });

  const onSubmit = async (v: FormValues) => {
    try {
      if (initial) await reparacoesService.update(initial.id, v);
      else await reparacoesService.create(v);
      toast.success(initial ? "Reparação atualizada" : "Reparação criada");
      onOpenChange(false); onSaved();
    } catch (e) {
      toast.error(e instanceof Error ? e.message : "Erro");
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{initial ? "Editar reparação" : "Nova reparação"}</DialogTitle>
        </DialogHeader>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-3">
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
              <SelectTrigger>
                <SelectValue />
              </SelectTrigger>
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
