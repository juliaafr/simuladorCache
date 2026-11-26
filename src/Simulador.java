import java.util.ArrayList;
import java.util.List;

public class Simulador {

    private final Configuracao config;
    private final BitSeparator separador;

    public Simulador(Configuracao config) {
        this.config = config;
        this.separador = new BitSeparator(config);
    }

    public ResultadoSimulacao simular(List<InputParse.AcessoLido> traco) {
        //a cache comeca vazia
        Conjunto[] cache = new Conjunto[config.conjuntos];
        for (int i = 0; i < cache.length; i++) {
            cache[i] = new Conjunto(config.vias);
        }
        ClassificadorFaltas classificador = new ClassificadorFaltas(config);
        Statistics estatisticas = new Statistics();
        List<ResultadoAcesso> resultados = new ArrayList<>();

        int numero = 0;
        for (InputParse.AcessoLido acesso : traco) {
            numero++;
            int endereco = acesso.endereco;
            int rotulo = separador.rotulo(endereco);
            int indice = separador.indice(endereco);
            int offset = separador.offset(endereco);
            boolean escrita = acesso.operacao == 'W';
            boolean sujar = escrita && config.isWriteBack();

            Conjunto conjunto = cache[indice];
            boolean acerto = conjunto.contem(rotulo);
            BlocoExpulso expulso = null;
            if (acerto) {
                conjunto.usar(rotulo, sujar);
            } else {
                //write-allocate: escrita que falta tambem traz o bloco
                expulso = conjunto.inserir(rotulo, sujar);
            }

            String tipoFalta = classificador.registrar(separador.numeroBloco(endereco), acerto);

            boolean expulsou = expulso != null;
            int enderecoExpulso = expulsou ? separador.enderecoDoBloco(expulso.rotulo, indice) : 0;
            boolean writeback = expulsou && expulso.sujo;

            ResultadoAcesso r = new ResultadoAcesso(numero, acesso.operacao, endereco, rotulo, indice, offset,
                    acerto, tipoFalta, expulsou, enderecoExpulso, writeback);
            resultados.add(r);
            estatisticas.registrarAcesso(r, config);
            if (escrita && !config.isWriteBack()) {
                estatisticas.registrarEscritaWT();
            }
        }

        //descarga final: blocos que terminaram com D=1
        if (config.isWriteBack()) {
            int sujos = 0;
            for (Conjunto c : cache) {
                sujos += c.blocosSujos();
            }
            estatisticas.registrarDescargaFinal(sujos, config);
        }

        return new ResultadoSimulacao(config, resultados, estatisticas);
    }
}
