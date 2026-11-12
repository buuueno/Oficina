package oficina;

public class RevisaoCompleta extends Servico {

    private double taxaDiagnostico = 80;

    public RevisaoCompleta() {
        super("Revisão completa", 3);
    }

    @Override
    public double calcularMaoDeObra() {
        return (horas * 130) + taxaDiagnostico;
    }
}
