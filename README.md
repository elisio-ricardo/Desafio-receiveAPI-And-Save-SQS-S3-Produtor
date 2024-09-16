# Desafio-Sensidia

### Este projeto consiste em duas APIs uma de produzir o recebimento de uma chamada para o processamento de um arquivo(contagem de linhas), onde é recebido o arquivo e os metadados do arquivo e do solicitante e enviado para o aws
 

## Recebimento da mensagen:

### Exemplo de Curl:

--location 'http://localhost:8080/uploads' \
--form 'file=@"/C:/"seu path"/x/x/DesafioSensedia-ServiçodeGestãodeProcessamentodeArquivos.pdf"' \
--form 'metadataUpload="{ \"user\": {  \"userId\": \"user123\", \"email\": \"user5@example.com\"  }, \"file\": {  \"fileName\": \"document.txt\", \"fileType\": \"text/plain\", \"fileSize\": 1024  }}"'

## Produção

### Após o recebimento da requisição, é enviado o arquivo do relatorio a ser processado para o bucket S3 e os metadados da requisição de processamento para a fila SQS 

## API Consumo - https://github.com/elisio-ricardo/Desafio-Sensidia-Consumo-AWS

### A api fica escutando a a fila SQS, quando chega uma solicitação de processamento, ela pega o nome do arquivo que esta dentro do corpo da mensagem e faz o download do arquivo que esta no S3


## Importante: a mensagem tem um Reetry de 3 tentativas após isso ela será enviada para o DLQ**

### Faz o processamento do arquivo e conta a quantidade de linhas que tem no arquivo, gera um relatorio e envia para o topico SNS
### Se houver erro ao tentar fazer download ou processamento do arquivo o status é definido como nulo

## Desenho da Solução


![image](https://github.com/user-attachments/assets/7719a509-68aa-478d-ba70-43efd7e98fad)


