package app.ecoRideLN.sOrdensServico;

public class OrdemServico {

     private int id;
     private Registo registo;
     private Integer codMecanico;
     private EstadoOS estado;
     private Conserto conserto;
     private Diagnostico diagnostico;
     private Pagamento pagamento;

     public OrdemServico(int id, Registo registo) {
          this.id = id;
          this.registo = registo;
          this.codMecanico = null;
          this.estado = EstadoOS.PendenteDiagnostico;
          this.conserto = null;
          this.diagnostico = null;
          this.pagamento = null;
     }

     public int getId() { return id; }
     public void setId(int id) { this.id = id; }

     public Registo getRegisto() { return registo; }
     public void setRegisto(Registo registo) { this.registo = registo; }

     public Integer getCodMecanico() { return codMecanico; }
     public void setCodMecanico(Integer codMecanico) { this.codMecanico = codMecanico; }

     public EstadoOS getEstado() { return estado; }
     public void setEstado(EstadoOS estado) { this.estado = estado; }

     public Conserto getConserto() {
          return this.conserto == null ? null : new Conserto(this.conserto);
     }
     public void setConserto(Conserto conserto) { this.conserto = conserto; }

     public Diagnostico getDiagnostico() {
          return this.diagnostico == null ? null : new Diagnostico(this.diagnostico);
     }
     public void setDiagnostico(Diagnostico diagnostico) { this.diagnostico = diagnostico; }

     public Pagamento getPagamento() { return pagamento; }
     public void setPagamento(Pagamento pagamento) { this.pagamento = pagamento; }
}
