# 🎮 Guilda Social

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen)
![Angular](https://img.shields.io/badge/Angular-19.0.5-red)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![PWA](https://img.shields.io/badge/PWA-Enabled-purple)
![Docker](https://img.shields.io/badge/Docker-Enabled-blue)

**Guilda Social** é uma plataforma de matchmaking para conectar jogadores de jogos presenciais (tabuleiro, RPG, card games) e online. O aplicativo utiliza um algoritmo de compatibilidade para sugerir parceiros de jogo com interesses semelhantes, proximidade geográfica e disponibilidade compatível.

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura](#-arquitetura)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Execução](#-instalação-e-execução)
- [Docker](#-docker)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [API Endpoints](#-api-endpoints)
- [Banco de Dados](#-banco-de-dados)
- [PWA (Progressive Web App)](#-pwa-progressive-web-app)
- [Sprints do Projeto](#-sprints-do-projeto)
- [Contribuidores](#-contribuidores)
- [Licença](#-licença)

---

## 🎯 Sobre o Projeto

O **Guilda Social** nasceu da necessidade de facilitar a conexão entre jogadores que buscam parceiros para suas aventuras. Diferente dos jogos online que possuem sistemas nativos de pareamento, os jogos presenciais e muitos jogos online ainda dependem de grupos dispersos em redes sociais ou fóruns.

**Propósito:** Reduzir a dificuldade de encontrar parceiros de jogo compatíveis em termos de localização, disponibilidade e interesses, promovendo a interação social em ambientes físicos e digitais.

**Público-alvo:** Jogadores casuais e competitivos, desde iniciantes até experientes, que valorizam a interação social e querem conhecer novas pessoas com interesses em comum.

---

## ✨ Funcionalidades

### ✅ Implementadas
- [x] Cadastro de usuário com foto de perfil
- [x] Login com autenticação JWT
- [x] Recuperação de senha por e-mail
- [x] Geolocalização automática
- [x] Edição de perfil completo (dados, foto, senha, raio de busca)
- [x] Algoritmo de matchmaking por afinidade e distância
- [x] Cards de sugestões estilo Tinder
- [x] Chat em tempo real (polling)
- [x] Lista de conversas com status (aguardando resposta / conversa ativa)
- [x] Notificações visuais de novas mensagens
- [x] PWA para instalação como aplicativo

---

## 🛠️ Tecnologias Utilizadas

### Backend
| Tecnologia | Versão | Finalidade |
|------------|--------|------------|
| Java | 21 LTS | Linguagem principal |
| Spring Boot | 3.4.0 | Framework principal |
| Spring Data JPA | 3.4.0 | Persistência de dados |
| PostgreSQL | 16 | Banco de dados relacional |
| JWT | 0.11.5 | Tokens de autenticação |
| Maven | 3.9+ | Gerenciador de dependências |
| Lombok | 1.18.36 | Redução de boilerplate |
| ModelMapper | 3.2.0 | Mapeamento de objetos |
| OpenAPI/Swagger | 2.6.0 | Documentação da API |

### Frontend
| Tecnologia | Versão | Finalidade |
|------------|--------|------------|
| Angular | 19.0.5 | Framework principal |
| TypeScript | 5.5.4 | Linguagem |
| RxJS | 7.8.1 | Programação reativa |
| PWA | - | Progressive Web App |
| CSS3 | - | Estilização |

### DevOps & Ferramentas
| Ferramenta | Finalidade |
|------------|------------|
| Docker | Containerização |
| Docker Compose | Orquestração de containers |
| Git | Controle de versão |
| GitHub | Repositório remoto |

---

## 🏗️ Arquitetura

┌─────────────────────────────────────────────────────────────┐
│ Frontend (Angular 19 + PWA) │
│ ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐ │
│ │ Login │ │Cadastro │ │Sugestões│ │ Perfil │ │
│ └────┬────┘ └────┬────┘ └────┬────┘ └────┬────┘ │
│ └────────────┴────────────┴────────────┘ │
│ │ │
│ HTTP Requests (JWT) │
└─────────────────────────┼───────────────────────────────────┘
│
▼
┌─────────────────────────────────────────────────────────────┐
│ Backend (Spring Boot) │
│ ┌─────────────────────────────────────────────────────┐ │
│ │ Security Layer (JWT Filter) │ │
│ └─────────────────────────┬───────────────────────────┘ │
│ │ │
│ ┌─────────────┐ ┌─────────┴─────────┐ ┌─────────────┐ │
│ │ Controllers │◄┤ Services │►│ Repositories│ │
│ └─────────────┘ └─────────┬─────────┘ └─────────────┘ │
│ │ │
│ ┌─────────────────────────┴───────────────────────────┐ │
│ │ JPA / Hibernate │ │
│ └─────────────────────────┬───────────────────────────┘ │
└────────────────────────────┼────────────────────────────────┘
│
▼
┌─────────────────────────────────────────────────────────────┐
│ PostgreSQL Database │
│ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ │
│ │ usuarios │ │ matches │ │ mensagens│ │ jogos │ │
│ └──────────┘ └──────────┘ └──────────┘ └──────────┘ │
└─────────────────────────────────────────────────────────────┘

---

## 📋 Pré-requisitos

### Obrigatórios
- **Java 21** ou superior
- **Node.js 20+** e **npm**
- **PostgreSQL 16** (local ou Docker)
- **Git**

### Opcionais (para desenvolvimento)
- **Docker** e **Docker Compose**
- **Angular CLI** (`npm install -g @angular/cli`)

---

## 🚀 Instalação e Execução

### Clone o repositório

```bash
git clone https://github.com/dntfindmel/guilda-social.git
cd guilda-social
```

## Estrutura do Projeto
guilda-social/
├── backend/                         # Spring Boot Backend
│   ├── src/main/java/com/guildasocial/
│   │   ├── api/                     # Controllers, DTOs, Exceptions
│   │   ├── config/                  # Configurações (CORS, Security, etc.)
│   │   ├── domain/                  # Models, Repositories, Services
│   │   └── security/                # JWT e autenticação
│   └── src/main/resources/
│       ├── application.properties
│       ├── application-docker.properties
│       └── db/migration/            # Flyway migrations
├── frontend/                        # Angular Frontend
│   ├── src/app/
│   │   ├── core/                    # Services, Guards, Interceptors
│   │   ├── features/                # Telas (login, cadastro, sugestões, chat, perfil)
│   │   ├── layout/                  # Header, Bottom Nav
│   │   └── shared/                  # Componentes compartilhados
│   ├── src/assets/                  # Imagens, ícones
│   └── src/manifest.webmanifest     # PWA manifest
├── database/                        # Scripts SQL
│   └── init/
│       ├── 01-schema.sql
│       └── 02-insert-jogos.sql
├── docker-compose.yml
└── README.md

### 📡 API Endpoints
Aqui está a sua lista de endpoints organizada em uma única tabela Markdown, separada por categorias para facilitar a leitura:

| Categoria | Método | Endpoint | Descrição |
| --- | --- | --- | --- |
| **Autenticação** | `POST` | `/api/auth/login` | Autenticar usuário |
|  | `POST` | `/api/auth/recuperar-senha` | Solicitar recuperação de senha |
|  | `POST` | `/api/auth/redefinir-senha` | Redefinir senha com token |
| **Usuários** | `POST` | `/api/usuarios` | Criar novo usuário |
|  | `GET` | `/api/usuarios/{id}` | Buscar usuário por ID |
|  | `GET` | `/api/usuarios` | Listar todos usuários |
|  | `PUT` | `/api/usuarios/{id}` | Atualizar usuário |
|  | `DELETE` | `/api/usuarios/{id}` | Desativar usuário |
| **Perfil** | `GET` | `/api/perfil/{usuarioId}` | Buscar perfil completo |
|  | `PUT` | `/api/perfil/{usuarioId}` | Atualizar perfil |
| **Upload** | `POST` | `/api/upload/foto/{usuarioId}` | Upload de foto de perfil |
|  | `DELETE` | `/api/upload/foto/{usuarioId}` | Remover foto de perfil |
| **Matches e Chat** | `GET` | `/api/matches/sugestoes/{usuarioId}` | Obter sugestões de jogadores |
|  | `POST` | `/api/matches/solicitar/{usuarioId}` | Enviar solicitação de match |
|  | `GET` | `/api/chat/matches/{usuarioId}` | Listar conversas do usuário |
|  | `GET` | `/api/chat/mensagens/{matchId}` | Buscar mensagens de um match |
|  | `POST` | `/api/chat/mensagens/{matchId}` | Enviar mensagem |

### 📱 PWA (Progressive Web App)
O Guilda Social é um PWA, o que significa que pode ser instalado como um aplicativo nativo no celular ou computador.

Funcionalidades PWA
✅ Instalação como aplicativo

✅ Modo offline básico

✅ Service Worker para cache

✅ Manifest para personalização

✅ Tela de splash

### 📅 Sprints do Projeto
Sprint	Período	Status	Entregas
Sprint 1	Semana 1-2	✅ Concluída	Cadastro, Perfil, Interesses
Sprint 2	Semana 3-4	✅ Concluída	Login, Autenticação JWT, Guards
Sprint 3	Semana 5-6	✅ Concluída	Matchmaking, Sugestões, Cards
Sprint 4	Semana 7-8	✅ Concluída	Perfil, Edição, Configurações
Sprint 5	Semana 9-10	✅ Concluída	Chat, Grupos, Mensagens

### 👥 Contribuidores
Melyssa Vitória da Silva Moya	Desenvolvedora Full Stack	@dntfindmel

### 📄 Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

Desenvolvido com 💛 para conectar jogadores 🎮
