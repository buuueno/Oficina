package oficina;

import java.util.ArrayList;
import java.util.List;

public class RepositorioEmMemoria implements OrdemRepository {

    private List<OrdemServico> ordens = new ArrayList<>();

    @Override
    public void salvar(OrdemServico ordem) {
        ordens.add(ordem);
    }

    @Override
    public OrdemServico buscarPorNumero(int numero) {
        for (OrdemServico ordem : ordens) {
            if (ordem.getNumero() == numero) {
                return ordem;
            }
        }
        return null;
    }

    @Override
    public List<OrdemServico> listarTodas() {
        return ordens;
    }
}
