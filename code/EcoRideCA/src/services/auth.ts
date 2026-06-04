import type { Role } from "@/lib/types";

const TOKEN_KEY = "ecoride-token";
const USER_KEY  = "ecoride-user";

const CARGO_TO_ROLE: Record<string, Role> = {
  Gerente:    "GERENTE",
  GestorStock: "GESTOR_STOCK",
  Secretaria: "SECRETARIA",
  Mecanico:   "MECANICO",
};

export interface AuthUser {
  id: number;
  idUtilizador: number;
  nome: string;
  cargo: Role;
}

async function extractMessage(res: Response, fallback: string): Promise<string> {
  const text = await res.text();
  if (!text) return fallback;
  try {
    const json = JSON.parse(text) as { mensagem?: string; message?: string };
    return json.mensagem ?? json.message ?? text;
  } catch {
    return text;
  }
}

export async function login(identificador: string, password: string): Promise<AuthUser> {
  const res = await fetch("/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ identificador, password }),
  });
  if (!res.ok) {
    throw new Error(await extractMessage(res, "Credenciais inválidas"));
  }
  const { token, cargo, idFuncionario, idUtilizador } = await res.json() as {
    token: string; cargo: string; idFuncionario: number; idUtilizador: number;
  };

  localStorage.setItem(TOKEN_KEY, token);

  const role = CARGO_TO_ROLE[cargo] ?? "MECANICO";

  const funcRes = await fetch(`/api/funcionarios/${idFuncionario}`, {
    headers: { "Authorization": token },
  });
  const nome: string = funcRes.ok
    ? ((await funcRes.json()) as { nome: string }).nome
    : identificador;

  const user: AuthUser = { id: idFuncionario, idUtilizador, nome, cargo: role };
  localStorage.setItem(USER_KEY, JSON.stringify(user));
  return user;
}

export function logout() {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(USER_KEY);
}

export async function changePassword(
  idUtilizador: number,
  currentPassword: string,
  newPassword: string,
): Promise<void> {
  const token = localStorage.getItem(TOKEN_KEY);
  const res = await fetch(`/api/utilizadores/${idUtilizador}/password`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: token } : {}),
    },
    body: JSON.stringify({ currentPassword, newPassword }),
  });
  if (!res.ok) {
    throw new Error(await extractMessage(res, "Erro a alterar password"));
  }
}

export function getCurrentUser(): AuthUser | null {
  try {
    const raw = localStorage.getItem(USER_KEY);
    if (!raw) return null;
    return JSON.parse(raw) as AuthUser;
  } catch {
    return null;
  }
}
