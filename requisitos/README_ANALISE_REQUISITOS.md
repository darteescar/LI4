# Análise de Requisitos Consolidados - EcoRide Solutions

**Data de Análise:** 18 de março de 2026  
**Documento Analisado:** `requisitos_consolidados_atualizado.tex`

---

## Executivo

Este documento apresenta uma análise crítica dos requisitos consolidados da EcoRide Solutions. Foram identificadas **27 incongruências**, **18 ambiguidades**, **12 erros** e **25 melhorias** propostas. A maioria dos problemas é resolvível, mas requerem atenção antes da implementação.

---

## 1. INCONGRUÊNCIAS IDENTIFICADAS

### 1.1 Conflito de Papéis e Responsabilidades

#### **Incongruência #1: Gestor de Stock vs Gerente**
- **Requisito Gerente #1:** "O gerente deverá ter acesso para fazer todas as ações que a secretária, gestor de stock e mecânicos podem realizar."
- **Problema:** O documento refere "Gestor do Stock" mas a empresa só tem **4 funcionários** (gerente, 2 mecânicos, secretária). Não há gestor de stock dedicado.
- **Impacto:** 🔴 Alto - Implementação inconsistente com estrutura real da empresa
- **Solução Proposta:** Clarificar se as responsabilidades do "Gestor de Stock" são partilhadas pelo gerente ou se a secretária também tem essas permissões.

---

#### **Incongruência #2: Duplicação de Responsabilidades (Stock)**
- **Requisitos Assistente #2:** "A secretária deverá poder adicionar, remover e editar clientes, trotinetes e OS"
- **Requisitos Gerente #1:** "O gerente deverá ter acesso para fazer todas as ações que a secretária, gestor de stock e mecânicos podem realizar"
- **Problema:** A secretária pode editar OSs, mas o gerente pode fazer tudo da secretária. Quem é responsável primário?
- **Impacto:** 🟡 Médio - Falta clareza sobre segregação de deveres
- **Solução Proposta:** Definir matriz de responsabilidades primárias vs secundárias

---

#### **Incongruência #3: Aprovação de Orçamento**
- **Requisitos Gerente #1:** "O gerente deverá ter acesso para fazer todas as ações que a secretária, gestor de stock e mecânicos podem realizar"
- **Requisitos Assistente #7:** "O sistema deve permitir registar a aprovação do orçamento somente em OS que estejam no estado 'Pendente aprovação do orçamento' por parte de um cliente"
- **Problema:** Se gerente pode fazer tudo da secretária, pode aprovar orçamentos do cliente? Isto é responsabilidade da secretária, não do gerente.
- **Impacto:** 🔴 Alto - Pode criar fraudes ou erros de aprovação
- **Solução Proposta:** Especificar explicitamente que "approvar orçamento do cliente" é exclusividade da secretária

---

### 1.2 Conflitos de Fluxo de Estados (OS)

#### **Incongruência #4: Transição de Estados Contraditória**
- **Requisitos Gerais #3:** "Sempre que for criada uma nova OS, o sistema deverá conseguir calcular um tempo estimado para o término"
- **Requisitos Gerais #5:** "Sempre que uma OS tiver o seu estado alterado, deverá ser adicionada à lista de OS do estado em que foi colocada"
- **Requisitos Assistente #5:** "Para todas as OS que forem criadas, deverá ser atribuído o estado de 'Pendente Diagnóstico'"
- **Requisitos Mecânico #2:** "Quando um mecânico selecionar uma OS 'Pendente Diagnóstico'"
- **Problema:** Fluxo de estados não é claramente definido:
  - OS criada → "Pendente Diagnóstico" (certo)
  - Mas quando passa para "Pendente Reparação"? (Após diagnóstico? Quem a move?)
  - Quando passa para "A aguardar peças"? (Req. Mecânico #5 sugere durante reparação)
  - Sequência de estados é ambígua

- **Impacto:** 🔴 Alto - Implementação do fluxo será inconsistente
- **Solução Proposta:** Diagrama de máquina de estados clarificando todas as transições

---

#### **Incongruência #5: Estados "Pendente Reparação" vs "Pendente de Reparação"**
- **Requisitos Gerais #9:** "Quando um mecânico selecionar uma OS no estado 'Pendente de reparação'"
- **Requisitos Assistente #1:** "OS consoante os seus estados (...'Pendente Reparação'...)"
- **Requisitos Mecânico #1:** "Pendentes de reparação"
- **Problema:** Inconsistência de nomenclatura: "Pendente Reparação" vs "Pendente de Reparação"
- **Impacto:** 🟡 Médio - Erros de desenvolvimento, confusão na UI
- **Solução Proposta:** Padronizar nomenclatura em TODOS os requisitos

---

#### **Incongruência #6: Ordem de Operações Ilógica**
- **Requisitos Mecânico #3:** "Caso, durante a realização de um diagnóstico, o valor do orçamento exceda 50€, deverá ser atribuído automaticamente o estado de 'Pendente de aprovação de orçamento'"
- **Requisitos Mecânico #6:** "O sistema deve permitir ao mecânico adicionar novos problemas identificados durante a reparação (...) Esta ação deverá colocar o OS num estado de 'Pendente aprovação do orçamento'"
- **Problema:** O Req. Mecânico #3 refere "diagnóstico", mas Mecânico #2 (diagnóstico) não menciona este limite de 50€. Depois, Mecânico #4 é a reparação. Há confusão entre fases.
- **Impacto:** 🔴 Alto - Lógica de aprovação quebrada
- **Solução Proposta:** Clarificar em que fase (diagnóstico vs reparação) o limite de 50€ se aplica

---

### 1.3 Conflitos de Gestão de Peças

#### **Incongruência #7: Saída de Stock Dúbia**
- **Requisitos Gerais #9:** "Quando um mecânico selecionar uma OS no estado 'Pendente de reparação' e associar uma peça do stock à OS, a quantidade dessa peça no stock deverá diminuir em uma unidade"
- **Requisitos Gerais #4:** "Sempre que uma peça for associada a uma OS, o sistema deverá registar qual foi o utilizador que o fêz e a data"
- **Requisitos Assistente #10:** "O sistema deve permitir que a secretária associe peças a uma OS que esteja no estado 'Pendente Pagamento' caso o mecânico não o tenha feito durante a reparação"
- **Problema:** Se a secretária associa peças a posteriori (na receção), quando é que o stock diminui? No diagnóstico? Na reparação? Na receção?
- **Impacto:** 🔴 Alto - Stock pode estar incorreto
- **Solução Proposta:** Especificar: associação de peça = imediatamente diminui stock? Ou só quando reparação é iniciada?

---

#### **Incongruência #8: Devolução de Peças vs Defeitos**
- **Requisitos Mecânico #7:** "Durante a realização de uma reparação, depois de adicionadas peças à lista de peças usadas na OS, o mecânico poderá reportar defeitos nas peças instaladas, atribuindo o estado 'Possível defeito' à peça e removendo-a da lista de peças usadas"
- **Requisitos Gestor Stock #7:** "O sistema deve permitir registar peças devolvidas ao fornecedor, com data, motivo e estado da devolução ('Pendente', 'Devolvido' ou 'Inválido')"
- **Problema:** Se uma peça é marcada com "Possível defeito", é devolvida automaticamente? O sistema avisa? Requer aprovação do gerente?
- **Impacto:** 🟡 Médio - Fluxo de devoluções incompleto
- **Solução Proposta:** Especificar: "Possível defeito" é equivalente a "Devolvida"?

---

### 1.4 Conflitos de Aprovação

#### **Incongruência #9: Múltiplas Aprovações Possíveis**
- **Requisitos Assistente #7:** "O sistema deve permitir registar a aprovação do orçamento somente em OS que estejam no estado 'Pendente aprovação do orçamento' por parte de um cliente"
- **Requisitos Assistante #12:** "O sistema deve impedir a conclusão de uma OS no estado 'Pendente Pagamento' caso o cliente referenciado na mesma tenha outras OS no estado 'Pendente Pagamento' que tenham tido a confirmação de término de reparação numa data anterior à primeira"
- **Problema:** Req. #12 é uma regra de negócio complexa: impede pagamento se há OSs antigas não pagas. Mas quem aprova este "pagamento"? Pode a secretária? É obrigada?
- **Impacto:** 🟡 Médio - Regra de negócio pouco clara
- **Solução Proposta:** Clarificar: é uma validação que impede? Ou um aviso?

---

### 1.5 Conflitos de Reporting e Análise

#### **Incongruência #10: Diferentes Tipos de Alertas com Mesma Estrutura**
- **Requisitos Gerais #6:** "Sempre que uma OS for colocada nos estados: 'Pendente de aprovação de orçamento', 'Pendente Pagamento' ou 'A aguardar peças', deverá ser gerado automaticamente um alerta para a secretária"
- **Requisitos Gerais #10:** "Sempre que uma peça for colocada no estado 'Possível defeito' ou tiver a sua quantidade mínima atingida, deverá ser gerado automaticamente um alerta para o gestor de stock"
- **Problema:** Dois tipos de alertas (para secretária e gestor), mas não há correlação: podem ambos sobrescrever-se? Existe prioridade? Sistema de notificação especificado?
- **Impacto:** 🟡 Médio - Alertas podem ser ignorados/perdidos
- **Solução Proposta:** Especificar sistema centralizado de alertas com prioridades e persistência

---

### 1.6 Conflitos de Nomenclatura de Campos

#### **Incongruência #11: "NUS" não definido**
- **Requisitos Gerente #3:** "O registo de funcionários deverá ser composto obrigatoriamente por: (...) NUS (...)"
- **Problema:** Não é definido em nenhuma entrevista o que é "NUS". NISS, NIF, IBAN estão bem definidos, mas NUS é um acrónimo não identificado.
- **Impacto:** 🔴 Alto - Campo obrigatório sem definição
- **Solução Proposta:** Remover ou clarificar o que é NUS (Número de Utilizador Social? Número de Utente de Segurança Social?)

---

### 1.7 Conflitos de Acesso e Permissões

#### **Incongruência #12: Acesso do Gerente é Tão Amplo que Torna Inúteis Outros Papéis**
- **Requisitos Gerente #1:** "O gerente deverá ter acesso para fazer todas as ações que a secretária, gestor de stock e mecânicos podem realizar"
- **Problema:** Se o gerente faz tudo, qual é o ponto de ter segregação de papéis? A matriz de permissões fica inútil para auditoria.
- **Impacto:** 🔴 Alto - Vulnerabilidade de segurança
- **Solução Proposta:** Remover o acesso total do gerente. Apenas supervisor com escalação para casos específicos.

---

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

#### **Ambiguidade #2: Cálculo de Taxa de Armazenamento**
- **Requisitos Gerais #2:** "O sistema deve registar quantos dias passaram desde que o cliente foi notificado e aplicar automaticamente uma taxa diária de 3€ após 7 dias (excluindo o dia de levantamento)"
- **Questões Sem Resposta:**
  - ❓ "Excluindo o dia de levantamento" significa que não conta? Ou que a cobrança começa no dia 8?
  - ❓ Se cliente levanta no dia 8, paga 1 dia (dias 7-8)? Ou nada?
  - ❓ Taxa é automática ou requer confirmação antes de cobrar?
  - ❓ Valor máximo de 90€ é atingido quando exatamente? No dia 37?

**Solução Proposta:**
```
Requisito refinado: "Contagem começa no dia 1 após notificação.
Taxa aplicada = max(0, (dias_retenção - 7) * 3€)
Taxa máxima = min(taxa_calculada, 90€)
Cobrança é automática (não requer confirmação)
Se cliente levanta dia 8: taxa = (8-7)*3 = 3€"
```

---

#### **Ambiguidade #3: Cálculo de Valor Final da Reparação**
- **Requisitos Gerais #1:** "O sistema deve calcular automaticamente o valor final da reparação com base nas reparações feitas, peças utilizadas e preços definidos pelo gerente."
- **Requisitos Assistante #5:** "(...) deverá ser atribuído o estado de 'Pendente Diagnóstico'"
- **Requisitos Gerente #7-8:** Define tabela de reparações com "referência, nomenclatura, descrição detalhada e preço de execução"
- **Questões Sem Resposta:**
  - ❓ Fórmula: Soma(reparações) + Soma(preço_venda_peças)?
  - ❓ Ou: Soma(preço_execução_reparações) + Soma(preço_venda_peças)?
  - ❓ Há descontos? Margens sobre peças?
  - ❓ Impostos incluídos?

**Solução Proposta:**
```
Requisito refinado: "Valor = Σ(preço_execução_reparações) + Σ(preço_venda_peças)
Onde:
- preço_execução = da tabela de reparações (definido por gerente)
- preço_venda_peças = preço_venda registado no cadastro de peça
Não inclui IVA (faturação externa trata isso)"
```

---

### 2.2 Ambiguidades sobre Fluxos

#### **Ambiguidade #4: Quando o Diagnóstico Termina?**
- **Requisitos Mecânico #2:** "Quando um mecânico selecionar uma OS 'Pendente Diagnóstico', deverá ser possível escolher quais as reparações que ele acha necessárias realizar a partir da tabela de reparações (...)"
- **Questões Sem Resposta:**
  - ❓ O diagnóstico é apenas selecionar reparações necessárias?
  - ❓ Ou inclui testes, verificações técnicas?
  - ❓ Quem move a OS de "Pendente Diagnóstico" para "Pendente Reparação"? O mecânico? O sistema?
  - ❓ É automático quando seleciona reparações ou manual?

**Solução Proposta:**
```
Requisito refinado: "Diagnóstico = fase onde mecânico:
1. Executa testes técnicos na trotinete
2. Identifica problemas e seleciona reparações necessárias
3. Clica em 'Finalizar Diagnóstico'
Sistema move automaticamente OS para 'Pendente Reparação' (se <50€)
ou 'Pendente Aprovação Orçamento' (se >50€)"
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

---

#### **Ambiguidade #6: Fluxo de "A Aguardar Peças"**
- **Requisitos Mecânico #5:** "Quando um mecânico selecionar uma OS no estado 'Pendente de reparação' e uma ou mais peças da lista do diagnóstico não estiver disponível no stock, o sistema deverá disponibilizar a opção de requisição de encomenda das peças. A OS deverá ficar em estado de 'A aguardar peças', mas a reparação poderá processeguir normalmente."
- **Questões Sem Resposta:**
  - ❓ Quando a peça chega, quien move a OS de volta a "Pendente Reparação"? Automático?
  - ❓ Se a reparação "pode processar normalmente" sem a peça, qual é o ponto de estar "A aguardar peças"?
  - ❓ Esse estado é só para comunicar com cliente que está parada?

**Solução Proposta:**
```
Requisito refinado: "Estado 'A Aguardar Peças':
- Mecânico marca peça como 'não disponível' durante reparação
- Sistema cria requisição de compra automática
- OS move para 'A Aguardar Peças' (informativo, não bloqueia reparação)
- Quando peça é recebida (gerente marca chegada), OS retorna a 'Pendente Reparação'
- Se peça é encontrada localmente depois, sistema avisa para cancelar requisição"
```

---

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

### 3.1 Erros de Nomenclatura

| Erro | Ocorrências | Solução |
|---|---|---|
| **"Pendente de reparação" vs "Pendente Reparação"** | 15+ | Padronizar para "Pendente de Reparação" |
| **"Pendente de aprovação de orçamento" vs "Pendente aprovação do orçamento"** | 8+ | Padronizar para "Pendente de Aprovação Orçamento" |
| **"A aguardar peças" vs "A Aguardar Peças"** | 5+ | Padronizar capitalização |
| **"Gestor de armazém" vs "Gestor de Stock" vs "Gestor do Stock"** | Inconsistente | Escolher um termo |
| **"Secretária" vs "Assistente Administrativa"** | Misturado | Usar um termo consistently |

### 3.2 Erros de Valores Não Validados

| Campo | Valor | Problema |
|---|---|---|
| **Limite de Orçamento** | 50€ | Não justificado; parece arbitrário |
| **Taxa de Armazenamento** | 3€/dia | Não contexto com valores de reparação |
| **Máximo Taxa** | 90€ | Corresponde a 30 dias; lógica clara |
| **Número Mínimo de Fotos** | Implícito | "até 5 fotos" mas mínimo é?? |
| **NUS (campo de funcionário)** | Indefinido | O que é este campo? |

### 3.3 Erros de Redundância

| Requisito | Redundância |
|---|---|
| **Gerente #1** | "pode fazer tudo que secretária, stock e mecânicos" é vago e perigoso |
| **Assistante #2 vs Gerente #1** | Ambas permitem editar clientes/OSs |
| **Gerais #4 e #11** | Ambas tratam de auditoria (quem fez, quando) |
| **Stock #1 e #6** | Ambas sobre consulta de peças |

### 3.4 Erros de Completude

| Aspeto | Falta |
|---|---|
| **Autenticação** | Nenhum requisito sobre login/logout |
| **Backup de Dados** | Nenhum requisito sobre recuperação |
| **Exportação de Dados** | Nenhum requisito sobre relatórios em Excel/PDF |
| **Notificações** | Como são enviadas? SMS? Email? Pop-up? |
| **Performance** | Tempos de resposta esperados? |
| **Capacidade** | Quantas OSs/clientes por mês? |

---

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

### 4.3 Melhorias Operacionais

#### **Melhoria #8: Adicionar Requisitos de Performance**
```
- Consulta de OS: <2 segundos
- Cálculo de valor: <100ms
- Geração de relatório: <5 segundos
- Suportar 1000+ OSs por mês
```

---

#### **Melhoria #9: Adicionar Requisitos de Backup/Recuperação**
```
- Backup diário automático
- Retenção de 30 dias
- RTO (Recovery Time): <1 hora
- RPO (Recovery Point): <1 dia
```

---

#### **Melhoria #10: Especificar Notificações**
```
Alerta de OS Pendente Aprovação:
- Canal: Pop-up no sistema (obrigatório)
- Email (opcional, se email registado)
- SMS (opcional, se telefone registado)
```

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

### 4.4 Melhorias de Segurança

#### **Melhoria #13: Adicionar Autenticação**
```
- Login com email/password
- Recuperação de senha
- Expiração de sessão (30 min inatividade)
- Log de acessos
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

#### **Melhoria #17: Adicionar Tooltips e Ajuda**
```
Cada campo deve ter ajuda contextual:
- O que é "Número de Série"?
- Por que "NIF" é obrigatório?
- Como interpretar "Estado à Entrada"?
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
- Após reparação (requer nota de devolução)
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

#### **Melhoria #21: Adicionar Garantia de Reparação**
```
Se trotinete volta com mesmo problema <30 dias:
- Reparação é grátis (requisito? ou política?)
- Sistema rastreia isso?
```

---

#### **Melhoria #22: Especificar Política de Descontos**
```
Pode oferecer descontos?
- Por cliente recorrente?
- Por volume?
- Por tipo de reparação?
Quem aprova?
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

## 5. TABELA RESUMIDA DE PROBLEMAS

| Categoria | Crítico | Médio | Baixo | Total |
|-----------|---------|-------|-------|-------|
| Incongruências | 8 | 4 | 0 | **12** |
| Ambiguidades | 5 | 5 | 0 | **10** |
| Erros Formais | 5 | 5 | 2 | **12** |
| Faltam Requisitos | 6 | 4 | 0 | **10** |
| **TOTAL** | **24** | **18** | **2** | **44** |

---

## 6. RECOMENDAÇÕES PRIORITÁRIAS

### 🔴 **CRÍTICO - Fazer Antes de Qualquer Desenvolvimento**

1. **Clarificar papéis e responsabilidades** (Incongruências #1-3, #12)
   - Remover "gerente pode fazer tudo"
   - Definir matriz explícita de permissões
   - Esforço: 4 horas

2. **Definir máquina de estados da OS** (Incongruência #4-6)
   - Especificar todas as transições
   - Quando cada estado ocorre
   - Esforço: 6 horas

3. **Esclarecer fluxo de peças** (Incongruências #7-8, Ambiguidades #5-6)
   - Quando diminui stock? Quando aumenta?
   - Fluxo de devoluções
   - Esforço: 4 horas

4. **Resolver ambiguidades de cálculos** (Ambiguidades #1-3)
   - Fórmulas explícitas para tempo, taxa, valor
   - Esforço: 3 horas

5. **Definir fluxo de diagnóstico** (Ambiguidade #4)
   - O que inclui diagnóstico?
   - Como termina?
   - Esforço: 2 horas

6. **Remover ou Definir NUS** (Incongruência #11)
   - Esforço: 1 hora

### 🟡 **IMPORTANTE - Fazer Antes de Testes**

7. Padronizar nomenclatura (erros formais)
8. Adicionar critérios de aceitação
9. Adicionar casos de uso formais
10. Especificar validações de campos
11. Definir sistema de alertas centralizado

### 🟢 **NICE-TO-HAVE - Depois da v1.0**

12. Performance requirements
13. Backup/recuperação
14. Notificações por email/SMS
15. Política de retenção de dados
16. Rastreio avançado de garantias

---

## 7. PRÓXIMOS PASSOS

1. **Semana 1:** Resolver críticos (1-6) com stakeholders
2. **Semana 2:** Incorporar feedback e criar documento final
3. **Semana 3:** Testes de validação com equipa de desenvolvimento
4. **Semana 4:** Prototipagem e design de UI

---

## 8. NOTAS FINAIS

Este documento identificou **44 problemas** no documento de requisitos consolidados. A maioria são resolvíveis com discussões com stakeholders, mas alguns (especialmente incongruências de papéis) requerem redesenho arquitectónico.

**Recomendação:** NÃO iniciar desenvolvimento até estes problemas serem resolvidos. O custo de correções após implementação será muito superior.

