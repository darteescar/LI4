
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



#### **Melhoria #11: Adicionar Requisitos de Auditoria**
```
- Quem criou a OS?
- Quem aprovou orçamento?
- Quem registou pagamento?
- Quem alterou preços?
Histórico completo com timestamps
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
