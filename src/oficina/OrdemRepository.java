package oficina;

import java.util.List;

public interface OrdemRepository {

    void salvar(OrdemServico ordem);

    OrdemServico buscarPorNumero(int numero);

    List<OrdemServico> listarTodas();

}
