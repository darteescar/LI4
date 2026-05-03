import { useState } from "react";
import { LogOut, Bell, KeyRound } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import type { z } from "zod";
import { toast } from "sonner";

import { SidebarTrigger } from "@/components/ui/sidebar";
import { Button } from "@/components/ui/button";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import {
  DropdownMenu, DropdownMenuContent, DropdownMenuItem,
  DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import {
  Dialog, DialogContent, DialogDescription, DialogFooter,
  DialogHeader, DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Alert, AlertDescription } from "@/components/ui/alert";

import { useAuth } from "@/context/AuthContext";
import { ROLE_LABELS } from "@/lib/types";
import { changePasswordSchema } from "@/lib/validators";
import { changePassword } from "@/services/auth";

type ChangePwdForm = z.infer<typeof changePasswordSchema>;

export function AppHeader() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [pwdOpen, setPwdOpen] = useState(false);

  const initials = user?.nome
    .split(" ")
    .map((p) => p[0])
    .slice(0, 2)
    .join("")
    .toUpperCase() ?? "?";

  const handleLogout = () => {
    logout();
    navigate("/login", { replace: true });
  };

  return (
    <header className="sticky top-0 z-30 flex h-14 items-center gap-3 border-b bg-card/80 px-3 backdrop-blur">
      <SidebarTrigger />
      <div className="flex-1" />
      <Button variant="ghost" size="icon" onClick={() => navigate("/alertas")} aria-label="Alertas">
        <Bell className="h-4 w-4" />
      </Button>
      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <button className="flex items-center gap-2 rounded-md px-2 py-1 hover:bg-muted transition-colors">
            <Avatar className="h-8 w-8">
              <AvatarFallback className="bg-primary text-primary-foreground text-xs">
                {initials}
              </AvatarFallback>
            </Avatar>
            <div className="hidden text-left sm:block">
              <div className="text-sm font-medium leading-tight">{user?.nome}</div>
              <div className="text-xs text-muted-foreground leading-tight">
                {user ? ROLE_LABELS[user.cargo] : ""}
              </div>
            </div>
          </button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="end" className="w-56">
          <DropdownMenuLabel>{user?.nome}</DropdownMenuLabel>
          <DropdownMenuSeparator />
          <DropdownMenuItem onClick={() => setPwdOpen(true)}>
            <KeyRound className="mr-2 h-4 w-4" />
            Alterar password
          </DropdownMenuItem>
          <DropdownMenuItem onClick={handleLogout}>
            <LogOut className="mr-2 h-4 w-4" />
            Terminar sessão
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>

      <ChangePasswordDialog open={pwdOpen} onOpenChange={setPwdOpen} userId={user?.id} />
    </header>
  );
}

function ChangePasswordDialog({
  open, onOpenChange, userId,
}: { open: boolean; onOpenChange: (o: boolean) => void; userId?: string }) {
  const [error, setError] = useState<string | null>(null);
  const form = useForm<ChangePwdForm>({
    resolver: zodResolver(changePasswordSchema),
    defaultValues: { currentPassword: "", newPassword: "", confirm: "" },
  });

  const onSubmit = async (data: ChangePwdForm) => {
    setError(null);
    if (!userId) return;
    try {
      await changePassword(userId, data.currentPassword, data.newPassword, data.confirm);
      toast.success("Password alterada com sucesso");
      form.reset();
      onOpenChange(false);
    } catch (e) {
      setError(e instanceof Error ? e.message : "Erro a alterar password");
    }
  };

  return (
    <Dialog open={open} onOpenChange={(o) => { onOpenChange(o); if (!o) { form.reset(); setError(null); } }}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Alterar password</DialogTitle>
          <DialogDescription>
            Indica a tua password atual e define uma nova.
          </DialogDescription>
        </DialogHeader>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-3">
          <div className="space-y-1.5">
            <Label htmlFor="currentPassword">Password atual</Label>
            <Input id="currentPassword" type="password" autoComplete="current-password"
              {...form.register("currentPassword")} />
            {form.formState.errors.currentPassword && (
              <p className="text-xs text-destructive">{form.formState.errors.currentPassword.message}</p>
            )}
          </div>
          <div className="space-y-1.5">
            <Label htmlFor="newPassword">Nova password</Label>
            <Input id="newPassword" type="password" autoComplete="new-password"
              placeholder="Mínimo 4 caracteres" {...form.register("newPassword")} />
            {form.formState.errors.newPassword && (
              <p className="text-xs text-destructive">{form.formState.errors.newPassword.message}</p>
            )}
          </div>
          <div className="space-y-1.5">
            <Label htmlFor="confirm">Confirmar nova password</Label>
            <Input id="confirm" type="password" autoComplete="new-password"
              {...form.register("confirm")} />
            {form.formState.errors.confirm && (
              <p className="text-xs text-destructive">{form.formState.errors.confirm.message}</p>
            )}
          </div>
          {error && (
            <Alert variant="destructive">
              <AlertDescription>{error}</AlertDescription>
            </Alert>
          )}
          <DialogFooter>
            <Button type="button" variant="outline" onClick={() => onOpenChange(false)}>Cancelar</Button>
            <Button type="submit" disabled={form.formState.isSubmitting}>
              {form.formState.isSubmitting ? "A alterar…" : "Alterar password"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
