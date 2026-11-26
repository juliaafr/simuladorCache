import java.util.Locale;

//monta o texto da saida no mesmo formato dos arquivos de resposta da calibracao
public class Output {

    public static String cabecalho(Configuracao c) {
        return String.format("CONFIGURACAO  capacidade=%d B  bloco=%d B  vias=%d  conjuntos=%d  escrita=%s\n",
                c.capacidade, c.bloco, c.vias, c.conjuntos, c.escrita)
                + String.format("CAMPOS        rotulo=%d bits  indice=%d bits  offset=%d bits\n",
                c.bitsRotulo, c.bitsIndice, c.bitsOffset);
    }

    public static String linhaAcesso(ResultadoAcesso r, Configuracao c) {
        int digitosRotulo = (c.bitsRotulo + 3) / 4;
        StringBuilder linha = new StringBuilder(String.format("%4d  %c 0x%08X  rot=0x%0" + digitosRotulo + "X  idx=%-3d off=%-3d ",
                r.numero, r.operacao, r.endereco, r.rotulo, r.indice, r.offset));
        if (r.acerto) {
            linha.append("ACERTO");
        } else {
            linha.append(String.format("FALTA  %-11s", r.tipoFalta));
            if (r.expulsou) {
                linha.append(String.format(" expulsa 0x%08X", r.enderecoExpulso));
                if (r.writeback) {
                    linha.append(" (writeback)");
                }
            }
        }
        return linha.toString().stripTrailing();
    }

    public static String resumo(Statistics e, Configuracao c) {
        StringBuilder s = new StringBuilder();
        s.append("acessos ............. ").append(e.acessos).append('\n');
        s.append("acertos ............. ").append(e.acertos).append('\n');
        s.append(String.format("faltas .............. %d  (compulsorias %d, conflito %d, capacidade %d)\n",
                e.faltas(), e.compulsorias, e.conflito, e.capacidade));
        s.append(String.format(Locale.ROOT, "taxa de acerto ...... %.2f%%\n", e.taxaAcerto()));
        s.append(String.format("blocos lidos da mem.  %d  (%d B)\n", e.blocosLidos, e.bytesLidos));
        s.append(String.format("escritas na memoria  %d  (%d B)", e.escritas, e.bytesEscritos));
        if (c.isWriteBack()) {
            s.append(String.format("  [writebacks %d + descarga final %d]", e.writebacks, e.descargaFinal));
        }
        s.append('\n');
        return s.toString();
    }

    //saida completa: cabecalho, uma linha por acesso e o resumo
    public static String saidaCompleta(ResultadoSimulacao r) {
        StringBuilder s = new StringBuilder(cabecalho(r.config)).append('\n');
        for (ResultadoAcesso a : r.acessos) {
            s.append(linhaAcesso(a, r.config)).append('\n');
        }
        return s.append('\n').append(resumo(r.estatisticas, r.config)).toString();
    }

    public static String somenteResumo(ResultadoSimulacao r) {
        return cabecalho(r.config) + "\n" + resumo(r.estatisticas, r.config);
    }
}
