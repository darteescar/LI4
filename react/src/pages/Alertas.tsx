import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Bell, CheckCheck, Trash2, ArrowRight, RefreshCw, CheckCircle2 } from "lucide-react";
import { toast } from "sonner";

import { PageHeader } from "@/components/layout/PageHeader";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ConfirmDialog } from "@/components/confirm-dialog";

import { alertasService, TIPO_ALERTA_LABELS } from "@/services/alertas";
import { useAuth } from "@/context/AuthContext";
import { formatDateTime } from "@/lib/format";
import type { Alerta, TipoAlerta } from "@/lib/types";

const TIPO_VARIANT: Record<TipoAlerta, string> = {
  ORCAMENTO_APROVAR: "bg-warning-soft text-warning",
  OS_PAGAMENTO: "bg-info-soft text-info",
  PECA_FALTA: "bg-warning-soft text-warning",
  DEFEITO_REPORTADO: "bg-destructive/10 text-destructive",
  STOCK_BAIXO: "bg-destructive/10 text-destructive",
};

export default function Alertas() {
  const { role } = useAuth();
  const [alertas, setAlertas] = useState<Alerta[]>([]);
  const [filtro, setFiltro] = useState<"TODAS" | "NAO_LIDAS">("NAO_LIDAS");

  const reload = async () => {
    const list = await alertasService.listForRole(role, {
      apenasNaoLidas: filtro === "NAO_LIDAS",
    });
    setAlertas(list);
  };

  useEffect(() => { reload(); /* eslint-disable-next-line */ }, [role, filtro]);

  const verificarStock = async () => {
    const n = await alertasService.gerarAlertasStockBaixo();
    if (n > 0) toast.success(`${n} novo(s) alerta(s) de stock baixo`);
    else toast.info("Nenhum novo alerta de stock");
    reload();
  };

  const marcarLida = async (id: string) => {
    await alertasService.marcarLida(id);
    reload();
  };

  const marcarTodas = async () => {
    await alertasService.marcarTodasLidas(role);
    toast.success("Todos os alertas marcados como lidos");
    reload();
  };

  const remover = async (id: string) => {
    await alertasService.remover(id);
    reload();
  };

  const naoLidasCount = alertas.filter((a) => !a.lida).length;

  return (
    <div>
      <PageHeader
        title="Centro de Alertas"
        description="Notificações relevantes para a tua função"
        actions={
          <div className="flex flex-wrap items-center gap-2">
            <Button variant="outline" onClick={verificarStock}>
              <RefreshCw className="h-4 w-4" /> Verificar stock
            </Button>
            <Button variant="outline" onClick={marcarTodas} disabled={naoLidasCount === 0}>
              <CheckCheck className="h-4 w-4" /> Marcar todas como lidas
            </Button>
          </div>
        }
      />

      <div className="mb-4 flex items-center justify-between">
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

      {alertas.length === 0 ? (
        <Card>
          <CardContent className="flex flex-col items-center gap-2 py-16 text-center">
            <Bell className="h-10 w-10 text-muted-foreground" />
            <p className="text-sm text-muted-foreground">
              {filtro === "NAO_LIDAS"
                ? "Não tens alertas por ler. Tudo em dia!"
                : "Sem alertas registados."}
            </p>
          </CardContent>
        </Card>
      ) : (
        <div className="space-y-2">
          {alertas.map((a) => (
            <Card key={a.id} className={a.lida ? "opacity-70" : ""}>
              <CardContent className="flex items-start gap-3 p-4">
                <div className={`flex h-9 w-9 shrink-0 items-center justify-center rounded-md ${TIPO_VARIANT[a.tipo]}`}>
                  <Bell className="h-4 w-4" />
                </div>
                <div className="min-w-0 flex-1">
                  <div className="flex flex-wrap items-center gap-2">
                    <Badge variant="outline" className="text-xs">
                      {TIPO_ALERTA_LABELS[a.tipo]}
                    </Badge>
                    {!a.lida && (
                      <Badge className="bg-primary text-primary-foreground text-xs">Novo</Badge>
                    )}
                    <span className="text-xs text-muted-foreground">
                      {formatDateTime(a.dataISO)}
                    </span>
                  </div>
                  <p className="mt-1 text-sm">{a.mensagem}</p>
                  {a.tratada && (
                    <Badge variant="secondary" className="mt-1 bg-success-soft text-success text-[10px]">
                      Tratado
                    </Badge>
                  )}
                </div>
                <div className="flex shrink-0 items-center gap-1">
                  {a.rota && (
                    <Button variant="ghost" size="sm" asChild onClick={() => marcarLida(a.id)}>
                      <Link to={a.rota}>
                        Abrir <ArrowRight className="h-3.5 w-3.5" />
                      </Link>
                    </Button>
                  )}
                  {!a.tratada && (
                    <Button variant="ghost" size="icon" title="Marcar como tratado"
                      onClick={async () => { await alertasService.marcarTratada(a.id); reload(); }}>
                      <CheckCircle2 className="h-4 w-4 text-success" />
                    </Button>
                  )}
                  {!a.lida && (
                    <Button variant="ghost" size="icon" title="Marcar como lida"
                      onClick={() => marcarLida(a.id)}>
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
                    onConfirm={() => remover(a.id)}
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
