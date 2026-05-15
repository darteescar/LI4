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
    <div className="flex min-h-screen items-center justify-center p-4"
  style={{
    background: "radial-gradient(ellipse at top left, hsl(var(--primary) / 0.4) 0%, hsl(var(--primary) / 0.15) 40%, hsl(var(--background)) 70%)"
  }}>
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
                placeholder="João Martins"
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
        </CardContent>
      </Card>
    </div>
  );
}
