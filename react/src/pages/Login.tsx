import { useState } from "react";
import { useNavigate, Navigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import logoEcoRide from "@/assets/logo.jpeg";

import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Alert, AlertDescription } from "@/components/ui/alert";
import { useAuth } from "@/context/AuthContext";
import { loginSchema } from "@/lib/validators";

type LoginForm = z.infer<typeof loginSchema>;

const DEMO_USERS = [
  { id: "ana", role: "Gerente" },
  { id: "bea", role: "Secretária" },
  { id: "carlos", role: "Gestor de Stock" },
  { id: "diogo", role: "Mecânico" },
];

export default function Login() {
  const { user, login } = useAuth();
  const navigate = useNavigate();
  const [error, setError] = useState<string | null>(null);

  const form = useForm<LoginForm>({
    resolver: zodResolver(loginSchema),
    defaultValues: { identificador: "", password: "" },
  });

  if (user) return <Navigate to="/" replace />;

  const onSubmit = async (data: LoginForm) => {
    setError(null);
    try {
      await login(data.identificador, data.password);
      navigate("/", { replace: true });
    } catch (e) {
      setError(e instanceof Error ? e.message : "Erro ao iniciar sessão");
    }
  };

  const fillDemo = (id: string) => {
    form.setValue("identificador", id);
    form.setValue("password", "1234");
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-gradient-to-br from-primary-soft via-background to-background p-4">
      <Card className="w-full max-w-md shadow-lg">
        <CardHeader className="space-y-3 text-center">
          <div className="mx-auto flex h-24 w-24 items-center justify-center rounded-2xl bg-white overflow-hidden shadow-md">
            <img src={logoEcoRide} alt="EcoRide Solutions" className="h-24 w-24 object-cover" />
          </div>
          <div>
            <CardTitle className="text-2xl">EcoRide Solutions</CardTitle>
            <CardDescription>Acesso ao sistema de gestão</CardDescription>
          </div>
        </CardHeader>
        <CardContent>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="identificador">Identificador</Label>
              <Input
                id="identificador"
                autoComplete="username"
                placeholder="ex: ana"
                {...form.register("identificador")}
              />
              {form.formState.errors.identificador && (
                <p className="text-xs text-destructive">{form.formState.errors.identificador.message}</p>
              )}
            </div>
            <div className="space-y-2">
              <Label htmlFor="password">Password</Label>
              <Input
                id="password"
                type="password"
                autoComplete="current-password"
                placeholder="••••"
                {...form.register("password")}
              />
              {form.formState.errors.password && (
                <p className="text-xs text-destructive">{form.formState.errors.password.message}</p>
              )}
            </div>

            {error && (
              <Alert variant="destructive">
                <AlertDescription>{error}</AlertDescription>
              </Alert>
            )}

            <Button type="submit" className="w-full" disabled={form.formState.isSubmitting}>
              {form.formState.isSubmitting ? "A entrar…" : "Entrar"}
            </Button>
          </form>

          <div className="mt-6 rounded-md border bg-muted/40 p-3">
            <p className="mb-2 text-xs font-medium text-muted-foreground">
              Utilizadores de demonstração (password: <code className="font-mono">1234</code>)
            </p>
            <div className="grid grid-cols-2 gap-2">
              {DEMO_USERS.map((u) => (
                <button
                  key={u.id}
                  type="button"
                  onClick={() => fillDemo(u.id)}
                  className="rounded border bg-card px-2 py-1.5 text-left text-xs hover:bg-accent transition-colors"
                >
                  <div className="font-medium">{u.id}</div>
                  <div className="text-muted-foreground">{u.role}</div>
                </button>
              ))}
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
