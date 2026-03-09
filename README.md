# Linguafy

Backend da plataforma de estudo de idiomas, com cadastro de usuários, idiomas, categorias e palavras, além de tradução automática via DeepL.

## Escopo do projeto

O projeto fornece uma API REST para:

- Gerenciar usuários.
- Gerenciar idiomas.
- Gerenciar categorias por idioma.
- Gerenciar palavras por categoria.
- Traduzir palavras automaticamente (DeepL).

## Stack

- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- Spring Security (estrutura JWT presente)
- PostgreSQL
- Flyway (migração inicial disponível)
- Swagger / OpenAPI (`springdoc-openapi`)

## Estrutura

Código-fonte principal em:

`linguafy/src/main/java/com/linguafy`

Configurações e scripts em:

- `linguafy/src/main/resources/application.yml`
- `linguafy/src/main/resources/db/migration/V1__initial_schema.sql`
- `linguafy/docker-compose.yml`

## Requisitos

- JDK 17+
- Maven 3.9+ (ou usar `mvnw`)
- Docker (opcional, para subir o PostgreSQL)

## Como executar

### 1) Subir banco de dados

No diretório `linguafy`:

```bash
docker compose up -d
```

Isso sobe um PostgreSQL com:

- host: `localhost`
- porta: `5433`
- database: `linguafy`
- usuário: `linguafy`
- senha: `linguafy`

### 2) Configurar variáveis opcionais

Para habilitar tradução automática com DeepL:

- `DEEPL_API_KEY` (obrigatória para traduzir)
- `DEEPL_BASE_URL` (opcional, padrão: `https://api-free.deepl.com`)
- `DEEPL_TARGET_LANG` (opcional, padrão: `PT-BR`)

### 3) Iniciar aplicação

No diretório `linguafy`:

Windows (PowerShell/cmd):

```bash
.\mvnw spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

API disponível em: `http://localhost:8080`

## Documentação da API

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Endpoints principais

Base URL: `http://localhost:8080`

### Users

- `GET /api/users`
- `GET /api/users/{id}`
- `POST /api/users`
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`

### Languages

- `GET /api/languages`
- `GET /api/languages/{id}`
- `POST /api/languages`
- `PUT /api/languages/{id}`
- `DELETE /api/languages/{id}`

### Categories

- `GET /api/categories`
- `GET /api/categories/{id}`
- `POST /api/categories`
- `PUT /api/categories/{id}`
- `DELETE /api/categories/{id}`

### Words

- `GET /api/words`
- `GET /api/words/{id}`
- `POST /api/words`
- `PUT /api/words/{id}`
- `DELETE /api/words/{id}`
- `POST /api/words/translate`

## Exemplos de payload

### Criar idioma

`POST /api/languages`

```json
{
	"name": "English",
	"code": "EN"
}
```

### Criar categoria

`POST /api/categories`

```json
{
	"name": "Saudações",
	"languageId": 1
}
```

### Criar palavra

`POST /api/words`

```json
{
	"word": "hello",
	"translation": "olá",
	"pronunciation": "həˈloʊ",
	"audioUrl": "https://example.com/audio/hello.mp3",
	"categoryId": 1
}
```

### Traduzir palavra (DeepL)

`POST /api/words/translate`

```json
{
	"word": "good morning",
	"sourceLanguageCode": "EN"
}
```

## Observações

- A configuração atual permite acesso aos endpoints em `/api/**` sem autenticação obrigatória.
- O projeto possui estrutura de segurança JWT preparada para evolução.
- `flyway.enabled` está `false` no `application.yml`; se desejar usar migrações automáticas, altere para `true`.
