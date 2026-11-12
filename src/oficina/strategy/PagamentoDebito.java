package oficina.strategy;


public class PagamentoDebito implements FormaPagamento {

    @Override
    public String getNome() {
        return "Cartão de débito";
    }

    @Override
    public double calcularValorFinal(double valor) {
        return valor;
    }
}
