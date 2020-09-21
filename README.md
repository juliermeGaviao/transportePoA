# TransportePoA

O projeto TransportePoA é uma API REST implementada em Spring Boot para controle de cadastro de linhas de ônibus e pontos de táxi da cidade de Porto Alegre.

Tal framework foi adotao em função de prévia experiência nessa plataforma, simplicidade e boa aceitação entre os desenvolvedores Java. O banco de dados usado é o H2, já que o volume de dados é pequeno e pode ser manipulado por um banco de dados relacional em memório RAM.

# Requerimentos

- JDK 1.8 ou superior
- Maven 3 ou superior
- Git 2.25 ou superior
- Docker 19.03 ou superior
- Postman 7 ou superior

# Construção e Implantação

O projeto está disponível via GIT, para obtê-lo:

`git clone https://github.com/juliermeGaviao/transportePoA.git`

Dentro do diretório "transportePoA", construir a aplicação:

`mvn install`

A partir disso, a aplicação pode ser implantada no Docker, é necessário permissão de super usuário:

`docker build --build-arg JAR_FILE=target/transportePoA-0.1.0.jar -t transportepoa .`

# Execução

A aplicação pode ser iniciada com o seguinte comando, também necessita permissão de super usuário:

`docker run -p 8080:8080 transportepoa`

A aplicação estará disponível na porta 8080. Esse processo leva ao redor de 1 min em função da carga de todas as coordenadas de itinerários das linhas de ônibus.

# Requisições

Requisições exemplo da API estão disponíveis no arquivo "TransportePoA.postman_collection.json", o qual pode ser importado e os testes disparados
no Postman.

**Todos os parâmetros das requisições, listados nas seções a seguir, são de preenchimento OBRIGATÓRIO**

# API Ônibus

## Buscar Linha Pelo Nome

###Caminho: /onibus/buscarLinhaPeloNome/{nome}

###Verbo HTTP: GET

| Parâmetro | Tipo   | Local | Descição                                             |
|-----------|--------|-------|------------------------------------------------------|
| nome      | String | URL   | Texto a ser buscado no nome de cada linha de ônibus. |

##Resultado

**Http Status - 200 OK** - Lista de linhas cujo nome contém o texto informado no parâmetro "nome". Cada objeto desta lista contém os seguintes campos:

| Campo       | Tipo    | Descrição                                                                                                     |
|-------------|---------|---------------------------------------------------------------------------------------------------------------|
| id          | Integer | Identificação da linha de ônibus                                                                              |
| codigo      | String  | Código da linha de ônibus. Ex: 250-1                                                                          |
| nome        | String  | Nome da linha de ônibus. Ex: Campus-Ipiranga                                                                  |
| itinerarios | List    | Linha de objetos com os pares de coordenadas geográficas. Tais pares compõem o itinerário da linha de ônibus. |

**Http Status - 204 No Content** - Nenhuma de linha de ônibus encontrada.

## Buscar Linha Pelo Código

###Caminho: /onibus/buscarLinhaPeloCodigo/{codigo}

###Verbo HTTP: GET

| Parâmetro | Tipo   | Local | Descição                                                                                           |
|-----------|--------|-------|----------------------------------------------------------------------------------------------------|
| codigo    | String | URL   | Código da linha de ônibus. Seleção é feita por igualdade entre esse parâmetro é o código da linha. |

##Resultado

**Http Status - 200 OK** - lista de linhas cujo código é igual ao texto informado no parâmetro "codigo". Cada objeto desta lista contém os seguintes campos:

| Campo       | Tipo    | Descrição                                                                                                     |
|-------------|---------|---------------------------------------------------------------------------------------------------------------|
| id          | Integer | Identificação da linha de ônibus                                                                              |
| codigo      | String  | Código da linha de ônibus. Ex: 250-1                                                                          |
| nome        | String  | Nome da linha de ônibus. Ex: Campus-Ipiranga                                                                  |
| itinerarios | List    | Linha de objetos com os pares de coordenadas geográficas. Tais pares compõem o itinerário da linha de ônibus. |

**Http Status - 204 No Content** - Nenhuma de linha de ônibus encontrada.

## Inserir Linha

###Caminho /onibus/inserirLinha

###Verbo HTTP: POST

| Parâmetro | Tipo   | Local | Descição                   |
|-----------|--------|-------|----------------------------|
| nome      | String | Corpo | Nome da linha de ônibus.   |
| codigo    | String | Corpo | Código da linha de ônibus. |

##Resultado

**Http Status - 201 Created** - Número inteiro identificando a linha de ônibus criada com sucesso.

**Http Status - 400 Bad Request** - Um dos parâmetros, nome ou codigo, está ausente ou sem conteúdo. A mensagem indica a inconsistência.

**Http Status - 409 Conflict** - Já existe outra linha de ônibus com o mesmo código.

## Editar Linha

###Caminho /onibus/editarLinha

###Verbo HTTP: POST

| Parâmetro | Tipo    | Local | Descição                                         |
|-----------|---------|-------|--------------------------------------------------|
| id        | Integer | Corpo | Identificação da linha de ônibus a ser alterada. |
| nome      | String  | Corpo | Nome da linha de ônibus.                         |
| codigo    | String  | Corpo | Código da linha de ônibus.                       |

##Resultado

**Http Status - 200 OK** - true, em caso de sucesso na alteração. 

**Http Status - 400 Bad Request** - Um dos parâmetros, nome ou codigo, está ausente ou sem conteúdo; ou não há linha de ônibus com a identificação informada. A mensagem indica a inconsistência.

**Http Status - 409 Conflict** - Já existe outra linha de ônibus com o mesmo código.

# API Táxi
