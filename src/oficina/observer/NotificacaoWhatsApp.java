package oficina.observer;

import oficina.Cliente;
import oficina.OrdemServico;


public class NotificacaoWhatsApp implements ObservadorOrdem {

    @Override
    public void atualizar(OrdemServico ordem) {
        Cliente cliente = ordem.getVeiculo().getProprietario();

        System.out.println("  [WhatsApp " + cliente.getTelefone() + "] Olá, " + cliente.getNome()
                + "! Seu " + ordem.getVeiculo().getModelo() + " está: " + ordem.getEstado().getDescricao());
    }
}
