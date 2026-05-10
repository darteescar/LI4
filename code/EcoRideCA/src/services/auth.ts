// Mock auth — passwords agora ficam guardadas em cada Funcionário (campo `password`).
// O gerente define a password inicial ao criar o funcionário. Cada funcionário pode
// depois alterar a sua própria password (mas tem de fornecer a password atual).
import { getDB, persist } from "@/lib/mock-db";
import type { Funcionario } from "@/lib/types";

const SESSION_KEY = "oficina-session-v1";

export interface Session {
  userId: string;
}

export async function login(identificador: string, password: string): Promise<Funcionario> {
  await new Promise((r) => setTimeout(r, 200));
  const id = identificador.trim().toLowerCase();
  const user = getDB().funcionarios.find(
    (f) => f.identificador.toLowerCase() === id && f.ativo,
  );
  if (!user) throw new Error("Credenciais inválidas");
  if (!user.password || user.password !== password) throw new Error("Credenciais inválidas");
  localStorage.setItem(SESSION_KEY, JSON.stringify({ userId: user.id } satisfies Session));
  return user;
}

/**
 * Permite ao próprio funcionário alterar a sua password. Tem de indicar a password
 * atual correta. O gerente NÃO pode alterar a password de outros funcionários.
 */
export async function changePassword(
  userId: string,
  currentPassword: string,
  newPassword: string,
  confirm: string,
): Promise<void> {
  await new Promise((r) => setTimeout(r, 200));
  if (newPassword !== confirm) throw new Error("As passwords não coincidem");
  if (newPassword.length < 4) throw new Error("Nova password tem de ter pelo menos 4 caracteres");
  const db = getDB();
  const i = db.funcionarios.findIndex((f) => f.id === userId);
  if (i < 0) throw new Error("Utilizador não encontrado");
  const user = db.funcionarios[i];
  if (user.password !== currentPassword) throw new Error("Password atual incorreta");
  db.funcionarios[i] = { ...user, password: newPassword };
  persist();
}

export function logout() {
  localStorage.removeItem(SESSION_KEY);
}

export function getCurrentUser(): Funcionario | null {
  try {
    const raw = localStorage.getItem(SESSION_KEY);
    if (!raw) return null;
    const session = JSON.parse(raw) as Session;
    return getDB().funcionarios.find((f) => f.id === session.userId) ?? null;
  } catch {
    return null;
  }
}
