# TransportePoA

O projeto TransportePoA é uma API REST implementada em Spring Boot para controle de cadastro de linhas de ônibus e pontos de táxi da cidade de Porto Alegre.

Tal framework foi adotao em função de prévia experiência nessa plataforma, simplicidade e boa aceitação entre os desenvolvedores Java. O banco de dados usado é o H2, já que o volume de dados é pequeno e pode ser manipulado por um banco de dados relacional em memório RAM.

## Requerimentos

- JDK 1.8 ou superior
- Maven 3 ou superior
- Git 2.25 ou superior
- Docker 19.03 ou superior
- Postman 7 ou superior

## Construção e Implantação

O projeto está disponível via GIT, para obtê-lo:

`git clone https://github.com/juliermeGaviao/transportePoA.git`

Dentro do diretório "transportePoA", construir a aplicação:

`mvn install`

A partir disso, a aplicação pode ser implantada no Docker, é necessário permissão de super usuário:

`docker build --build-arg JAR_FILE=target/transportePoA-0.1.0.jar -t transportepoa .`

## Execução

A aplicação pode ser iniciada com o seguinte comando, também necessita permissão de super usuário:

`docker run -p 8080:8080 transportepoa`

A aplicação estará disponível na porta 8080. Esse processo leva ao redor de 1 min em função da carga de todas as coordenadas de itinerários das linhas de ônibus.

## Requisições

Requisições exemplo da API estão disponíveis no arquivo "TransportePoA.postman_collection.json", o qual pode ser importado e os testes disparados
no Postman.

## API

A API TransportePoA é divida em Ônibus e Táxi. A primeira trata das linhas de ônibus enquanto que a segunda dos pontos de Táxi.

**Todos os parâmetros das requisições, listados nas seções seguintes, são de preenchimento OBRIGATÓRIO**.

## API Ônibus

Este conjunto de operações está divido em dois grupos:

1. Linha de ônibus: Buscar Linha Pelo Nome, Buscar Linha Pelo Código, Inserir Linha, Editar Linha, Remover Linha e Filtrar Linhas por Raio;
2. Itinerário da linha ônibus: Buscar Itinerário Pela Linha, Inserir Itinerário, Editar Itinerário e Remover Itinerário;

### Buscar Linha Pelo Nome

#### Caminho: /onibus/buscarLinhaPeloNome/{nome}

#### Verbo HTTP: GET

| Parâmetro | Tipo   | Local | Descição                                             |
|-----------|--------|-------|------------------------------------------------------|
| nome      | String | URL   | Texto a ser buscado no nome de cada linha de ônibus. |

#### Resultado

**Http Status - 200 OK** - Lista de linhas cujo nome contém o texto informado no parâmetro "nome". Cada objeto desta lista contém os seguintes campos:

| Campo       | Tipo    | Descrição                                                                                                     |
|-------------|---------|---------------------------------------------------------------------------------------------------------------|
| id          | Integer | Identificação da linha de ônibus.                                                                              |
| codigo      | String  | Código da linha de ônibus. Ex: 250-1.                                                                          |
| nome        | String  | Nome da linha de ônibus. Ex: Campus-Ipiranga.                                                                  |
| itinerarios | List    | Linha de objetos com os pares de coordenadas geográficas. Tais pares compõem o itinerário da linha de ônibus. |

**Http Status - 204 No Content** - Nenhuma de linha de ônibus encontrada.

## Buscar Linha Pelo Código

### Caminho: /onibus/buscarLinhaPeloCodigo/{codigo}

### Verbo HTTP: GET

| Parâmetro | Tipo   | Local | Descição                                                                                           |
|-----------|--------|-------|----------------------------------------------------------------------------------------------------|
| codigo    | String | URL   | Código da linha de ônibus. Seleção é feita por igualdade entre esse parâmetro é o código da linha. |

### Resultado

**Http Status - 200 OK** - lista de linhas cujo código é igual ao texto informado no parâmetro "codigo". Cada objeto desta lista contém os seguintes campos:

| Campo       | Tipo    | Descrição                                                                                                     |
|-------------|---------|---------------------------------------------------------------------------------------------------------------|
| id          | Integer | Identificação da linha de ônibus.                                                                              |
| codigo      | String  | Código da linha de ônibus. Ex: 250-1.                                                                          |
| nome        | String  | Nome da linha de ônibus. Ex: Campus-Ipiranga.                                                                  |
| itinerarios | List    | Linha de objetos com os pares de coordenadas geográficas. Tais pares compõem o itinerário da linha de ônibus. |

**Http Status - 204 No Content** - Nenhuma de linha de ônibus encontrada.

## Inserir Linha

### Caminho /onibus/inserirLinha

### Verbo HTTP: POST

| Parâmetro | Tipo   | Local | Descição                   |
|-----------|--------|-------|----------------------------|
| nome      | String | Corpo | Nome da linha de ônibus.   |
| codigo    | String | Corpo | Código da linha de ônibus. |

### Resultado

**Http Status - 201 Created** - Número inteiro identificando a linha de ônibus criada com sucesso.

**Http Status - 400 Bad Request** - Um dos parâmetros, nome ou codigo, está ausente ou sem conteúdo. A mensagem acessória da resposta indica a inconsistência.

**Http Status - 409 Conflict** - Já existe outra linha de ônibus com o mesmo código.

## Editar Linha

### Caminho /onibus/editarLinha

### Verbo HTTP: POST

| Parâmetro | Tipo    | Local | Descição                                         |
|-----------|---------|-------|--------------------------------------------------|
| id        | Integer | Corpo | Identificação da linha de ônibus a ser alterada. |
| nome      | String  | Corpo | Nome da linha de ônibus.                         |
| codigo    | String  | Corpo | Código da linha de ônibus.                       |

### Resultado

**Http Status - 200 OK** - true, em caso de sucesso na alteração.

**Http Status - 400 Bad Request** - Um dos parâmetros, nome ou codigo, está ausente ou sem conteúdo; ou não há linha de ônibus com a identificação informada no parâmetro id. A mensagem acessória da resposta indica a inconsistência.

**Http Status - 409 Conflict** - Já existe outra linha de ônibus com o mesmo código.

## Remover Linha

### Caminho /onibus/removerLinha/{idLinha}

### Verbo HTTP: DELETE

| Parâmetro | Tipo    | Local | Descição                                         |
|-----------|---------|-------|--------------------------------------------------|
| idLinha   | Integer | URL   | Identificação da linha de ônibus a ser removida. |

### Resultado

**Http Status - 200 OK** - true, em caso de sucesso na remoção. 

**Http Status - 204 No Content** - Não há linha de ônibus com a identificação informado no parâmetro id.

## Filtrar Linhas por Raio

### Caminho /onibus/linhasDentroRaio

### Verbo HTTP: POST

| Parâmetro | Tipo  | Local | Descição                                                                                   |
|-----------|-------|-------|--------------------------------------------------------------------------------------------|
| latitude  | Float | Corpo | Latitude do ponto de referência para busca das linhas de ônibus mais próximas.             |
| longitude | Float | Corpo | Longitude do ponto de referência para busca das linhas de ônibus mais próximas.            |
| raio      | Float | Corpo | Extensão do raio a partir do ponto de referência, em Km, para filtrar as linhas de ônibus. |

### Resultado

**Http Status - 200 OK** - Lista de objetos, linhas de ônibus, com os seguintes campos:

| Parâmetro   | Tipo    | Descrição                                                                                                                                                                                                                                                                    |
|-------------|---------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| id          | Integer | Identificação da linha de ônibus.                                                                                                                                                                                                                                            |
| codigo      | String  | Código da linha de ônibus. Ex: 250-1                                                                                                                                                                                                                                         |
| nome        | String  | Nome da linha de ônibus. Ex: Campus-Ipiranga.                                                                                                                                                                                                                                |
| itinerarios | List    | Linha de objetos com os pares de coordenadas geográficas. Tais pares compõem o itinerário da linha de ônibus. No entanto, essa operação produz uma lista vazia, para não onerar a rede. Tal lista pode ser obtida com operação "Buscar Itinerário Pela Linha", nesta API. |

**Http Status - 400 Bad Request** - Um dos parâmetros está ausente: latitude, longitude ou raio. A mensagem acessória da resposta indica o campo faltante.

## Buscar Itinerário Pela Linha

### Caminho: /onibus/buscarItinerario/{idLinha}

### Verbo HTTP: GET

| Parâmetro | Tipo    | Local | Descição                                                         |
|-----------|---------|-------|------------------------------------------------------------------|
| idLinha   | Integer | URL   | Identificação da linha de ônibus cujo itinerário será retornado. |

### Resultado

**Http Status - 200 OK** - lista de objetos correspondentes às coordenadas que compõem o itinerário da linha de ônibus. Cada objeto desta lista contém os seguintes campos:

| Parâmetro | Tipo    | Descrição                                                                     |
|-----------|---------|-------------------------------------------------------------------------------|
| id        | Integer | Identificação da coordenada do itinerário.                                    |
| idLinha   | Integer | Identificação da linha de ônibus ao qual a coordenada do itinerário pertence. |
| ordem     | Integer | A ordem da coordenada ao longo do itinerário da linha de ônibus.              |
| latitude  | Float   | Latitude da coordenada do itinerário.                                         |
| longitude | Float   | Longitude da coordenada do itinerário.                                        |

**Http Status - 204 No Content** - Não há linha de ônibus com a identificação informado no parâmetro idLinha.

## Inserir Itinerário

Esta operação inclui uma coordenada geodésica como parte do itinerário de uma linha de ônibus.

### Caminho: /onibus/inserirItinerario/{idLinha}

### Verbo HTTP: POST

| Parâmetro | Tipo    | Local | Descição                                                                     |
|-----------|---------|-------|------------------------------------------------------------------------------|
| idLinha   | Integer | URL   | Identificação da linha de ônibus para a qual o itinerário será incrementado. |
| latitude  | Float   | Corpo | Latitude da coordenada a ser inserida no itinerário.                         |
| longitude | Float   | Corpo | Latitude da coordenada a ser inserida no itinerário.                         |

### Resultado

**Http Status - 201 Created** - número inteiro correspondente à identificação atribuída à coordenada inserida com sucesso no itinerário da linha de ônibus indicada pelo parâmetro idLinha.

**Http Status - 400 Bad Request** - Um dos parâmetros está ausente: latitude ou longitude. A mensagem acessória da resposta indica o campo faltante.

**Http Status - 204 No Content** - Não há linha de ônibus com a identificação informado no parâmetro idLinha.

## Editar Itinerário

### Caminho: /onibus/editarItinerario

### Verbo HTTP: POST

| Parâmetro | Tipo    | Local | Descição                                                  |
|-----------|---------|-------|-----------------------------------------------------------|
| id        | Integer | Corpo | Identificação da coordenada do itinerário a ser alterada. |
| latitude  | Float   | Corpo | Latitude da coordenada a ser alterada.                    |
| longitude | Float   | Corpo | Latitude da coordenada a ser alterada.                    |

### Resultado

**Http Status - 200 Ok** - true, em caso de sucesso na alteração.

**Http Status - 400 Bad Request** - Um dos parâmetros está ausente: id, latitude ou longitude. A mensagem acessória da resposta indica o campo faltante.

**Http Status - 204 No Content** - Não há coordenada de itinerário com identificação correspondente ao conteúdo do campo id.

## Remover Itinerário

### Caminho /onibus/removerItinerario/{idItinerario}

### Verbo HTTP: DELETE

| Parâmetro    | Tipo    | Local | Descição                                                  |
|--------------|---------|-------|-----------------------------------------------------------|
| idItinerario | Integer | URL   | Identificação da coordenada do itinerário a ser removida. |

### Resultado

**Http Status - 200 OK** - true, em caso de sucesso na remoção. 

**Http Status - 204 No Content** - Não há coordenada de itinerário com identificação correspondente ao conteúdo do campo idItinerario.

# API Táxi

As operações para pontos de táxi são: Buscar Ponto de Táxi, Inserir Ponto de Táxi, Filtrar Pontos de Táxi por Raio e Remover Ponto de Táxi.

## Buscar Ponto de Táxi

### Caminho: /taxi/buscarPontoTaxi/{nome}

### Verbo HTTP: GET

| Parâmetro | Tipo   | Local | Descição                                                                                                                                             |
|-----------|--------|-------|------------------------------------------------------------------------------------------------------------------------------------------------------|
| nome      | String | URL   | Texto que será buscado em cada nome de ponto de táxi como substring. Portanto, todos os pontos de táxi cujo nome contêm esse texto serão retornados. |

### Resultado

**Http Status - 200 OK** - lista de objetos correspondentes aos pontos de táxi. Cada objeto desta lista contém os seguintes campos:

| Parâmetro    | Tipo   | Descrição                                   |
|--------------|--------|---------------------------------------------|
| nome         | String | Nome do ponto de táxi.                      |
| latitude     | Float  | Latitude da coordenada do ponto de táxi.    |
| longitude    | Float  | Longitude da coordenada do ponto de táxi.   |
| dataCadastro | String | Data em que o ponto de táxi foi cadastrado. |

**Http Status - 204 No Content** - Não há ponto de táxi cujo nome contém o texto informado no parâmetro nome.

## Inserir Ponto de Táxi

### Caminho: /taxi/inserirPontoTaxi

### Verbo HTTP: POST

| Parâmetro | Tipo   | Local | Descição                                            |
|-----------|--------|-------|-----------------------------------------------------|
| nome      | String | Corpo | Nome do novo ponto de táxi.                         |
| latitude  | Float  | Corpo | Latitude da coordenada geodésica do ponto de táxi.  |
| longitude | Float  | Corpo | Longitude da coordenada geodésica do ponto de táxi. |

### Resultado

**Http Status - 201 Created** - objeto contendo os seguintes campos do ponto de táxi, mesma estrutura dos parâmetros, exceto que há a adição da data de cadastro do ponto de táxi:

| Parâmetro    | Tipo   | Descrição                                   |
|--------------|--------|---------------------------------------------|
| nome         | String | Nome do ponto de táxi.                      |
| latitude     | Float  | Latitude da coordenada do ponto de táxi.    |
| longitude    | Float  | Longitude da coordenada do ponto de táxi.   |
| dataCadastro | String | Data em que o ponto de táxi foi cadastrado. |

**Http Status - 400 Bad Request** - Um dos parâmetros está ausente: nome, latitude ou longitude. A mensagem acessória da resposta indica o campo faltante.

**Http Status - 500 Internal Server Error** - Problema na gravação do arquivo que contém os pontos de táxi.

## Remover Ponto de Táxi

### Caminho /taxi/removerPontoTaxi/{nome}

### Verbo HTTP: DELETE

| Parâmetro | Tipo   | Local | Descição                                                                                                                                     |
|-----------|--------|-------|----------------------------------------------------------------------------------------------------------------------------------------------|
| nome      | String | URL   | Nome do ponto de táxi a ser removido. O conteúdo desse parâmetro deve ser igual ao nome do ponto de táxi cadastrado que se pretende remover. |

### Resultado

**Http Status - 200 OK** - true, em caso de sucesso na remoção. 

**Http Status - 204 No Content** - Não há ponto de táxi com o mesmo contéudo no campo nome que aquele informado no parâmetro nome.

**Http Status - 500 Internal Server Error** - Problema na gravação do arquivo que contém os pontos de táxi.

## Filtrar Pontos de Táxi por Raio

### Caminho /taxi/pontosTaxiDentroRaio

### Verbo HTTP: POST

| Parâmetro | Tipo  | Local | Descição                                                                                 |
|-----------|-------|-------|------------------------------------------------------------------------------------------|
| latitude  | Float | Corpo | Latitude do ponto de referência para busca dos pontos de táxi mais próximos.             |
| longitude | Float | Corpo | Longitude do ponto de referência para busca dos pontos de táxi mais próximos.            |
| raio      | Float | Corpo | Extensão do raio a partir do ponto de referência, em Km, para filtrar os pontos de táxi. |

### Resultado

**Http Status - 200 OK** - Lista de objetos, pontos de táxi, com os seguintes campos:

| Parâmetro    | Tipo   | Descrição                                   |
|--------------|--------|---------------------------------------------|
| nome         | String | Nome do ponto de táxi.                      |
| latitude     | Float  | Latitude da coordenada do ponto de táxi.    |
| longitude    | Float  | Longitude da coordenada do ponto de táxi.   |
| dataCadastro | String | Data em que o ponto de táxi foi cadastrado. |

**Http Status - 400 Bad Request** - Um dos parâmetros está ausente: latitude, longitude ou raio. A mensagem acessória da resposta indica o campo faltante.
