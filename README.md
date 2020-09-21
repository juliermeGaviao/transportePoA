# TransportePoA

O projeto TransportePoA é uma API REST implementada em Spring Boot para controle de cadastro de linhas de ônibus e pontos de táxi da cidade de Porto Alegre.

Tal framework foi adotao em função de prévia experiência nessa plataforma, simplicidade e bem conhecido no mercado de desenvolvedores Java. O banco de dados usado é o H2, já que o volume de dados é pequeno e pode ser manipulado por um banco de dados relacional em memório RAM.

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

A aplicação estará disponível na porta 8080.

# Requisições

Requisições exemplo da API estão disponíveis no arquivo "TransportePoA.postman_collection.json", o qual pode ser importado e as requisições disparadas
no Postman.

# API Ônibus

## Buscar Linha Pelo Nome

**Caminho**: /onibus/buscarLinhaPeloNome/{nome}

**Verbo HTTP**: GET

| Parâmetro | Tipo   | Local | Obrigatório | Descição                                             |
|-----------|--------|-------|-------------|------------------------------------------------------|
| nome      | String | URL   | Sim         | Texto a ser buscado no nome de cada linha de ônibus. |

**Resultado**: lista de linhas cujo nome contém o texto informado no parâmetro "nome". Cada objeto desta lista contém os seguintes campos:

| Campo       | Tipo    | Descrição                                                                                                     |
|-------------|---------|---------------------------------------------------------------------------------------------------------------|
| id          | Integer | Identificação da linha de ônibus                                                                              |
| codigo      | String  | Código da linha de ônibus. Ex: 250-1                                                                          |
| nome        | String  | Nome da linha de ônibus. Ex: Campus-Ipiranga                                                                  |
| itinerarios | List    | Linha de objetos com os pares de coordenadas geográficas. Tais pares compõem o itinerário da linha de ônibus. |

# API Táxi
