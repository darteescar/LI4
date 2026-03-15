
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
