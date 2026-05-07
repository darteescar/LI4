package app.IecoRideCA.controllers.financeiro;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import app.IecoRideCA.auth.GestorSessoes;
import app.ecoRideLN.IEcoRideLN;
import app.ecoRideLN.sAutenticacao.Cargo;
import app.ecoRideLN.sFinanceiro.AnaliseFinanceira;
import app.ecoRideLN.sFinanceiro.MovimentoFinanceiro;
import app.ecoRideLN.sFinanceiro.TipoMovimento;
import app.IecoRideCA.controllers.financeiro.dto.MovimentoFinanceiroResponse;
import io.javalin.Javalin;

public class FinanceiroController {

    private final IEcoRideLN facade;

    public FinanceiroController(IEcoRideLN facade) {
        this.facade = facade;
    }

    public void register(Javalin app) {

        app.get("/api/movimentosfinanceiros", ctx -> {

        GestorSessoes.verifica_cargo(ctx, Cargo.Gerente);

        String desdeStr = ctx.queryParam("desde");
        String ateStr = ctx.queryParam("ate");
        String tipoStr = ctx.queryParam("tipo");

        LocalDate desde = null;
        LocalDate ate = null;
        TipoMovimento tipo = null;

        try {

            if (desdeStr != null) {
                desde = LocalDate.parse(desdeStr);
            }

            if (ateStr != null) {
                ate = LocalDate.parse(ateStr);
            }

            if (tipoStr != null) {
                tipo = TipoMovimento.valueOf(tipoStr.toUpperCase());
            }

        } catch (Exception e) {
            ctx.status(400).result("Parâmetros inválidos");
            return;
        }

        List<MovimentoFinanceiro> movimentos =
            facade.obterMovimentosFinanceirosFiltrados(desde, ate, tipo);

        AnaliseFinanceira analise =
            facade.calcularAnaliseFinanceira(movimentos);

        ctx.json(new MovimentoFinanceiroResponse(movimentos, analise));
    });

    }
}
