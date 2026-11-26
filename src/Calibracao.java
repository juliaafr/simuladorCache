import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

//roda os 4 tracos de calibracao e compara linha a linha com os arquivos *_esperado.txt
public class Calibracao {

    private static final String SEPARADOR = "\n" + "=".repeat(70) + "\n\n";

    //nome do traco e as configuracoes do enunciado (o C3 roda em WB e em WT)
    private static final String[] TRACOS = {"C1_mapeamento_direto", "C2_associativa_LRU", "C3_escrita", "C4_classificacao"};
    private static final Configuracao[][] CONFIGS = {
            {new Configuracao(128, 16, 1, "WB")},
            {new Configuracao(64, 16, 2, "WB")},
            {new Configuracao(64, 16, 1, "WB"), new Configuracao(64, 16, 1, "WT")},
            {new Configuracao(64, 16, 1, "WB")}
    };

    public static boolean executar(String pasta, boolean mostrarDiferencas) {
        boolean tudoOk = true;
        for (int t = 0; t < TRACOS.length; t++) {
            String nome = TRACOS[t];
            try {
                List<InputParse.AcessoLido> traco = new InputParse().lerArquivo(pasta + "/" + nome + ".txt");
                StringBuilder obtido = new StringBuilder();
                for (int i = 0; i < CONFIGS[t].length; i++) {
                    if (i > 0) obtido.append(SEPARADOR);
                    obtido.append(Output.saidaCompleta(new Simulador(CONFIGS[t][i]).simular(traco)));
                }
                String esperado = Files.readString(Path.of(pasta, nome + "_esperado.txt"), StandardCharsets.UTF_8);

                List<String> a = obtido.toString().strip().lines().map(String::stripTrailing).toList();
                List<String> b = esperado.strip().lines().map(String::stripTrailing).toList();
                if (a.equals(b)) {
                    System.out.println(" OK      " + nome);
                } else {
                    tudoOk = false;
                    System.out.println(" DIFERE  " + nome);
                    if (mostrarDiferencas) {
                        for (int i = 0; i < Math.max(a.size(), b.size()); i++) {
                            String x = i < a.size() ? a.get(i) : "<fim>";
                            String y = i < b.size() ? b.get(i) : "<fim>";
                            if (!x.equals(y)) {
                                System.out.println("   linha " + (i + 1) + "\n     obtido:   " + x + "\n     esperado: " + y);
                            }
                        }
                    }
                }
            } catch (Exception erro) {
                tudoOk = false;
                System.out.println(" ERRO    " + nome + ": " + erro.getMessage());
            }
        }
        return tudoOk;
    }
}
