package oficina.decorator;

import oficina.Servico;

public class Lavagem extends ServicoAdicional {

    private static final double VALOR = 50;

    public Lavagem(Servico servico) {
        super(servico, "Lavagem");
    }

    @Override
    public double calcularMaoDeObra() {
        return super.calcularMaoDeObra() + VALOR;
    }
}
