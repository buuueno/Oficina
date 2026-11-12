package oficina.decorator;

import java.util.List;

import oficina.Peca;
import oficina.Servico;


public abstract class ServicoAdicional extends Servico {

    protected Servico servico; 

    public ServicoAdicional(Servico servico, String nomeAdicional) {
        super(servico.getDescricao() + " + " + nomeAdicional, servico.getHoras());
        this.servico = servico;
    }

    @Override
    public double calcularMaoDeObra() {
        return servico.calcularMaoDeObra();
    }

    @Override
    public double calcularPecas() {
        return servico.calcularPecas();
    }

    @Override
    public List<Peca> getPecas() {
        return servico.getPecas();
    }

    @Override
    public void adicionarPeca(Peca peca) {
        servico.adicionarPeca(peca);
    }
}
