import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class InputParse {

    //guarda cada acesso lido do traco
    public static class AcessoLido {
        public final char operacao; //'R' ou 'W'
        public final int endereco; //endereco de 32 bits, em bytes
        public final String linhaOriginal;

        public AcessoLido(char operacao, int endereco, String linhaOriginal) {
            this.operacao = operacao;
            this.endereco = endereco;
            this.linhaOriginal = linhaOriginal;
        }
    }

    //tudo depois de # e comentario
    public String removerComentario(String linha) {
        int pos = linha.indexOf('#');
        if (pos >= 0) {
            linha = linha.substring(0, pos);
        }
        return linha.trim();
    }

    //converte "R 0x10000000" em um AcessoLido
    public AcessoLido parse(String linha, int numeroLinha) throws IOException {
        String[] partes = linha.split("\\s+");
        if (partes.length != 2) {
            throw new IOException("linha " + numeroLinha + " invalida: " + linha);
        }
        String op = partes[0].toUpperCase();
        if (!op.equals("R") && !op.equals("W")) {
            throw new IOException("linha " + numeroLinha + ": operacao deve ser R ou W: " + linha);
        }
        String hex = partes[1].toLowerCase();
        if (hex.startsWith("0x")) {
            hex = hex.substring(2);
        }
        long valor;
        try {
            valor = Long.parseLong(hex, 16);
        } catch (NumberFormatException erro) {
            throw new IOException("linha " + numeroLinha + ": endereco invalido: " + partes[1]);
        }
        if (valor < 0 || valor > 0xFFFFFFFFL) {
            throw new IOException("linha " + numeroLinha + ": endereco maior que 32 bits");
        }
        if (valor % 4 != 0) {
            throw new IOException("linha " + numeroLinha + ": endereco nao alinhado a palavra de 4 bytes");
        }
        return new AcessoLido(op.charAt(0), (int) valor, linha);
    }

    public List<AcessoLido> lerArquivo(String caminho) throws IOException {
        List<String> linhas = Files.readAllLines(Path.of(caminho), StandardCharsets.UTF_8);
        List<AcessoLido> acessos = new ArrayList<>();
        for (int i = 0; i < linhas.size(); i++) {
            String linha = removerComentario(linhas.get(i));
            if (linha.isEmpty()) {
                continue;
            }
            acessos.add(parse(linha, i + 1));
        }
        return acessos;
    }
}
