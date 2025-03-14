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

## Instruções de Execução

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

