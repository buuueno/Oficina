package oficina;

import java.util.Arrays;
import java.util.List;

import oficina.decorator.Lavagem;
import oficina.decorator.LevaETraz;
import oficina.observer.Financeiro;
import oficina.observer.NotificacaoWhatsApp;
import oficina.observer.PainelRecepcao;
import oficina.strategy.FormaPagamento;
import oficina.strategy.PagamentoCredito;
import oficina.strategy.PagamentoDebito;
import oficina.strategy.PagamentoPix;

public class Main {

    public static void main(String[] args) {

        OrdemRepository repositorio = new RepositorioEmMemoria();
        OficinaService oficina = new OficinaService(repositorio);

        oficina.registrarObservador(new NotificacaoWhatsApp());
        oficina.registrarObservador(new PainelRecepcao());
        oficina.registrarObservador(new Financeiro());

        Cliente cliente = new Cliente("Marlon", "41 99999-0000");
        Veiculo veiculo = new Veiculo("ABC1D23", "Celta", 2012, cliente);

        Cliente cliente2 = new Cliente("Anna", "41 98888-0000");
        Veiculo veiculo2 = new Veiculo("XYZ9A88", "Gol", 2018, cliente2);

        try {

            oficina.abrirOrdem(1, veiculo);
            oficina.adicionarServico(1, "troca de oleo");
            oficina.adicionarServico(1, "alinhamento");

            oficina.abrirOrdem(2, veiculo2);

           
            Servico revisaoDaAnna = ServicoFactory.criar("revisao");
            revisaoDaAnna.adicionarPeca(new Peca("Disco de freio", 210, 2));

            Servico revisaoComAdicionais = new LevaETraz(new Lavagem(revisaoDaAnna));
            oficina.adicionarServico(2, revisaoComAdicionais);

            titulo("ORÇAMENTOS");
            imprimirOrcamento(oficina.buscarOrdem(1));
            imprimirOrcamento(oficina.buscarOrdem(2));

            titulo("PROTOTYPE - o modelo da fábrica não muda");
            Servico novaRevisao = ServicoFactory.criar("revisao");
            System.out.println("Revisão da Anna: " + revisaoDaAnna.getPecas().size() + " peças");
            System.out.println("Nova revisão saindo da fábrica: " + novaRevisao.getPecas().size()
                    + " peças (o disco de freio ficou só na cópia da Anna)");

            titulo("STATE + OBSERVER - a ordem muda de etapa e todos são avisados");
            System.out.println("Ordem 1 está: " + oficina.buscarOrdem(1).getEstado().getDescricao());
            System.out.println("> Mecânico começou a ordem 1");
            oficina.iniciarServico(1);
            System.out.println("> Mecânico terminou a ordem 1");
            oficina.concluirServico(1);

            titulo("STRATEGY - forma de pagamento");
            OrdemServico ordem1 = oficina.buscarOrdem(1);
            List<FormaPagamento> formas = Arrays.asList(
                    new PagamentoPix(),
                    new PagamentoDebito(),
                    new PagamentoCredito(3),
                    new PagamentoCredito(10));

            System.out.println("Simulação da ordem 1 (" + reais(ordem1.calcularTotal()) + "):");
            for (FormaPagamento forma : formas) {
                System.out.println("  " + forma.getNome() + ": "
                        + reais(forma.calcularValorFinal(ordem1.calcularTotal())));
            }

            System.out.println("> Marlon escolheu Pix");
            double valorPago = oficina.fecharOrdem(1, new PagamentoPix());
            System.out.println("Ordem 1 fechada. Valor pago: " + reais(valorPago));
            System.out.println("Ordens no sistema: " + oficina.listarOrdens().size());

        } catch (OficinaException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        titulo("TRATAMENTO DE ERROS");

        try {
            oficina.adicionarServico(1, "alinhamento"); // State: ordem fechada não aceita serviço
        } catch (OficinaException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        try {
            oficina.adicionarServico(2, "martelinho de ouro"); // Factory: tipo não cadastrado
        } catch (OficinaException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        try {
            oficina.fecharOrdem(99, new PagamentoDebito()); // ordem inexistente
        } catch (OficinaException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        try {
            oficina.fecharOrdem(2, new PagamentoPix()); // State: ordem 2 ainda está aberta
        } catch (OficinaException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        try {
            oficina.iniciarServico(1);
        } catch (OficinaException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        try {
            new PagamentoCredito(15); // Strategy: parcelamento inválido
        } catch (OficinaException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void imprimirOrcamento(OrdemServico ordem) {

        System.out.println("Ordem " + ordem.getNumero()
                + " - " + ordem.getVeiculo().getModelo()
                + " " + ordem.getVeiculo().getAno()
                + " (" + ordem.getVeiculo().getProprietario().getNome() + ")");

        for (Servico servico : ordem.getServicos()) {
            System.out.println("  " + servico.getDescricao()
                    + " | mão de obra: " + reais(servico.calcularMaoDeObra())
                    + " | peças: " + reais(servico.calcularPecas())
                    + " | subtotal: " + reais(servico.calcularValor()));

            for (Peca peca : servico.getPecas()) {
                System.out.println("      - " + peca.getQuantidade() + "x " + peca.getNome()
                        + " (" + reais(peca.calcularSubtotal()) + ")");
            }
        }

        System.out.println("  TOTAL: " + reais(ordem.calcularTotal()));
        System.out.println();
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("========== " + texto + " ==========");
    }

    private static String reais(double valor) {
        return String.format("R$ %.2f", valor);
    }
}
