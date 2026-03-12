# Análise de Requisitos - EcoRide Solutions

**Data da Análise:** 12 de março de 2026

---

## 1. ESTRUTURA E ORGANIZAÇÃO DOS DOCUMENTOS

### Observações Positivas
✅ **Estrutura clara em diferentes perspetivas:** O levantamento de requisitos foi feito através de entrevistas com diferentes papéis (gerente, mecânico, secretária), o que é uma abordagem sólida.

✅ **Documentação bem formatada:** Os ficheiros LaTeX têm uma apresentação profissional e consistente.

✅ **Múltiplas fontes de informação:** Combinação de entrevistas, observação em campo e questionário a clientes oferece perspetivas diversas.

### Oportunidades de Melhoria na Estrutura

⚠️ **Falta de documento de síntese executiva:** Não existe um documento que agregue todos os requisitos de forma consolidada e hierarquizada.

⚠️ **Questionário de clientes incompleto:** O documento `quest_clientes.tex` apresenta apenas o template sem resultados ou análise dos dados recolhidos.

⚠️ **Falta de relatório de investigação completo:** O ficheiro `relatorio_quest_clientes.tex` não aparenta existir com conteúdo.

**Recomendação:** Criar um documento centralizado `REQUISITOS_CONSOLIDADOS.md` ou similar que agregue todos os 28 requisitos com priorização, dependências e casos de uso.

---

## 2. ANÁLISE DE AMBIGUIDADES NOS REQUISITOS

### Ambiguidades Identificadas

#### **A. Registo de Danos Prévios (Req. #2 - Investigação)**
**Problema:** A funcionalidade não especifica:
- ❓ Quantas fotos são aceitáveis por ordem de serviço?
- ❓ Qual é o tamanho máximo de arquivo?
- ❓ Quem pode ver estas fotos (apenas staff ou também cliente)?
- ❓ As fotos são vinculativas legalmente ou apenas documentação interna?

**Clarificação Necessária:**
```
Requisito refinado: "O sistema deve permitir registar até 5 fotos digitais 
de danos prévios na abertura da ordem de serviço, visíveis apenas ao staff 
interno, para fins de proteção contra reclamações de danos pós-entrega."
```

---

#### **B. Taxas de Armazenamento (Req. #23 - Gerente)**
**Problema:** Especificação incompleta:
- ❓ "Após 7 dias" conta em dias civis ou dias úteis?
- ❓ Como é comunicada a taxa ao cliente? (SMS, email, carta?)
- ❓ A taxa é autoapplicável ou requer confirmação manual?
- ❓ Existe limite máximo de taxa acumulada?

**Clarificação Necessária:**
```
Requisito refinado: "Após 7 dias civis desde a notificação do fim da 
reparação, aplica-se automaticamente taxa de 3€/dia. Sistema gera 
notificação por email informando cliente. Taxa máxima é 21€ (7 dias). 
Se cliente não busca após 30 dias, ordem passa a estado 'Pendente Levantamento'."
```

---

#### **C. Peças "Pré-aprovadas de Baixo Valor" (Req. #5 - Investigação)**
**Problema:** Critério vago:
- ❓ Qual é o valor limite para "baixo valor"?
- ❓ Como funciona o "pré-aprovado"? Quem aprova?
- ❓ Existe lista pré-definida ou é dinâmica?
- ❓ Funciona para todos os clientes ou apenas alguns?

**Clarificação Necessária:**
```
Requisito refinado: "Peças com custo ≤ 15€ podem ser adicionadas direto 
pelo mecânico à ordem sem aprovação. Acima de 15€, requer aprovação 
gerente (que valida contra lista de peças 'pré-autorizadas' por cliente 
ou categoria de serviço)."
```

---

#### **D. Perfis de Utilizador (Req. #26 - Gerente)**
**Problema:** Permissões não detalhadas:
- ❓ Pode o mecânico ver informações financeiras?
- ❓ Pode a secretária alterar preços de serviços?
- ❓ Que relatórios cada perfil pode aceder?
- ❓ Existe auditoria de quem viu que dados?

**Clarificação Necessária:**
```
Definir matriz explícita de permissões:
- Gerente: Acesso total, controlo de preços, relatórios financeiros
- Secretária: Gestão de clientes, ordens, não vê custos internos
- Mecânico: Ver sua ficha de trabalho, atualizar estado, não acessa financeiro
```

---

#### **E. "Data Prevista de Chegada" (Req. #6 - Investigação)**
**Problema:** Fluxo de atualização unclear:
- ❓ Quem atualiza a ETA? (gerente, secretária?)
- ❓ Como integra com sistema de encomendas? (manual ou API?)
- ❓ A ETA atualiza automaticamente ou requer confirmação?
- ❓ Se atraso é superior a X dias, notifica cliente?

**Clarificação Necessária:**
```
Requisito refinado: "Gerente regista ETA na encomenda. Se ETA é 
atualizada com atraso >3 dias, sistema notifica secretária para 
contactar cliente. Estado passa a 'Aguardando Peças - Atrasado'."
```

---

#### **F. Número de Série em Peças (Req. #13 - Gerente)**
**Problema:** Ambiguidade sobre quando é obrigatório:
- ❓ Todas as peças >70€ têm número de série?
- ❓ Se um fornecedor não fornece, o que fazer?
- ❓ É armazenado por unidade ou por lote?
- ❓ Está associado ao histórico de garantia?

**Clarificação Necessária:**
```
Requisito refinado: "Para peças com custo >70€, número de série é 
obrigatório. Se fornecedor não fornece, usar referência de lote + 
data entrega como identificador único. Rastreia garantia por unidade."
```

---

### Resumo de Ambiguidades Críticas
| Requisito | Nível de Risco | Impacto |
|-----------|---|---|
| #2 - Danos Prévios | ⚠️ Médio | Funcionalidade parcial |
| #5 - Peças Pré-aprovadas | 🔴 Alto | Não implementável sem clarificação |
| #6 - Data Prevista | ⚠️ Médio | Fluxo incompleto |
| #13 - Número de Série | ⚠️ Médio | Dados inconsistentes |
| #23 - Taxa Armazenamento | 🔴 Alto | Impacto financeiro |
| #26 - Perfis Utilizador | 🔴 Alto | Segurança comprometida |

---

## 3. GRANULARIDADE DOS REQUISITOS

### Análise de Granularidade

#### Requisitos Muito Genéricos (REQUER DECOMPOSIÇÃO)
- **Req. #1:** "Visualizar estado de todas as ordens de serviço"
  - 🔴 Muito amplo: falta especificar formatos de visualização, filtros exatos, alertas
  - **Sugestão:** Dividir em:
    - #1a: Dashboard de ordens (painel principal)
    - #1b: Filtros avançados (por estado, data, cliente, mecânico, localização física)
    - #1c: Alertas automáticos (ordens paradas >24h, clientes aguardando, ATAs vencidas)

- **Req. #24:** "Gerar relatórios sobre número de reparações, tempos médios, rentabilidade e trotinetes paradas"
  - 🔴 Agrupa 4 tipos de relatórios distintos
  - **Sugestão:** Dividir em:
    - #24a: Relatório de volume (n.º reparações por período, por tipo)
    - #24b: Relatório de eficiência (tempo médio, taxa conclusão, gargalos)
    - #24c: Relatório financeiro (rentabilidade por tipo serviço, margem média)
    - #24d: Relatório de stock (trotinetes paradas, dias médios retenção)

- **Req. #28:** "Calcular custos e lucros com filtro temporal"
  - 🔴 Demasiado alta nível
  - **Sugestão:** Especificar:
    - Granularidade temporal (dia, semana, mês, trimestre, ano)
    - Categorias de despesa (salários, peças, renda, utilidades, etc.)
    - Formato de apresentação (gráficos, tabelas, dashboards)
    - Exportação (PDF, Excel, integração fiscal?)

#### Requisitos com Granularidade Apropriada
✅ **Req. #3:** "Registar marca, modelo, número de série, estado à entrada..."
- Bem definido, testável, específico

✅ **Req. #4:** "Registar dados pessoais (nome, morada, telemóvel, email...)"
- Clara lista de campos, sem ambiguidade

✅ **Req. #9:** "Bloquear reparação até aprovação de orçamento"
- Condição clara, comportamento bem definido

#### Requisitos Muito Granulares (PODE CONSOLIDAR-SE)
- **Req. #14 + #15:** Ambas relacionadas com gestão de saída de peças
  - 🟡 Poderiam ser combinadas num único requisito sobre "Gestão de Movimento de Peças"

- **Req. #16 + #17:** Dados de fornecedores
  - 🟡 Podem agrupar-se em "Cadastro Completo de Fornecedores"

### Tabela de Resumo de Granularidade

| Requisito | Nível Granularidade | Status | Recomendação |
|-----------|---|---|---|
| #1 | Baixa (muito genérico) | 🔴 | **Decomposição** |
| #2 | Média-Alta | ✅ | Mantém com clarificações |
| #3 | Alta | ✅ | OK |
| #4 | Alta | ✅ | OK |
| #5 | Baixa (conceito vago) | ⚠️ | **Clarificar** |
| #6 | Alta | ✅ | OK |
| #7 | Média | ⚠️ | Especificar fluxo |
| #8 | Alta | ✅ | OK |
| #9 | Alta | ✅ | OK |
| #10 | Alta | ✅ | OK |
| #11 | Alta | ✅ | OK |
| #12 | Média | ⚠️ | Clarificar estrutura tabela |
| #13 | Média-Alta | ⚠️ | Clarificar quando é obrigatório |
| #14-15 | Média | ⚠️ | **Consolidar em 1 req** |
| #16-17 | Média | ⚠️ | **Consolidar em 1 req** |
| #18 | Média-Baixa | ⚠️ | Especificar algoritmo |
| #19 | Alta | ✅ | OK |
| #20 | Alta | ✅ | OK |
| #21 | Alta | ✅ | OK |
| #22 | Alta | ✅ | OK |
| #23 | Média-Baixa | 🔴 | **Clarificar completamente** |
| #24 | Muito Baixa | 🔴 | **Decomposição em 4** |
| #25 | Alta | ✅ | OK |
| #26 | Muito Baixa | 🔴 | **Matriz de permissões** |
| #27 | Média-Alta | ⚠️ | Clarificar o que é "ação crítica" |
| #28 | Muito Baixa | 🔴 | **Decompor em 4-5 req** |

---

## 4. REQUISITOS AUSENTES OU SUBESPECIFICADOS

### A. Gestão de Comunicação com Cliente

⚠️ **Gap:** Não há especificação sobre canais de comunicação preferidos
- Req. #22 menciona "notificação" mas não como será feita
- Questionário sugere múltiplos canais (SMS, Email, WhatsApp, etc.)

**Novo Requisito Proposto:**
```
#29: O sistema deve permitir registar o canal de comunicação preferido 
do cliente (SMS, Email, WhatsApp, Telefone) e usar automaticamente esse 
canal para notificações de: 
- Orçamento pronto
- Reparação concluída
- Atraso em encomendas de peças
- Taxa de armazenamento aplicada
```

---

### B. Recuperação de Peças com Defeito de Fábrica

⚠️ **Gap:** Mencionado na entrevista do mecânico, mas não tem requisito formal
- Mecânico: "Já aconteceu montar uma peça nova e perceber que não funciona"
- Sem processo formal de devolução ao fornecedor

**Novo Requisito Proposto:**
```
#30: Sistema deve permitir marcar peça como "Defeituosa ao Montar" 
durante diagnóstico. Gera automaticamente:
1. Flag na ordem de serviço
2. Processo de devolução ao fornecedor
3. Notificação para considerar orçamento alterado
4. Rastreio se fornecedor é recorrente em devoluções
```

---

### C. Integração com Sistema de Faturação Externo

⚠️ **Gap:** Sistema menciona integração com programa fiscal externo, mas não especifica
- Como são exportados dados?
- Que formato?
- Validações antes de envio?

**Novo Requisito Proposto:**
```
#31: Sistema deve gerar ficheiro de exportação (formato XML ou CSV) 
compatível com software de faturação certificado pela AT, contendo:
- Dados cliente e NIF
- Descrição serviços/peças
- Valores IVA incluído
- Timestamp da geração
Com validação antes de exportação para evitar erros fiscais.
```

---

### D. Gestão de Alertas e Escalação

⚠️ **Gap:** Não há conceito formal de alertas ou SLAs
- Que acontece se ordem está parada >2 dias?
- Quem é notificado?
- Existe timeout de trotinetes em armazém?

**Novo Requisito Proposto:**
```
#32: Sistema deve gerar alertas automáticos:
- Verde: Ordem <1 dia de retenção
- Amarela: Ordem 1-3 dias parada
- Vermelha: Ordem >3 dias sem movimento
E notificar gerente com recomendação de ação (escalar, contactar cliente, etc.)
```

---

### E. Rastreio de Garantias de Peças

⚠️ **Gap:** Número de série é registado mas sem rastreio de garantia
- Quando expira?
- Que cobertura tem?
- Processo de reclamação?

**Novo Requisito Proposto:**
```
#33: Cada peça com número de série deve ter:
- Data de compra e garantia (meses)
- Automático cálculo de data fim garantia
- Alertas quando se aproxima fim de cobertura
- Histórico de devoluções ao fornecedor ligadas à garantia
```

---

### F. Relatório Técnico para Cliente (Mencionado em Questionário)

✅ **Presente em Questionário:** Clientes querem "relatório técnico detalhado da reparação"

⚠️ **Ausente em Requisitos:** Não há especificação formal

**Novo Requisito Proposto:**
```
#34: Sistema deve gerar "Relatório Técnico de Reparação" em PDF 
contendo:
- Data entrada/saída
- Diagnóstico realizado
- Peças substituídas (com código)
- Testes de funcionamento executados
- Assinatura digital do mecânico
- Disponível para download pelo cliente
```

---

### G. Histórico de Reparações por Trotinete

⚠️ **Gap:** Não há requisito para "ver histórico completo de uma trotinete"
- Quando foi reparada anterior?
- Quais foram os problemas?
- Qual foi o custo?

**Novo Requisito Proposto:**
```
#35: Sistema deve manter histórico completo de cada trotinete (por 
número de série) mostrando:
- Todas as reparações prévias (data, tipo, custo)
- Peças trocadas
- Padrões de falha recorrentes
- Alertas se trotinete volta com mesmo problema <30 dias
```

---

## 5. COBERTURA POR STAKEHOLDER

### Gerente (João Martins)
- ✅ Requisitos bem cobertos (dashboard, financeiro, stock)
- 🔴 Falta: Alertas automáticos, rastreio de fornecedores problemáticos

### Secretária (Silvina Matagal)
- ✅ Bem representada (gestão de clientes, ordens, comunicação)
- 🔴 Falta: Integração com telefonia/email para notificações

### Mecânico (Ramiro Ramalho)
- ✅ Fluxo de trabalho coberto
- 🔴 Falta: Feedback quando peça é defeituosa, histórico técnico de trotinete

### Clientes (Questionário)
- ✅ Comunicação coberta
- 🔴 Falta: Acesso a relatório técnico, tracking de reparação (pedir password?)

---

## 6. DEPENDÊNCIAS ENTRE REQUISITOS

### Cadeia de Dependências Críticas
```
Req. #9 (Bloquear sem aprovação)
  ↓ depende de ↓
Req. #22 (Notificar cliente) 
  ↓ depende de ↓
Req. #29 (Canal comunicação)

Req. #14 (Saída de peças)
  ↓ depende de ↓
Req. #13 (Entrada e gestão stock)
  ↓ depende de ↓
Req. #18 (Gerar lista encomendas)

Req. #24 (Relatórios)
  ↓ depende de ↓
Req. #27 (Auditoria de ações)
  ↓ depende de ↓
Req. #26 (Perfis e permissões)
```

### Requisitos que Conflituam ou Precisam Sincronização
- **#5 + #14:** Peças "pré-aprovadas" e "saída automática" precisam estar sincronizados
- **#23 + #22:** Taxa de armazenamento depende de ter notificado cliente primeiro
- **#12 + #25:** Tabela de intervenções deve ter preços atualizados

---

## 7. RECOMENDAÇÕES FINAIS

### 🔴 Crítico - Resolver Antes de Desenvolvimento

1. **Criar Matriz de Permissões Explícita** (Req. #26)
   - Especificar exatamente o que cada perfil pode fazer/ver

2. **Clarificar Limites de "Baixo Valor"** (Req. #5)
   - Definir threshold em €

3. **Detalhar Sistema de Alertas** (Novo Req. #32)
   - Defini SLAs, tempos limite, escalações

4. **Consolidar Relatórios Financeiros** (Req. #28)
   - Separar em requisitos granulares por tipo de relatório

### 🟡 Médio - Validar com Stakeholders

1. **Confirmar Casos de Uso de Peças Defeituosas** (Novo Req. #30)
2. **Detalhar Integração com Sistema Fiscal** (Novo Req. #31)
3. **Definir Política de Retenção de Trotinetes** (Relacionado Req. #23)

### 🟢 Baixa Prioridade - Implementar Depois

1. **Rastreio de Garantias** (Novo Req. #33)
2. **Relatório Técnico para Cliente** (Novo Req. #34)
3. **Histórico por Trotinete** (Novo Req. #35)

---

## 8. CHECKLIST DE VALIDAÇÃO

- [ ] Todos os 28 requisitos têm testes de aceitação definidos?
- [ ] Cada ambiguidade foi clarificada com o cliente?
- [ ] Novos requisitos (#29-35) foram validados?
- [ ] Matriz de permissões foi aprovada?
- [ ] Limites de valores (baixo valor, taxa, etc.) foram confirmados?
- [ ] Integração com sistema fiscal foi especificada?
- [ ] Fluxos de alerta e escalação foram desenhados?
- [ ] Casos de erro foram considerados?
- [ ] Performance de relatórios foi avaliada?
- [ ] Segurança de dados foi considerada?

---

## 9. PRÓXIMOS PASSOS

1. **Workshop de Validação:** Reunir gerente, secretária e mecânico para confirmar ambiguidades
2. **Refinar Requisitos:** Incorporar feedback e criar documento final de requisitos
3. **Design de Telas:** Começar prototipagem com base em requisitos clarificados
4. **Planeamento de Sprints:** Priorizar requisitos por dependências e valor

