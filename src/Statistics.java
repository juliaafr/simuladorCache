public class Statistics {

    public int acessos = 0;
    public int acertos = 0;
    public int compulsorias = 0;
    public int conflito = 0;
    public int capacidade = 0;
    public int blocosLidos = 0;
    public long bytesLidos = 0;
    public int escritas = 0; //escritas enviadas a memoria
    public long bytesEscritos = 0;
    public int writebacks = 0; //write-back: durante a execucao
    public int descargaFinal = 0; //write-back: blocos com D=1 no fim do traco

    public void registrarAcesso(ResultadoAcesso r, Configuracao config) {
        acessos++;
        if (r.acerto) {
            acertos++;
        } else {
            blocosLidos++;
            bytesLidos += config.bloco;
            switch (r.tipoFalta) {
                case "compulsoria":
                    compulsorias++;
                    break;
                case "conflito":
                    conflito++;
                    break;
                default:
                    capacidade++;
            }
        }
        if (r.writeback) {
            writebacks++;
            escritas++;
            bytesEscritos += config.bloco;
        }
    }

    //write-through: cada escrita envia uma palavra (4 bytes)
    public void registrarEscritaWT() {
        escritas++;
        bytesEscritos += Configuracao.TAMANHO_PALAVRA;
    }

    public void registrarDescargaFinal(int blocos, Configuracao config) {
        descargaFinal += blocos;
        escritas += blocos;
        bytesEscritos += (long) blocos * config.bloco;
    }

    public int faltas() {
        return acessos - acertos;
    }

    public double taxaAcerto() {
        return acessos == 0 ? 0 : 100.0 * acertos / acessos;
    }
}
