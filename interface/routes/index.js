const express = require('express');
const router = express.Router();

// Dashboard
router.get('/', (req, res) => res.render('index', { title: 'Dashboard - Oficina de Trotinetes' }));

// Gestão de Funcionários
router.get('/funcionarios', (req, res) => res.render('funcionarios', { title: 'Gestão de Funcionários' }));

// Clientes e Trotinetes
router.get('/clientes', (req, res) => res.render('clientes', { title: 'Gestão de Clientes' }));
router.get('/trotinetes', (req, res) => res.render('trotinetes', { title: 'Gestão de Trotinetes' }));

// Ordens de Serviço (OS)
router.get('/ordens-servico', (req, res) => res.render('ordens_servico', { title: 'Ordens de Serviço' }));

// Armazém / Stock
router.get('/stock', (req, res) => res.render('stock', { title: 'Gestão de Stock' }));
router.get('/fornecedores', (req, res) => res.render('fornecedores', { title: 'Fornecedores' }));
router.get('/encomendas', (req, res) => res.render('encomendas', { title: 'Listas de Encomendas' }));
router.get('/devolucoes', (req, res) => res.render('devolucoes', { title: 'Devoluções de Peças' }));

// Tabela de Reparações
router.get('/reparacoes', (req, res) => res.render('reparacoes', { title: 'Tabela de Reparações' }));

// Finanças
router.get('/financas', (req, res) => res.render('financas', { title: 'Estado Financeiro' }));

module.exports = router;