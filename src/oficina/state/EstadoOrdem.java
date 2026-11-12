package oficina.state;

import oficina.OficinaException;
import oficina.OrdemServico;


public abstract class EstadoOrdem {

    public abstract String getDescricao();

   
    public boolean aceitaServicos() {
        return false;
    }

    public EstadoOrdem iniciar(OrdemServico ordem) throws OficinaException {
        throw acaoProibida(ordem, "iniciar");
    }

    public EstadoOrdem concluir(OrdemServico ordem) throws OficinaException {
        throw acaoProibida(ordem, "concluir");
    }

    public EstadoOrdem fechar(OrdemServico ordem) throws OficinaException {
        throw acaoProibida(ordem, "fechar");
    }

    protected OficinaException acaoProibida(OrdemServico ordem, String acao) {
        return new OficinaException("Não é possível " + acao + " a ordem " + ordem.getNumero()
                + ": ela está \"" + getDescricao() + "\"");
    }
}
