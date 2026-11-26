//divide o endereco em rotulo | indice | offset
public class BitSeparator {

    private final Configuracao config;

    public BitSeparator(Configuracao config) {
        this.config = config;
    }

    public int offset(int endereco) {
        return endereco & (config.bloco - 1);
    }

    public int indice(int endereco) {
        return (endereco >>> config.bitsOffset) & (config.conjuntos - 1);
    }

    public int rotulo(int endereco) {
        return endereco >>> (config.bitsOffset + config.bitsIndice);
    }

    //endereco sem o offset: identifica o bloco de memoria
    public int numeroBloco(int endereco) {
        return endereco >>> config.bitsOffset;
    }

    //endereco inicial do bloco que esta na cache com esse rotulo e indice
    public int enderecoDoBloco(int rotulo, int indice) {
        return ((rotulo << config.bitsIndice) | indice) << config.bitsOffset;
    }
}
