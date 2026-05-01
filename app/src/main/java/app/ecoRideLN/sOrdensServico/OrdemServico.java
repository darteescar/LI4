package app.ecoRideLN.sOrdensServico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import app.ecoRideLN.sClientes.Trotinete;
import app.ecoRideLN.sFuncionarios.Funcionario;

public class OrdemServico {
     private int id;
     private String descricao;
     private LocalDateTime data_criacao;
     private int codTrotinete;
     private int codCliente;
     private int codResponsavel;
     private List<Fotografia> fotografias;
     private EstadoOS estado;
     private List<String> acessorios;
     
     private static final TrotineteDAO trotDAO = TrotineteDAO.getInstance();
     private static final ClienteDAO clienteDAO = ClienteDAO.getInstance();
     private static final FuncionarioDAO funcionarioDAO = FuncionarioDAO.getInstance();

     public OrdemServico(int id, String descricao, LocalDateTime data_criacao, int codTrotinete, int codCliente, int codResponsavel, List<Fotografia> fotografias, EstadoOS estado, List<String> acessorios) {
          this.id = id;
          this.descricao = descricao;
          this.data_criacao = data_criacao;
          this.codTrotinete = codTrotinete;
          this.codCliente = codCliente;
          this.codResponsavel = codResponsavel;
          this.fotografias = new ArrayList<>(fotografias);
          this.estado = estado;
          this.acessorios = new ArrayList<>(acessorios);
     }

     public int getId() {
          return id;
     } 

     public void setId(int id) {
          this.id = id;
     }

     public String getDescricao() {
          return descricao;
     }

     public void setDescricao(String descricao) {
          this.descricao = descricao;
     }

     public LocalDateTime getDataCriacao() {
          return data_criacao;
     }

     public void setDataCriacao(LocalDateTime data_criacao) {
          this.data_criacao = data_criacao;
     }

     public int getCodTrotinete() {
          return codTrotinete;
     }

     public void setCodTrotinete(int codTrotinete) {
          this.codTrotinete = codTrotinete;
     }

     public int getCodCliente() {
          return codCliente;
     }

     public void setCodCliente(int codCliente) {
          this.codCliente = codCliente;
     }

     public int getCodResponsavel() {
          return codResponsavel;
     }

     public void setCodResponsavel(int codResponsavel) {
          this.codResponsavel = codResponsavel;
     }

     public List<Fotografia> getFotografias() {
          return new ArrayList<>(fotografias);
     }

     public void setFotografias(List<Fotografia> fotografias) {
          this.fotografias = new ArrayList<>(fotografias);
     }

     public EstadoOS getEstado() {
          return estado;
     }

     public void setEstado(EstadoOS estado) {
          this.estado = estado;
     }

     public List<String> getAcessorios() {
          return new ArrayList<>(acessorios);
     }

     public void setAcessorios(List<String> acessorios) {
          this.acessorios = new ArrayList<>(acessorios);
     }

     // Métodos a mais

     public Trotinete getTrotinete() {
          return trotDAO.getTrotineteById(codTrotinete);
     }

     public Cliente getCliente() {
          return clienteDAO.getClienteById(codCliente);
     }

     public Funcionario getResponsavel() {
          return funcionarioDAO.getFuncionarioById(codResponsavel);
     }





}
