## 2. AMBIGUIDADES IDENTIFICADAS

### 2.1 Ambiguidades sobre Cálculos

#### **Ambiguidade #1: Cálculo de Tempo Estimado**
- **Requisitos Gerais #3:** "Sempre que for criada uma nova OS, o sistema deverá conseguir calcular um tempo estimado para o término da reparação, baseado no tempo médio que uma reparação demora e o número de reparações que estão pendentes de diagnóstico e reparação."
- **Questões Sem Resposta:**
  - ❓ O tempo médio é por tipo de reparação ou global?
  - ❓ Inclui apenas o tempo do diagnóstico ou também preparação/papelada?
  - ❓ Conta com um ou dois mecânicos disponíveis?
  - ❓ Há paralização ou as reparações são sequenciais?

**Solução Proposta:**
```
Requisito refinado: "O sistema deve calcular tempo estimado baseado em:
- Tipo de reparação (tempo base de tabela)
- Número de OSs pendentes (precedência FIFO)
- Disponibilidade de mecânicos ativos
- Fórmula: tempo_base + (n_pendentes * tempo_médio) / n_mecânicos"
```

---


#### **Ambiguidade #5: Quando Exatamente Peças são Associadas?**
- **Requisitos Gerais #9:** "Quando um mecânico selecionar uma OS no estado 'Pendente de reparação' e associar uma peça do stock à OS, a quantidade dessa peça no stock deverá diminuir em uma unidade"
- **Requisitos Assistante #10:** "O sistema deve permitir que a secretária associe peças a uma OS que esteja no estado 'Pendente Pagamento' caso o mecânico não o tenha feito durante a reparação"
- **Questões Sem Resposta:**
  - ❓ Mecânico associa durante reparação (imediato) ou após terminar?
  - ❓ Se não associa, secretária associa depois. Stock diminui quando? No impacto de pegár na peça ou quando secretária a registar?
  - ❓ Se mecânico "revoga" peça (Req. Gerais #9), pode a secretária re-adiciona-la depois?

**Solução Proposta:**
```
Requisito refinado: "Fluxo de Peças:
1. Mecânico durante reparação: seleciona peça, stock diminui IMEDIATAMENTE
2. Se mecânico NÃO asociou: secretária na receção associa (para auditoria)
   Stock já diminuiu no passo 1, não diminui novamente
3. Se mecânico revoga: stock aumenta
4. Secretária não pode 'desfazer' peças, apenas registar as não-documentadas"
```

### 2.3 Ambiguidades sobre Validações

#### **Ambiguidade #7: "Estruturado e Validado"**
- **Requisitos Assistante #5:** "Todos os campos deverão ser validados e estruturados."
- **Questões Sem Resposta:**
  - ❓ Quais campos? Apenas clientes/trotinetes ou também OS?
  - ❓ Validações são regras de negócio? Formatos? (ex: email válido?)
  - ❓ Mensagens de erro específicas?

**Solução Proposta:**
```
Requisito refinado: "Validações obrigatórias:
- Cliente: nome não vazio, email é email válido, telefone é número, NIF é 9 dígitos
- Trotinete: marca/modelo não vazios, número de série único
- OS: descrição do problema não vazia, máximo 5 fotos
Mensagens de erro devem ser específicas (ex: 'NIF deve ter 9 dígitos')"
```

---

### 2.4 Ambiguidades sobre Relatórios

#### **Ambiguidade #8: Que Dados Aparecem em Relatório Financeiro?**
- **Requisitos Gerente #5-6:** "O sistema deverá permitir ao gerente consultar o estado financeiro da empresa, apresentando os movimentos financeiros realizados (...) deverá ainda disponibilizar mecanismos de filtragem por intervalo de datas e por tipo de gasto ou receita, permitindo visualizar os valores totais por categoria, como salários, gastos com peças, despesas mensais (energia, água e serviços de rede), lucros com mão de obra e lucros provenientes da venda de peças."
- **Questões Sem Resposta:**
  - ❓ Sistema importa dados de "energia, água e serviços de rede"? De onde?
  - ❓ O sistema só rastreia o que passa por SI (salários, peças, reparações) ou integra com contabilidade externa?
  - ❓ "Lucro com mão de obra" como é calculado? (Valor reparações - custo salário?)
  - ❓ Relatório é por OS? Por mês? Personalizável?

**Solução Proposta:**
```
Requisito refinado: "Relatório Financeiro oferece:
- Receitas: Σ(valor_reparações) + Σ(lucro_peças)
- Despesas: Σ(salários) + Σ(custos_peças) + [despesas_fixas_manual]
- Lucro líquido = Receitas - Despesas
- Despesas fixas (energia, água, renda) são introduzidas manualmente
- Periodos: dia, semana, mês, trimestre, ano
- Exportação em PDF/Excel"
```

---

### 2.5 Ambiguidades sobre Interfaces

#### **Ambiguidade #9: O que é 'Interface Individual' do Mecânico?**
- **Requisitos Gerais #12:** "A interface dos mecânicos deverá ser individual (para cada um deles)."
- **Questões Sem Resposta:**
  - ❓ Significa que cada mecânico vê apenas suas OSs? Ou todas?
  - ❓ Dados pessoais de outros mecânicos aparecem?
  - ❓ Histórico de trabalho é pessoal ou compartilhado?
  - ❓ Por que é requisito? Qual é a razão de negócio?

**Solução Proposta:**
```
Requisito refinado: "Interface do Mecânico:
- Dashboard mostra apenas suas OSs em andamento
- Pode consultar histórico de suas reparações (para stats)
- Não vê dados de outros mecânicos
- Pode consultar banco de peças/reparações (público)
- Fim: privacidade e foco no seu trabalho"
```

---

### 2.6 Ambiguidades sobre Segurança

#### **Ambiguidade #10: O que é "Tratado" em Alertas?**
- **Requisitos Gerais #6:** "Cada alerta deverá ter um campo que indique se já foi tratado ou não."
- **Questões Sem Resposta:**
  - ❓ "Tratado" significa lido? Ou ação foi executada?
  - ❓ Pode marcar como "tratado" sem fazer nada?
  - ❓ Sistema valida se ação foi realmente feita? (ex: se alerta é "pagar peça", valida pagamento?)

**Solução Proposta:**
```
Requisito refinado: "Estados de Alerta:
- 'Novo' - acaba de ser criado
- 'Lido' - utilizador viu o alerta
- 'Tratado' - ação associada foi completa (ex: pagamento efetuado)
Marcação manual (sem validação): risco de alertas não-tratados"
```

---

## 3. ERROS FORMAIS IDENTIFICADOS


### 3.4 Erros de Completude

| Aspeto | Falta |
|---|---|
| **Autenticação** | Nenhum requisito sobre login/logout |
| **Backup de Dados** | Nenhum requisito sobre recuperação |

## 4. POSSÍVEIS MELHORIAS

### 4.1 Melhorias de Estrutura

#### **Melhoria #1: Criar Glossário de Termos**
Adicionar seção no início com definições:
- OS (Ordem de Serviço)
- Diagnóstico vs Reparação
- Peça vs Componente
- NIF, NISS, IBAN, NUS
- Estados da OS (lista completa com definições)

---

#### **Melhoria #2: Adicionar Diagrama de Máquina de Estados**
Visualizar todas as transições possíveis:
```
Pendente Diagnóstico → Pendente Reparação (se <50€)
Pendente Diagnóstico → Pendente Aprovação (se >50€)
Pendente Reparação → A Aguardar Peças (se peça falta)
Pendente Reparação → Pendente Pagamento (concluído)
Pendente Aprovação → Pendente Reparação (aprovado)
Pendente Aprovação → Rejeitada (recusado)
```

---

#### **Melhoria #3: Adicionar Matriz de Responsabilidades**
```
| Ação | Gerente | Secretária | Mecânico | Stock |
|------|---------|-----------|----------|-------|
| Criar OS | ✓ | ✓ | - | - |
| Aprovar Orçamento | - | ✓ | - | - |
| Executar Reparação | - | - | ✓ | - |
| Alterar Preços Peças | ✓ | - | - | ✓ |
| Finalizar Pagamento | ✓ | ✓ | - | - |
```

---

#### **Melhoria #4: Especificar Casos de Uso**
Adicionar use cases formais:
- UC1: Criar Ordem de Serviço
- UC2: Executar Diagnóstico
- UC3: Solicitar Aprovação Orçamento
- UC4: Registar Reparação
- UC5: Marcar Pagamento
- UC6: Gerar Relatório Financeiro

---

### 4.2 Melhorias de Clareza

#### **Melhoria #5: Separar "Deve Permitir" de "Deve Impedir"**
Requisitos atuais misturam ações permitidas e bloqueadas. Exemplo:
- **Permite:** "O sistema deve permitir registar a aprovação do orçamento"
- **Impede:** "O sistema deve impedir que dois mecânicos iniciem a mesma OS"

Seria mais claro ter duas seções:
- **Funcionalidades Permitidas**
- **Restrições e Bloqueios**

---

#### **Melhoria #6: Adicionar Critérios de Aceitação**
Cada requisito deveria ter "Como validar?". Exemplo:
```
Requisito: "O sistema deve calcular automaticamente o valor final da reparação"
Critério de Aceitação:
- ✓ Se OS tem 1 reparação (50€) + 2 peças (20€ cada), total = 90€
- ✓ Se peça é revogada, total é recalculado
- ✓ Valor aparece em Pendente Pagamento
```

---

#### **Melhoria #7: Adicionar Prioridades aos Requisitos**
Nem todos os requisitos têm igual importância:
- **P0 (Crítico):** Cálculo de reparações, aprovação orçamento, gestão stock
- **P1 (Importante):** Alertas, histórico trotinete, relatórios
- **P2 (Nice-to-have):** Estimativa tempo, checklist segurança, interface individual

---

#### **Melhoria #11: Adicionar Requisitos de Auditoria**
```
- Quem criou a OS?
- Quem aprovou orçamento?
- Quem registou pagamento?
- Quem alterou preços?
Histórico completo com timestamps
```

---

#### **Melhoria #12: Especificar Formatos de Exportação**
```
Relatórios devem ser exportáveis em:
- PDF (para imprimir)
- Excel (para análise)
- CSV (para integração)
```

---

#### **Melhoria #14: Adicionar Autorização Granular**
```
Substituir "O gerente pode fazer tudo" com:
- Gerente: CRUD Funcionários, Alterar Preços, Aprovar Devoluções
- Secretária: CRUD Clientes, Criar OS, Registar Pagamentos
- Mecânico: Consultar OSs, Registar Reparações
- Stock: CRUD Peças, Registar Entradas/Saídas
```

---

#### **Melhoria #15: Adicionar Política de Retenção de Dados**
```
- OSs: retenção indefinida (fins fiscais)
- Clientes: retenção enquanto ativos + 3 anos
- Logs: retenção 1 ano
```

---

### 4.5 Melhorias de UX

#### **Melhoria #16: Esclarecer Fluxo Visual**
```
Criar protótipos mostrando:
- Tela de criação de OS
- Tela de diagnóstico (com seleção de reparações)
- Tela de aprovação orçamento
- Dashboard de alertas
```

---


#### **Melhoria #18: Especificar Listagens**
```
Requisito vago: "O mecânico poderá escolher de entre duas listas de OS"
Melhorado: "Sistema mostra dois separadores:
  - Pendentes Diagnóstico: ordenadas por data (mais antigas primeiro)
  - Pendentes Reparação: ordenadas por data, com alerta se >24h parada"
```

---

### 4.6 Melhorias de Validação de Negócio

#### **Melhoria #19: Adicionar Regras de Cancelamento**
```
Quando pode uma OS ser cancelada?
- Antes de diagnóstico (qualquer hora)
- Após diagnóstico (requer aprovação cliente)
```

---

#### **Melhoria #20: Adicionar Fluxo de Devoluções**
```
Se cliente não quer reparação após diagnóstico:
- Sistema retorna peças ao stock
- Registar motivo de cancelamento
- Cobrar apenas tempo diagnóstico?
```

---

### 4.7 Melhorias de Rastreabilidade

#### **Melhoria #23: Adicionar Rastreio de Peças por Trotinete**
```
Histórico completo:
- Quais peças foram substituídas?
- Quando?
- Por qual mecânico?
- Qual foi o custo?
```

---

#### **Melhoria #24: Adicionar Rastreio de Garantia de Peças**
```
Se peça é marcada "Possível defeito":
- Registar fornecedor
- Data de compra
- Período de garantia
- Automático contacto fornecedor?
```

---

#### **Melhoria #25: Adicionar Rastreio de Alertas**
```
Quando alerta é criado:
- Data/hora
- Quem o recebeu
- Quando foi lido
- Quando foi resolvido (se aplicável)
```

---
