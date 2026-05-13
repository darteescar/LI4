import { useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Plus, Pencil, Trash2, Clock, Banknote } from "lucide-react";
import { toast } from "sonner";

import { PageHeader } from "@/components/layout/PageHeader";
import { DataTable, type Column } from "@/components/data-table";
import { ConfirmDialog } from "@/components/confirm-dialog";
import { Button } from "@/components/ui/button";
import {
  Dialog, DialogContent, DialogDescription, DialogFooter,
  DialogHeader, DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select, SelectContent, SelectItem, SelectTrigger, SelectValue,
} from "@/components/ui/select";
import { Badge } from "@/components/ui/badge";

import { api } from "@/services/api";
import { ROLE_LABELS, type Role } from "@/lib/types";
import { formatEUR } from "@/lib/format";

// Campo retornado pelo backend
interface Funcionario {
  id: number;
  nome: string;
  telemovel: string;
  email: string;
  data_nascimento: string;
  NISS: string;
  NIF: string;
  NUS: string;
  IBAN: string;
  salario_hora: number;
  salario_liquido: number;
  salario_bruto: number;
  horas_extra: number;
  numero_porta: string;
  rua: string;
  localidade: string;
  codigo_postal: string;
}

interface Utilizador {
  id: number;
  idFuncionario: number;
  cargo: string;
  identificador: string;
}

type FuncionarioRow = Funcionario & { cargo?: Role; identificador?: string; utilizadorId?: number };

const ROLES: Role[] = ["GERENTE", "GESTOR_STOCK", "SECRETARIA", "MECANICO"];

const ROLE_BADGE: Record<Role, string> = {
  GERENTE:      "bg-purple-100 text-purple-700",
  GESTOR_STOCK: "bg-blue-100 text-blue-700",
  SECRETARIA:   "bg-pink-100 text-pink-700",
  MECANICO:     "bg-orange-100 text-orange-700",
};

// Mapping backend cargo name → Role
const CARGO_MAP: Record<string, Role> = {
  Gerente: "GERENTE", GestorStock: "GESTOR_STOCK",
  Secretaria: "SECRETARIA", Mecanico: "MECANICO",
};

const createSchema = z.object({
  nome:          z.string().trim().min(2, "Nome obrigatório"),
  telemovel:     z.string().regex(/^9\d{8}$/, "Telemóvel inválido"),
  email:         z.string().trim().email("Email inválido"),
  data_nascimento: z.string().min(1, "Data obrigatória"),
  NISS:          z.string().regex(/^\d{11}$/, "NISS deve ter 11 dígitos"),
  NIF:           z.string().regex(/^\d{9}$/, "NIF deve ter 9 dígitos"),
  NUS:           z.string().trim().min(1, "NUS obrigatório"),
  IBAN:          z.string().regex(/^\d{21}$/, "IBAN deve ter 21 dígitos após PT50-"),
  salario_hora:  z.coerce.number().min(0),
  salario_bruto: z.coerce.number().min(0),
  numero_porta:  z.string().regex(/^\d{1,4}[A-Za-z]?$/, "Número de porta inválido"),
  rua:           z.string().trim().min(3, "Morada obrigatória"),
  localidade:    z.string().trim().min(1, "Localidade obrigatória"),
  codigo_postal: z.string().regex(/^\d{4}-\d{3}$/, "Código postal inválido"),
  // utilizador
  cargo:         z.enum(["GERENTE", "GESTOR_STOCK", "SECRETARIA", "MECANICO"]),
  identificador: z.string().trim().min(3, "Identificador obrigatório"),
  password:      z.string().min(4, "Password mín. 4 caracteres"),
});

type CreateValues = z.infer<typeof createSchema>;

export default function Funcionarios() {
  const qc = useQueryClient();
  const [editing, setEditing] = useState<FuncionarioRow | null>(null);
  const [openForm, setOpenForm] = useState(false);
  const [heFor, setHeFor] = useState<FuncionarioRow | null>(null);

  const { data: funcionarios = [], isLoading: loadingF } = useQuery<Funcionario[]>({
    queryKey: ["funcionarios"],
    queryFn: () => api.get<Funcionario[]>("/funcionarios"),
  });

  const { data: utilizadores = [] } = useQuery<Utilizador[]>({
    queryKey: ["utilizadores"],
    queryFn: () => api.get<Utilizador[]>("/utilizadores"),
  });

  // Join funcionarios with utilizadores to get cargo and identificador
  const rows: FuncionarioRow[] = funcionarios.map((f) => {
    const u = utilizadores.find((ut) => ut.idFuncionario === f.id);
    return {
      ...f,
      cargo: u ? (CARGO_MAP[u.cargo] ?? undefined) : undefined,
      identificador: u?.identificador,
      utilizadorId: u?.id,
    };
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => api.delete(`/funcionarios/${id}`),
    onSuccess: () => {
      toast.success("Funcionário eliminado");
      qc.invalidateQueries({ queryKey: ["funcionarios"] });
    },
    onError: (e: Error) => toast.error(e.message),
  });

  const columns: Column<FuncionarioRow>[] = [
    { key: "nome",   header: "Nome",        cell: (f) => <span className="font-medium">{f.nome}</span> },
    { key: "ident",  header: "Identificador", cell: (f) => f.identificador ? <code className="text-xs">{f.identificador}</code> : <span className="text-muted-foreground">—</span> },
    { key: "cargo",  header: "Cargo",       cell: (f) => f.cargo ? (
      <Badge variant="secondary" className={ROLE_BADGE[f.cargo]}>{ROLE_LABELS[f.cargo]}</Badge>
    ) : <span className="text-muted-foreground">—</span> },
    { key: "email",  header: "Email",       cell: (f) => f.email },
    { key: "tel",    header: "Telemóvel",   cell: (f) => f.telemovel },
    { key: "sh",     header: "€/hora",      cell: (f) => formatEUR(f.salario_hora) },
    { key: "sb",     header: "Bruto",       cell: (f) => formatEUR(f.salario_bruto) },
    { key: "he",     header: "Horas extra", cell: (f) => (
      <Badge variant={f.horas_extra > 0 ? "secondary" : "outline"}>{f.horas_extra} h</Badge>
    ) },
  ];

  return (
    <div>
      <PageHeader
        title="Funcionários"
        description="Gestão de colaboradores da oficina"
        actions={
          <Button onClick={() => { setEditing(null); setOpenForm(true); }}>
            <Plus className="h-4 w-4" /> Novo funcionário
          </Button>
        }
      />

      <DataTable
        data={rows}
        columns={columns}
        searchKeys={["nome", "email", "NIF"]}
        searchPlaceholder="Pesquisar por nome, email ou NIF…"
        isLoading={loadingF}
        rowActions={(f) => (
          <>
            <Button variant="ghost" size="icon" onClick={() => setHeFor(f)} title="Registar horas extra">
              <Clock className="h-4 w-4" />
            </Button>
            <ConfirmDialog
              trigger={<Button variant="ghost" size="icon" title="Pagar salário"><Banknote className="h-4 w-4" /></Button>}
              title="Pagar salário?"
              description={`As horas extra de ${f.nome} serão liquidadas.`}
              onConfirm={async () => {
                await api.patch(`/funcionarios/pagar/${f.id}`, {});
                toast.success("Salário pago");
                qc.invalidateQueries({ queryKey: ["funcionarios"] });
              }}
            />
            <Button variant="ghost" size="icon" onClick={() => { setEditing(f); setOpenForm(true); }}>
              <Pencil className="h-4 w-4" />
            </Button>
            <ConfirmDialog
              trigger={<Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>}
              title="Eliminar funcionário?"
              description={`Esta ação remove ${f.nome} permanentemente.`}
              destructive
              onConfirm={() => deleteMutation.mutate(f.id)}
            />
          </>
        )}
      />

      <FuncionarioForm
        key={editing?.id ?? "new"}
        open={openForm}
        onOpenChange={setOpenForm}
        initial={editing}
        onSaved={() => {
          qc.invalidateQueries({ queryKey: ["funcionarios"] });
          qc.invalidateQueries({ queryKey: ["utilizadores"] });
        }}
      />
      <HorasExtraDialog
        funcionario={heFor}
        onClose={() => setHeFor(null)}
        onSaved={() => qc.invalidateQueries({ queryKey: ["funcionarios"] })}
      />
    </div>
  );
}

function FuncionarioForm({
  open, onOpenChange, initial, onSaved,
}: {
  open: boolean; onOpenChange: (o: boolean) => void;
  initial: FuncionarioRow | null; onSaved: () => void;
}) {
  const form = useForm<CreateValues>({
    resolver: zodResolver(initial ? createSchema.extend({ password: z.string().optional() }) : createSchema) as any,
    values: initial ? {
      nome: initial.nome, telemovel: initial.telemovel, email: initial.email,
      data_nascimento: initial.data_nascimento, NISS: initial.NISS || (initial as any).niss || "", NIF: initial.NIF || (initial as any).nif || "",
      NUS: initial.NUS || (initial as any).nus || "", IBAN: (initial.IBAN || (initial as any).iban || "").replace(/^PT50/, ""), salario_hora: initial.salario_hora,
      salario_bruto: initial.salario_bruto, numero_porta: initial.numero_porta, rua: initial.rua,
      localidade: initial.localidade, codigo_postal: initial.codigo_postal,
      cargo: initial.cargo ?? "MECANICO", identificador: initial.identificador ?? "", password: "",
    } : {
      nome: "", telemovel: "", email: "", data_nascimento: "", NISS: "", NIF: "", NUS: "",
      IBAN: "", salario_hora: "" as unknown as number, salario_bruto: "" as unknown as number, numero_porta: "", rua: "", localidade: "",
      codigo_postal: "", cargo: "MECANICO", identificador: "", password: "",
    },
  });

  const saveMutation = useMutation({
    mutationFn: async (v: CreateValues) => {
      const { cargo, identificador, password, ...funcData } = v;
      if (initial) {
        // update funcionario
        await api.patch(`/funcionarios/${initial.id}`, {
          ...funcData, IBAN: "PT50" + funcData.IBAN,
          salario_liquido: funcData.salario_bruto,
          horas_extra: initial.horas_extra,
        });
        // update utilizador if exists
        if (initial.utilizadorId) {
          await api.patch(`/utilizadores/${initial.utilizadorId}`, {
            idFuncionario: initial.id, cargo: cargoToBackend(cargo), identificador,
          });
        }
      } else {
        const newFunc = await api.post<Funcionario>("/funcionarios", {
          ...funcData, IBAN: "PT50" + funcData.IBAN,
          salario_liquido: funcData.salario_bruto,
          horas_extra: 0,
        });
        await api.post("/utilizadores", {
          password, idFuncionario: newFunc.id, cargo: cargoToBackend(cargo), identificador,
        });
      }
    },
    onSuccess: () => {
      toast.success(initial ? "Funcionário atualizado" : "Funcionário criado");
      onOpenChange(false);
      onSaved();
    },
    onError: (e: Error) => toast.error(e.message),
  });

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle>{initial ? "Editar funcionário" : "Novo funcionário"}</DialogTitle>
          <DialogDescription>Todos os campos são obrigatórios.</DialogDescription>
        </DialogHeader>
        <form onSubmit={form.handleSubmit((v) => saveMutation.mutate(v))} className="grid grid-cols-1 gap-3 sm:grid-cols-2">
          <Field label="Nome" error={form.formState.errors.nome?.message} className="sm:col-span-2">
            <Input {...form.register("nome")} />
          </Field>
          <Field label="Identificador (login)" error={form.formState.errors.identificador?.message}>
            <Input {...form.register("identificador")} />
          </Field>
          {!initial && (
            <Field label="Password inicial" error={form.formState.errors.password?.message}>
              <Input type="password" autoComplete="new-password" {...form.register("password")} />
            </Field>
          )}
          <Field label="Cargo" error={form.formState.errors.cargo?.message}>
            <Select
              value={form.watch("cargo")}
              onValueChange={(v) => form.setValue("cargo", v as Role, { shouldValidate: true })}
            >
              <SelectTrigger><SelectValue /></SelectTrigger>
              <SelectContent>
                {ROLES.map((r) => <SelectItem key={r} value={r}>{ROLE_LABELS[r]}</SelectItem>)}
              </SelectContent>
            </Select>
          </Field>
          <Field label="Email" error={form.formState.errors.email?.message}>
            <Input type="email" {...form.register("email")} />
          </Field>
          <Field label="Telemóvel" error={form.formState.errors.telemovel?.message}>
            <Input {...form.register("telemovel")} />
          </Field>
          <Field label="NIF" error={form.formState.errors.NIF?.message}>
            <Input {...form.register("NIF")} maxLength={9} />
          </Field>
          <Field label="NISS" error={form.formState.errors.NISS?.message}>
            <Input {...form.register("NISS")} maxLength={11} />
          </Field>
          <Field label="NUS" error={form.formState.errors.NUS?.message}>
            <Input {...form.register("NUS")} />
          </Field>
          <Field label="IBAN" error={form.formState.errors.IBAN?.message}>
            <div className="flex h-10 rounded-md border border-input overflow-hidden ring-offset-background focus-within:ring-2 focus-within:ring-ring focus-within:ring-offset-2">
              <span className="flex items-center bg-muted px-3 text-sm text-muted-foreground select-none border-r border-input">
                PT50-
              </span>
              <input
                {...form.register("IBAN")}
                maxLength={21}
                className="flex-1 bg-background px-3 py-2 text-sm outline-none disabled:cursor-not-allowed disabled:opacity-50"
              />
            </div>
          </Field>
          <Field label="Data de nascimento" error={form.formState.errors.data_nascimento?.message}>
            <Input type="date" {...form.register("data_nascimento")} />
          </Field>
          <Field label="Número de porta" error={form.formState.errors.numero_porta?.message}>
            <Input {...form.register("numero_porta")} maxLength={5} />
          </Field>
          <Field label="Rua" error={form.formState.errors.rua?.message}>
            <Input {...form.register("rua")} />
          </Field>
          <Field label="Localidade" error={form.formState.errors.localidade?.message}>
            <Input {...form.register("localidade")} />
          </Field>
          <Field label="Código postal" error={form.formState.errors.codigo_postal?.message}>
            <Input {...form.register("codigo_postal")} placeholder="1234-567" />
          </Field>
          <Field label="Vencimento por hora (€/h)" error={form.formState.errors.salario_hora?.message}>
            <Input type="number" step="0.01" placeholder="0" {...form.register("salario_hora")} />
          </Field>
          <Field label="Vencimento bruto (€)" error={form.formState.errors.salario_bruto?.message}>
            <Input type="number" step="0.01" placeholder="0" {...form.register("salario_bruto")} />
          </Field>
          <DialogFooter className="sm:col-span-2 mt-2">
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

function HorasExtraDialog({
  funcionario, onClose, onSaved,
}: { funcionario: FuncionarioRow | null; onClose: () => void; onSaved: () => void }) {
  const [horas, setHoras] = useState("1");
  const [pending, setPending] = useState(false);

  const submit = async () => {
    if (!funcionario) return;
    const n = Math.round(Number(horas));
    if (!n || n <= 0) { toast.error("Horas inválidas"); return; }
    setPending(true);
    try {
      await api.patch(`/funcionarios/horas_extra/${funcionario.id}`, { horas_extra: n });
      toast.success("Horas extra registadas");
      onClose();
      onSaved();
    } catch (e) {
      toast.error(e instanceof Error ? e.message : "Erro");
    } finally {
      setPending(false);
    }
  };

  return (
    <Dialog open={!!funcionario} onOpenChange={(o) => !o && onClose()}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Horas extra — {funcionario?.nome}</DialogTitle>
        </DialogHeader>
        <div className="space-y-3">
          <div className="space-y-1.5">
            <Label>Nº de horas</Label>
            <Input type="number" min="1" value={horas} onChange={(e) => setHoras(e.target.value)} />
          </div>
        </div>
        <DialogFooter>
          <Button type="button" variant="outline" onClick={onClose}>Cancelar</Button>
          <Button onClick={submit} disabled={pending}>Registar</Button>
        </DialogFooter>
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

function cargoToBackend(role: Role): string {
  const map: Record<Role, string> = {
    GERENTE: "Gerente", GESTOR_STOCK: "GestorStock",
    SECRETARIA: "Secretaria", MECANICO: "Mecanico",
  };
  return map[role];
}
