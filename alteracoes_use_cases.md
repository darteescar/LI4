# Registo de Alterações nos Use Cases

---

## UC-10 — Registar Peça (FEITO)

**Requisito estruturado (REQ-008):**
- **Editado** — `requisito_utilizador`: removida a menção a "preço de compra" (o preço de compra pertence à entrada de stock, não à peça).

**Fluxo normal:**
- **Sem alterações** (o preço de venda já estava presente).

**Fluxos de exceção:**
- **Editado** — exceção "Preço de Venda inválido", passo 8.1: corrigido "número **inteiro** positivo" para "número **real** positivo".

---

## UC-11 — Editar Peça (FEITO)

**Fluxo normal:**
- **Editado** — passo 7: corrigido "nível mínimo aceitável em stock é um número inteiro **positivo**" para "número inteiro **não negativo**" (consistente com UC-10 e REQ-008).

**Fluxos de exceção:**
- **Editado** — exceção "Nível mínimo aceitável em stock inválido", passo 7.1: corrigido "número inteiro **positivo**" para "número inteiro **não negativo**".
- **Editado** — exceção "Preço de Venda inválido", passo 8.1: corrigido "número **inteiro** positivo" para "número **real** positivo" e "impede o **registo**" para "impede a **atualização**".

---

## UC-13 — Registar Entrada de Peças em Stock (FEITO)

**Pós-condições:**
- **Editado** — removida a menção "é registada com data, hora e utilizador". Nova pós-condição: *"Uma nova entrada de peça em stock é registada com identificador único numérico e a quantidade em stock é aumentada."*

---

## UC-14 — Editar Entrada de Stock de Peças (FEITO)

**Pós-condições:**
- **Editado** — removida a menção "e é registada com data, hora e utilizador". Nova pós-condição: *"Os dados da entrada de stock são atualizados no sistema com os novos valores fornecidos."*

**Fluxo normal:**
- **Editado** — último passo: removido "regista a data, hora e utilizador da edição, e". Passa a ser apenas: *"O sistema atualiza os dados da entrada de stock e apresenta os dados atualizados."*

---

## UC-15 — Registar Devolução de Peça (FEITO)

**Pós-condições:**
- **Editado** — removida a menção "e data, hora e utilizador são registados". Nova pós-condição: *"Uma nova devolução é registada no sistema com estado 'Pendente de Devolução' e um identificador único é atribuído."*

**Fluxo normal:**
- **Editado** — último passo: corrigido erro tipográfico "O apresenta os dados da nova devolução" para *"O sistema apresenta os dados da nova devolução."*

---

## UC-16 — Editar Devolução de Peça (FEITO)

**Pós-condições:**
- **Editado** — removida a menção "e data, hora e utilizador da edição são registados". Nova pós-condição: *"Os dados da devolução são atualizados no sistema com os novos valores fornecidos e as transições de estado causam os efeitos correspondentes no stock e na gestão financeira."*

**Requisito estruturado (REQ-010):**
- **Removido** — linha duplicada e contraditória que dizia "uma unidade dessa peça deverá ser adicionada ao stock". Mantida apenas a linha que diz "a quantidade especificada na devolução deverá ser adicionada ao stock".

---

## UC-17 — Remover Devolução de Peça (FEITO)

**Pós-condições:**
- **Editado** — removida a menção "e data, hora e utilizador da eliminação são registados". Nova pós-condição: *"A devolução é removida do sistema e os gastos em peças são aumentados consoante o valor da peça e quantidade."*

---

## UC-18 — Registar Fornecedor (FEITO)

**Pós-condições:**
- **Editado** — removida a menção "e data, hora e utilizador são registados". Nova pós-condição: *"Um novo fornecedor é registado no sistema com um identificador único."*

---

## UC-19 — Editar Fornecedor (FEITO)

**Pós-condições:**
- **Editado** — removida a menção "e data, hora e utilizador da edição são registados". Nova pós-condição: *"Os dados do fornecedor são atualizados no sistema com os novos valores fornecidos."*

---

## UC-20 — Remover Fornecedor (FEITO)

**Pós-condições:**
- **Editado** — removida a menção "e data, hora e utilizador da eliminação são registados". Nova pós-condição: *"O fornecedor é removido do sistema e deixa de ser possível associar peças a este fornecedor."*

---

## UC-21 — Gerar Lista de Encomendas (FEITO)

**Fluxo normal:**
- **Editado** — último passo: adicionado o registo da data e hora de criação da lista. Passa a ser: *"O sistema cria a lista, atribui-lhe um identificador único, define o estado como 'Rascunho', **regista a data e hora da criação** e apresenta os dados da nova lista..."*

---

## UC-22 — Editar Lista de Encomendas (FEITO)

**Fluxo alternativo — Rascunho → Enviada:**
- **Adicionado** — passo 7.1.1: *"O sistema regista a data e hora do envio."*
- **Renumerado** — passo de retorno: era 7.1.1, passa a ser **7.1.2**.

**Fluxo alternativo — Enviada → Recebida:**
- **Adicionado** — passo 7.2.1: *"O sistema regista a data e hora da receção."*
- **Renumerados** — todos os passos seguintes: 7.2.1 → **7.2.2**, 7.2.2 → **7.2.3**, 7.2.3 → **7.2.4**.

**Fluxo alternativo — Preço de compra superior a 70€ (dentro do Recebida):**
- **Renumerado** — o fluxo alternativo era referenciado em passo "7.2.1"; passa a ser **"7.2.2"**.
- **Renumerados** — todos os sub-passos internos: 7.2.1.x → **7.2.2.x**.

**Fluxos de exceção:**
- **Renumerados** — "Número de série inválido": passo era "7.2.1.3", passa a ser **"7.2.2.3"**; sub-passo era 7.2.1.3.1, passa a ser **7.2.2.3.1**.
- **Renumerados** — "Tempo de garantia inválido": passo era "7.2.1.4", passa a ser **"7.2.2.4"**; sub-passo era 7.2.1.4.1, passa a ser **7.2.2.4.1**.

---

## UC-30 — Registar Ordem de Serviço (FEITO)

**Pós-condições:**
- **Editado** — removida a menção a "utilizador" e clarificada a linguagem. Nova pós-condição: *"...e a data e hora de criação são registadas."*

**Fluxo normal:**
- **Editado** — último passo: adicionado o registo da data e hora de criação. Passa a ser: *"O sistema cria a ordem de serviço, atribui um identificador único numérico, define o estado como 'Pendente Diagnóstico', **regista a data e hora de criação** e apresenta os dados da nova ordem de serviço."*

---

## UC-31 — Editar Ordem de Serviço (FEITO)

**Pós-condições:**
- **Editado** — removida a menção "e data, hora, utilizador e estado são registados". Nova pós-condição: *"Os dados da ordem de serviço são atualizados no sistema com os novos valores fornecidos."*

**Fluxo alternativo — O funcionário regista a notificação de término (passo 4.2.1):**
- **Adicionado** — passo 4.2.1.3: *"O sistema regista a data e hora da notificação."*
- **Renumerado** — passo de retorno: era 4.2.1.3, passa a ser **4.2.1.4**.

**Fluxo alternativo — O funcionário associa ou remove peças da OS (passo 4.2.2):**
- **Editado** — passo 4.2.2.6: removido "regista a data, hora e utilizador que associou as peças e". Passa a ser: *"O sistema associa as peças à OS e atualiza o valor total da OS."*

**Fluxo de exceção — O funcionário não confirma a aprovação do orçamento (passo 4.1.2):**
- **Editado** — passo 4.1.2.1: removido "regista a data, hora e utilizador e". Passa a ser: *"O sistema altera o estado da ordem de serviço para 'Orçamento não aprovado' e impossibilita o uso dos dados da OS para estatísticas da empresa."*

---

## UC-32 — Remover Ordem de Serviço (FEITO)

**Pós-condições:**
- **Editado** — removida a menção "Data, hora e utilizador da eliminação são registados." Nova pós-condição: *"A ordem de serviço muda para estado 'Eliminada' e não é possível usar os seus dados para estatísticas da empresa."*

**Fluxo normal:**
- **Editado** — passo 5: removido "regista a data, hora e utilizador que realizou a eliminação". Passa a ser: *"O sistema altera o estado da ordem de serviço para 'Eliminada', impossibilita o uso dos dados da OS para estatísticas da empresa e apresenta uma mensagem de confirmação de eliminação bem-sucedida."*

---

## UC-34 — Executar Diagnóstico de Ordem de Serviço (FEITO)

**Pós-condições:**
- **Editado** — removidas "data e hora" do registo do diagnóstico; mantido apenas o registo do funcionário. Removida também a referência ao estado "Pendente Reparação" (o diagnóstico vai sempre para "Pendente aprovação do orçamento"). Nova pós-condição: *"O diagnóstico é registado com o funcionário que o realizou. A OS transita para o estado 'Pendente aprovação do orçamento'."*

**Fluxo normal:**
- **Editado** — último passo: adicionado o registo do funcionário que realizou o diagnóstico (sem data/hora). Passa a ser: *"O sistema calcula o valor do orçamento e altera o estado da OS para 'Pendente aprovação do orçamento', **regista o funcionário que realizou o diagnóstico** e gera um alerta com o identificador da OS e o seu estado para a secretária"*

---

## UC-35 — Executar Reparação de Ordem de Serviço (FEITO)

**Pós-condições:**
- **Editado** — removida a menção "as quantidades de peças são descontadas do stock com registo de data, hora e utilizador". Adicionado o registo do funcionário que realizou a reparação (sem data/hora). Nova pós-condição: *"...o custo final é calculado, o funcionário que realizou a reparação é registado e a OS transita para o estado 'Pendente Pagamento'..."*

**Fluxo normal:**
- **Editado** — passo 5: removido "e regista a data, hora e utilizador". Passa a ser apenas: *"O sistema desconta do stock as quantidades das peças marcadas como utilizadas."*
- **Editado** — último passo (14): adicionado o registo do funcionário que realizou a reparação. Passa a ser: *"O sistema calcula o custo final da reparação, altera o estado da OS para 'Pendente Pagamento', **regista o funcionário que realizou a reparação**, gera um alerta... e apresenta um resumo final da OS."*

**Fluxos — reestruturação de alternativo/exceção:**
Os seguintes fluxos estavam classificados como **fluxos de exceção** mas foram movidos para **fluxos alternativos** (por serem caminhos válidos, não erros), e foram adicionados os passos de retorno em falta:

| Condição | Antes | Depois |
|---|---|---|
| Uma peça prescrita não está disponível em stock | fluxo_excecao, sem retorno | fluxo_alternativo, adicionado passo **2.1.5** (retorna ao passo 3) |
| O funcionário pretende adicionar reparações adicionais | fluxo_excecao, sem retorno | fluxo_alternativo, adicionado passo **6.4** (retorna ao passo 7) |
| O funcionário pretende adicionar peças adicionais | fluxo_excecao, sem retorno | fluxo_alternativo, adicionado passo **7.5** (retorna ao passo 8) |

Os fluxos de cancelamento correspondentes (cancelar requisição, cancelar reparações, cancelar peças) mantiveram-se como **fluxos de exceção**.
