package oficina.state;

import oficina.OrdemServico;


public class Pronta extends EstadoOrdem {

    @Override
    public String getDescricao() {
        return "Pronta para retirada";
    }

    @Override
    public EstadoOrdem fechar(OrdemServico ordem) {
        return new Fechada();
    }
}
