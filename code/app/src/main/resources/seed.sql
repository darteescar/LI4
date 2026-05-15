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

INSERT IGNORE INTO Peca (
    id, referencia, marca, nome, descricao,
    stock_minimo, preco_venda,
    codFornecedor, ativa, garantia
)
VALUES
    (1,  'BAT-XM-36V10A',   'Xiaomi',      'Bateria 36V 10Ah',          'Bateria de lítio para trotinetes elétricas.',                     2, 149.99, 1, TRUE, 12),
    (2,  'BAT-XM-48V15A',   'Ninebot',     'Bateria 48V 15Ah',          'Bateria de alta autonomia.',                                     2, 229.99, 1, TRUE, 12),
    (3,  'MOT-HUB-350W',    'Bafang',      'Motor Hub 350W',            'Motor brushless de roda dianteira.',                             1, 119.99, 2, TRUE, 24),
    (4,  'MOT-HUB-500W',    'Bosch',       'Motor Hub 500W',            'Motor brushless de alto desempenho.',                            1, 169.99, 2, TRUE, 24),
    (5,  'MOT-HUB-800W',    'Bafang',      'Motor Hub 800W',            'Motor para trotinetes de potência elevada.',                     1, 249.99, 2, TRUE, 24),
    (6,  'PNEU-8.5',        'Kenda',       'Pneu 8.5 Polegadas',        'Pneu compatível com Xiaomi M365.',                               6, 24.99, 3, TRUE, 6),
    (7,  'PNEU-10',         'Michelin',    'Pneu 10 Polegadas',         'Pneu reforçado para estrada.',                                   6, 29.99, 3, TRUE, 6),
    (8,  'CAM-8.5',         'Continental', 'Câmara de Ar 8.5',          'Câmara de ar resistente.',                                       10, 7.99, 3, TRUE, 3),
    (9,  'CAM-10',          'Schwalbe',    'Câmara de Ar 10',           'Câmara de ar para pneus 10".',                                   10, 8.99, 3, TRUE, 3),
    (10, 'TRV-PST-01',      'Brembo',      'Pastilhas de Travão',       'Conjunto de pastilhas semi-metálicas.',                          8, 11.99, 4, TRUE, 6),

    (11, 'TRV-DSC-120',     'Tektro',      'Disco de Travão 120mm',     'Disco ventilado em aço.',                                        4, 14.99, 4, TRUE, 12),
    (12, 'TRV-CAB-01',      'Shimano',     'Cabo de Travão',            'Cabo reforçado para travagem.',                                  5, 6.50, 4, TRUE, 6),
    (13, 'LCD-M365',        'Xiaomi',      'Display LCD M365',          'Painel digital compatível com Xiaomi.',                          2, 34.99, 1, TRUE, 12),
    (14, 'LCD-UNIV',        'Ninebot',     'Display Universal',         'Display LCD universal para trotinetes.',                         2, 39.99, 1, TRUE, 12),
    (15, 'CTRL-36V',        'Bosch',       'Controlador 36V',           'Controlador eletrónico 36V.',                                    2, 49.99, 2, TRUE, 12),
    (16, 'CTRL-48V',        'Bosch',       'Controlador 48V',           'Controlador eletrónico 48V.',                                    2, 64.99, 2, TRUE, 12),
    (17, 'ACEL-THR-01',     'Xiaomi',      'Acelerador Universal',      'Punho acelerador universal.',                                    4, 12.99, 1, TRUE, 6),
    (18, 'LUZ-FRT-LED',     'Ninebot',     'Luz Frontal LED',           'Farol LED frontal branco.',                                      5, 15.99, 3, TRUE, 12),
    (19, 'LUZ-TRS-LED',     'Ninebot',     'Luz Traseira LED',          'Luz traseira vermelha LED.',                                     5, 11.99, 3, TRUE, 12),
    (20, 'SUSP-FRT',        'TRP',         'Suspensão Dianteira',       'Kit de suspensão dianteira.',                                    1, 59.99, 2, TRUE, 12),

    (21, 'SUSP-TRS',        'TRP',         'Suspensão Traseira',        'Suspensão traseira reforçada.',                                  1, 64.99, 2, TRUE, 12),
    (22, 'GUARDA-FRT',      'Xiaomi',      'Guarda-Lamas Frontal',      'Guarda-lamas frontal em ABS.',                                   3, 13.99, 4, TRUE, 6),
    (23, 'GUARDA-TRS',      'Xiaomi',      'Guarda-Lamas Traseiro',     'Guarda-lamas traseiro em ABS.',                                  3, 15.99, 4, TRUE, 6),
    (24, 'SUP-TELEM',       'Ninebot',     'Suporte de Telemóvel',      'Suporte ajustável para guiador.',                                5, 9.99, 1, TRUE, 3),
    (25, 'CAR-42V2A',       'Xiaomi',      'Carregador 42V 2A',         'Carregador para baterias 36V.',                                  3, 29.99, 1, TRUE, 12),
    (26, 'CAR-54V2A',       'Ninebot',     'Carregador 54V 2A',         'Carregador para baterias 48V.',                                  3, 39.99, 1, TRUE, 12),
    (27, 'MAN-GUI-01',      'Schwalbe',    'Punhos de Guiador',         'Punhos ergonómicos antiderrapantes.',                            6, 8.99, 3, TRUE, 3),
    (28, 'CAB-ELET-01',     'Bosch',       'Kit Cablagem',              'Conjunto de cabos elétricos.',                                   4, 17.99, 2, TRUE, 6),
    (29, 'FUS-15A',         'Bosch',       'Fusível 15A',               'Fusível de proteção elétrica.',                                  10, 2.99, 4, TRUE, 3),
    (30, 'FUS-20A',         'Bosch',       'Fusível 20A',               'Fusível para controladores.',                                    10, 3.49, 4, TRUE, 3),

    (31, 'ROL-608',         'Shimano',     'Rolamento 608ZZ',           'Rolamento metálico de precisão.',                                10, 4.50, 3, TRUE, 12),
    (32, 'ROL-6001',        'Shimano',     'Rolamento 6001',            'Rolamento para eixo de motor.',                                  8, 5.99, 3, TRUE, 12),
    (33, 'EST-BAT-01',      'Xiaomi',      'Suporte de Bateria',        'Base de fixação para bateria.',                                  3, 12.99, 2, TRUE, 6),
    (34, 'PED-BOR-01',      'Ninebot',     'Tapete Antiderrapante',     'Tapete em borracha para base.',                                  5, 14.99, 4, TRUE, 6),
    (35, 'CAM-TRS-01',      'Xiaomi',      'Câmara Traseira',           'Câmara traseira de vigilância.',                                 1, 49.99, 1, TRUE, 12),
    (36, 'BUZ-ELT',         'Ninebot',     'Campainha Elétrica',        'Campainha eletrónica compacta.',                                 5, 9.50, 3, TRUE, 6),
    (37, 'DOB-HINGE',       'Xiaomi',      'Dobradiça de Fecho',        'Mecanismo de dobragem reforçado.',                               2, 24.99, 2, TRUE, 12),
    (38, 'PARAF-M6',        'Bosch',       'Kit Parafusos M6',          'Conjunto de parafusos em aço.',                                  15, 5.99, 4, TRUE, 3),
    (39, 'PARAF-M8',        'Bosch',       'Kit Parafusos M8',          'Parafusos de fixação reforçados.',                               15, 7.99, 4, TRUE, 3),
    (40, 'DESC-LAT',        'Xiaomi',      'Descanso Lateral',          'Suporte lateral dobrável.',                                      4, 11.99, 3, TRUE, 6),

    (41, 'SEL-BAT-01',      'Continental', 'Vedante de Bateria',        'Vedante impermeável para compartimento.',                        5, 6.99, 2, TRUE, 6),
    (42, 'PLACA-BT',        'Xiaomi',      'Módulo Bluetooth',          'Módulo Bluetooth para app móvel.',                               2, 18.99, 1, TRUE, 12),
    (43, 'GPS-TRACK',       'Ninebot',     'Localizador GPS',           'Dispositivo GPS para rastreamento.',                             1, 39.99, 1, TRUE, 12),
    (44, 'TRV-HID-01',      'Brembo',      'Travão Hidráulico',         'Sistema de travagem hidráulica.',                                2, 79.99, 4, TRUE, 24),
    (45, 'PED-ALUM',        'Xiaomi',      'Base em Alumínio',          'Plataforma reforçada em alumínio.',                              1, 69.99, 2, TRUE, 24),
    (46, 'COL-DIR-01',      'TRP',         'Coluna de Direção',         'Coluna principal de direção.',                                   1, 54.99, 2, TRUE, 24),
    (47, 'REF-LAT-01',      'Continental', 'Refletor Lateral',          'Refletor de segurança lateral.',                                 8, 3.99, 3, TRUE, 3),
    (48, 'BLOQ-DISC',       'Tektro',      'Bloqueador de Disco',       'Sistema anti-roubo para disco.',                                 2, 21.99, 4, TRUE, 12),
    (49, 'BOL-TRANSP',      'Xiaomi',      'Bolsa de Transporte',       'Bolsa impermeável para acessórios.',                             3, 16.99, 1, TRUE, 6),
    (50, 'KIT-FERR',        'Shimano',     'Kit Ferramentas',           'Conjunto de ferramentas para manutenção.',                       2, 27.99, 4, TRUE, 12);

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

INSERT IGNORE INTO Cliente (id, nome, email, telemovel, NIF)
VALUES
(1,  'João Silva',        'joao.silva@gmail.com',        '912345678', '245678901'),
(2,  'Ana Martins',       'ana.martins@gmail.com',       '913456789', '256789012'),
(3,  'Pedro Costa',       'pedro.costa@gmail.com',       '914567890', '267890123'),
(4,  'Mariana Sousa',     'mariana.sousa@gmail.com',     '915678901', '278901234'),
(5,  'Ricardo Ferreira',  'ricardo.ferreira@gmail.com',  '916789012', '289012345'),
(6,  'Carla Mendes',      'carla.mendes@gmail.com',      '917890123', '290123456'),
(7,  'Tiago Oliveira',    'tiago.oliveira@gmail.com',    '918901234', '201234567'),
(8,  'Beatriz Gomes',     'beatriz.gomes@gmail.com',     '919012345', '212345678'),
(9,  'Luís Almeida',      'luis.almeida@gmail.com',      '920123456', '223456789'),
(10, 'Sofia Ribeiro',     'sofia.ribeiro@gmail.com',     '921234567', '234567890');


INSERT IGNORE INTO Trotinete (
    id, modelo, marca,
    num_serie, tipo_motor,
    cod_cliente
)
VALUES
(1,  'Mi Electric Scooter Pro 2', 'Xiaomi',   'XM2026A001', 'Brushless',     1),
(2,  'Max G30',                   'Ninebot',  'NB2026B002', 'Hub Motor',     2),
(3,  'M365',                      'Xiaomi',   'XM2026C003', 'Brushless',     3),
(4,  'Kirin M4',                  'Kugoo',    'KG2026D004', 'Dual Motor',    4),
(5,  'S1',                        'SmartGyro','SG2026E005', 'Rear Drive',    5),
(6,  'E25E',                      'Ninebot',  'NB2026F006', 'Front Drive',   6),
(7,  'Zero 9',                    'Zero',     'ZR2026G007', 'Hub Motor',     7),
(8,  'Speedway Mini 4 Pro',       'Minimotors','MM2026H008','Brushless',     8),
(9,  'Urban Glide Ride 80XL',     'UrbanGlide','UG2026I009','Direct Drive',  9),
(10, 'Dualtron Mini',             'Dualtron', 'DT2026J010', 'Dual Motor',    10);