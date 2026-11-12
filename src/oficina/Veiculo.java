package oficina;

public class Veiculo {

    private String placa;
    private String modelo;
    private int ano;
    private Cliente proprietario;

    public Veiculo(String placa, String modelo, int ano, Cliente proprietario) {
        this.placa = placa;
        this.modelo = modelo;
        this.ano = ano;
        this.proprietario = proprietario;
    }

    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAno() {
        return ano;
    }

    public Cliente getProprietario() {
        return proprietario;
    }
}
