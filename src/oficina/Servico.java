package oficina;

import java.util.ArrayList;
import java.util.List;


public abstract class Servico implements Cloneable {

    protected String descricao;
    protected double horas;
    protected List<Peca> pecas = new ArrayList<>();

    public Servico(String descricao, double horas) {
        this.descricao = descricao;
        this.horas = horas;
    }

    public abstract double calcularMaoDeObra();

    public void adicionarPeca(Peca peca) {
        pecas.add(peca);
    }

    public double calcularPecas() {
        double total = 0;
        for (Peca peca : pecas) {
            total += peca.calcularSubtotal();
        }
        return total;
    }

    public double calcularValor() {
        return calcularMaoDeObra() + calcularPecas();
    }

    public String getDescricao() {
        return descricao;
    }

    public double getHoras() {
        return horas;
    }

    public List<Peca> getPecas() {
        return pecas;
    }

   
    public Servico clonar() {
        try {
          
            Servico copia = (Servico) clone();

            
            copia.pecas = new ArrayList<>(this.pecas);
            return copia;

        } catch (CloneNotSupportedException e) {
     
            throw new RuntimeException(e);
        }
    }
}
