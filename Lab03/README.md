# Laboratório 3: Chat Seguro - S-DES/RC4 + ECB/CBC + DH

## Problema
Desenvolver uma aplicação para troca de mensagens de texto (estilo chat com interface gráfica, veja descrição de layout abaixo) entre você e seus colegas de maneira que seja possível trocar mensagens de texto entre pares utilizando criptografia com o S-DES e o RC4, desenvolvidos por você.
Assuma que a comunicação será feita pela porta 3000, com socket TCP.

Interface: GUI contendo campos para digitação de texto a enviar  e visualização de texto recebidos, caixas de texto para configuração de IP de destino, caixas de seleção algoritmo criptográfico e definição de chaves.

Assuma também que ambos os pares já conhecem a chave de segurança e essa poderá ser modificada pelo usuário em tempo de execução da aplicação, bem como o algoritmo de criptografia utilizado.

