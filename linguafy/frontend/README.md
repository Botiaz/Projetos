# Linguafy Front-end

Início do front-end do projeto Linguafy usando React + Vite.

## Requisitos

- Node.js 20+ recomendado
- Backend do Linguafy rodando na porta 8080

## Configuração

1. Crie o arquivo `.env` com base em `.env.example`
2. Ajuste a URL da API se necessário:

```env
VITE_API_URL=http://localhost:8080
```

## Comandos

```bash
npm install
npm run dev
```

Importante: não abra `index.html` diretamente no navegador. Sempre inicie pelo servidor de desenvolvimento do Vite.

Para build de produção:

```bash
npm run build
```

## Estrutura inicial

- Página inicial (`/`)
- Página de idiomas (`/idiomas`) consumindo `GET /api/languages`
- Camada simples de serviços em `src/services`
