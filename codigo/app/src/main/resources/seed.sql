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
    (1, 'txiXcBnz1JraH/xktt+Xs4nj9jjqGiVuHrFNc8eHxRbupVvHywxzTZOsFbsEkBR9nQ==', 'vdZlCiqXf2gH4lkPo5tPEwLj8bUaKiKMoOnIdYpXiYGiV86SXA==', 'fM3txdguclS6N4hHsG4JnolsDNeafJ4Qr/hRM/mfvOu5bHopO7qe6PAuZ/o=', 'w/OtBu+Oht0cM3AFNXujCTbHz78Y/UvA3mKV9+aJ433FF7U56Ws=',
     'R1eznDtBCbjxIPt6KJkNLM0aObyqRowPSGmTQhzA4vy4M/WhUphn', '2oY+pV8hvK5TiDTkPK60nN+DsbIotsz4Rfr0JmlXrq8pIoUXmg==', 'g1t/x0mx6xps8W1QM9nTXyZzQgNypWNk6h+oe+ig7FkYEHzasg==', '1N0RwcNs6/em5Q0q14wyN4NGZhXAZ+2A0SmBpaPYKhg3FuF8lQVgfHRxI1a+UwEnMKS/IKk=',
     '9U6UalScGCsQJoQIM2AZwLhofA+8Lm3eCFdwxm3TImw=', 'HnXw8EE/XagOhEq3mYDG24rl0VKAEmyRheLotU1hAPL/4g==', 'jVPRMRV3ZNDvaooRD8Dvc96QQRj74zu7+7iRA7QuY4XJwg==', 0,
     'df4dNEjK1sTD8l2F224W41DGjx2Tzo9B6/Ckd/I=', 'RkIZo2bkac6WjJUaNtBrHYf21OlJt1LNJPAC96KNjXBjuSl8BlAeGwQ=', '9qa/MlVS4ROHpCES1psgWdR67aCYshIkTYdBKDX3RIMODg==', 'omE0gs5U8Xtgv/gs/u5HbdOuutQSVLiys9bioJHBDuVDDXaE'),
    (2, 'dmilGOiwP5zk1CBKMJWWDQm01jyLYgKL+tdx2KVTyp1FtVvjqH1P', 'WQCNwx/3o5LO6vZ7A0Bo9FHK3ypbDlE4OGJzobOIz67xRDdIig==', 'zna3HUebif5vY/32J+TeFw1+qQWYZgBef2y/ik2gIQM8mJkl0ay+ANglHA==', '3EafdXiNv6S1kJOVcV/QhQ1/Bzy0+v61aEYXA8suUGUrRFWs+AY=',
     'KNRof1ZB65PYQMOmPdysFMw2xNLHpblW87zN/b0d+xa58GkqOFyw', 'ycCZt70/NG197w8sTbSx/vszMIPd9ahFmGswfMftde19u0QJqw==', 'OukYHA/taVZ462mGBWC9Cu8W+hl7PvtkaTzfL1tX5mupWiSX1g==', 'RLkUiQHjwFnlLXoORoEyl5eZXeyZCyFvL2iOMt1CzOkBQQHK5UiL8zhwecJSZQYlqlP5xOY=',
     'IheACt8R1SNVPitb/6zBvR/1nbVDuo1lGYUwh+aXZwE=', 'pLszODu43bTMspwZ8CUHrAeDL2Y3uofPe2tbRKb5qLvxEg==', 'FXEQjLbH09Xru9wq9sl1S3j+r/nrmMX655PgW/9AY8hhUQ==', 0,
     '3/ttGGM+n2X3bx1gZjJGM3ewqoAG7Ly1vxMoyDs=', 'xHwYVuRZUi4caZaoWFoJ3Q5YzbvUxA4VJN0kD4tYZ7qj9b50BZZ0SfAk6g==', 'Zs2ZM10kIrIB2G3XFdoYAWqWh5u/BjrmtOPiyUX8+llXYg==', 'C6wAuNsgEvL4lVp51uD53TqpD78jR7tNvHXmjZZrkfGDvKsL'),
    (3, 'WAA73qiOp/ifVv5862n05LavXDG1WOORn6KKX7Ww3LrjLqKlFDCcbazr8g==', 'loRY7CCIwOeLqDhX3Ro2m3q1lL8OQagFcqlyxKmSm4Yb/wOORQ==', 'WaH4LYtP1oeiDQ0BrlmBj0ZItJDwGimiUaKt8LWjz+rh96j8j7ke3nmvDEEubw==', 'SE19gtFlw2+QiDypP2rNNBCmZBPdv94+VibOiwn5tYOoxsRKJk8=',
     '/+8rDI4QQ/8h8auEvLlhdtAb4PJYx7NQcP+hCWg7MjqLq/XNGimm', 'nrcTz3lkJe701hWv7mRpzZV2C2G0hViEK0fZ8jlXA4DaN6HoUw==', 'efj+fIo0zG4A9bm7SKvat8GAsRqIeumO2/zGovmZBB7UlzbREQ==', 'f5XnDS+HNRxUiNse0RH4PDvaO8Z1ZWojRaXevZ4PBEm2utH97nQ48qmIIJf94wxFQoR1Ygs=',
     'dQifHQOTFs3Lc3mBv1sIRx2WkJGKdNhLt3se+0GA8qs=', 'msogadNbx8eJZbOfLDsj/QUbFuAdGXwmWXKtV9E5wU5HNA==', 'dxi94LmuI/cfbiE3CQCSkgL+12/sy1DT11yMvMVW8bcwgw==', 0,
     'SP4yLwy9eAlp19MBjTUTTCMNsLa8Zem0B4x2qHs=', 'FXPgfFbUxEVuja0q7RQYFIc9dyQytFpPSVVQY5k2fHjTqJPoQzyLUyQ=', '5hn80W645NM3YhdO+Iao0UR4mYbx4n0HDk7ZjPZVGEkVIA==', 'lCVA/GezWX10b5g6IunBj6LOLFBKDL1wzDGAGbVNbSmKppYG'),
    (4, 'WIACjz+ldN6Bmyj/AO7xFVUiWPTv/Tua4pQi8ZSEJSGrYamRhHobOHa7', 'ko1yJxgB26LdyMQi/A5+SQOYTn3OPIzC5XMRLgUwlqs6eclO6g==', 'l7a7hjRX5dw3MNqvNdZsBFVorU35NvcOzYIRPImzulSnFfyrA2tBELku4dEp', 'Ht12txE74HMyBCZzirKJ0AGlKk0pddNjf4nIhMT3hsgnXO9DZ7o=',
     'Q1Ybx+VlUUR4MuneUOXPSao8kRh+yB8ACXqBBC4MZUWsN7LCZdqU', 'xy54oWG6+V8+Hgd3P7HfrnRV18gWyErnh6RiHNwosX0NbNBjwQ==', 'rlQ6F9FueoJ1E7wyiFmKruEg+cNxVKn+7PTG8GO17WK8dBkBsQ==', 'VKlcWyspVUFEu4Hwrcs/Z/Q30oWzgnSsQBNTfoNFCrxaEtx6Be7JshUkujKBdAZkyIKSZDQ=',
     'uHGVhhEOOCaDUaUbKDscWyPzOi6pFYB9kb5YhIAfxdQ=', 'sTdTKXTW/ibcyB7cCsNf2yW6XhSN2AfSJp2GAhaCM2qFIA==', 'dRVlTHdzW8oW63W2Nhz3rO1TooOUOVa+eARpmUhG5bynwQ==', 0,
     'CUeNDJFVUTrCSDckYwrE5hKFNa8CGB6a8C0H+Yk=', 'OlgR9RXDAlZ+6a0CQyfJnVSAvYxb/n5ICUVsvFuG14p6Z2nxvh0DdDxCvoZT3Elr', 'Wg1A0p7tiijEHb9HSe2bk//Xi9utOXFweDUvIU1WRIerCw==', 'BmmGQOvdsxQc22xfgKDcZQ5mhJYpR+cLeXgB8iN82Q/qw+Tn'),
    (5, 'gye8hniBYnVN5bT0BqBJlg7vSQvpReGAB4xmOXqqOgcLsqbCntw=', 'KpzfJHIGtxScQwYi2TLQpYA7jiEefnoiyead7DDPxZe2roRhDQ==', 'azzoC9uMkWWkjrCWt+YLKGbuw+skHdy4M+2oe9sg7FLV19yNhVcXZMlOvsI=', '+4bFNV0K8l89ocXb8dn5ECXntoBUDa8l84dNF9BnBxNSR9cva+E=',
     '0llvSPSqc2RcjEsnEXuxiECES5YbxxW8xdIBh17jCFhfaU+YOAwS', 'WI9YHRE+Ut4l5q58VU0iwuLQ9NhNn5o22L4+VDXm1uslGps7JA==', 'P2VDKbhZ/1U/CIN3lgnrWQWe31iiBTfeIQS5T31d379UxiuC7g==', 'C5kOqHPhXXpkt8KxbzdV/QsRi7gKle7ZG8WQYGc5BG23weiUDIUWCRKX9z5LovXtcv/qWos=',
     'hYOr/49sZ0GWPY+sacerndMqgmB/7s0cMKrojkbZDkI=', 'tSkU3OY65YMSqZRdPnKL9edju/AELPegVsXMtIcksVv4Lw==', '9HOr5VNnXrlqjCnaj7a67gQPLZehU9KBDjNm0AJINrwpAQ==', 0,
     'An8kX7xXL3Khjx764eWokx/aiB6el7WwK27fmzA=', 'cmBtt2f08ryUPjIUJrOVFTMYiL1mabcryRqgPm+VLw2LwkKWdbFTsm1fQTk=', 'bXrq2khEykCmtvMkOrw3TZjTiZkqj29J1nq6KJ2c66l6Pg==', 'Cd1O31Au2K+DmNO6a4y6AseaEz0zqwtJPse/BuT8Uv5ETCu/');

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

-- =========================================================
-- Stock
-- =========================================================

-- Stocks consumidos nos conseretos das OS Pagas (estado final correto após conserto)
INSERT IGNORE INTO Stock (id, preco_compra, codPeca, data_chegada, quantidade, garantia, estado)
VALUES
    (1,  15.00, 6,  '2025-01-10', 1, '2025-07-10', 'StockUsadoConserto'),  -- Pneu 8.5 — usado na OS 1
    (2,   5.00, 8,  '2025-01-10', 1,  NULL,         'StockUsadoConserto'),  -- Câmara 8.5 — usado na OS 1
    (3,  99.00, 1,  '2025-02-01', 1, '2026-02-01',  'StockUsadoConserto'),  -- Bateria 36V — usado na OS 2
    (4,  35.00, 15, '2025-03-01', 1, '2026-03-01',  'StockUsadoConserto'),  -- Controlador 36V — usado na OS 3
    (5,  12.00, 28, '2025-03-01', 1,  NULL,         'StockUsadoConserto');  -- Kit Cablagem — usado na OS 3

-- Stocks em armazém disponíveis para futuras reparações
INSERT IGNORE INTO Stock (id, preco_compra, codPeca, data_chegada, quantidade, garantia, estado)
VALUES
    (6,  15.00, 6,  '2025-04-01', 5, '2025-10-01', 'StockEmArmazem'),  -- Pneu 8.5
    (7,   5.00, 8,  '2025-04-01', 8,  NULL,         'StockEmArmazem'),  -- Câmara 8.5
    (8,   8.00, 10, '2025-04-05', 6, '2025-10-05', 'StockEmArmazem'),  -- Pastilhas de Travão
    (9,  99.00, 1,  '2025-04-10', 3, '2026-04-10', 'StockEmArmazem'),  -- Bateria 36V 10Ah
    (10, 34.00, 13, '2025-04-15', 2, '2026-04-15', 'StockEmArmazem');  -- Display LCD M365

-- =========================================================
-- Ordens de Serviço — Pagas (3) e Eliminadas (2)
-- =========================================================

INSERT IGNORE INTO OrdemServico (id, descricao, data_criacao, codTrotinete, codCliente, codCriador, codMecanico, estado)
VALUES
    (1, 'Pneu furado e câmara de ar danificada. Cliente refere impacto com passeio.',
        '2025-01-15 09:00:00', 1, 1, 3, 4, 'Paga'),
    (2, 'Bateria não segura carga. Trotinete desliga-se após percurso mínimo.',
        '2025-02-10 10:30:00', 3, 3, 3, 5, 'Paga'),
    (3, 'Controlador com falhas intermitentes e cablagem de alimentação danificada.',
        '2025-03-05 14:00:00', 5, 5, 3, 4, 'Paga'),
    (4, 'Reclamação de ruído no motor. Cliente não compareceu para levantamento.',
        '2025-02-20 11:00:00', 2, 2, 3, 4, 'Eliminada'),
    (5, 'Solicitação de revisão geral. Cliente cancelou o serviço.',
        '2025-04-01 09:30:00', 7, 7, 1, NULL, 'Eliminada');

-- Pagamentos das OSs pagas (clienteNotificado=TRUE pois já estão pagas)
INSERT IGNORE INTO Pagamento (idOS, metodo, dataPagamento, clienteNotificado, dataNotificacao)
VALUES
    (1, 'MULTIBANCO', '2025-01-20 10:00:00', TRUE, '2025-01-19 15:00:00'),
    (2, 'NUMERARIO',  '2025-02-15 11:30:00', TRUE, '2025-02-14 09:00:00'),
    (3, 'MBWAY',      '2025-03-10 16:00:00', TRUE, '2025-03-09 14:00:00');

-- Acessórios entregues com as trotinetes
INSERT IGNORE INTO OrdemServico_Acessorio (idOS, ordem, valor)
VALUES
    (1, 1, 'Cadeado'),
    (1, 2, 'Carregador original'),
    (2, 1, 'Carregador original'),
    (3, 1, 'Bolsa de transporte');

-- =========================================================
-- Diagnósticos (apenas OS Pagas — aprovado = TRUE)
-- =========================================================

INSERT IGNORE INTO Diagnostico (idOS, descricao, orcamento, aprovado)
VALUES
    -- OS1: rep2(15)+rep3(8) + peca6(24.99)+peca8(7.99) = 55.98
    (1, 'Pneu com corte irreparável. Câmara de ar furada. Necessária substituição de ambos.', 55.98, TRUE),
    -- OS2: rep1(10)+rep8(20) + peca1(149.99) = 179.99
    (2, 'Bateria com células degradadas. Capacidade real inferior a 20% do original.', 179.99, TRUE),
    -- OS3: rep9(20)+rep18(18) + peca15(49.99)+peca28(17.99) = 105.98
    (3, 'Controlador com MOSFET danificado. Cablagem de alimentação com quebra interna.', 105.98, TRUE);

INSERT IGNORE INTO Diagnostico_PecaOrcamento (idOS, codPeca, quantidade)
VALUES
    (1, 6,  1),  -- Pneu 8.5
    (1, 8,  1),  -- Câmara 8.5
    (2, 1,  1),  -- Bateria 36V 10Ah
    (3, 15, 1),  -- Controlador 36V
    (3, 28, 1);  -- Kit Cablagem

INSERT IGNORE INTO Diagnostico_Reparacao (idOS, codReparacao)
VALUES
    (1, 2),   -- Troca de Pneu
    (1, 3),   -- Reparação de Câmara de Ar
    (2, 1),   -- Diagnóstico Geral
    (2, 8),   -- Substituição de Bateria
    (3, 9),   -- Reparação de Controlador
    (3, 18);  -- Reparação de Cabelagem

-- =========================================================
-- Conseretos (apenas OS Pagas)
-- =========================================================

INSERT IGNORE INTO Conserto (idOS, preco_total, chk_luzes, chk_pneus, chk_aceleracao, chk_travagem, chk_visor, chk_teste_pratico)
VALUES
    -- OS1: rep2(15)+rep3(8) + peca6(24.99)+peca8(7.99) = 55.98 <= orcamento(55.98)
    (1,  55.98, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE),
    -- OS2: rep8(20) + peca1(149.99) = 169.99 <= orcamento(179.99)
    (2, 169.99, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE),
    -- OS3: rep9(20)+rep18(18) + peca15(49.99)+peca28(17.99) = 105.98 <= orcamento(105.98)
    (3, 105.98, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE);

INSERT IGNORE INTO Conserto_PecaUsada (idOS, codStock, quantidade)
VALUES
    (1, 1, 1),  -- Pneu 8.5   (stock 1)
    (1, 2, 1),  -- Câmara 8.5 (stock 2)
    (2, 3, 1),  -- Bateria 36V (stock 3)
    (3, 4, 1),  -- Controlador 36V (stock 4)
    (3, 5, 1);  -- Kit Cablagem (stock 5)

INSERT IGNORE INTO Conserto_Reparacao (idOS, codReparacao)
VALUES
    (1, 2),   -- Troca de Pneu
    (1, 3),   -- Reparação de Câmara de Ar
    (2, 8),   -- Substituição de Bateria
    (3, 9),   -- Reparação de Controlador
    (3, 18);  -- Reparação de Cabelagem