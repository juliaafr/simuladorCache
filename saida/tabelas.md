# Tabelas medidas pelo simulador

A saida por acesso de cada linha esta em `saida/<experimento>/`.

## E1 - Tamanho do bloco numa varredura (256 B, 1 via, WB)

**E1_varredura**

| Bloco (B) | Acertos | Taxa de acerto | Compuls. | Conflito | Capac. | Bytes lidos |
|---|---|---|---|---|---|---|
| 4 | 0 | 0,00% | 128 | 0 | 0 | 512 |
| 8 | 64 | 50,00% | 64 | 0 | 0 | 512 |
| 16 | 96 | 75,00% | 32 | 0 | 0 | 512 |
| 32 | 112 | 87,50% | 16 | 0 | 0 | 512 |
| 64 | 120 | 93,75% | 8 | 0 | 0 | 512 |

## E2 - Tamanho do bloco com dados espalhados (256 B, 1 via, WB)

**E2_registros**

| Bloco (B) | Acertos | Taxa de acerto | Compuls. | Conflito | Capac. | Bytes lidos |
|---|---|---|---|---|---|---|
| 4 | 48 | 75,00% | 16 | 0 | 0 | 64 |
| 8 | 48 | 75,00% | 16 | 0 | 0 | 128 |
| 16 | 24 | 37,50% | 16 | 24 | 0 | 640 |
| 32 | 0 | 0,00% | 16 | 0 | 48 | 2048 |
| 64 | 0 | 0,00% | 16 | 0 | 48 | 4096 |

## E3 - Associatividade contra conflito (256 B, bloco 16 B, WB)

**E3_dois_vetores**

| Vias | Acertos | Taxa de acerto | Compuls. | Conflito | Capac. |
|---|---|---|---|---|---|
| 1 | 0 | 0,00% | 16 | 48 | 0 |
| 2 | 48 | 75,00% | 16 | 0 | 0 |
| 4 | 48 | 75,00% | 16 | 0 | 0 |
| 16 | 48 | 75,00% | 16 | 0 | 0 |

**E3_dois_vetores_modificado (segundo vetor em 0x10000080)**

| Vias | Acertos | Taxa de acerto | Compuls. | Conflito | Capac. |
|---|---|---|---|---|---|
| 1 | 48 | 75,00% | 16 | 0 | 0 |

## E4 - Associatividade numa varredura (256 B, bloco 16 B, WB)

**E1_varredura**

| Vias | Acertos | Taxa de acerto | Compuls. | Conflito | Capac. |
|---|---|---|---|---|---|
| 1 | 96 | 75,00% | 32 | 0 | 0 |
| 2 | 96 | 75,00% | 32 | 0 | 0 |
| 4 | 96 | 75,00% | 32 | 0 | 0 |
| 16 | 96 | 75,00% | 32 | 0 | 0 |

## E5 - Laco maior e menor que a cache (256 B, bloco 16 B, WB)

**E5_laco_80**

| Vias | Acertos | Taxa de acerto | Compuls. | Conflito | Capac. |
|---|---|---|---|---|---|
| 1 | 204 | 85,00% | 20 | 0 | 16 |
| 2 | 196 | 81,67% | 20 | 0 | 24 |
| 4 | 180 | 75,00% | 20 | 0 | 40 |
| 16 | 180 | 75,00% | 20 | 0 | 40 |

**E5_laco_60**

| Vias | Acertos | Taxa de acerto | Compuls. | Conflito | Capac. |
|---|---|---|---|---|---|
| 1 | 165 | 91,67% | 15 | 0 | 0 |
| 2 | 165 | 91,67% | 15 | 0 | 0 |
| 4 | 165 | 91,67% | 15 | 0 | 0 |
| 16 | 165 | 91,67% | 15 | 0 | 0 |

## E6 - Politicas de escrita (256 B, bloco 16 B, 2 vias)

**E6_acumulador**

| Politica | Acertos | Taxa de acerto | Compuls. | Conflito | Capac. | Escritas | Bytes escritos | Bytes lidos |
|---|---|---|---|---|---|---|---|---|
| WT | 99 | 99,00% | 1 | 0 | 0 | 50 | 200 | 16 |
| WB | 99 | 99,00% | 1 | 0 | 0 | 1 | 16 | 16 |

**E6_copia**

| Politica | Acertos | Taxa de acerto | Compuls. | Conflito | Capac. | Escritas | Bytes escritos | Bytes lidos |
|---|---|---|---|---|---|---|---|---|
| WT | 96 | 75,00% | 32 | 0 | 0 | 64 | 256 | 512 |
| WB | 96 | 75,00% | 32 | 0 | 0 | 16 | 256 | 512 |

**E6_esparsa**

| Politica | Acertos | Taxa de acerto | Compuls. | Conflito | Capac. | Escritas | Bytes escritos | Bytes lidos |
|---|---|---|---|---|---|---|---|---|
| WT | 0 | 0,00% | 32 | 0 | 0 | 32 | 128 | 512 |
| WB | 0 | 0,00% | 32 | 0 | 0 | 32 | 512 | 512 |

## Desafio (256 B, bloco 16 B, WB)

**Desafio**

| Vias | Acertos | Taxa de acerto | Compuls. | Conflito | Capac. |
|---|---|---|---|---|---|
| 1 | 0 | 0,00% | 4 | 36 | 0 |
| 2 | 0 | 0,00% | 4 | 36 | 0 |
| 4 | 36 | 90,00% | 4 | 0 | 0 |
