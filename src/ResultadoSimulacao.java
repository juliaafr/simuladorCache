import java.util.List;

public class ResultadoSimulacao {

    public final Configuracao config;
    public final List<ResultadoAcesso> acessos;
    public final Statistics estatisticas;

    public ResultadoSimulacao(Configuracao config, List<ResultadoAcesso> acessos, Statistics estatisticas) {
        this.config = config;
        this.acessos = acessos;
        this.estatisticas = estatisticas;
    }
}
