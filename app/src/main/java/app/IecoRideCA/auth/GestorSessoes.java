package app.IecoRideCA.auth;

import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;

import app.ecoRideLN.sAutenticacao.Cargo;
import io.javalin.http.Context;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GestorSessoes {

    private final Map<String, SessaoUtilizador> sessoes = new ConcurrentHashMap<>();

    public String criarSessao(SessaoUtilizador sessao) {
        String token = UUID.randomUUID().toString();
        sessoes.put(token, sessao);
        return token;
    }

    public SessaoUtilizador validar(String token) {
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

    public static SessaoUtilizador sessao(Context ctx) {
        SessaoUtilizador s = ctx.attribute("sessao");
        if (s == null) throw new ForbiddenResponse("Sem sessão ativa");
        return s;
    }

    public static SessaoUtilizador verifica_cargo(Context ctx, Cargo... cargos) {
        SessaoUtilizador s = sessao(ctx);
        if (Arrays.stream(cargos).noneMatch(c -> c == s.getCargo()))
            throw new ForbiddenResponse("Sem permissão para esta operação");
        return s;
    }
}
