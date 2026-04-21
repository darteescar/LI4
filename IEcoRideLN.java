import java.time.LocalDateTime;
import java.util.List;

public interface IEcoRideLN {

    Funcionario autenticar(int id, String palavra_passe);

    Funcionario registarFuncionario(String nome, String numero_porta, String rua, String localidade, String codigo_postal, String telemovel, String email, String data_nascimento, String niss, String nif, String nus, String iban, float salario_hora, float salario_bruto, float salario_liquido, String palavra_passe);

    Funcionario obterDadosFuncionario(int id);

    boolean removerFuncionario(int id);

    boolean existeFuncionario(int id);

    MovimentoFinanceiro obterDadosMovimentoFinanceiro(int id);

    boolean removerMovimentoFinanceiro(int id);

    List<MovimentoFinanceiro> obterMovimentos(LocalDateTime desde, LocalDateTime ate, TipoMovimento tipo);

    boolean existeMovimentoFinanceiro(int id);

    Reparacao registarReparacao(String nomenclatura, String descricao, float preco);

    Reparacao obterDadosReparacao(int id);

    boolean removerReparacao(int id);

    boolean existeFornecedor(int id);

    Peca registarPeca(String ref, int stock_minimo, float preco_venda, int id_fornecedor);

    Fornecedor obterDadosFornecedor(int id);

    boolean removerFornecedor(int id);

    Peca obterDadosPeca(int id);

    boolean removerPeca(int id);

    boolean existePeca_id(int id);

    boolean existePeca_ref(String ref);

    Stock obterDadosStock(int id);

    boolean existeStock(int id);

    boolean removerStock(int id);

    Stock registarStock_PecaSuperior70(int id_peca, float preco_compra, LocalDateTime data, int garantia, String nr_serie);

    Stock registarStock_PecaNormal(int id_peca, float preco_compra, LocalDateTime data);

    Fornecedor registarFornecedor(String nome, String telemovel, String email);

    int pecasDefeituosas_Stock(int id_peca);

    Devolucao criarDevolucao(LocalDateTime data_devolucao, String motivo, int id_stock);

    void alteraEstado_Stock_PendenteDevolucao(List<String> pecas);

    Devolucao obterDadosDevolucao(int id);

    boolean removerDevolucao(int id);

    List<Integer> obter_Pecas_baixo_Stock_minimo();

    int quantidade_encomendar_peca(int id_peca);

    void criarEncomenda(List<Stock> pecas);

    Encomenda obterDadosEncomenda(int id);

    void adicionar_PecasEncomenda_Stock(List<Stock> pecas);

    boolean removerEncomenda(int id);

    Cliente registarCliente(String nome, String email, String telemovel, String nif);

    Cliente obterDadosCliente(int id);

    List<OrdemServico> obterOS_Cliente(int id);

    boolean removerCliente(int id);

    List<Trotinete> obterTrotinetes_Cliente(int id);

    boolean existeCliente(int id_cliente);

    Trotinete registarTrotinete(String modelo, String marca, String numero_serie, String tipo_moto, int id_cliente);

    List<OrdemServico> obterOS_Trotinete(int id);

    Trotinete obterDadosTrotinete(int id);

    boolean removerTrotinete(int id);

    void atualizaEstadoStock(int id, EstadosStock estado);

    MovimentoFinanceiro criarMovimentoFinanceiro(float valor_compra, LocalDateTime data, String descricao, TipoMovimento tipo);

    OrdemServico registarOS(int codResponsavel, int id_cliente, int id_trotinete, String descricao);

    OrdemServico registarOS_Extras(int codResponsavel, int id_cliente, int id_trotinete, String descricao, List<String> acessorios, List<Fotografia> fotos);

    OrdemServico obterDadosOS(int id);

    boolean removerOS(int id);

    float adicionarHorasExtra(int id, int horas_extra);

    void alterarDescricaoOS(int id, String descricao);

    void alterarAcessoriosOS(int id, List<String> acessorios);

    void alterarFotografiasOS(int id, List<Fotografia> fotos);

    void alterarEstadoOS(int id, EstadoOS estado);

    void adicionarPecas_Conserto_OS(int id, List<Stock> pecas);

    boolean clienteNaoTemPagamentosPendentes(int id);

    List<OrdemServico> filtrarOSs(EstadoOS estado, LocalDateTime desde, LocalDateTime ate, int id_cliente, int id_funcionario);

    List<Conserto> obterConsertosAnteriores(int id_trotinete);

    int obter_quantidade_Stock_Peca_id(int id);

    int obter_quantidade_Stock_Peca_ref(String referencia);

    float calcularOrcamento(List<QuantidadePeca> listaPecas, List<Reparacao> reparacoes);

    boolean pecasDiagnosticoDisponiveisReparacao(int id_OS);

    boolean removerPecaConserto_OS(int id_OS, int id_Stock);

    void registar_Defeito_entradaStock(int id_Stock);

    boolean validarNome(String nome);

    boolean validarNrPorta(String numero_porta);

    boolean validarRua(String rua);

    boolean validarLocalidade(String localidade);

    boolean validarCodigoPostal(String codigo_postal);

    boolean validarTelemovel(String telemovel);

    boolean validarEmail(String email);

    boolean validarDataNascimento(String data);

    boolean validarNISS(String niss);

    boolean validarNIF(String nif);

    boolean validarNUS(String nus);

    boolean validarIBAN(String iban);

    boolean validarSalarioHora(float salario_hora);

    boolean validarSalarioBruto(float salario_bruto);

    boolean validarSalarioLiquido(float salario_liquido);

    boolean validarPalavraPasse(String palavra_passe);

    boolean validarHorasExtra(int horas_extra);

    boolean validarNomenclatura(String nomenclatura);

    boolean validarDescricao(String descricao);

    boolean validarPrecoExecucao(float preco);

    boolean validarReferencia(String ref);

    boolean validarStockMinimo(int stock_minimo);

    boolean validarPrecoVenda(float preco_venda);

    boolean validarQuantidade(int quantidade);

    boolean validarPrecoCompra(float preco);

    boolean validarNrSerie(String nr_serie);

    boolean validarGarantia(int garantia);

    boolean validarMotivo(String motivo);

    boolean validaEncomenda_Rascunho(int id_Encomenda);

    boolean validarMarca(String marca);

    boolean validarModelo(String modelo);

    boolean validarTipoMotor(String tipo_motor);

    boolean validarAcessorio(String acessorio);

    boolean validarFotografia(Fotografia foto);

    void atualizarNomeFuncionario(int id, String nome);

    void atualizarNumeroPortaFuncionario(int id, String numero_porta);

    void atualizarRuaFuncionario(int id, String rua);

    void atualizarLocalidadeFuncionario(int id, String localidade);

    void atualizarCodigoPostalFuncionario(int id, String codigo_postal);

    void atualizarTelemovelFuncionario(int id, String telemovel);

    void atualizarEmailFuncionario(int id, String email);

    void atualizarDataNascimentoFuncionario(int id, String data_nascimento);

    void atualizarNISSFuncionario(int id, String niss);

    void atualizarNIFFuncionario(int id, String nif);

    void atualizarIBANFuncionario(int id, String iban);

    void atualizarNUSFuncionario(int id, String nus);

    void atualizarSalarioHoraFuncionario(int id, float salario_hora);

    void atualizarSalarioBrutoFuncionario(int id, float salario_bruto);

    void atualizarSalarioLiquidoFuncionario(int id, float salario_liquido);

    void atualizarPalavraPasseFuncionario(int id, String palavra_passe);

    void atualizarNomenclaturaReparacao(int id, String nomenclatura);

    void atualizarDescricaoReparacao(int id, String descricao);

    void atualizarPrecoReparacao(int id, float preco);

    void atualizarValorCompraMovimentoFinanceiro(int id, float valor_compra);

    void atualizarDataMovimentoFinanceiro(int id, LocalDateTime data);

    void atualizarDescricaoMovimentoFinanceiro(int id, String descricao);

    void atualizarNomeCliente(int id, String nome);

    void atualizarEmailCliente(int id, String email);

    void atualizarTelemovelCliente(int id, String telemovel);

    void atualizarNIFCliente(int id, String nif);

    void atualizarModeloTrotinete(int id, String modelo);

    void atualizarMarcaTrotinete(int id, String marca);

    void atualizarNumeroSerieTrotinete(int id, String numero_serie);

    void atualizarTipoMotorTrotinete(int id, String tipo_motor);

    void atualizarReferenciaPeca(int id, String referencia);

    void atualizarStockMinimoPeca(int id, int stock_minimo);

    void atualizarPrecoVendaPeca(int id, float preco_venda);

    void atualizarIdFornecedorPeca(int id, int id_fornecedor);

    void atualizarIdPecaStock(int id_stock, int id_peca);

    void atualizarPrecoCompraStock(int id_stock, float preco_compra);

    void atualizarDataRececaoStock(int id_stock, LocalDateTime data_rececao);

    void atualizarGarantiaStock(int id_stock, int garantia);

    void atualizarNrSerieStock(int id_stock, String nr_serie);

    void atualizarNomeFornecedor(int id, String nome);

    void atualizarTelemovelFornecedor(int id, String telemovel);

    void atualizarEmailFornecedor(int id, String email);

    void atualizarPecasEncomenda(int id, List<Stock> pecas);

    void atualizarDataChegadaEncomenda(int id, LocalDateTime data_chegada);

    void atualizarDataPedidoEncomenda(int id, LocalDateTime data_pedido);

    void atualizarEstadoEncomenda(int id, EstadoEncomenda estado);

    Historico obterDadosHistorico(int id);

    boolean removerHistorico(int id);

    boolean existeHistorico(int id);

    void atualizarDataHoraHistorico(int id, LocalDateTime data_hora);

    void atualizarCodAutorHistorico(int id, int codAutor);

    Notificacao obterDadosNotificacao(int id);

    boolean removerNotificacao(int id);

    boolean existeNotificacao(int id);

    void atualizarDescricaoNotificacao(int id, String descricao);

    void atualizarDataEmissaoNotificacao(int id, LocalDateTime data_emissao);

    void atualizarIdRemetenteNotificacao(int id, int id_remetente);

    void atualizarIdDestinatarioNotificacao(int id, int id_destinatario);

    List<Notificacao> obterNotificacoesPorClienteEIntervalo(int id_cliente, LocalDateTime data_inicio, LocalDateTime data_fim);

    List<Notificacao> obterNotificacoesPorFuncionarioEIntervalo(int id_funcionario, LocalDateTime data_inicio, LocalDateTime data_fim);

    List<Notificacao> obterNotificacoesPorIntervalo(LocalDateTime data_inicio, LocalDateTime data_fim);

    boolean existeDevolucao(int id);

    void atualizarMotivoDevolucao(int id, String motivo);

    void atualizarDataDevolucao(int id, LocalDateTime data);

    void atualizarCodStockDevolucao(int id, int codStock);

    void atualizarEstadoDevolucao(int id, EstadoDevolucao estado);

    boolean validaEstadoDevolucao_PendenteDevolucao(int id);

    Notificacao criarNotificacao(String descricao, int id_remetente, int id_destinatario);

    void alterarDataCriacaoOS(int id, LocalDateTime data_criacao);

    void alterarFuncionarioResponsavelOS(int id, int codResponsavel);

    void alterarClienteOS(int id, int idCliente);

    void alterarTrotineteOS(int id, int idTrotinete);

    List<OrdemServico> filtrarOSsPorCliente(int id_cliente);

    List<OrdemServico> filtrarOSsPorFuncionario(int id_funcionario);

    List<OrdemServico> filtrarOSsPorIntervalo(LocalDateTime desde, LocalDateTime ate);

    List<OrdemServico> filtrarOSsPorClienteEIntervalo(int id_cliente, LocalDateTime desde, LocalDateTime ate);

    List<OrdemServico> filtrarOSsPorFuncionarioEIntervalo(int id_funcionario, LocalDateTime desde, LocalDateTime ate);

    List<OrdemServico> filtrarOSsPorClienteEFuncionario(int id_cliente, int id_funcionario);

    void registarDiagnosticoOS(int id, List<QuantidadePeca> listaPecas, List<Reparacao> reparacoes, String descricao);

    List<Reparacao> obterReparacoesDiagnosticoOS(int id_OS);

    List<QuantidadePeca> obterPecasQuantidadeDiagnosticoOS(int id_OS);

    void adicionarPecaConserto_OS(int id_OS, int id_Stock);

    void registarConsertoOS(int id_OS, int idMecanico, List<Stock> pecas, List<Reparacao> reparacoes);

    boolean validarCheckList_ConsertoOS(int idOS);

}
