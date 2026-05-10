import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Plus, Pencil, Trash2, Clock, RotateCcw } from "lucide-react";
import { toast } from "sonner";

import { PageHeader } from "@/components/layout/PageHeader";
import { DataTable, type Column } from "@/components/data-table";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { Button } from "@/components/ui/button";
import {
  Dialog, DialogContent, DialogDescription, DialogFooter,
  DialogHeader, DialogTitle, DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select, SelectContent, SelectItem, SelectTrigger, SelectValue,
} from "@/components/ui/select";
import { Badge } from "@/components/ui/badge";

import { funcionariosService } from "@/services/entities";
import { funcionarioSchema, horasExtraSchema } from "@/lib/validators";
import { ROLE_LABELS, type Funcionario, type Role } from "@/lib/types";
import { formatEUR } from "@/lib/format";

type FormValues = z.infer<typeof funcionarioSchema>;
type HEValues = z.infer<typeof horasExtraSchema>;

const ROLES: Role[] = ["GERENTE", "GESTOR_STOCK", "SECRETARIA", "MECANICO"];

const ROLE_BADGE: Record<Role, string> = {
  GERENTE: "bg-primary-soft text-primary",
  GESTOR_STOCK: "bg-info-soft text-info",
  SECRETARIA: "bg-warning-soft text-warning",
  MECANICO: "bg-success-soft text-success",
};

export default function Funcionarios() {
  const [data, setData] = useState<Funcionario[]>([]);
  const [editing, setEditing] = useState<Funcionario | null>(null);
  const [openForm, setOpenForm] = useState(false);
  const [heFor, setHeFor] = useState<Funcionario | null>(null);

  const reload = () => funcionariosService.list().then(setData);
  useEffect(() => { reload(); }, []);

  const openNew = () => { setEditing(null); setOpenForm(true); };
  const openEdit = (f: Funcionario) => { setEditing(f); setOpenForm(true); };

  const handleRemove = async (f: Funcionario) => {
    await funcionariosService.remove(f.id);
    toast.success(`${f.nome} eliminado`);
    reload();
  };

  const columns: Column<Funcionario>[] = [
    { key: "nome", header: "Nome", cell: (f) => <span className="font-medium">{f.nome}</span> },
    { key: "id", header: "Identificador", cell: (f) => <code className="text-xs">{f.identificador}</code> },
    { key: "cargo", header: "Cargo", cell: (f) => (
      <Badge variant="secondary" className={ROLE_BADGE[f.cargo]}>{ROLE_LABELS[f.cargo]}</Badge>
    ) },
    { key: "email", header: "Email", cell: (f) => f.email },
    { key: "telemovel", header: "Telemóvel", cell: (f) => f.telemovel },
    { key: "vencHora", header: "€/hora", cell: (f) => formatEUR(f.vencimentoHora) },
    { key: "vencBruto", header: "Bruto", cell: (f) => formatEUR(f.vencimentoBruto) },
    { key: "horasAc", header: "Horas extra acum.", cell: (f) => (
      <Badge variant={f.horasExtraAcumuladas > 0 ? "secondary" : "outline"}>
        {f.horasExtraAcumuladas.toFixed(1)} h
      </Badge>
    ) },
  ];

  return (
    <div>
      <PageHeader
        title="Funcionários"
        description="Gestão de colaboradores da oficina"
        actions={
          <Button onClick={openNew}>
            <Plus className="h-4 w-4" />
            Novo funcionário
          </Button>
        }
      />

      <DataTable
        data={data}
        columns={columns}
        searchKeys={["nome", "identificador", "email", "nif"]}
        searchPlaceholder="Pesquisar por nome, identificador, email…"
        rowActions={(f) => (
          <>
            <Button variant="ghost" size="icon" onClick={() => setHeFor(f)} title="Registar horas extra">
              <Clock className="h-4 w-4" />
            </Button>
            <ConfirmDialog
              trigger={
                <Button variant="ghost" size="icon" title="Pagar Salário">
                  <RotateCcw className="h-4 w-4" />
                </Button>
              }
              title="Pagar Salário?"
              description={`As horas extra acumuladas de ${f.nome} ficam a 0.`}
              onConfirm={async () => {
                await funcionariosService.resetHorasExtra(f.id);
                toast.success("Salário pago");
                reload();
              }}
            />
            <Button variant="ghost" size="icon" onClick={() => openEdit(f)}>
              <Pencil className="h-4 w-4" />
            </Button>
            <ConfirmDialog
              trigger={<Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
              title="Eliminar funcionário?"
              description={`Esta ação remove ${f.nome} permanentemente.`}
              destructive
              onConfirm={() => handleRemove(f)}
            />
          </>
        )}
      />

      <FuncionarioForm
        open={openForm}
        onOpenChange={setOpenForm}
        initial={editing}
        onSaved={reload}
      />
      <HorasExtraDialog
        funcionario={heFor}
        onClose={() => setHeFor(null)}
      />
    </div>
  );
}

function FuncionarioForm({
  open, onOpenChange, initial, onSaved,
}: {
  open: boolean;
  onOpenChange: (o: boolean) => void;
  initial: Funcionario | null;
  onSaved: () => void;
}) {
  const form = useForm<FormValues>({
    resolver: zodResolver(funcionarioSchema) as any,
    defaultValues: initial ? {
      nome: initial.nome, identificador: initial.identificador, password: initial.password ?? "",
      email: initial.email, telemovel: initial.telemovel, nif: initial.nif, niss: initial.niss,
      nus: initial.nus, iban: initial.iban, morada: initial.morada, codigoPostal: initial.codigoPostal,
      cargo: initial.cargo, vencimentoHora: initial.vencimentoHora, vencimentoBruto: initial.vencimentoBruto,
      dataAdmissao: initial.dataAdmissao,
    } : {
      nome: "", identificador: "", password: "", email: "", telemovel: "", nif: "", niss: "", nus: "",
      iban: "", morada: "", codigoPostal: "", cargo: "MECANICO",
      vencimentoHora: 0, vencimentoBruto: 0,
      dataAdmissao: new Date().toISOString().slice(0, 10),
    },
    values: initial ? {
      nome: initial.nome, identificador: initial.identificador, password: initial.password ?? "",
      email: initial.email, telemovel: initial.telemovel, nif: initial.nif, niss: initial.niss,
      nus: initial.nus, iban: initial.iban, morada: initial.morada, codigoPostal: initial.codigoPostal,
      cargo: initial.cargo, vencimentoHora: initial.vencimentoHora, vencimentoBruto: initial.vencimentoBruto,
      dataAdmissao: initial.dataAdmissao,
    } : undefined,
  });

  const onSubmit = async (values: FormValues) => {
    try {
      if (initial) {
        // Quando o gerente edita um funcionário NÃO pode alterar a password.
        const { password, ...rest } = values;
        await funcionariosService.update(initial.id, rest);
        toast.success("Funcionário atualizado");
      } else {
        await funcionariosService.create({ ...values, horasExtraAcumuladas: 0 });
        toast.success("Funcionário criado");
      }
      onOpenChange(false);
      onSaved();
    } catch (e) {
      toast.error(e instanceof Error ? e.message : "Erro");
    }
  };


  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle>{initial ? "Editar funcionário" : "Novo funcionário"}</DialogTitle>
          <DialogDescription>Todos os campos são obrigatórios.</DialogDescription>
        </DialogHeader>
        <form onSubmit={form.handleSubmit(onSubmit)} className="grid grid-cols-1 gap-3 sm:grid-cols-2">
          <Field label="Nome" error={form.formState.errors.nome?.message}>
            <Input {...form.register("nome")} />
          </Field>
          <Field label="Identificador (login)" error={form.formState.errors.identificador?.message}>
            <Input {...form.register("identificador")} disabled={!!initial} />
          </Field>
          {!initial && (
            <Field label="Password inicial" error={form.formState.errors.password?.message}>
              <Input type="password" autoComplete="new-password" {...form.register("password")} />
            </Field>
          )}
          <Field label="Email" error={form.formState.errors.email?.message}>
            <Input type="email" {...form.register("email")} />
          </Field>
          <Field label="Telemóvel" error={form.formState.errors.telemovel?.message}>
            <Input {...form.register("telemovel")} placeholder="9XXXXXXXX" />
          </Field>
          <Field label="NIF" error={form.formState.errors.nif?.message}>
            <Input {...form.register("nif")} maxLength={9} />
          </Field>
          <Field label="NISS" error={form.formState.errors.niss?.message}>
            <Input {...form.register("niss")} maxLength={11} />
          </Field>
          <Field label="NUS" error={form.formState.errors.nus?.message}>
            <Input {...form.register("nus")} />
          </Field>
          <Field label="IBAN" error={form.formState.errors.iban?.message}>
            <Input {...form.register("iban")} placeholder="PT50…" />
          </Field>
          <Field label="Morada" error={form.formState.errors.morada?.message} className="sm:col-span-2">
            <Input {...form.register("morada")} />
          </Field>
          <Field label="Código postal" error={form.formState.errors.codigoPostal?.message}>
            <Input {...form.register("codigoPostal")} placeholder="1234-567" />
          </Field>
          <Field label="Cargo" error={form.formState.errors.cargo?.message}>
            <Select
              value={form.watch("cargo")}
              onValueChange={(v) => form.setValue("cargo", v as Role, { shouldValidate: true })}
            >
              <SelectTrigger><SelectValue /></SelectTrigger>
              <SelectContent>
                {ROLES.map((r) => (
                  <SelectItem key={r} value={r}>{ROLE_LABELS[r]}</SelectItem>
                ))}
              </SelectContent>
            </Select>
          </Field>
          <Field label="Vencimento por hora (€/h)" error={form.formState.errors.vencimentoHora?.message}>
            <Input type="number" step="0.01" {...form.register("vencimentoHora")} />
          </Field>
          <Field label="Vencimento bruto (€)" error={form.formState.errors.vencimentoBruto?.message}>
            <Input type="number" step="0.01" {...form.register("vencimentoBruto")} />
          </Field>
          <Field label="Data de admissão" error={form.formState.errors.dataAdmissao?.message}>
            <Input type="date" {...form.register("dataAdmissao")} />
          </Field>

          <DialogFooter className="sm:col-span-2 mt-2">
            <Button type="button" variant="outline" onClick={() => onOpenChange(false)}>Cancelar</Button>
            <Button type="submit" disabled={form.formState.isSubmitting}>
              {initial ? "Guardar" : "Criar"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}

function HorasExtraDialog({
  funcionario, onClose,
}: { funcionario: Funcionario | null; onClose: () => void }) {
  const open = !!funcionario;
  const form = useForm<HEValues>({
    resolver: zodResolver(horasExtraSchema) as any,
    defaultValues: {
      funcionarioId: funcionario?.id ?? "",
      data: new Date().toISOString().slice(0, 10),
      horas: 1,
    },
    values: funcionario ? {
      funcionarioId: funcionario.id,
      data: new Date().toISOString().slice(0, 10),
      horas: 1,
    } : undefined,
  });

  const onSubmit = async (v: HEValues) => {
    try {
      await funcionariosService.addHorasExtra(v);
      toast.success("Horas extra registadas");
      onClose();
      form.reset();
    } catch (e) {
      toast.error(e instanceof Error ? e.message : "Erro");
    }
  };

  return (
    <Dialog open={open} onOpenChange={(o) => !o && onClose()}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Horas extra — {funcionario?.nome}</DialogTitle>
        </DialogHeader>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-3">
          <Field label="Data" error={form.formState.errors.data?.message}>
            <Input type="date" {...form.register("data")} />
          </Field>
          <Field label="Nº de horas" error={form.formState.errors.horas?.message}>
            <Input type="number" step="0.5" min="0.5" {...form.register("horas")} />
          </Field>
          <DialogFooter>
            <Button type="button" variant="outline" onClick={onClose}>Cancelar</Button>
            <Button type="submit">Registar</Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}

function Field({
  label, error, children, className,
}: { label: string; error?: string; children: React.ReactNode; className?: string }) {
  return (
    <div className={`space-y-1.5 ${className ?? ""}`}>
      <Label>{label}</Label>
      {children}
      {error && <p className="text-xs text-destructive">{error}</p>}
    </div>
  );
}
