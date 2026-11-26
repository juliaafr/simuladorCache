public class Configuracao {

    public static final int BITS_ENDERECO = 32;
    public static final int TAMANHO_PALAVRA = 4;

    public final int capacidade; //bytes de dados
    public final int bloco; //bytes por bloco
    public final int vias;
    public final String escrita; //"WB" ou "WT"
    public final int conjuntos;
    public final int bitsOffset;
    public final int bitsIndice;
    public final int bitsRotulo;

    //recusa combinacoes impossiveis
    public Configuracao(int capacidade, int bloco, int vias, String escrita) {
        escrita = escrita.toUpperCase();
        if (!potenciaDe2(capacidade)) throw new IllegalArgumentException("capacidade=" + capacidade + " nao e potencia de 2");
        if (!potenciaDe2(bloco)) throw new IllegalArgumentException("bloco=" + bloco + " nao e potencia de 2");
        if (!potenciaDe2(vias)) throw new IllegalArgumentException("vias=" + vias + " nao e potencia de 2");
        if (bloco < TAMANHO_PALAVRA) throw new IllegalArgumentException("bloco=" + bloco + " B e menor que uma palavra (4 B)");
        if (bloco > capacidade) throw new IllegalArgumentException("bloco=" + bloco + " B e maior que a capacidade (" + capacidade + " B)");
        int totalBlocos = capacidade / bloco;
        if (vias > totalBlocos) throw new IllegalArgumentException("vias=" + vias + " e maior que o numero de blocos (" + totalBlocos + ")");
        if (!escrita.equals("WB") && !escrita.equals("WT")) throw new IllegalArgumentException("escrita deve ser WB ou WT");

        this.capacidade = capacidade;
        this.bloco = bloco;
        this.vias = vias;
        this.escrita = escrita;
        this.conjuntos = totalBlocos / vias;
        this.bitsOffset = log2(bloco);
        this.bitsIndice = log2(conjuntos);
        this.bitsRotulo = BITS_ENDERECO - bitsOffset - bitsIndice;
    }

    public boolean isWriteBack() {
        return escrita.equals("WB");
    }

    public int totalBlocos() {
        return capacidade / bloco;
    }

    private static boolean potenciaDe2(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    private static int log2(int n) {
        return 31 - Integer.numberOfLeadingZeros(n);
    }
}
