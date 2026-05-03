import { NavLink, useLocation } from "react-router-dom";
import { cn } from "@/lib/utils";

const TABS = [
  { to: "/stock/pecas", label: "Peças" },
  { to: "/stock/entradas", label: "Entradas" },
  { to: "/stock/defeitos", label: "Defeitos" },
  { to: "/stock/devolucoes", label: "Devoluções" },
  { to: "/stock/encomendas", label: "Encomendas" },
];

export function StockTabs() {
  const location = useLocation();
  return (
    <div className="mb-4 flex flex-wrap gap-1 rounded-lg bg-muted p-1">
      {TABS.map((t) => {
        const active = location.pathname.startsWith(t.to);
        return (
          <NavLink
            key={t.to}
            to={t.to}
            className={cn(
              "rounded-md px-3 py-1.5 text-sm font-medium transition-colors",
              active
                ? "bg-background text-foreground shadow-sm"
                : "text-muted-foreground hover:text-foreground",
            )}
          >
            {t.label}
          </NavLink>
        );
      })}
    </div>
  );
}
