# Desafio-Sensidia

## Esta api consiste no recebimento de uma chamada com arquivo e metadatas e envio para o aws e por praticidade a mesma api consome estes arquivos e metadatas, mas sendo aconselhavel a separação de produção e consumo

## Recebimento da mensagen:

### Exemplo de Curl:

### curl --location 'http://localhost:8080/uploads' \
--form 'file=@"/C:/"seu path"/x/x/DesafioSensedia-ServiçodeGestãodeProcessamentodeArquivos.pdf"' \
--form 'metadataUpload="{ \"user\": {  \"userId\": \"user123\", \"email\": \"user5@example.com\"  }, \"file\": {  \"fileName\": \"document.txt\", \"fileType\": \"text/plain\", \"fileSize\": 1024  }}"'

## Produção

### Após o recebimento da requisição, é enviado o arquivo do relatorio para o bucket S3 e os metadatas  da requisição de processamento para a fila SQS 
