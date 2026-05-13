import { useLocation } from "react-router-dom";
import {
  LayoutDashboard, Users, ClipboardList, Package,
  Truck, Settings2, Bell, Wallet, UserCog, Bike,
} from "lucide-react";
import logoEcoRide from "@/assets/logo.jpeg";
import { NavLink } from "@/components/NavLink";
import {
  Sidebar, SidebarContent, SidebarGroup, SidebarGroupContent, SidebarGroupLabel,
  SidebarMenu, SidebarMenuButton, SidebarMenuItem, SidebarHeader, SidebarFooter,
  useSidebar,
} from "@/components/ui/sidebar";
import { useAuth } from "@/context/AuthContext";
import { canAccess, type Area } from "@/lib/permissions";

interface NavItem {
  title: string;
  url: string;
  icon: typeof LayoutDashboard;
  area: Area;
}

const NAV_GROUPS: { label: string; items: NavItem[] }[] = [
  {
    label: "Geral",
    items: [
      { title: "Dashboard", url: "/", icon: LayoutDashboard, area: "dashboard" },
      { title: "Alertas", url: "/alertas", icon: Bell, area: "alertas" },
    ],
  },
  {
    label: "Operações",
    items: [
      { title: "Ordens de Serviço", url: "/os", icon: ClipboardList, area: "os" },
      { title: "Clientes", url: "/clientes", icon: Users, area: "clientes" },
      { title: "Trotinetes", url: "/trotinetes", icon: Bike, area: "trotinetes" },
      { title: "Reparações", url: "/reparacoes", icon: Settings2, area: "reparacoes" },
    ],
  },
  {
    label: "Stock",
    items: [
      { title: "Peças & Stock", url: "/stock/pecas", icon: Package, area: "stock" },
      { title: "Fornecedores", url: "/fornecedores", icon: Truck, area: "fornecedores" },
    ],
  },
  {
    label: "Administração",
    items: [
      { title: "Funcionários", url: "/funcionarios", icon: UserCog, area: "funcionarios" },
      { title: "Financeiro", url: "/financeiro", icon: Wallet, area: "financeiro" },
    ],
  },
];

export function AppSidebar() {
  const { state } = useSidebar();
  const collapsed = state === "collapsed";
  const { role } = useAuth();
  const location = useLocation();

  const isActive = (url: string) =>
    url === "/" ? location.pathname === "/" : location.pathname === url || location.pathname.startsWith(url + "/") || (url === "/stock/pecas" && location.pathname.startsWith("/stock"));

  return (
    <Sidebar collapsible="icon" className="border-r">
      <SidebarHeader className="border-b border-sidebar-border">
        <div className={`flex items-center gap-2 py-2 ${collapsed ? "px-0 justify-center" : "px-2"}`}>
          <div className="flex h-8 w-8 items-center justify-center rounded-md bg-white shrink-0 overflow-hidden">
            <img src={logoEcoRide} alt="EcoRide Solutions" className="h-8 w-8 object-cover" />
          </div>
          {!collapsed && (
            <div className="flex flex-col leading-tight">
              <span className="text-sm font-semibold text-sidebar-foreground">EcoRide Solutions</span>
              <span className="text-xs text-sidebar-foreground/60">Sistema de gestão</span>
            </div>
          )}
        </div>
      </SidebarHeader>

      <SidebarContent>
        {NAV_GROUPS.map((group) => {
          const visibleItems = group.items.filter((item) => canAccess(role, item.area));
          if (visibleItems.length === 0) return null;
          return (
            <SidebarGroup key={group.label}>
              {!collapsed && <SidebarGroupLabel>{group.label}</SidebarGroupLabel>}
              <SidebarGroupContent>
                <SidebarMenu>
                  {visibleItems.map((item) => (
                    <SidebarMenuItem key={item.url}>
                      <SidebarMenuButton asChild isActive={isActive(item.url)} tooltip={item.title}>
                        <NavLink
                          to={item.url}
                          end={item.url === "/"}
                          className="flex items-center gap-2"
                        >
                          <item.icon className="h-4 w-4 shrink-0" />
                          <span>{item.title}</span>
                        </NavLink>
                      </SidebarMenuButton>
                    </SidebarMenuItem>
                  ))}
                </SidebarMenu>
              </SidebarGroupContent>
            </SidebarGroup>
          );
        })}
      </SidebarContent>
    </Sidebar>
  );
}
