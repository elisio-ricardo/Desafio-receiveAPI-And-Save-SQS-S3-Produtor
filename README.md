# Desafio-Sensidia

## Esta api consiste no recebimento de uma chamada com arquivo e metadatas e envio para o aws e por praticidade a mesma api consome estes arquivos e metadatas, mas sendo aconselhavel a separação de produção e consumo

## Recebimento da mensagen:

### Exemplo de Curl:

### curl --location 'http://localhost:8080/uploads' \
--form 'file=@"/C:/"seu path"/x/x/DesafioSensedia-ServiçodeGestãodeProcessamentodeArquivos.pdf"' \
--form 'metadataUpload="{ \"user\": {  \"userId\": \"user123\", \"email\": \"user5@example.com\"  }, \"file\": {  \"fileName\": \"document.txt\", \"fileType\": \"text/plain\", \"fileSize\": 1024  }}"'

## Produção

### Após o recebimento da requisição, é enviado o arquivo do relatorio para o bucket S3 e os metadatas  da requisição de processamento para a fila SQS 

## Consumo

A api fica escutando a a fila SQS, quando chega uma solicitação de processamento, ela pega o nome do arquivo que esta dentro do corpo da mensagem e faz o download do arquivo que esta no S3

Faz o processamento do arquivo e conta a quantidade de linhas que tem no arquivo, gera um relatorio e envia para o topico SNS
