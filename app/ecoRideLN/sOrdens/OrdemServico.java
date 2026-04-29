package app.ecoRideLN.sOrdens;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico {

    private int id;
    private String descricao;
    private int codCliente;
    private int codTrotinete;
    private LocalDateTime data_criacao;
    private int codResponsavel;
    private EstadoOS estado;
    private final List<String> acessorios;
    private final List<Integer> codFotografias;
    private Diagnostico diagnostico;
    private Conserto conserto;

    public OrdemServico(int id, String descricao, int codCliente, int codTrotinete,
                        LocalDateTime data_criacao, int codResponsavel) {
        this.id = id;
        this.descricao = descricao;
        this.codCliente = codCliente;
        this.codTrotinete = codTrotinete;
        this.data_criacao = data_criacao;
        this.codResponsavel = codResponsavel;
        this.estado = EstadoOS.PENDENTE_DIAGNOSTICO;
        this.acessorios = new ArrayList<>();
        this.codFotografias = new ArrayList<>();
        this.diagnostico = null;
        this.conserto = null;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getCodCliente() { return codCliente; }
    public void setCodCliente(int codCliente) { this.codCliente = codCliente; }

    public int getCodTrotinete() { return codTrotinete; }
    public void setCodTrotinete(int codTrotinete) { this.codTrotinete = codTrotinete; }

    public LocalDateTime getData_criacao() { return data_criacao; }
    public void setData_criacao(LocalDateTime data_criacao) { this.data_criacao = data_criacao; }

    public int getCodResponsavel() { return codResponsavel; }
    public void setCodResponsavel(int codResponsavel) { this.codResponsavel = codResponsavel; }

    public EstadoOS getEstado() { return estado; }
    public void setEstado(EstadoOS estado) { this.estado = estado; }

    public List<String> getAcessorios() { return acessorios; }
    public List<Integer> getCodFotografias() { return codFotografias; }

    public Diagnostico getDiagnostico() { return diagnostico; }
    public void setDiagnostico(Diagnostico diagnostico) { this.diagnostico = diagnostico; }

    public Conserto getConserto() { return conserto; }
    public void setConserto(Conserto conserto) { this.conserto = conserto; }
}
