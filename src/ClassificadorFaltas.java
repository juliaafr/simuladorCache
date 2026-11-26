import java.util.HashSet;
import java.util.Set;

//compulsoria: primeiro acesso ao bloco em todo o traco
//capacidade: tambem faltaria numa cache totalmente associativa LRU de mesma capacidade e bloco
//conflito: essa cache totalmente associativa acertaria
public class ClassificadorFaltas {

    private final Conjunto totalmenteAssociativa; //recebe todos os acessos, nao aparece na saida
    private final Set<Integer> blocosVistos = new HashSet<>();

    public ClassificadorFaltas(Configuracao config) {
        this.totalmenteAssociativa = new Conjunto(config.totalBlocos());
    }

    //devolve null se a cache configurada acertou, senao o tipo da falta
    public String registrar(int numeroBloco, boolean acertouNaCache) {
        boolean acertariaTotalmenteAssociativa = totalmenteAssociativa.contem(numeroBloco);
        if (acertariaTotalmenteAssociativa) {
            totalmenteAssociativa.usar(numeroBloco, false);
        } else {
            totalmenteAssociativa.inserir(numeroBloco, false);
        }

        String tipo = null;
        if (!acertouNaCache) {
            if (!blocosVistos.contains(numeroBloco)) {
                tipo = "compulsoria";
            } else if (acertariaTotalmenteAssociativa) {
                tipo = "conflito";
            } else {
                tipo = "capacidade";
            }
        }
        blocosVistos.add(numeroBloco);
        return tipo;
    }
}
