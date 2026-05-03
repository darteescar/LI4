import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import {
  ClipboardList, Package, AlertTriangle, Users, Wrench, ArrowRight,
} from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { PageHeader } from "@/components/layout/PageHeader";
import { useAuth } from "@/context/AuthContext";
import { ROLE_LABELS } from "@/lib/types";
import { getDB } from "@/lib/mock-db";
import { canAccess } from "@/lib/permissions";

export default function Dashboard() {
  const { user, role } = useAuth();
  const [stats, setStats] = useState({
    osAbertas: 0, alertas: 0, pecasBaixoStock: 0, totalClientes: 0,
  });

  useEffect(() => {
    const db = getDB();
    setStats({
      osAbertas: db.ordens.filter((o) => !["PAGA", "CANCELADA"].includes(o.estado)).length,
      alertas: db.alertas.filter((a) => !a.lida).length,
      pecasBaixoStock: db.pecas.filter((p) => p.stockAtual < p.stockMinimo).length,
      totalClientes: db.clientes.length,
    });
  }, []);

  const quickActions = [
    { label: "Nova ordem de serviço", to: "/os/nova", icon: ClipboardList, area: "os" as const },
    { label: "Novo cliente", to: "/clientes", icon: Users, area: "clientes" as const },
    { label: "Gerir stock", to: "/stock", icon: Package, area: "stock" as const },
    { label: "Catálogo de reparações", to: "/reparacoes", icon: Wrench, area: "reparacoes" as const },
  ].filter((a) => canAccess(role, a.area));

  const cards = [
    { label: "OS abertas", value: stats.osAbertas, icon: ClipboardList, color: "text-info bg-info-soft", area: "os" as const },
    { label: "Alertas pendentes", value: stats.alertas, icon: AlertTriangle, color: "text-warning bg-warning-soft", area: "alertas" as const },
    { label: "Peças abaixo do mínimo", value: stats.pecasBaixoStock, icon: Package, color: "text-destructive bg-destructive/10", area: "stock" as const },
    { label: "Clientes registados", value: stats.totalClientes, icon: Users, color: "text-primary bg-primary-soft", area: "clientes" as const },
  ].filter((c) => canAccess(role, c.area));

  return (
    <div>
      <PageHeader
        title={`Bem-vindo, ${user?.nome.split(" ")[0]}`}
        description={`A operar como ${user ? ROLE_LABELS[user.cargo] : ""}`}
      />

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
        {cards.map((c) => (
          <Card key={c.label} className="shadow-sm transition-shadow hover:shadow-md">
            <CardContent className="flex items-center gap-4 p-5">
              <div className={`flex h-11 w-11 items-center justify-center rounded-lg ${c.color}`}>
                <c.icon className="h-5 w-5" />
              </div>
              <div>
                <div className="text-2xl font-semibold">{c.value}</div>
                <div className="text-xs text-muted-foreground">{c.label}</div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {quickActions.length > 0 && (
        <Card className="mt-6 shadow-sm">
          <CardHeader>
            <CardTitle className="text-base">Acessos rápidos</CardTitle>
          </CardHeader>
          <CardContent className="grid gap-2 sm:grid-cols-2 lg:grid-cols-4">
            {quickActions.map((a) => (
              <Button
                key={a.to}
                asChild
                variant="outline"
                className="h-auto justify-between py-3"
              >
                <Link to={a.to}>
                  <span className="flex items-center gap-2">
                    <a.icon className="h-4 w-4 text-primary" />
                    {a.label}
                  </span>
                  <ArrowRight className="h-4 w-4 text-muted-foreground" />
                </Link>
              </Button>
            ))}
          </CardContent>
        </Card>
      )}
    </div>
  );
}
