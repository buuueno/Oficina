package oficina.state;

import oficina.OrdemServico;


public class EmAndamento extends EstadoOrdem {

    @Override
    public String getDescricao() {
        return "Em andamento";
    }

    @Override
    public boolean aceitaServicos() {
        return true;
    }

    @Override
    public EstadoOrdem concluir(OrdemServico ordem) {
        return new Pronta();
    }
}
