-- Dados iniciais — corre com INSERT IGNORE para ser idempotente
-- Credenciais de arranque: identificador=admin  password=admin123
INSERT IGNORE INTO Reparacao(id, nomenclatura, descricao, preco, disponivel)
VALUES
    (1,  'Diagnóstico Geral',           'Verificação completa da trotinete para identificar problemas.',                     10.00, TRUE),
    (2,  'Troca de Pneu',               'Remoção e instalação de um novo pneu.',                                             15.00, TRUE),
    (3,  'Reparação de Câmara de Ar',   'Reparação ou substituição da câmara de ar furada.',                                  8.00, TRUE),
    (4,  'Substituição de Pastilhas',   'Troca das pastilhas de travão desgastadas.',                                        12.00, TRUE),
    (5,  'Afinação de Travões',         'Ajuste e calibração do sistema de travagem.',                                        7.50, TRUE),
    (6,  'Troca de Disco de Travão',    'Substituição do disco de travão danificado.',                                       18.00, TRUE),
    (7,  'Reparação de Motor',          'Desmontagem e reparação básica do motor.',                                           35.00, TRUE),
    (8,  'Substituição de Bateria',     'Instalação e teste de uma nova bateria.',                                            20.00, TRUE),
    (9,  'Reparação de Controlador',    'Reparação ou substituição do controlador eletrónico.',                              20.00, TRUE),
    (10, 'Troca de Acelerador',         'Substituição do acelerador e teste funcional.',                                      10.00, TRUE),
    (11, 'Substituição de Display',     'Troca e configuração do painel/display.',                                            15.00, TRUE),
    (12, 'Atualização de Firmware',     'Atualização do software interno da trotinete.',                                      10.00, TRUE),
    (13, 'Reparação de Luzes',          'Reparação do sistema de iluminação.',                                                 8.00, TRUE),
    (14, 'Troca de Suspensão',          'Substituição e ajuste da suspensão.',                                                25.00, TRUE),
    (15, 'Alinhamento de Direção',      'Ajuste da direção para maior estabilidade.',                                         12.50, TRUE),
    (16, 'Lubrificação Geral',          'Lubrificação das partes móveis da trotinete.',                                        5.00, TRUE),
    (17, 'Limpeza Técnica Completa',    'Limpeza detalhada com manutenção preventiva básica.',                                15.00, TRUE),
    (18, 'Reparação de Cabelagem',       'Correção de ligações elétricas danificadas.',                                        18.00, TRUE),
    (19, 'Revisão Completa',            'Revisão geral de segurança e funcionamento.',                                      30.00, TRUE);

INSERT IGNORE INTO Fornecedor(id,nome,telemovel,email)
VALUES
    (1, 'Correia & Cardoso', '253801080', 'geral@correiacardoso.com'),
    (2, 'MotoFreitas',       '253576231', 'motofreitas@sapo.pt'),
    (3, 'Barcelpeças',       '253809420', 'geral@barcelpecas.pt'),
    (4, 'Centrauto',         '253837214', 'barcelos@centrauto.pt');



INSERT IGNORE INTO Funcionario
    (id, nome, telemovel, email, data_nascimento,
     NISS, NIF, NUS, IBAN,
     salario_hora, salario_liquido, salario_bruto, horas_extra,
     numero_porta, rua, localidade, codigo_postal)
VALUES
    (1, 'Administrador EcoRide', '912345678', 'admin@ecoride.pt', '1985-01-15',
     '12345678901', '123456789', '123456789', 'PT50000201231234567890154',
     15.00, 1800.00, 2200.00, 0,
     '1', 'Rua Principal', 'Lisboa', '1000-001'),
    (2, 'João Silva', '912345679', 'joao@ecoride.pt', '1990-05-20',
     '98765432109', '987654321', '987654321', 'PT50000201231234567890154',
     12.00, 1440.00, 1800.00, 0,
     '2', 'Avenida Central', 'Lisboa', '1000-002'),
    (3, 'Silvina Matagal', '912345680', 'silvina@ecoride.pt', '1988-12-10',
     '45678912309', '456789123', '456789123', 'PT50000201231234567890154',
     14.00, 1680.00, 2100.00, 0,
     '3', 'Travessa Seca', 'Lisboa', '1000-003'),
    (4, 'Ramiro Ramalho', '912345681', 'ramiro@ecoride.pt', '1987-08-25',
     '32165498709', '321654987', '321654987', 'PT50000201231234567890154',
     13.00, 1560.00, 1950.00, 0,
     '4', 'Praça da República', 'Lisboa', '1000-004'),
    (5, 'Edgar Novo', '912345682', 'edgar@ecoride.pt', '1992-03-15',
     '65432198709', '654321987', '654321987', 'PT50000201231234567890154',
     11.00, 1320.00, 1650.00, 0,
     '5', 'Rua do Comércio', 'Lisboa', '1000-005');

INSERT IGNORE INTO Utilizador (id, password, idFuncionario, cargo, identificador)
VALUES (1, 'admin123',   1, 'Gerente',     'admin'),
       (2, 'joao123',    2, 'Gerente',     'joao'),
       (3, 'joaoS123',   2, 'GestorStock', 'joaoS'),
       (4, 'silvina123', 3, 'Secretaria',  'silvina'),
       (5, 'ramiro123',  4, 'Mecanico',    'ramiro'),
       (6, 'edgar123',   5, 'Mecanico',    'edgar');