import { cn } from "@/lib/utils";
import type { EstadoOS, EstadoDevolucao, EstadoEncomenda } from "@/lib/types";

type AnyState = EstadoOS | EstadoDevolucao | EstadoEncomenda | string;

const VARIANTS: Record<string, string> = {
  // OS
  REGISTADA: "bg-muted text-muted-foreground",
  EM_DIAGNOSTICO: "bg-info-soft text-info",
  AGUARDA_APROVACAO: "bg-warning-soft text-warning",
  APROVADA: "bg-primary-soft text-primary",
  EM_REPARACAO: "bg-info-soft text-info",
  AGUARDA_PECAS: "bg-warning-soft text-warning",
  CONCLUIDA: "bg-success-soft text-success",
  AGUARDA_PAGAMENTO: "bg-warning-soft text-warning",
  PAGA: "bg-success-soft text-success",
  CANCELADA: "bg-destructive/10 text-destructive",
  // Devoluções
  PENDENTE: "bg-warning-soft text-warning",
  DEVOLVIDA: "bg-success-soft text-success",
  INVALIDA: "bg-destructive/10 text-destructive",
  // Encomendas
  RASCUNHO: "bg-muted text-muted-foreground",
  ENVIADA: "bg-info-soft text-info",
  RECEBIDA: "bg-success-soft text-success",
};

const LABELS: Record<string, string> = {
  REGISTADA: "Registada",
  EM_DIAGNOSTICO: "Em diagnóstico",
  AGUARDA_APROVACAO: "Aguarda aprovação",
  APROVADA: "Aprovada",
  EM_REPARACAO: "Em reparação",
  AGUARDA_PECAS: "Aguarda peças",
  CONCLUIDA: "Concluída",
  AGUARDA_PAGAMENTO: "Aguarda pagamento",
  PAGA: "Paga",
  CANCELADA: "Cancelada",
  PENDENTE: "Pendente",
  DEVOLVIDA: "Devolvida",
  INVALIDA: "Inválida",
  RASCUNHO: "Rascunho",
  ENVIADA: "Enviada",
  RECEBIDA: "Recebida",
};

export function StateBadge({ state, className }: { state: AnyState; className?: string }) {
  return (
    <span
      className={cn(
        "inline-flex items-center rounded-full px-2 py-0.5 text-xs font-medium",
        VARIANTS[state] ?? "bg-muted text-muted-foreground",
        className,
      )}
    >
      {LABELS[state] ?? state}
    </span>
  );
}
