//o que aconteceu em um acesso (uma linha da saida)
public class ResultadoAcesso {

    public final int numero;
    public final char operacao;
    public final int endereco;
    public final int rotulo;
    public final int indice;
    public final int offset;
    public final boolean acerto;
    public final String tipoFalta; //null se acertou
    public final boolean expulsou;
    public final int enderecoExpulso;
    public final boolean writeback;

    public ResultadoAcesso(int numero, char operacao, int endereco, int rotulo, int indice, int offset,
                           boolean acerto, String tipoFalta, boolean expulsou, int enderecoExpulso, boolean writeback) {
        this.numero = numero;
        this.operacao = operacao;
        this.endereco = endereco;
        this.rotulo = rotulo;
        this.indice = indice;
        this.offset = offset;
        this.acerto = acerto;
        this.tipoFalta = tipoFalta;
        this.expulsou = expulsou;
        this.enderecoExpulso = enderecoExpulso;
        this.writeback = writeback;
    }
}
