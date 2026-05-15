package app.IEcoRideLN.auth;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import app.ecoRideLN.sAutenticacao.Cargo;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;

public class GestorSessoes {

    private final Map<String, SessaoUtilizador> sessoes = new ConcurrentHashMap<>();

    public String criarSessao(SessaoUtilizador sessao) {
        String token = UUID.randomUUID().toString();
        sessoes.put(token, sessao);
        return token;
    }

    public SessaoUtilizador validar(String token) throws UnauthorizedResponse {
        SessaoUtilizador s = sessoes.get(token);
        if (s == null || s.expirou()) {
            sessoes.remove(token);
            throw new UnauthorizedResponse("Sessão inválida ou expirada");
        }
        return s;
    }

    public void terminarSessao(String token) {
        sessoes.remove(token);
    }

    public static SessaoUtilizador sessao(Context ctx) throws UnauthorizedResponse {
        SessaoUtilizador s = ctx.attribute("sessao");
        if (s == null) throw new UnauthorizedResponse("Sessão não encontrada ou expirada");
        return s;
    }

    public static SessaoUtilizador verifica_cargo(Context ctx, Cargo... cargos) throws ForbiddenResponse, UnauthorizedResponse {
        SessaoUtilizador s = sessao(ctx);
        if (Arrays.stream(cargos).noneMatch(c -> c == s.getCargo()))
            throw new ForbiddenResponse("Sem permissão para esta operação");
        return s;
    }
}
