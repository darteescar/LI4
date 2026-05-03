import type { Role } from "./types";

export type Area =
  | "dashboard"
  | "funcionarios"
  | "financeiro"
  | "clientes"
  | "trotinetes"
  | "os"
  | "reparacoes"
  | "stock"
  | "fornecedores"
  | "alertas";

const PERMISSIONS: Record<Area, Role[]> = {
  dashboard: ["GERENTE", "GESTOR_STOCK", "SECRETARIA", "MECANICO"],
  funcionarios: ["GERENTE"],
  financeiro: ["GERENTE"],
  clientes: ["GERENTE", "SECRETARIA"],
  trotinetes: ["GERENTE", "SECRETARIA"],
  os: ["GERENTE", "SECRETARIA", "MECANICO"],
  reparacoes: ["GERENTE"],
  stock: ["GERENTE", "GESTOR_STOCK"],
  fornecedores: ["GERENTE", "GESTOR_STOCK"],
  alertas: ["GERENTE", "GESTOR_STOCK", "SECRETARIA", "MECANICO"],
};

export function canAccess(role: Role | undefined, area: Area): boolean {
  if (!role) return false;
  return PERMISSIONS[area].includes(role);
}
