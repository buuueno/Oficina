package oficina;

import java.util.HashMap;
import java.util.Map;


public class ServicoFactory {

    private static final Map<String, Servico> modelos = criarModelos();

    private static Map<String, Servico> criarModelos() {
        Map<String, Servico> catalogo = new HashMap<>();

        Servico trocaDeOleo = new TrocaDeOleo();
        trocaDeOleo.adicionarPeca(new Peca("Óleo 5W30", 45, 4));
        trocaDeOleo.adicionarPeca(new Peca("Filtro de óleo", 38, 1));
        catalogo.put("troca de oleo", trocaDeOleo);

        catalogo.put("alinhamento", new Alinhamento());

        Servico revisao = new RevisaoCompleta();
        revisao.adicionarPeca(new Peca("Filtro de ar", 60, 1));
        revisao.adicionarPeca(new Peca("Pastilha de freio", 180, 2));
        catalogo.put("revisao", revisao);

        return catalogo;
    }

    public static Servico criar(String tipo) throws OficinaException {
        Servico modelo = modelos.get(tipo.toLowerCase());

        if (modelo == null) {
            throw new OficinaException("Tipo de serviço não cadastrado: " + tipo);
        }
        return modelo.clonar(); 
    }
}
