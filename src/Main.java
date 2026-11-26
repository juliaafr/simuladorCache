import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/*
 * simulador de cache - UNIVALI, organizacao de computadores
 * autores: Júlia Alves França e João Guilherme
 *
 * uso: java Main <traco> <capacidade> <bloco> <vias> [WB|WT] [resumo] [arquivo_saida]
 * sem argumentos: roda os 4 tracos de calibracao e confere com as respostas
 * o que falta: modificar o codigo para usar a cache modificada e fazer os experimentos
 * pro relatorio: o codigo ta pronto, falta a parte escrita
 *  - antes de olhar os resultados, precisa preencher a folha de previsoes (pag. 8 do pdf)
 *  - roda o MainExperimentos: as tabelas saem em saida/tabelas.md e a saida
 *    de cada acesso em saida/<experimento>/, e ali que voce acha os numeros
 *    dos acessos pra citar nas justificativas
 *  - pra cada experimento: previsao, tabela, respostas das perguntas e onde a previsao errou
 *  - explicar o E3 modificado e o desafio (testes/proprios/, o comentario no topo
 *    de cada arquivo diz a ideia)
 */
public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("sem argumentos: rodando a calibracao (testes/calibracao)");
            System.out.println("uso: java Main <traco> <capacidade> <bloco> <vias> [WB|WT] [resumo] [arquivo_saida]");
            System.out.println();
            boolean ok = Calibracao.executar("testes/calibracao", true);
            System.out.println(ok ? "calibracao: os 4 tracos reproduzidos exatamente  [OK]" : "calibracao: ha diferencas  [FALHOU]");
            return;
        }
        if (args.length < 4) {
            System.err.println("uso: java Main <traco> <capacidade> <bloco> <vias> [WB|WT] [resumo] [arquivo_saida]");
            System.exit(2);
        }

        String caminho = args[0];
        String escrita = "WB";
        boolean soResumo = false;
        String arquivoSaida = null;
        for (int i = 4; i < args.length; i++) {
            String a = args[i];
            if (a.equalsIgnoreCase("WB") || a.equalsIgnoreCase("WT")) {
                escrita = a;
            } else if (a.equalsIgnoreCase("resumo")) {
                soResumo = true;
            } else {
                arquivoSaida = a;
            }
        }

        try {
            Configuracao config = new Configuracao(Integer.parseInt(args[1]), Integer.parseInt(args[2]),
                    Integer.parseInt(args[3]), escrita);
            List<InputParse.AcessoLido> traco = new InputParse().lerArquivo(caminho);
            ResultadoSimulacao resultado = new Simulador(config).simular(traco);
            String texto = soResumo ? Output.somenteResumo(resultado) : Output.saidaCompleta(resultado);

            if (arquivoSaida != null) {
                Files.writeString(Path.of(arquivoSaida), texto, StandardCharsets.UTF_8);
                System.out.println("saida gravada em " + arquivoSaida);
            } else {
                System.out.print(texto);
            }
        } catch (NumberFormatException erro) {
            System.err.println("capacidade, bloco e vias devem ser numeros inteiros");
            System.exit(2);
        } catch (IllegalArgumentException erro) {
            System.err.println("configuracao impossivel: " + erro.getMessage());
            System.exit(2);
        } catch (IOException erro) {
            System.err.println("nao foi possivel ler o traco: " + erro.getMessage());
            System.exit(2);
        }
    }
}
