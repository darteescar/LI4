package app.IEcoRideLN;

import app.common.EcoRideException;
import io.javalin.Javalin;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;

public class GlobalExceptionHandler {

    private GlobalExceptionHandler() {}

    public static void register(Javalin app) {

        // 400 — regra de negócio violada (identificador duplicado, transição inválida, etc.)
        app.exception(EcoRideException.class, (e, ctx) ->
            ctx.status(400).json(new ErroResponse(400, "Bad Request", e.getMessage()))
        );

        // 400 — parâmetro numérico inválido (ex: /api/os/abc)
        app.exception(NumberFormatException.class, (e, ctx) ->
            ctx.status(400).json(new ErroResponse(400, "Bad Request", "Parâmetro numérico inválido: " + e.getMessage()))
        );

        // 400 — argumento ilegal passado a um método da LN
        app.exception(IllegalArgumentException.class, (e, ctx) ->
            ctx.status(400).json(new ErroResponse(400, "Bad Request", e.getMessage()))
        );

        // 401 — sem sessão ou token inválido
        app.exception(UnauthorizedResponse.class, (e, ctx) ->
            ctx.status(401).json(new ErroResponse(401, "Unauthorized", e.getMessage()))
        );

        // 403 — sessão válida mas sem permissão para a operação
        app.exception(ForbiddenResponse.class, (e, ctx) ->
            ctx.status(403).json(new ErroResponse(403, "Forbidden", e.getMessage()))
        );

        // 500 — erro inesperado (bug, falha de BD não antecipada, etc.)
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace(); // visível nos logs do servidor para depuração
            ctx.status(500).json(new ErroResponse(500, "Internal Server Error", "Erro interno do servidor"));
        });
    }

    public record ErroResponse(int status, String erro, String mensagem) {}
}
