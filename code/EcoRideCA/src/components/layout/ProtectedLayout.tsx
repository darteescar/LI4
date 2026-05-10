import { Navigate, Outlet, useLocation } from "react-router-dom";
import { SidebarProvider, SidebarInset } from "@/components/ui/sidebar";
import { AppSidebar } from "./AppSidebar";
import { AppHeader } from "./AppHeader";
import { useAuth } from "@/context/AuthContext";
import { canAccess, type Area } from "@/lib/permissions";

interface ProtectedLayoutProps {
  area?: Area;
}

export function ProtectedLayout({ area }: ProtectedLayoutProps) {
  const { user, role, loading } = useAuth();
  const location = useLocation();

  if (loading) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <div className="text-sm text-muted-foreground">A carregar…</div>
      </div>
    );
  }

  if (!user) {
    return <Navigate to="/login" replace state={{ from: location.pathname }} />;
  }

  if (area && !canAccess(role, area)) {
    return <Navigate to="/" replace />;
  }

  return (
    <SidebarProvider>
      <div className="flex min-h-screen w-full bg-background">
        <AppSidebar />
        <SidebarInset className="flex flex-1 flex-col">
          <AppHeader />
          <main className="flex-1 p-4 md:p-6 animate-fade-in">
            <Outlet />
          </main>
        </SidebarInset>
      </div>
    </SidebarProvider>
  );
}
