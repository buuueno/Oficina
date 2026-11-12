package oficina.state;

import oficina.OficinaException;
import oficina.OrdemServico;


public class Aberta extends EstadoOrdem {

    @Override
    public String getDescricao() {
        return "Aberta";
    }

    @Override
    public boolean aceitaServicos() {
        return true;
    }

    @Override
    public EstadoOrdem iniciar(OrdemServico ordem) throws OficinaException {
        if (ordem.getServicos().isEmpty()) {
            throw new OficinaException("A ordem " + ordem.getNumero() + " não tem serviços para iniciar");
        }
        return new EmAndamento();
    }
}
