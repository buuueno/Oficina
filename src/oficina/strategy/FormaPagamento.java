package oficina.strategy;

public interface FormaPagamento {

    String getNome();

    double calcularValorFinal(double valor);
}
