import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

//um conjunto da cache com substituicao LRU
//linkedhashmap em ordem de acesso: o primeiro item e o usado ha mais tempo
public class Conjunto {

    private final int vias;
    private final LinkedHashMap<Integer, Boolean> blocos = new LinkedHashMap<>(16, 0.75f, true); //rotulo -> bit D

    public Conjunto(int vias) {
        this.vias = vias;
    }

    public boolean contem(int rotulo) {
        return blocos.containsKey(rotulo);
    }

    //acerto: conta como uso e, se for escrita no write-back, liga o bit D
    public void usar(int rotulo, boolean sujar) {
        boolean sujo = blocos.get(rotulo);
        blocos.put(rotulo, sujo || sujar);
    }

    //falta: traz o bloco; se o conjunto estiver cheio, expulsa o LRU e devolve ele (senao null)
    public BlocoExpulso inserir(int rotulo, boolean sujo) {
        BlocoExpulso expulso = null;
        if (blocos.size() >= vias) {
            Iterator<Map.Entry<Integer, Boolean>> it = blocos.entrySet().iterator();
            Map.Entry<Integer, Boolean> lru = it.next();
            expulso = new BlocoExpulso(lru.getKey(), lru.getValue());
            it.remove();
        }
        blocos.put(rotulo, sujo);
        return expulso;
    }

    public int blocosSujos() {
        int total = 0;
        for (boolean sujo : blocos.values()) {
            if (sujo) total++;
        }
        return total;
    }
}
