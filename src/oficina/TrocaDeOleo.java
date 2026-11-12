package oficina;

public class TrocaDeOleo extends Servico {

    public TrocaDeOleo() {
        super("Troca de óleo", 0.5);
    }

    @Override
    public double calcularMaoDeObra() {
        return horas * 90;
    }
}
