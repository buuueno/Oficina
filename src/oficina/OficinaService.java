package oficina;

import java.util.ArrayList;
import java.util.List;

import oficina.observer.ObservadorOrdem;
import oficina.strategy.FormaPagamento;

public class OficinaService {

   
    private OrdemRepository repositorio;

    private List<ObservadorOrdem> observadores = new ArrayList<>();

  
    public OficinaService(OrdemRepository repositorio) {
        this.repositorio = repositorio;
    }

    
    public void registrarObservador(ObservadorOrdem observador) {
        observadores.add(observador);
    }

    public OrdemServico abrirOrdem(int numero, Veiculo veiculo) throws OficinaException {
        if (repositorio.buscarPorNumero(numero) != null) {
            throw new OficinaException("Já existe uma ordem com o número " + numero);
        }
        OrdemServico ordem = new OrdemServico(numero, veiculo);
        for (ObservadorOrdem observador : observadores) {
            ordem.adicionarObservador(observador);
        }

        repositorio.salvar(ordem);
        return ordem;
    }

    public OrdemServico buscarOrdem(int numero) throws OficinaException {
        OrdemServico ordem = repositorio.buscarPorNumero(numero);
        if (ordem == null) {
            throw new OficinaException("Ordem de serviço não encontrada: " + numero);
        }
        return ordem;
    }

 
    public void adicionarServico(int numero, String tipo) throws OficinaException {
        OrdemServico ordem = buscarOrdem(numero);
        ordem.adicionarServico(ServicoFactory.criar(tipo));
    }
    
    public void adicionarServico(int numero, Servico servico) throws OficinaException {
        OrdemServico ordem = buscarOrdem(numero);
        ordem.adicionarServico(servico);
    }

   
    public void iniciarServico(int numero) throws OficinaException {
        buscarOrdem(numero).iniciar();
    }

    public void concluirServico(int numero) throws OficinaException {
        buscarOrdem(numero).concluir();
    }

   
    public double fecharOrdem(int numero, FormaPagamento formaPagamento) throws OficinaException {
        OrdemServico ordem = buscarOrdem(numero);
        ordem.fechar();
        return formaPagamento.calcularValorFinal(ordem.calcularTotal());
    }

    public List<OrdemServico> listarOrdens() {
        return repositorio.listarTodas();
    }
}
