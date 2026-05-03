import { z } from "zod";

// Common regex
const reNIF = /^\d{9}$/;
const reNISS = /^\d{11}$/;
const reTelemovel = /^9\d{8}$/;
const reCP = /^\d{4}-\d{3}$/;
const reIBAN = /^PT50\d{21}$/;

export const funcionarioSchema = z.object({
  nome: z.string().trim().min(2, "Nome obrigatório").max(100),
  identificador: z.string().trim().min(3, "Identificador obrigatório").max(40),
  password: z.string().min(4, "Password tem de ter pelo menos 4 caracteres").max(60),
  email: z.string().trim().email("Email inválido").max(120),
  telemovel: z.string().regex(reTelemovel, "Telemóvel inválido (9XXXXXXXX)"),
  nif: z.string().regex(reNIF, "NIF deve ter 9 dígitos"),
  niss: z.string().regex(reNISS, "NISS deve ter 11 dígitos"),
  nus: z.string().trim().min(1, "NUS obrigatório").max(20),
  iban: z.string().regex(reIBAN, "IBAN PT inválido"),
  morada: z.string().trim().min(3, "Morada obrigatória").max(200),
  codigoPostal: z.string().regex(reCP, "Código postal inválido (XXXX-XXX)"),
  cargo: z.enum(["GERENTE", "GESTOR_STOCK", "SECRETARIA", "MECANICO"]),
  vencimentoHora: z.coerce.number().min(0, "Vencimento por hora inválido"),
  vencimentoBruto: z.coerce.number().min(0, "Vencimento bruto inválido"),
  dataAdmissao: z.string().min(1, "Data obrigatória"),
});

export const clienteSchema = z.object({
  nome: z.string().trim().min(2, "Nome obrigatório").max(100),
  nif: z.string().regex(reNIF, "NIF deve ter 9 dígitos"),
  telemovel: z.string().regex(reTelemovel, "Telemóvel inválido"),
  email: z.string().trim().email("Email inválido").max(120),
  morada: z.string().trim().min(3, "Morada obrigatória").max(200),
  codigoPostal: z.string().regex(reCP, "Código postal inválido"),
  notas: z.string().max(500).optional().or(z.literal("")),
});

export const trotineteSchema = z.object({
  clienteId: z.string().min(1, "Cliente obrigatório"),
  marca: z.string().trim().min(1, "Marca obrigatória").max(50),
  modelo: z.string().trim().min(1, "Modelo obrigatório").max(50),
  numeroSerie: z.string().trim().min(1, "Nº de série obrigatório").max(50),
  motor: z.string().trim().min(1, "Motor obrigatório").max(50),
  notas: z.string().max(300).optional().or(z.literal("")),
});

export const fornecedorSchema = z.object({
  nome: z.string().trim().min(2, "Nome obrigatório").max(100),
  nif: z.string().regex(reNIF, "NIF deve ter 9 dígitos"),
  telemovel: z.string().regex(/^[29]\d{8}$/, "Telefone inválido"),
  email: z.string().trim().email("Email inválido").max(120),
});

export const pecaSchema = z.object({
  referencia: z.string().trim().min(1, "Referência obrigatória").max(40),
  nome: z.string().trim().min(2, "Nome obrigatório").max(80),
  descricao: z.string().max(300).optional().or(z.literal("")),
  fornecedorId: z.string().min(1, "Fornecedor obrigatório"),
  tipo: z.string().trim().min(1, "Tipo obrigatório").max(40),
  precoVenda: z.coerce.number().min(0, "Preço inválido"),
  stockMinimo: z.coerce.number().int().min(0, "Stock mínimo inválido"),
});

export const reparacaoSchema = z.object({
  nomenclatura: z.string().trim().min(2, "Nomenclatura obrigatória").max(80),
  descricao: z.string().trim().min(3, "Descrição obrigatória").max(300),
  preco: z.coerce.number().min(0, "Preço inválido"),
});

export const unidadeSerieSchema = z.object({
  numeroSerie: z.string().trim().min(1, "Nº de série obrigatório").max(60),
  garantiaMeses: z.coerce.number().int().min(0).max(120),
});

export const entradaStockSchema = z
  .object({
    pecaId: z.string().min(1, "Peça obrigatória"),
    quantidade: z.coerce.number().int().min(1, "Quantidade ≥ 1"),
    precoUnitario: z.coerce.number().min(0, "Preço inválido"),
    data: z.string().min(1, "Data obrigatória"),
    unidades: z.array(unidadeSerieSchema).optional(),
  })
  .refine(
    (d) => d.precoUnitario <= 70 || (d.unidades && d.unidades.length === d.quantidade),
    {
      message: "Preço > 70€: cada unidade precisa de nº de série e garantia",
      path: ["unidades"],
    },
  );

export const devolucaoSchema = z.object({
  pecaId: z.string().min(1, "Peça obrigatória"),
  fornecedorId: z.string().min(1, "Fornecedor obrigatório"),
  quantidade: z.coerce.number().int().min(1, "Quantidade ≥ 1"),
  motivo: z.string().trim().min(3, "Motivo obrigatório").max(300),
});

export const encomendaItemSchema = z.object({
  pecaId: z.string().min(1),
  quantidade: z.coerce.number().int().min(1, "Qtd ≥ 1"),
  precoUnitario: z.coerce.number().min(0, "Preço inválido"),
});

export const encomendaSchema = z.object({
  fornecedorId: z.string().min(1, "Fornecedor obrigatório"),
  itens: z.array(encomendaItemSchema).min(1, "Adiciona pelo menos uma peça"),
});

export const horasExtraSchema = z.object({
  funcionarioId: z.string().min(1, "Funcionário obrigatório"),
  data: z.string().min(1, "Data obrigatória"),
  horas: z.coerce.number().min(0.5, "Horas ≥ 0.5").max(24, "Horas ≤ 24"),
  motivo: z.string().trim().min(2, "Motivo obrigatório").max(200),
});

export const loginSchema = z.object({
  identificador: z.string().trim().min(1, "Identificador obrigatório"),
  password: z.string().min(1, "Password obrigatória"),
});

export const changePasswordSchema = z
  .object({
    currentPassword: z.string().min(1, "Password atual obrigatória"),
    newPassword: z.string().min(4, "Nova password tem de ter pelo menos 4 caracteres").max(60),
    confirm: z.string().min(1, "Confirma a nova password"),
  })
  .refine((d) => d.newPassword === d.confirm, {
    message: "As passwords não coincidem",
    path: ["confirm"],
  });
