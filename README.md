# Cadastro de Chaves PIX


[![Java Version](https://img.shields.io/badge/Java-21-blue.svg)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)](https://img.shields.io/)
[![Code Coverage](https://img.shields.io/badge/Code%20Coverage-90%25-yellow.svg)](https://img.shields.io/)


## Descrição

Sistema para cadastro, alteração, deleção e consulta de chaves PIX, permitindo a vinculação das chaves às contas dos correntistas do Itaú.

## Funcionalidades

- **Inclusão de Chaves**: Cadastro de chaves PIX vinculadas à agência e conta do correntista.
- **Alteração de Chaves**: Modificação de dados de chaves registradas.
- **Inativação de Chaves**: Desativação de chaves, impedindo alterações ou consultas futuras.
- **Consultas**: Diversas opções de consulta de chaves, incluindo por ID, tipo de chave, agência e conta, nome do correntista, data de inclusão e data de inativação.

## Tecnologias Utilizadas

- **Linguagem**: Java 21
- **Framework**: Spring Boot 3.4.3
- **Banco de Dados**: H2 (em memória) para desenvolvimento e testes
- **Dependências**:
    - `spring-boot-starter-web`: Para construção de aplicações web.
    - `springdoc-openapi-starter-webmvc-ui`: Geração automática de documentação OpenAPI.
    - `spring-boot-starter-actuator`: Monitoramento e gestão da aplicação.
    - `spring-boot-starter-data-jpa`: Acesso a dados com JPA.
    - `spring-boot-starter-validation`: Validação de dados.
    - `spring-boot-devtools`: Ferramentas de desenvolvimento.
    - `h2`: Banco de dados em memória para testes.
    - `lombok`: Redução de boilerplate no código.
    - `spring-boot-starter-test`: Frameworks de teste.
    - `mockito-core`: Framework de mocking para testes.

## Design Patterns Utilizados

- **Factory Pattern**: Utilizado para encapsular a criação de objetos DTOs e entidades, garantindo maior flexibilidade e organização no código. A classe `PixChaveFactory` centraliza a lógica de conversão entre entidades e DTOs, facilitando a manutenção e evitando a duplicação de código.

## Metodologia 12-Factor App

Este projeto segue diversas práticas da metodologia **12-Factor App**, garantindo escalabilidade, manutenibilidade e boas práticas para desenvolvimento de aplicações modernas. Abaixo, os fatores aplicados:

1. **Código Base** (*Codebase*)
    - O projeto está versionado no Git, garantindo controle de versão e colaboração eficiente.

2. **Dependências** (*Dependencies*)
    - Gerenciamento de dependências via **Maven**, garantindo que todas as bibliotecas necessárias sejam definidas explicitamente no `pom.xml`.

3. **Configurações** (*Config*)
    - Configurações externas ao código-fonte, utilizando **variáveis de ambiente** sempre que possível.

4. **Backing Services** (*Backing Services*)
    - O banco de dados **H2** é utilizado em memória para desenvolvimento e testes, permitindo rápida inicialização sem necessidade de configurações adicionais.

5. **Construção, Release e Execução** (*Build, Release, Run*)
    - Processo de **build automatizado** com Maven, garantindo consistência entre os ambientes.

6. **Paridade entre Ambientes** (*Dev/Prod Parity*)
    - O projeto pode ser executado de forma similar em **ambiente de desenvolvimento e produção**, utilizando **Spring Profiles** para configurar propriedades distintas.

7. **Gerenciamento de Processos** (*Processes*)
    - O projeto segue a abordagem de processos stateless, permitindo a escalabilidade horizontal e evitando a dependência de estado interno em memória.

8. **Logs** (*Logs*)
    - Logs estruturados e gerenciáveis, utilizando **Spring Boot Logging**, facilitando a rastreabilidade e debugging.

9. **Administração de Portas** (*Port Binding*)
    - A aplicação expõe um serviço HTTP acessível via **porta 8080**, que pode ser configurada através de variáveis de ambiente.

10. **Escalabilidade** (*Disposability*)
    - O projeto pode ser facilmente reiniciado ou escalado, seguindo a filosofia de processos efêmeros.

Esses fatores garantem que a aplicação seja **portável, escalável e resiliente**, seguindo boas práticas de desenvolvimento moderno. 🚀

## Instruções de Execução

## Pré-requisitos

Para rodar o projeto, é necessário ter as seguintes ferramentas instaladas:

- **Java 21** ([Download](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) ou [SDKMAN!](https://sdkman.io/))
- **Maven** ([Download](https://maven.apache.org/download.cgi))

⚠️ **Atenção**: O **Maven** deve estar corretamente configurado no ambiente local, incluindo a variável de ambiente `MAVEN_HOME` e o caminho do `mvn` adicionado ao `PATH`.

Para verificar se o **Maven** está instalado corretamente, execute o seguinte comando no terminal:

    ```sh
    mvn -version
    
    1. **Clonar o Repositório**:
    
       ```bash
       git clone https://github.com/cesar-augusto-alves-barbosa/apichavepix.git
       cd apichavepix
    
    2. **Compilar e Empacotar o Projeto**:
        ```bash
        mvn clean package -DskipTests
    
    3. **Executar a Aplicação**:
    
        ```bash
        mvn spring-boot:run



## 💡 **Sugestões de Melhorias**
### **1️⃣ Alterar o Status Code para `201 Created` na criação de chaves PIX**
Atualmente, o cadastro retorna um **200 OK**, mas o status mais adequado para operações de criação de recursos REST é **201 Created**, indicando que um novo recurso foi gerado com sucesso.

### **2️⃣ Usar `409 Conflict` para chaves já registradas**
Quando uma chave PIX já existe, o código de resposta atual é `422 Unprocessable Entity`, mas um código mais apropriado seria **409 Conflict**, pois a chave já está cadastrada.

### **3️⃣ Retornar `400 Bad Request` para erros de formatação de dados**
Alguns erros relacionados a formato de dados (UUID inválido, tipos errados no body da requisição) retornam `422 Unprocessable Entity`, mas o código mais adequado nesses casos é **400 Bad Request**, indicando erro na requisição.

### **4️⃣ Melhorar o relacionamento entre entidades**
Atualmente, há apenas uma entidade principal **PixChave**, mas poderia haver uma separação mais clara entre **Conta** e **Chave PIX**, onde uma conta bancaria poderia ter um relacionamento 1:N com chaves PIX. Isso facilitaria futuras expansões e melhoraria a integridade dos dados.