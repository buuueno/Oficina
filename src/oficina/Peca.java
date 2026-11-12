package oficina;

public class Peca {

    private String nome;
    private double valorUnitario;
    private int quantidade;

    public Peca(String nome, double valorUnitario, int quantidade) {
        this.nome = nome;
        this.valorUnitario = valorUnitario;
        this.quantidade = quantidade;
    }

    public double calcularSubtotal() {
        return valorUnitario * quantidade;
    }

    public String getNome() {
        return nome;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }
}
