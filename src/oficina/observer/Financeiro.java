package oficina.observer;

import oficina.OrdemServico;
import oficina.state.Pronta;


public class Financeiro implements ObservadorOrdem {

    @Override
    public void atualizar(OrdemServico ordem) {
        if (ordem.getEstado() instanceof Pronta) {
            System.out.println("  [Financeiro] Ordem " + ordem.getNumero() + " pronta. Cobrar "
                    + String.format("R$ %.2f", ordem.calcularTotal()) + " na retirada.");
        }
    }
}
