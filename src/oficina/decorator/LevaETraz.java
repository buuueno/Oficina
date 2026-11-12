package oficina.decorator;

import oficina.Servico;


public class LevaETraz extends ServicoAdicional {

    private static final double VALOR = 40;

    public LevaETraz(Servico servico) {
        super(servico, "Leva e traz");
    }

    @Override
    public double calcularMaoDeObra() {
        return super.calcularMaoDeObra() + VALOR;
    }
}
