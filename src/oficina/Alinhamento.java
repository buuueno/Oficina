package oficina;

public class Alinhamento extends Servico {

    public Alinhamento() {
        super("Alinhamento e balanceamento", 1.5);
    }

    @Override
    public double calcularMaoDeObra() {
        return horas * 110;
    }
}
