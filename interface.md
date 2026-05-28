Estrutura do Frontend — EcoRideCA
Ficheiros de configuração (raiz)
Ficheiro	Função
vite.config.ts	Bundler e dev server. Porta 8080, proxy /api → backend:7000 em desenvolvimento
tailwind.config.ts	Sistema de cores e estilos customizados (cores por papel, dark mode via CSS class)
components.json	Configuração do shadcn/ui — define aliases @/components, @/lib, @/hooks
nginx.conf	Servidor de produção: serve /dist, faz proxy /api/ → backend, fallback SPA
Dockerfile	Build multi-stage: (1) Node 22 compila React, (2) nginx serve o /dist
index.html	HTML raiz — ponto de entrada que carrega main.tsx
package.json	Dependências: React 18, React Router 6, TanStack Query 5, React Hook Form + Zod, Recharts, shadcn/ui
src/ — organização por camadas
Entrada da aplicação
Ficheiro	Função
main.tsx	Ponto de entrada — monta <App/> no DOM
App.tsx	Router principal — define todas as rotas e envolve-as com ProtectedLayout
index.css	Estilos globais Tailwind (reset + variáveis CSS)
services/ — comunicação com a API
Ficheiro	Função
api.ts	Cliente HTTP genérico. Injeta o token Authorization em todos os pedidos. Redireciona para /login em 401. Expõe api.get, api.post, api.patch, api.delete
auth.ts	Lógica de login/logout. Guarda token e dados do utilizador em localStorage. Converte cargos do backend (string) para enums do frontend (ex: "Gerente" → "GERENTE")
context/ — estado global
Ficheiro	Função
AuthContext.tsx	Context que fornece { user, role, loading, login, logout } a toda a aplicação via hook useAuth()
lib/ — utilitários e contratos de dados
Ficheiro	Função
types.ts	Todos os tipos TypeScript do domínio: Funcionario, Cliente, Trotinete, OS, Peca, Alerta, etc. e todos os enums de estado
permissions.ts	RBAC — mapa Area → Role[] que define quais papéis acedem a cada secção. Usado por ProtectedLayout e sidebar
validators.ts	Schemas Zod para validação de formulários (funcionário, cliente, trotinete, peça, etc.)
format.ts	Formatação localizada pt-PT: formatEUR(), formatDate(), formatDateTime()
utils.ts	cn() — merge de classes Tailwind sem conflitos (usa clsx + tailwind-merge)
hooks/ — lógica reutilizável
Ficheiro	Função
use-toast.ts	Hook para mostrar toasts (notificações temporárias)
use-mobile.tsx	Detecta se o ecrã é mobile (breakpoint 768px)
components/ — componentes reutilizáveis
Ficheiro/pasta	Função
data-table.tsx	Tabela genérica DataTable<T> com pesquisa, colunas configuráveis e ações por linha. Reutilizada em quase todas as páginas
confirm-dialog.tsx	Dialog de confirmação reutilizável ("Tens a certeza?")
state-badge.tsx	Badge colorido para estados (ex: EmProgresso → amarelo, Concluida → verde)
NavLink.tsx	Link de navegação com highlight de rota ativa
layout/	
ProtectedLayout.tsx	Wrapper de rota: verifica autenticação + permissão da area, redireciona se necessário
AppHeader.tsx	Barra de topo: título da página, botão logout, avatar
AppSidebar.tsx	Sidebar esquerda: links agrupados por secção, visibilidade filtrada por papel
PageHeader.tsx	Cabeçalho de página com título, subtítulo e slot para botões de ação
StockTabs.tsx	Tabs de navegação dentro da secção Stock
ui/	~50 componentes primitivos da biblioteca shadcn/ui (Button, Input, Dialog, Table, Select, Card, etc.) — não se editam diretamente
pages/ — páginas (rotas)
Ficheiro/pasta	Função
Login.tsx	Formulário de autenticação
Dashboard.tsx	Painel principal com estatísticas e atalhos, filtrados por papel
Funcionarios.tsx	CRUD de funcionários (só GERENTE)
Clientes.tsx	CRUD de clientes
Trotinetes.tsx	Gestão de trotinetes
Reparacoes.tsx	Catálogo de reparações disponíveis
Fornecedores.tsx	Gestão de fornecedores
Financeiro.tsx	Movimentos financeiros e análise (receitas/despesas)
Alertas.tsx	Notificações do utilizador
NotFound.tsx	Página 404
OS/	
OS/List.tsx	Lista de ordens de serviço com filtros
OS/New.tsx	Criação de nova OS
OS/Detail.tsx	Detalhe de OS: diagnóstico, conserto, pagamento, checklist
Stock/	
Stock/Pecas.tsx	Catálogo de peças
Stock/Entradas.tsx	Entradas de stock
Stock/Encomendas.tsx	Gestão de encomendas a fornecedores
Stock/Devolucoes.tsx	Devoluções de stock
Stock/Defeitos.tsx	Registo de defeitos
Como as camadas interagem

App.tsx (rotas)
  └── ProtectedLayout (autenticação + permissões)
        └── AppSidebar + AppHeader
              └── Page (ex: Funcionarios.tsx)
                    ├── useAuth()          → AuthContext
                    ├── api.get/post/...   → services/api.ts → backend
                    ├── useQuery/Mutation  → TanStack Query (cache)
                    ├── useForm + Zod      → validação de formulários
                    └── DataTable<T>       → renderização tabular
Resumo em 3 linhas: o frontend é uma SPA React organizada em 3 camadas — serviços (comunicação API), estado global (AuthContext), e páginas (uma por funcionalidade). Cada página busca dados com TanStack Query, valida formulários com React Hook Form + Zod, e renderiza com componentes shadcn/ui. O acesso a cada rota é controlado por RBAC em lib/permissions.ts.