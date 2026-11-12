package oficina;

import java.util.ArrayList;
import java.util.List;

import oficina.observer.ObservadorOrdem;
import oficina.state.Aberta;
import oficina.state.EstadoOrdem;


public class OrdemServico {

    private int numero;
    private Veiculo veiculo;
    private List<Servico> servicos = new ArrayList<>();
    private EstadoOrdem estado;
    private List<ObservadorOrdem> observadores = new ArrayList<>();

    public OrdemServico(int numero, Veiculo veiculo) {
        this.numero = numero;
        this.veiculo = veiculo;
        this.estado = new Aberta(); 
    }

    public void adicionarServico(Servico servico) throws OficinaException {
        if (!estado.aceitaServicos()) {
            throw new OficinaException("A ordem " + numero + " está \"" + estado.getDescricao()
                    + "\" e não aceita novos serviços.");
        }
        servicos.add(servico);
    }

    public double calcularTotal() {
        double total = 0;
        for (Servico servico : servicos) {
            total += servico.calcularValor();
        }
        return total;
    }



    public void iniciar() throws OficinaException {
        mudarEstado(estado.iniciar(this));
    }

    public void concluir() throws OficinaException {
        mudarEstado(estado.concluir(this));
    }

    public void fechar() throws OficinaException {
        mudarEstado(estado.fechar(this));
    }

    private void mudarEstado(EstadoOrdem novoEstado) {
        this.estado = novoEstado;
        notificarObservadores(); 
    }



    public void adicionarObservador(ObservadorOrdem observador) {
        observadores.add(observador);
    }

    public void removerObservador(ObservadorOrdem observador) {
        observadores.remove(observador);
    }

    private void notificarObservadores() {
        for (ObservadorOrdem observador : observadores) {
            observador.atualizar(this);
        }
    }



    public EstadoOrdem getEstado() {
        return estado;
    }

    public int getNumero() {
        return numero;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public List<Servico> getServicos() {
        return servicos;
    }
}
