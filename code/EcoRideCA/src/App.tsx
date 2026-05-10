import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { Toaster } from "@/components/ui/toaster";
import { TooltipProvider } from "@/components/ui/tooltip";

import { AuthProvider } from "@/context/AuthContext";
import { ProtectedLayout } from "@/components/layout/ProtectedLayout";

import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Funcionarios from "./pages/Funcionarios";
import Clientes from "./pages/Clientes";
import Trotinetes from "./pages/Trotinetes";
import Fornecedores from "./pages/Fornecedores";
import Reparacoes from "./pages/Reparacoes";
import OSList from "./pages/OS/List";
import NewOS from "./pages/OS/New";
import OSDetail from "./pages/OS/Detail";
import StockPecas from "./pages/Stock/Pecas";
import StockEntradas from "./pages/Stock/Entradas";
import StockDevolucoes from "./pages/Stock/Devolucoes";
import StockEncomendas from "./pages/Stock/Encomendas";
import StockDefeitos from "./pages/Stock/Defeitos";
import Financeiro from "./pages/Financeiro";
import Alertas from "./pages/Alertas";
import { Placeholder } from "./pages/Placeholder";
import NotFound from "./pages/NotFound.tsx";

const queryClient = new QueryClient();

const App = () => (
  <QueryClientProvider client={queryClient}>
    <TooltipProvider>
      <Toaster />
      <Sonner position="top-right" richColors />
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            <Route path="/login" element={<Login />} />

            <Route element={<ProtectedLayout area="dashboard" />}>
              <Route path="/" element={<Dashboard />} />
            </Route>
            <Route element={<ProtectedLayout area="funcionarios" />}>
              <Route path="/funcionarios" element={<Funcionarios />} />
            </Route>
            <Route element={<ProtectedLayout area="clientes" />}>
              <Route path="/clientes" element={<Clientes />} />
            </Route>
            <Route element={<ProtectedLayout area="trotinetes" />}>
              <Route path="/trotinetes" element={<Trotinetes />} />
            </Route>
            <Route element={<ProtectedLayout area="fornecedores" />}>
              <Route path="/fornecedores" element={<Fornecedores />} />
            </Route>
            <Route element={<ProtectedLayout area="reparacoes" />}>
              <Route path="/reparacoes" element={<Reparacoes />} />
            </Route>
            <Route element={<ProtectedLayout area="os" />}>
              <Route path="/os" element={<OSList />} />
              <Route path="/os/nova" element={<NewOS />} />
              <Route path="/os/:id" element={<OSDetail />} />
            </Route>
            <Route element={<ProtectedLayout area="stock" />}>
              <Route path="/stock" element={<Navigate to="/stock/pecas" replace />} />
              <Route path="/stock/pecas" element={<StockPecas />} />
              <Route path="/stock/entradas" element={<StockEntradas />} />
              <Route path="/stock/defeitos" element={<StockDefeitos />} />
              <Route path="/stock/devolucoes" element={<StockDevolucoes />} />
              <Route path="/stock/encomendas" element={<StockEncomendas />} />
            </Route>
            <Route element={<ProtectedLayout area="financeiro" />}>
              <Route path="/financeiro" element={<Financeiro />} />
            </Route>
            <Route element={<ProtectedLayout area="alertas" />}>
              <Route path="/alertas" element={<Alertas />} />
            </Route>

            <Route path="*" element={<NotFound />} />
          </Routes>
        </AuthProvider>
      </BrowserRouter>
    </TooltipProvider>
  </QueryClientProvider>
);

export default App;
