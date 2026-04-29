package app.ecoRideLN.sFinanceiro;

import java.time.LocalDateTime;

public class MovimentoFinanceiro {

    private int id;
    private float valor;
    private LocalDateTime data;
    private String descricao;
    private int codEntidade;
    private String tipoEntidade;
    private TipoMovimento tipo;

    public MovimentoFinanceiro(int id, float valor, LocalDateTime data, String descricao,
                               TipoMovimento tipo, int codEntidade, String tipoEntidade) {
        this.id = id;
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
        this.tipo = tipo;
        this.codEntidade = codEntidade;
        this.tipoEntidade = tipoEntidade;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public float getValor() { return valor; }
    public void setValor(float valor) { this.valor = valor; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getCodEntidade() { return codEntidade; }
    public void setCodEntidade(int codEntidade) { this.codEntidade = codEntidade; }

    public String getTipoEntidade() { return tipoEntidade; }
    public void setTipoEntidade(String tipoEntidade) { this.tipoEntidade = tipoEntidade; }

    public TipoMovimento getTipo() { return tipo; }
    public void setTipo(TipoMovimento tipo) { this.tipo = tipo; }
}
