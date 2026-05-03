package app.ecoRideCD.sOrdensServico;

import app.ecoRideLN.sOrdensServico.OrdemServico;

// Stub mínimo para o projeto compilar enquanto o subsistema SOrdensServico
// não está implementado. Substituir por um Map<Integer, OrdemServico> completo.
public class OrdemServicoDAO {
    private static OrdemServicoDAO instance;

    private OrdemServicoDAO() {}

    public static OrdemServicoDAO getInstance() {
        if (instance == null) instance = new OrdemServicoDAO();
        return instance;
    }

    public OrdemServico getById(int id) {
        return null;
    }
}
