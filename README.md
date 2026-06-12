# ⏰ Sistema de Ponto - API Backend

> **status: 🚧 Em Fase de Construção / Desenvolvimento**

Este repositório contém a API REST (Backend) do **Sistema de Ponto** (Time Tracking System). O projeto foi desenvolvido com a arquitetura monólito em mente, utilizando a versão mais recente do ecossistema Spring.

---

## 🛠️ Tecnologias Utilizadas

O projeto é estruturado utilizando as seguintes tecnologias e frameworks:

- **Linguagem:** [Java 21](https://www.oracle.com/java/technologies/downloads/)
- **Framework Principal:** [Spring Boot 3.5.14](https://spring.io/projects/spring-boot)
- **Banco de Dados & Persistência:**
  - Spring Data JPA (Hibernate)
  - [Flyway Migration](https://flywaydb.org/) (Para controle de versão e evolução do esquema do banco de dados)
- **Segurança & Autenticação:**
  - Spring Security
  - `JWT-Package` (Biblioteca interna personalizada para gerenciamento e validação de tokens JWT)
- **Arquitetura & Nuvem:**
  - Spring Cloud (Eureka Discovery Client/Server para registro de serviços)
  - Resilience4j (Circuit Breaker para resiliência e tolerância a falhas)
- **Documentação de API:**
  - [Springdoc OpenAPI UI / Swagger](https://springdoc.org/) (Versão `2.8.16`)
- **Produtividade:**
  - Lombok (Redução de código boilerplate)
  - Maven (Gerenciador de dependências e build)

---

## 📦 Estrutura de Pacotes do Projeto

O código-fonte principal está organizado dentro de `src/main/java/betolara1/Ponto`:

- 📂 **`config/`**: Configurações gerais da aplicação (CORS, Segurança/Spring Security, Swagger/OpenAPI).
- 📂 **`controller/`**: Controladores REST responsáveis por expor os endpoints HTTP.
- 📂 **`dto/`**: Objetos de Transferência de Dados (Requests e Responses personalizados).
- 📂 **`model/`**: Entidades persistentes mapeadas com JPA (representação das tabelas do banco).
- 📂 **`repository/`**: Interfaces Spring Data JPA para operações de CRUD e consultas personalizadas.
- 📂 **`service/`**: Classes de serviço contendo a regra de negócio da aplicação.
- 📂 **`utils/`**: Utilitários gerais do sistema (ex: paginação e helpers).

---

## 🔐 Configuração de Segurança (JWT)

A autenticação é baseada em tokens JWT. As principais propriedades devem ser configuradas no arquivo [application.properties](file:///c:/Users/Ralf/Desktop/Programa%C3%A7%C3%A3o/SistemaPonto/Back/src/main/resources/application.properties):

```properties
# Nome do microsserviço
spring.application.name=Ponto

# Chave secreta de assinatura do JWT (deve ter pelo menos 32 caracteres seguros)
jwt.secret-key=minha_chave_secreta_super_longa_e_segura_32_chars

# Tempo de expiração em milissegundos (Ex: 43200000ms = 12 horas)
jwt.expiration-time=43200000 

# Rotas públicas que não exigem Token de Autenticação
jwt.excluded-paths=/auth/login, /public/**, /swagger-ui/**
```

---

## 🚀 Como Executar o Projeto Localmente

### Pré-requisitos

1. **Java SDK 21** ou superior instalado e configurado nas variáveis de ambiente (`JAVA_HOME`).
2. **Maven 3.x** instalado (ou utilize o wrapper `./mvnw` incluso no repositório).
3. Banco de dados configurado (conforme necessário nas variáveis de ambiente ou arquivo `application.properties`).

### Passo a Passo

1. **Clonar o repositório:**
   ```bash
   git clone https://github.com/betolara1/Sistema-De-Ponto.git
   cd Sistema-De-Ponto/Back
   ```

2. **Compilar e baixar dependências:**
   ```bash
   ./mvnw clean install
   # No Windows (PowerShell/CMD):
   .\mvnw.cmd clean install
   ```

3. **Iniciar a aplicação:**
   ```bash
   ./mvnw spring-boot:run
   # No Windows (PowerShell/CMD):
   .\mvnw.cmd spring-boot:run
   ```

A API estará disponível por padrão na porta definida (normalmente `8080` caso não especificado de outra forma).

---

## 📖 Documentação da API (Swagger UI)

Com o servidor rodando, você pode visualizar e interagir com todos os endpoints disponíveis acessando a interface do Swagger UI:

- **URL:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) *(substitua a porta se alterada)*

---

## 📌 Endpoints Atuais (Recursos Implementados)

Os seguintes recursos e seus respectivos endpoints estão disponíveis:

### 🏢 Empresas (`/empresa`)

| Método | Endpoint | Descrição | Parâmetros de Query / Path |
| :--- | :--- | :--- | :--- |
| **GET** | `/empresa` | Retorna lista de empresas paginada e ordenada. | `page` (padrão 0), `size` (padrão 10), `sortBy` (padrão `dateCreated`), `direction` (padrão `desc`) |
| **GET** | `/empresa/{id}` | Busca os detalhes de uma empresa específica por ID. | `{id}` (Path variable) |
| **GET** | `/empresa?active={isActive}` | Lista empresas ativas ou inativas com paginação. | `isActive` (Boolean), `page`, `size`, `sortBy`, `direction` |
| **GET** | `/empresa?dateCreated={dateString}` | Lista empresas pela data de criação. | `dateString` (String), `page`, `size`, `sortBy`, `direction` |
| **GET** | `/empresa?dataUpdated={dateString}` | Lista empresas pela data de atualização. | `dateString` (String), `page`, `size`, `sortBy`, `direction` |
| **GET** | `/empresa?nome={nome}` | Busca uma empresa pelo nome exato. | `nome` (String) |
| **GET** | `/empresa?cnpj={cnpj}` | Busca uma empresa pelo CNPJ. | `cnpj` (String) |
| **POST** | `/empresa` | Cadastra uma nova empresa no sistema. | JSON Body (`SaveEmpresaRequest`) |
| **PUT** | `/empresa/{id}` | Atualiza os dados de uma empresa existente. | JSON Body (`UpdateEmpresaRequest`), `{id}` |
| **DELETE**| `/empresa/{id}` | Desativa temporariamente (Soft Delete) ou remove uma empresa. | `{id}` |

### 📍 Coordenadas Geográficas (`/coordenadas`)

Permite definir limites e localizações geográficas para as empresas. *Observação: Não expõe endpoint de deleção (DELETE) para preservar o histórico de auditoria.*

| Método | Endpoint | Descrição | Parâmetros de Query / Path |
| :--- | :--- | :--- | :--- |
| **GET** | `/coordenadas` | Retorna lista de coordenadas paginada e ordenada. | `page` (padrão 0), `size` (padrão 10), `sortBy` (padrão `dateCreated`), `direction` (padrão `desc`) |
| **GET** | `/coordenadas/{id}` | Busca as coordenadas geográficas de uma empresa específica. | `{id}` (ID da Empresa, Path variable) |
| **POST** | `/coordenadas` | Vincula e cadastra novas coordenadas para uma empresa. | JSON Body (`SaveCoordenadasRequest`) |
| **PUT** | `/coordenadas/{id}` | Atualiza as coordenadas geográficas existentes por ID da coordenada. | JSON Body (`UpdateCoordenadasRequest`), `{id}` (ID da Coordenada, Path variable) |

### 👥 Colaboradores (`/colaboradores`)

Permite o gerenciamento de funcionários (colaboradores) e suas respectivas associações a empresas do sistema.

| Método | Endpoint | Descrição | Parâmetros de Query / Path |
| :--- | :--- | :--- | :--- |
| **GET** | `/colaboradores` | Retorna lista de colaboradores paginada e ordenada. | `page` (padrão 0), `size` (padrão 10), `sortBy` (padrão `dateCreated`), `direction` (padrão `desc`) |
| **GET** | `/colaboradores/{id}` | Busca os detalhes de um colaborador específico por ID. | `{id}` (Path variable) |
| **GET** | `/colaboradores?nome={nome}` | Busca um colaborador por parte do nome (case-insensitive). | `nome` (String) |
| **GET** | `/colaboradores?cpf={cpf}` | Busca um colaborador pelo CPF exato. | `cpf` (String) |
| **GET** | `/colaboradores?empresaId={empresaId}` | Busca colaborador vinculado a um ID de empresa. | `empresaId` (Long) |
| **GET** | `/colaboradores?active={isActive}&isActive={isActive}` | Lista colaboradores ativos ou inativos de forma paginada. | `isActive` (Boolean), `page`, `size`, `sortBy`, `direction` |
| **GET** | `/colaboradores?dateCreated={dateString}` | Lista colaboradores pela data de criação. | `dateString` (String), `page`, `size`, `sortBy`, `direction` |
| **GET** | `/colaboradores?dataUpdated={dateString}` | Lista colaboradores pela data de atualização. | `dateString` (String), `page`, `size`, `sortBy`, `direction` |
| **POST** | `/colaboradores` | Cadastra um novo colaborador no sistema. | JSON Body (`SaveColaboradoresRequest`) |
| **PUT** | `/colaboradores?id={id}` | Atualiza os dados cadastrais de um colaborador existente. | JSON Body (`UpdateColaboradoresRequest`), `id` (Query param) |
| **DELETE**| `/colaboradores/{id}` | Desativa temporariamente um colaborador (Soft Delete). | `{id}` (Path variable) |

---

## 🧪 Execução de Testes

O projeto conta com testes unitários e de integração abrangentes para as camadas de Service e Controller dos recursos `Empresa`, `Coordenadas` e `Colaboradores`.

Para rodar todos os testes automatizados da aplicação:

```bash
./mvnw test
# No Windows (PowerShell/CMD):
.\mvnw.cmd test
```

---

## 📋 Próximos Passos (Roadmap de Desenvolvimento)

- [ ] Criar scripts iniciais de migração de banco de dados (`V1__create_tables.sql`) em `src/main/resources/db/migration`.
- [x] Implementar a entidade de `Colaborador` (Funcionários) e seus respectivos endpoints e testes.
- [ ] Implementar a lógica de registro de ponto (entradas, saídas, intervalos).
- [ ] Configurar conexão dedicada a banco de dados em produção (PostgreSQL/MySQL).
- [ ] Conectar ao Eureka Server de registro de microsserviços.

