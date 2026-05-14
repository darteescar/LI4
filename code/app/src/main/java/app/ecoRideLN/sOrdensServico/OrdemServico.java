package app.ecoRideLN.sOrdensServico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico {

     private int id;
     private String descricao;
     private LocalDateTime data_criacao;
     private int codTrotinete;
     private int codCliente;
     private int codCriador;
     private Integer codMecanico;
     private EstadoOS estado;
     private List<String> acessorios;
     private Conserto conserto;
     private Diagnostico diagnostico;
     private Metodo_Pagamento metodo_pagamento;

     public OrdemServico(int id, String descricao, LocalDateTime data_criacao, int codTrotinete, int codCliente, int codCriador, List<String> acessorios) {
          this.id = id;
          this.descricao = descricao;
          this.data_criacao = data_criacao;
          this.codTrotinete = codTrotinete;
          this.codCliente = codCliente;
          this.codCriador = codCriador;
          this.codMecanico = null;
          this.estado = EstadoOS.PendenteDiagnostico;
          if (acessorios == null) {
               this.acessorios = new ArrayList<>();
          } else {
               this.acessorios = new ArrayList<>(acessorios);
          }
          this.conserto = null;
          this.diagnostico = null;
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

     public int getCodCriador() {
          return codCriador;
     }

     public void setCodCriador(int codCriador) {
          this.codCriador = codCriador;
     }

     public Integer getCodMecanico() {
          return codMecanico;
     }

     public void setCodMecanico(Integer codMecanico) {
          this.codMecanico = codMecanico;
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

     public Conserto getConserto() {
          if (this.conserto == null) {
               return null;
          }
          return new Conserto(this.conserto);
     }

     public void setConserto(Conserto conserto) {
          this.conserto = conserto;
     }

     public Diagnostico getDiagnostico() {
          if (this.diagnostico == null) {
               return null;
          }
          return new Diagnostico(this.diagnostico);
     }

     public void setDiagnostico(Diagnostico diagnostico) {
          this.diagnostico = diagnostico;
     }

     public Metodo_Pagamento getMetodo_pagamento() {
          return metodo_pagamento;
     }

     public void setMetodo_pagamento(Metodo_Pagamento metodo_pagamento) {
          this.metodo_pagamento = metodo_pagamento;
     }

}
