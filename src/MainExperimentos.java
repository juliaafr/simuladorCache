import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
 * roda todas as configuracoes dos experimentos E1 a E6, do E3 modificado e do desafio
 * gera saida/tabelas.md e a saida por acesso de cada execucao em saida/<experimento>/
 */
public class MainExperimentos {

    private static final String EXPERIMENTOS = "testes/experimentos/";
    private static final String PROPRIOS = "testes/proprios/";
    private static final StringBuilder tabelas = new StringBuilder();

    public static void main(String[] args) {
        try {
            tabelas.append("# Tabelas medidas pelo simulador\n\n")
                    .append("A saida por acesso de cada linha esta em `saida/<experimento>/`.\n");

            tabelas.append("\n## E1 - Tamanho do bloco numa varredura (256 B, 1 via, WB)\n");
            tabela("E1_varredura", "Bloco (B)", variarBloco("E1", EXPERIMENTOS + "E1_varredura.txt"), true, false);

            tabelas.append("\n## E2 - Tamanho do bloco com dados espalhados (256 B, 1 via, WB)\n");
            tabela("E2_registros", "Bloco (B)", variarBloco("E2", EXPERIMENTOS + "E2_registros.txt"), true, false);

            int[] vias = {1, 2, 4, 16};
            tabelas.append("\n## E3 - Associatividade contra conflito (256 B, bloco 16 B, WB)\n");
            tabela("E3_dois_vetores", "Vias", variarVias("E3", EXPERIMENTOS + "E3_dois_vetores.txt", vias), false, false);
            tabela("E3_dois_vetores_modificado (segundo vetor em 0x10000080)", "Vias",
                    variarVias("E3", PROPRIOS + "E3_dois_vetores_modificado.txt", new int[]{1}), false, false);

            tabelas.append("\n## E4 - Associatividade numa varredura (256 B, bloco 16 B, WB)\n");
            tabela("E1_varredura", "Vias", variarVias("E4", EXPERIMENTOS + "E1_varredura.txt", vias), false, false);

            tabelas.append("\n## E5 - Laco maior e menor que a cache (256 B, bloco 16 B, WB)\n");
            tabela("E5_laco_80", "Vias", variarVias("E5", EXPERIMENTOS + "E5_laco_80.txt", vias), false, false);
            tabela("E5_laco_60", "Vias", variarVias("E5", EXPERIMENTOS + "E5_laco_60.txt", vias), false, false);

            tabelas.append("\n## E6 - Politicas de escrita (256 B, bloco 16 B, 2 vias)\n");
            for (String traco : new String[]{"E6_acumulador", "E6_copia", "E6_esparsa"}) {
                List<String> rotulos = new ArrayList<>();
                List<Statistics> linhas = new ArrayList<>();
                for (String politica : new String[]{"WT", "WB"}) {
                    rotulos.add(politica);
                    linhas.add(rodar("E6", EXPERIMENTOS + traco + ".txt", new Configuracao(256, 16, 2, politica)));
                }
                tabela(traco, "Politica", new Linhas(rotulos, linhas), true, true);
            }

            tabelas.append("\n## Desafio (256 B, bloco 16 B, WB)\n");
            tabela("Desafio", "Vias", variarVias("Desafio", PROPRIOS + "Desafio.txt", new int[]{1, 2, 4}), false, false);

            Files.createDirectories(Path.of("saida"));
            Files.writeString(Path.of("saida", "tabelas.md"), tabelas.toString(), StandardCharsets.UTF_8);
            System.out.print(tabelas);
            System.out.println("\narquivos gravados em saida/");
        } catch (IOException erro) {
            System.err.println("nao foi possivel ler ou gravar: " + erro.getMessage());
        }
    }

    private static class Linhas {
        final List<String> rotulos;
        final List<Statistics> estatisticas;

        Linhas(List<String> rotulos, List<Statistics> estatisticas) {
            this.rotulos = rotulos;
            this.estatisticas = estatisticas;
        }
    }

    //simula e grava a saida por acesso em saida/<experimento>/<traco>_cap.._bloco.._vias.._WB.txt
    private static Statistics rodar(String experimento, String caminho, Configuracao c) throws IOException {
        ResultadoSimulacao r = new Simulador(c).simular(new InputParse().lerArquivo(caminho));
        String nome = Path.of(caminho).getFileName().toString().replace(".txt", "");
        Path pasta = Path.of("saida", experimento);
        Files.createDirectories(pasta);
        String arquivo = String.format("%s_cap%d_bloco%d_vias%d_%s.txt", nome, c.capacidade, c.bloco, c.vias, c.escrita);
        Files.writeString(pasta.resolve(arquivo), Output.saidaCompleta(r), StandardCharsets.UTF_8);
        return r.estatisticas;
    }

    private static Linhas variarBloco(String experimento, String caminho) throws IOException {
        List<String> rotulos = new ArrayList<>();
        List<Statistics> linhas = new ArrayList<>();
        for (int bloco : new int[]{4, 8, 16, 32, 64}) {
            rotulos.add(String.valueOf(bloco));
            linhas.add(rodar(experimento, caminho, new Configuracao(256, bloco, 1, "WB")));
        }
        return new Linhas(rotulos, linhas);
    }

    private static Linhas variarVias(String experimento, String caminho, int[] vias) throws IOException {
        List<String> rotulos = new ArrayList<>();
        List<Statistics> linhas = new ArrayList<>();
        for (int v : vias) {
            rotulos.add(String.valueOf(v));
            linhas.add(rodar(experimento, caminho, new Configuracao(256, 16, v, "WB")));
        }
        return new Linhas(rotulos, linhas);
    }

    private static void tabela(String titulo, String coluna, Linhas l, boolean bytesLidos, boolean escrita) {
        tabelas.append("\n**").append(titulo).append("**\n\n");
        tabelas.append("| ").append(coluna).append(" | Acertos | Taxa de acerto | Compuls. | Conflito | Capac.");
        if (escrita) tabelas.append(" | Escritas | Bytes escritos");
        if (bytesLidos) tabelas.append(" | Bytes lidos");
        tabelas.append(" |\n|");
        int colunas = 6 + (escrita ? 2 : 0) + (bytesLidos ? 1 : 0);
        tabelas.append("---|".repeat(colunas)).append('\n');
        for (int i = 0; i < l.rotulos.size(); i++) {
            Statistics e = l.estatisticas.get(i);
            String taxa = String.format(Locale.ROOT, "%.2f%%", e.taxaAcerto()).replace('.', ',');
            tabelas.append(String.format("| %s | %d | %s | %d | %d | %d", l.rotulos.get(i), e.acertos, taxa,
                    e.compulsorias, e.conflito, e.capacidade));
            if (escrita) tabelas.append(String.format(" | %d | %d", e.escritas, e.bytesEscritos));
            if (bytesLidos) tabelas.append(" | ").append(e.bytesLidos);
            tabelas.append(" |\n");
        }
    }
}
