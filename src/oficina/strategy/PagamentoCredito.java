package oficina.strategy;

import oficina.OficinaException;


public class PagamentoCredito implements FormaPagamento {

    private static final int MAX_PARCELAS = 12;
    private static final int PARCELAS_SEM_JUROS = 3;
    private static final double ACRESCIMO = 0.05;

    private int parcelas;

    public PagamentoCredito(int parcelas) throws OficinaException {
        if (parcelas < 1 || parcelas > MAX_PARCELAS) {
            throw new OficinaException("Parcelamento inválido: " + parcelas
                    + "x (permitido de 1x a " + MAX_PARCELAS + "x)");
        }
        this.parcelas = parcelas;
    }

    @Override
    public String getNome() {
        if (parcelas <= PARCELAS_SEM_JUROS) {
            return "Crédito em " + parcelas + "x sem juros";
        }
        return "Crédito em " + parcelas + "x (+5%)";
    }

    @Override
    public double calcularValorFinal(double valor) {
        if (parcelas <= PARCELAS_SEM_JUROS) {
            return valor;
        }
        return valor * (1 + ACRESCIMO);
    }
}
