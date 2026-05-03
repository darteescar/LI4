import { createContext, useCallback, useContext, useEffect, useState, type ReactNode } from "react";
import type { Funcionario, Role } from "@/lib/types";
import { getCurrentUser, login as doLogin, logout as doLogout } from "@/services/auth";

interface AuthContextValue {
  user: Funcionario | null;
  role: Role | undefined;
  loading: boolean;
  login: (identificador: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<Funcionario | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setUser(getCurrentUser());
    setLoading(false);
  }, []);

  const login = useCallback(async (identificador: string, password: string) => {
    const u = await doLogin(identificador, password);
    setUser(u);
  }, []);

  const logout = useCallback(() => {
    doLogout();
    setUser(null);
  }, []);

  return (
    <AuthContext.Provider value={{ user, role: user?.cargo, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth deve ser usado dentro de AuthProvider");
  return ctx;
}
