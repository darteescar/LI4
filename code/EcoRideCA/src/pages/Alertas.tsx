import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { Bell, CheckCheck, Trash2, CheckCircle2 } from "lucide-react";
import { toast } from "sonner";

import { PageHeader } from "@/components/layout/PageHeader";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ConfirmDialog } from "@/components/confirm-dialog";

import { api } from "@/services/api";
import { useAuth } from "@/context/AuthContext";
import { formatDateTime } from "@/lib/format";

interface Notificacao {
  id: number;
  descricao: string;
  data_emissao: string;
  id_remetente: number;
  id_destinatario: number;
  estado: "NAOLIDA" | "LIDA" | "TRATADA";
  data_horaTratada: string | null;
}

export default function Alertas() {
  const { user } = useAuth();
  const qc = useQueryClient();
  const [filtro, setFiltro] = useState<"TODAS" | "NAO_LIDAS">("NAO_LIDAS");

  const { data: todas = [], isLoading } = useQuery<Notificacao[]>({
    queryKey: ["notificacoes", user?.id],
    queryFn: () => api.get<Notificacao[]>(`/notificacoes/minhas`),
    enabled: !!user,
  });

  const alertas = filtro === "NAO_LIDAS" ? todas.filter((n) => n.estado === "NAOLIDA") : todas;
  const naoLidasCount = todas.filter((n) => n.estado === "NAOLIDA").length;

  const marcarLida = useMutation({
    mutationFn: (id: number) => api.patch(`/notificacoes/lida/${id}`, {}),
    onSuccess: () => qc.invalidateQueries({ queryKey: ["notificacoes", user?.id] }),
  });

  const marcarTratada = useMutation({
    mutationFn: (id: number) => api.patch(`/notificacoes/tratada/${id}`, {}),
    onSuccess: () => qc.invalidateQueries({ queryKey: ["notificacoes", user?.id] }),
  });

  const remover = useMutation({
    mutationFn: (id: number) => api.delete(`/notificacoes/${id}`),
    onSuccess: () => qc.invalidateQueries({ queryKey: ["notificacoes", user?.id] }),
  });

  const marcarTodas = async () => {
    await Promise.all(
      todas.filter((n) => n.estado === "NAOLIDA").map((n) => api.patch(`/notificacoes/lida/${n.id}`, {}))
    );
    toast.success("Todos os alertas marcados como lidos");
    qc.invalidateQueries({ queryKey: ["notificacoes", user?.id] });
  };

  return (
    <div>
      <PageHeader
        title="Centro de Alertas"
        description="Notificações relevantes para a tua função"
        actions={
          <Button variant="outline" onClick={marcarTodas} disabled={naoLidasCount === 0}>
            <CheckCheck className="h-4 w-4" /> Marcar todas como lidas
          </Button>
        }
      />

      <div className="mb-4">
        <Tabs value={filtro} onValueChange={(v) => setFiltro(v as typeof filtro)}>
          <TabsList>
            <TabsTrigger value="NAO_LIDAS">
              Não lidas {filtro === "NAO_LIDAS" && naoLidasCount > 0 && (
                <Badge variant="secondary" className="ml-2">{naoLidasCount}</Badge>
              )}
            </TabsTrigger>
            <TabsTrigger value="TODAS">Todas</TabsTrigger>
          </TabsList>
        </Tabs>
      </div>

      {isLoading ? (
        <p className="text-sm text-muted-foreground">A carregar…</p>
      ) : alertas.length === 0 ? (
        <Card>
          <CardContent className="flex flex-col items-center gap-2 py-16 text-center">
            <Bell className="h-10 w-10 text-muted-foreground" />
            <p className="text-sm text-muted-foreground">
              {filtro === "NAO_LIDAS" ? "Não tens alertas por ler. Tudo em dia!" : "Sem alertas registados."}
            </p>
          </CardContent>
        </Card>
      ) : (
        <div className="space-y-2">
          {alertas.map((a) => (
            <Card key={a.id} className={a.estado !== "NAOLIDA" ? "opacity-70" : ""}>
              <CardContent className="flex items-start gap-3 p-4">
                <div className={`flex h-9 w-9 shrink-0 items-center justify-center rounded-md ${
                  a.estado === "NAOLIDA" ? "bg-warning-soft text-warning" : "bg-muted text-muted-foreground"
                }`}>
                  <Bell className="h-4 w-4" />
                </div>
                <div className="min-w-0 flex-1">
                  <div className="flex flex-wrap items-center gap-2">
                    {a.estado === "NAOLIDA" && (
                      <Badge className="bg-primary text-primary-foreground text-xs">Novo</Badge>
                    )}
                    {a.estado === "TRATADA" && (
                      <Badge variant="secondary" className="bg-success-soft text-success text-xs">Tratada</Badge>
                    )}
                    {a.estado === "LIDA" && (
                      <Badge variant="secondary" className="bg-info-soft text-info text-xs">Lida</Badge>
                    )}
                    <span className="text-xs text-muted-foreground">
                      {formatDateTime(a.data_emissao)}
                    </span>
                  </div>
                  <p className="mt-1 text-sm">{a.descricao}</p>
                </div>
                <div className="flex shrink-0 items-center gap-1">
                  {a.estado !== "TRATADA" && (
                    <Button variant="ghost" size="icon" title="Marcar como tratado"
                      onClick={() => marcarTratada.mutate(a.id)}>
                      <CheckCircle2 className="h-4 w-4 text-success" />
                    </Button>
                  )}
                  {a.estado === "NAOLIDA" && (
                    <Button variant="ghost" size="icon" title="Marcar como lida"
                      onClick={() => marcarLida.mutate(a.id)}>
                      <CheckCheck className="h-4 w-4" />
                    </Button>
                  )}
                  <ConfirmDialog
                    trigger={
                      <Button variant="ghost" size="icon" title="Remover">
                        <Trash2 className="h-4 w-4 text-destructive" />
                      </Button>
                    }
                    title="Remover este alerta?"
                    destructive
                    onConfirm={() => remover.mutate(a.id)}
                  />
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
}
