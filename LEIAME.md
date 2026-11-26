# Simulador de cache: UNIVALI, Organização de Computadores

**Autores:** Júlia Alves França e João Guilherme

Projeto IntelliJ em Java, sem bibliotecas externas. Abra a pasta `simulador/` como projeto.

## Estrutura

```
simulador/
├── .idea/  simulador.iml
├── src/
│   ├── Main.java                 simula um traço (sem argumentos: roda a calibração)
│   ├── MainExperimentos.java     roda E1 a E6, o E3 modificado e o Desafio
│   ├── Calibracao.java           compara C1 a C4 com os arquivos *_esperado.txt
│   ├── InputParse.java           lê o traço (R/W + endereço hexadecimal, # = comentário)
│   ├── Configuracao.java         capacidade, bloco, vias, escrita; recusa combinações impossíveis
│   ├── BitSeparator.java         divide o endereço em rótulo | índice | offset
│   ├── Conjunto.java             conjunto da cache com LRU e bit D
│   ├── BlocoExpulso.java
│   ├── ClassificadorFaltas.java  compulsória / conflito / capacidade (cache totalmente associativa em paralelo)
│   ├── Simulador.java            executa o traço
│   ├── ResultadoAcesso.java      uma linha da saída
│   ├── ResultadoSimulacao.java
│   ├── Statistics.java           contadores do resumo
│   └── Output.java               formata a saída igual aos arquivos de resposta
├── testes/
│   ├── calibracao/               C1 a C4 + respostas do professor
│   ├── experimentos/             E1 a E6
│   └── proprios/                 E3_dois_vetores_modificado.txt e Desafio.txt
└── saida/                        gerada pelo MainExperimentos (tabelas.md + saída por acesso)
```

## Executar

No IntelliJ, rode a classe desejada. O diretório de trabalho deve ser a pasta `simulador/`, que é o padrão.

Pelo terminal, na pasta `simulador/`:

```
javac -encoding UTF-8 -d out/production/simulador src/*.java
java -cp out/production/simulador Main
java -cp out/production/simulador Main <traco> <capacidade> <bloco> <vias> [WB|WT] [resumo] [arquivo_saida]
java -cp out/production/simulador MainExperimentos
```

O que cada comando faz:
- **`Main` sem argumentos:** roda os 4 traços de calibração e confere com as respostas do professor.
- **`Main` com argumentos:** simula um traço. Use `vias 1` para mapeamento direto e `vias` = capacidade ÷ bloco para totalmente associativa. A política de escrita padrão é WB.
- **`MainExperimentos`:** gera `saida/tabelas.md` e a saída por acesso de cada configuração em `saida/<experimento>/`.

## Defesa: os 4 traços de calibração

```
java -cp out/production/simulador Main testes/calibracao/C1_mapeamento_direto.txt 128 16 1 WB
java -cp out/production/simulador Main testes/calibracao/C2_associativa_LRU.txt 64 16 2 WB
java -cp out/production/simulador Main testes/calibracao/C3_escrita.txt 64 16 1 WB
java -cp out/production/simulador Main testes/calibracao/C3_escrita.txt 64 16 1 WT
java -cp out/production/simulador Main testes/calibracao/C4_classificacao.txt 64 16 1 WB
```
