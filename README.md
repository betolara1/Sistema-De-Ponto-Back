# ⏰ Sistema de Ponto - API Backend

> **status: 🚧 Em Fase de Construção / Desenvolvimento**

Este repositório contém a API REST (Backend) do **Sistema de Ponto** (Time Tracking System). O projeto foi desenvolvido com a arquitetura monólito em mente, utilizando a versão mais recente do ecossistema Spring.

---

## 🛠️ Tecnologias Utilizadas

O projeto é estruturado utilizando as seguintes tecnologias e frameworks:

- **Linguagem:** [Java 21](https://www.oracle.com/java/technologies/downloads/)
- **Framework Principal:** [Spring Boot 3.5.14](https://spring.io/projects/spring-boot)
- **Banco de Dados & Persistência:**
  - [PostgreSQL](https://www.postgresql.org/) (SGBD Relacional utilizado em ambiente local/produção)
  - Spring Data JPA (Hibernate)
  - [Flyway Migration](https://flywaydb.org/) (Para controle de versão e evolução do esquema do banco de dados)
- **Segurança & Autenticação:**
  - Spring Security
  - `JWT-Package` (Biblioteca interna personalizada para gerenciamento e validação de tokens JWT)
- **Cache & Performance:**
  - Spring Cache (Ativado com `@EnableCaching` para otimização de consultas repetitivas)
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

- 📂 **`config/`**: Configurações gerais da aplicação (CORS, Segurança/Spring Security, Swagger/OpenAPI, Cache).
- 📂 **`controller/`**: Controladores REST responsáveis por expor os endpoints HTTP.
- 📂 **`dto/`**: Objetos de Transferência de Dados (Requests e Responses personalizados).
- 📂 **`model/`**: Entidades persistentes mapeadas com JPA (representação das tabelas do banco de dados).
- 📂 **`repository/`**: Interfaces Spring Data JPA para operações de CRUD e consultas personalizadas.
- 📂 **`service/`**: Classes de serviço contendo a regra de negócio da aplicação.
- 📂 **`utils/`**: Utilitários gerais do sistema (ex: paginação e helpers).

---

## ⚙️ Configurações do Sistema (`application.properties`)

As principais propriedades de ambiente da aplicação devem ser configuradas no arquivo [application.properties](file:///c:/Users/Ralf/Desktop/Programação/SistemaPonto/Back/src/main/resources/application.properties):

```properties
# Nome do microsserviço
spring.application.name=Ponto

# Chave secreta de assinatura do JWT (deve ter pelo menos 32 caracteres seguros)
jwt.secret-key=minha_chave_secreta_super_longa_e_segura_32_chars

# Tempo de expiração em milissegundos (Ex: 43200000ms = 12 horas)
jwt.expiration-time=43200000 

# Rotas públicas que não exigem Token de Autenticação
jwt.excluded-paths=/auth/login, /public/**, /swagger-ui/**

# Configurações de Conexão com o Banco de Dados (PostgreSQL)
spring.datasource.url=jdbc:postgresql://localhost:5432/sistema_ponto
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuração do Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true

# Validação automática de esquema pelo Hibernate (sem auto-update para uso com Flyway)
spring.jpa.hibernate.ddl-auto=validate
```

---

## 🚀 Como Executar o Projeto Localmente

### Pré-requisitos

1. **Java SDK 21** ou superior instalado e configurado nas variáveis de ambiente (`JAVA_HOME`).
2. **Maven 3.x** instalado (ou utilize o wrapper `./mvnw` incluso no repositório).
3. Banco de dados PostgreSQL rodando localmente com a base configurada.

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

### 🔐 Autenticação (`/auth`)

| Método | Endpoint | Descrição | Parâmetros de Query / Path |
| :--- | :--- | :--- | :--- |
| **POST** | `/auth/login` | Realiza a autenticação de um colaborador por CPF e senha, retornando um token JWT. | JSON Body (`LoginDTO`) |

### 🏢 Empresas (`/empresa`)

| Método | Endpoint | Descrição | Parâmetros de Query / Path |
| :--- | :--- | :--- | :--- |
| **GET** | `/empresa` | Retorna lista de empresas paginada e ordenada. | `page` (padrão 0), `size` (padrão 10), `sortBy` (padrão `dateCreated`), `direction` (padrão `desc`) |
| **GET** | `/empresa/{id}` | Busca os detalhes de uma empresa específica por ID. | `{id}` (Path variable) |
| **GET** | `/empresa?active={active}` | Lista empresas ativas ou inativas com paginação. | `active` (Boolean), `page`, `size`, `sortBy`, `direction` |
| **GET** | `/empresa?dateCreated={dateCreated}` | Lista empresas pela data de criação. | `dateCreated` (String), `page`, `size`, `sortBy`, `direction` |
| **GET** | `/empresa?dataUpdated={dataUpdated}` | Lista empresas pela data de atualização. | `dataUpdated` (String), `page`, `size`, `sortBy`, `direction` |
| **GET** | `/empresa?nome={nome}` | Busca uma empresa pelo nome exato. | `nome` (String) |
| **GET** | `/empresa?cnpj={cnpj}` | Busca uma empresa pelo CNPJ. | `cnpj` (String) |
| **POST** | `/empresa` | Cadastra uma nova empresa no sistema. *(Rota pública para onboarding/registro)* | JSON Body (`SaveEmpresaRequest`) |
| **PUT** | `/empresa/{id}` | Atualiza os dados de uma empresa existente. | JSON Body (`UpdateEmpresaRequest`), `{id}` (Path variable) |
| **DELETE**| `/empresa/{id}` | Desativa temporariamente (Soft Delete) ou remove uma empresa. | `{id}` (Path variable) |

### 📍 Coordenadas Geográficas (`/coordenadas`)

Permite definir limites e localizações geográficas para as empresas. *Observação: Não expõe endpoint de deleção (DELETE) para preservar o histórico de auditoria.*

| Método | Endpoint | Descrição | Parâmetros de Query / Path |
| :--- | :--- | :--- | :--- |
| **GET** | `/coordenadas` | Retorna lista de coordenadas paginada e ordenada. | `page` (padrão 0), `size` (padrão 10), `sortBy` (padrão `dateCreated`), `direction` (padrão `desc`) |
| **GET** | `/coordenadas/{id}` | Busca as coordenadas geográficas de uma empresa específica. | `{id}` (ID da Empresa, Path variable) |
| **POST** | `/coordenadas` | Vincula e cadastra novas coordenadas para uma empresa. | JSON Body (`SaveCoordenadasRequest`) |
| **PUT** | `/coordenadas/{id}` | Atualiza as coordenadas geográficas existentes por ID da coordenada. | JSON Body (`UpdateCoordenadasRequest`), `{id}` (ID da Coordenada, Path variable) |

### 👥 Colaboradores (`/colaboradores`)

Permite o gerenciamento de funcionários (colaboradores), suas respectivas associações a empresas e credenciais de login.

| Método | Endpoint | Descrição | Parâmetros de Query / Path |
| :--- | :--- | :--- | :--- |
| **GET** | `/colaboradores` | Retorna lista de colaboradores paginada e ordenada. | `page` (padrão 0), `size` (padrão 10), `sortBy` (padrão `dateCreated`), `direction` (padrão `desc`) |
| **GET** | `/colaboradores/{id}` | Busca os detalhes de um colaborador específico por ID. | `{id}` (Path variable) |
| **GET** | `/colaboradores?nome={nome}` | Busca um colaborador por parte do nome (case-insensitive). | `nome` (String) |
| **GET** | `/colaboradores?cpf={cpf}` | Busca um colaborador pelo CPF exato. | `cpf` (String) |
| **GET** | `/colaboradores?empresaId={empresaId}` | Busca colaborador vinculado a um ID de empresa. | `empresaId` (Long) |
| **GET** | `/colaboradores?active={active}` | Lista colaboradores ativos ou inativos de forma paginada. | `active` (Boolean), `page`, `size`, `sortBy`, `direction` |
| **GET** | `/colaboradores?dateCreated={dateCreated}` | Lista colaboradores pela data de criação. | `dateCreated` (String), `page`, `size`, `sortBy`, `direction` |
| **GET** | `/colaboradores?dataUpdated={dataUpdated}` | Lista colaboradores pela data de atualização. | `dataUpdated` (String), `page`, `size`, `sortBy`, `direction` |
| **POST** | `/colaboradores` | Cadastra um novo colaborador no sistema. *(Rota pública para registro)* | JSON Body (`SaveColaboradoresRequest`) |
| **PUT** | `/colaboradores/{id}` | Atualiza os dados cadastrais de um colaborador existente. | JSON Body (`UpdateColaboradoresRequest`), `{id}` (Path variable) |
| **DELETE**| `/colaboradores/{id}` | Desativa temporariamente um colaborador (Soft Delete). | `{id}` (Path variable) |

### ⏰ Registro de Ponto (`/ponto`)

Gerenciamento de registros de entradas, saídas, intervalos e correções de marcações de ponto.

| Método | Endpoint | Descrição | Parâmetros de Query / Path |
| :--- | :--- | :--- | :--- |
| **GET** | `/ponto` | Retorna lista de registros de ponto paginada e ordenada. | `page` (padrão 0), `size` (padrão 10), `sortBy` (padrão `ponto`), `direction` (padrão `desc`) |
| **GET** | `/ponto/{id}` | Busca registro de ponto específico por ID. | `{id}` (Path variable) |
| **GET** | `/ponto?colaboradorId={colaboradorId}` | Busca registros de ponto por ID do colaborador. | `colaboradorId` (Long) |
| **GET** | `/ponto?colaboradorUpdater={colaboradorUpdater}` | Busca registros de ponto por ID do colaborador que realizou a última atualização/correção. | `colaboradorUpdater` (Long) |
| **GET** | `/ponto?ponto={dateString}` | Lista registros de ponto pela data de criação. | `dateString` (String), `page`, `size`, `sortBy`, `direction` |
| **POST** | `/ponto` | Cadastra um novo registro de ponto. | JSON Body (`SavePontoRequest`) |
| **PUT** | `/ponto/{id}` | Atualiza/corrige um registro de ponto por ID. | JSON Body (`UpdatePontoRequest`), `{id}` (Path variable) |

### 📸 Fotos dos Colaboradores (`/fotos`)

Gerenciamento de imagens de perfil ou fotos de validação biométrica dos colaboradores.

| Método | Endpoint | Descrição | Parâmetros de Query / Path |
| :--- | :--- | :--- | :--- |
| **GET** | `/fotos` | Retorna lista de fotos paginada e ordenada. | `page` (padrão 0), `size` (padrão 10), `sortBy` (padrão `dateCreated`), `direction` (padrão `desc`) |
| **GET** | `/fotos/{id}` | Busca a foto por ID. | `{id}` (Path variable) |
| **GET** | `/fotos?colaboradorId={colaboradorId}` | Busca a foto vinculada a um colaborador específico por ID. | `colaboradorId` (Long) |
| **POST** | `/fotos` | Cadastra/associa uma nova foto de colaborador. | JSON Body (`SaveFotosRequest`) |
| **PUT** | `/fotos/{id}` | Atualiza uma foto de colaborador existente por ID. | JSON Body (`UpdateFotosRequest`), `{id}` (Path variable) |

---

## 🔌 Como Consumir a API (Guia de Integração)

Esta seção descreve como integrar e consumir os endpoints da API do **Sistema de Ponto** de forma externa.

### 1. Autenticação e Token JWT
A maioria dos endpoints da API é protegida e exige autenticação por meio de um token JWT. Apenas as rotas de login (`/auth/login`) e os cadastros iniciais (`POST /empresa` e `POST /colaboradores`) são públicos.

Para obter o token, envie uma requisição `POST` para `/auth/login` com o CPF e Senha:

- **Endpoint:** `POST` `/auth/login`
- **Cabeçalhos:** `Content-Type: application/json`
- **Corpo da Requisição (JSON):**
  ```json
  {
    "cpf": "12345678900",
    "password": "sua_senha_segura"
  }
  ```
- **Resposta de Sucesso (200 OK):**
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  ```

---

### 2. Enviando Requisições para Rotas Protegidas
Com o token em mãos, inclua-o no cabeçalho **`Authorization`** no formato **`Bearer <token>`** para todas as outras requisições (como registrar ponto, consultar colaboradores, etc.).

- **Exemplo de Cabeçalho:**
  ```http
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
  ```

---

### 3. Exemplos Práticos de Consumo

#### Exemplo com `cURL` (Linha de Comando)

1. **Obter Token de Autenticação:**
   ```bash
   curl -X POST http://localhost:8080/auth/login \
        -H "Content-Type: application/json" \
        -d '{"cpf": "12345678900", "password": "sua_senha_segura"}'
   ```

2. **Registrar um Ponto (Rota Protegida):**
   ```bash
   curl -X POST http://localhost:8080/ponto \
        -H "Content-Type: application/json" \
        -H "Authorization: Bearer <INSIRA_O_TOKEN_AQUI>" \
        -d '{"colaboradorId": 1}'
   ```

#### Exemplo com JavaScript (Fetch API)

```javascript
const API_URL = 'http://localhost:8080';

// 1. Obter o Token JWT
async function login(cpf, password) {
  const response = await fetch(`${API_URL}/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ cpf, password })
  });

  if (!response.ok) {
    throw new Error('Falha na autenticação');
  }

  const data = await response.json();
  return data.token; // Token JWT
}

// 2. Registrar Ponto usando o Token
async function registrarPonto(token, colaboradorId) {
  const response = await fetch(`${API_URL}/ponto`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({ colaboradorId })
  });

  if (!response.ok) {
    const errorMsg = await response.text();
    throw new Error(`Erro ao registrar ponto: ${errorMsg}`);
  }

  const result = await response.json();
  console.log('Ponto registrado com sucesso:', result);
}

// Fluxo completo
(async () => {
  try {
    const token = await login('12345678900', 'senha123');
    await registrarPonto(token, 1);
  } catch (error) {
    console.error('Erro:', error.message);
  }
})();
```

---

## 🧪 Execução de Testes

O projeto conta com testes unitários e de integração abrangentes para as camadas de Service e Controller dos recursos `Empresa`, `Coordenadas`, `Colaboradores`, `Ponto` e `Fotos`.

Para rodar todos os testes automatizados da aplicação:

```bash
./mvnw test
# No Windows (PowerShell/CMD):
.\mvnw.cmd test
```

---

## 📋 Próximos Passos (Roadmap de Desenvolvimento)

- [x] Criar scripts iniciais de migração de banco de dados (`V1__criar_tabelas.sql`) em `src/main/resources/db/migration`.
- [x] Implementar a entidade de `Colaborador` (Funcionários) e seus respectivos endpoints e testes.
- [x] Implementar a lógica de registro de ponto (entradas, saídas, intervalos, correções).
- [x] Configurar conexão dedicada a banco de dados local/produção (PostgreSQL).
- [x] Implementar a entidade de `Fotos` dos colaboradores e seus respectivos endpoints e testes.
- [x] Integrar Cache (`Spring Cache` com `@EnableCaching`).
- [ ] Conectar ao Eureka Server de registro de microsserviços.
