package oficina.observer;

import oficina.OrdemServico;


public class PainelRecepcao implements ObservadorOrdem {

    @Override
    public void atualizar(OrdemServico ordem) {
        System.out.println("  [Painel da recepção] Ordem " + ordem.getNumero() + " | "
                + ordem.getVeiculo().getPlaca() + " | " + ordem.getEstado().getDescricao());
    }
}
