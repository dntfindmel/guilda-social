# 🎮 Guilda Social

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen)
![Angular](https://img.shields.io/badge/Angular-19.0.5-red)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![PWA](https://img.shields.io/badge/PWA-Enabled-purple)

**Guilda Social** é uma plataforma de matchmaking para conectar jogadores de jogos presenciais (tabuleiro, RPG, card games) e online. O aplicativo utiliza um algoritmo de compatibilidade para sugerir parceiros de jogo com interesses semelhantes, proximidade geográfica e disponibilidade compatível.

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura](#-arquitetura)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Execução](#-instalação-e-execução)
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

### Sprint 1 - Cadastro e Perfil ✅
- [x] Cadastro de usuário com dados pessoais
- [x] Upload de foto de perfil
- [x] Definição de interesses (jogos de tabuleiro, cartas, RPG)
- [x] Geolocalização automática
- [x] Validação de idade (mínimo 13 anos)

### Sprint 2 - Autenticação ✅
- [x] Login com e-mail e senha
- [x] Autenticação JWT (JSON Web Token)
- [x] Proteção de rotas com Guards
- [x] Interceptor para envio de token
- [x] Logout

### Sprint 3 - Matchmaking e Sugestões ✅
- [x] Algoritmo de compatibilidade
- [x] Cards de sugestões estilo Tinder
- [x] Perfil detalhado do jogador
- [x] Envio de solicitações de conexão
- [x] Seção "Online Recentemente"

### Sprint 4 - Perfil e Edição (Em desenvolvimento)
- [ ] Visualização do próprio perfil
- [ ] Edição de informações pessoais
- [ ] Gerenciamento de interesses
- [ ] Configurações de privacidade

### Sprint 5 - Grupos e Chat (Em desenvolvimento)
- [ ] Criação de grupos
- [ ] Convites para grupos
- [ ] Chat em tempo real
- [ ] Sistema de eventos

### Sprint 6 - Notificações e PWA (Em desenvolvimento)
- [ ] Notificações push
- [ ] Modo offline
- [ ] Instalação como aplicativo
- [ ] Sincronização em segundo plano

---

## 🛠️ Tecnologias Utilizadas

### Backend
| Tecnologia | Versão | Finalidade |
|------------|--------|------------|
| Java | 21 LTS | Linguagem principal |
| Spring Boot | 3.4.0 | Framework principal |
| Spring Security | 3.4.0 | Autenticação e autorização |
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
| TypeScript | 5.4.5 | Linguagem |
| RxJS | 7.8.1 | Programação reativa |
| PWA | - | Progressive Web App |
| CSS3 | - | Estilização |
| HTML5 | - | Estrutura |

### DevOps & Ferramentas
| Ferramenta | Finalidade |
|------------|------------|
| Docker | Containerização |
| Docker Compose | Orquestração de containers |
| Git | Controle de versão |
| GitHub | Repositório remoto |
| Figma | Design de interface |

---

## 🏗️ Arquitetura
┌─────────────────────────────────────────────────────────────┐
│ Frontend (Angular 19) │
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
│ │ usuarios │ │ jogos │ │ matches │ │ grupos │ │
│ └──────────┘ └──────────┘ └──────────┘ └──────────┘ │
└─────────────────────────────────────────────────────────────┘

text

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
- **IntelliJ IDEA** ou **VS Code**

---

## 🚀 Instalação e Execução

### Clone o repositório

```bash
git clone https://github.com/seu-usuario/guilda-social.git
cd guilda-social
Opção 1: Execução com Docker (Recomendado)
bash
# Iniciar todos os serviços
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar serviços
docker-compose down
Opção 2: Execução Local
Backend
bash
cd backend

# Compilar
./mvnw clean compile

# Executar
./mvnw spring-boot:run

# Build para produção
./mvnw clean package -DskipTests
java -jar target/guilda-social-backend-1.0.0.jar
Frontend
bash
cd frontend

# Instalar dependências
npm install

# Executar em desenvolvimento
npm start

# Build para produção (PWA)
npm run build -- --configuration production
Banco de Dados (PostgreSQL)
bash
# Docker
docker run --name guilda-postgres \
  -e POSTGRES_DB=guilda_social \
  -e POSTGRES_USER=guilda_user \
  -e POSTGRES_PASSWORD=guilda_pass \
  -p 5432:5432 \
  -d postgres:16-alpine

# Local (Linux/macOS)
sudo -u postgres psql
CREATE DATABASE guilda_social;
CREATE USER guilda_user WITH PASSWORD 'guilda_pass';
GRANT ALL PRIVILEGES ON DATABASE guilda_social TO guilda_user;
Acessar a aplicação
Serviço	URL
Frontend (PWA)	http://localhost:4200
Backend API	http://localhost:8080/api
Swagger UI	http://localhost:8080/swagger-ui.html
📁 Estrutura do Projeto
text
guilda-social/
├── backend/                          # Spring Boot Backend
│   ├── src/main/java/com/guildasocial/
│   │   ├── api/                      # Controllers, DTOs, Exceptions
│   │   │   ├── controller/           # Endpoints REST
│   │   │   ├── dto/                  # Data Transfer Objects
│   │   │   └── exception/            # Exception handlers
│   │   ├── config/                   # Configurações
│   │   │   ├── SecurityConfig.java   # Segurança JWT
│   │   │   ├── CorsConfig.java       # CORS
│   │   │   └── ModelMapperConfig.java
│   │   ├── domain/                   # Domínio da aplicação
│   │   │   ├── model/                # Entidades JPA
│   │   │   ├── repository/           # Repositórios
│   │   │   └── service/              # Regras de negócio
│   │   └── security/                 # JWT e autenticação
│   └── src/main/resources/
│       ├── application.properties    # Configurações
│       └── db/migration/             # Flyway migrations
├── frontend/                         # Angular Frontend
│   ├── src/app/
│   │   ├── core/                     # Serviços, guards, interceptors
│   │   │   ├── guards/               # AuthGuard, LoginGuard
│   │   │   ├── interceptors/         # AuthInterceptor
│   │   │   ├── models/               # Interfaces TypeScript
│   │   │   └── services/             # AuthService, MatchService
│   │   ├── features/                 # Funcionalidades
│   │   │   ├── auth/                 # Login e Cadastro
│   │   │   └── matches/              # Sugestões e Detalhe
│   │   ├── layout/                   # Componentes de layout
│   │   │   └── header/               # Header com logout
│   │   └── shared/                   # Componentes compartilhados
│   └── src/assets/                   # Imagens, ícones
├── database/                         # Scripts SQL
│   └── init/                         # Inicialização do banco
├── docker-compose.yml                # Orquestração Docker
├── .gitignore
└── README.md
📡 API Endpoints
Autenticação
Método	Endpoint	Descrição
POST	/api/auth/login	Autenticar usuário
Usuários
Método	Endpoint	Descrição
POST	/api/usuarios	Criar novo usuário
GET	/api/usuarios/{id}	Buscar usuário por ID
GET	/api/usuarios	Listar todos usuários
PUT	/api/usuarios/{id}	Atualizar usuário
DELETE	/api/usuarios/{id}	Desativar usuário
Matches
Método	Endpoint	Descrição
GET	/api/matches/sugestoes/{usuarioId}	Obter sugestões
POST	/api/matches/solicitar/{usuarioId}	Enviar solicitação
GET	/api/matches/meus-matches/{usuarioId}	Meus matches
🗄️ Banco de Dados
Modelo Entidade-Relacionamento
text
┌─────────────┐     ┌─────────────────┐     ┌─────────────┐
│  usuarios   │────▶│ perfis_jogador  │────▶│preferencias │
├─────────────┤     ├─────────────────┤     │  _jogo      │
│ id (PK)     │     │ id (PK)         │     ├─────────────┤
│ nome        │     │ usuario_id (FK) │     │ id (PK)     │
│ email (UK)  │     │ estilo_jogo     │     │ perfil_id   │
│ senha       │     │ descricao       │     │ jogo_id     │
│ data_nasc   │     │ limite_distancia│     │ nivel_inter │
│ cidade      │     └─────────────────┘     └──────┬──────┘
│ estado      │                                   │
│ latitude    │     ┌─────────────┐               │
│ longitude   │     │   jogos     │◄──────────────┘
│ descricao   │     ├─────────────┤
│ foto_perfil │     │ id (PK)     │
│ data_cadastro│    │ nome        │
│ ativo       │     │ tipo        │
│ ultimo_login│     │ min_jogadores│
└─────────────┘     │ max_jogadores│
                    └─────────────┘
Scripts SQL
Os scripts de criação do banco estão em:

database/init/01-schema.sql - Estrutura das tabelas

database/init/02-insert-jogos.sql - Dados iniciais (jogos)

📱 PWA (Progressive Web App)
O Guilda Social é um PWA, o que significa que pode ser instalado como um aplicativo nativo no celular ou computador.

Funcionalidades PWA
✅ Instalação como aplicativo

✅ Modo offline básico

✅ Service Worker para cache

✅ Manifest para personalização

✅ Notificações push (em desenvolvimento)

Como instalar
No celular (Android/iOS)
Abra o site no Chrome/Safari

Toque no menu (⋮) → "Adicionar à tela inicial"

Confirme a instalação

No computador (Chrome/Edge)
Abra o site

Clique no ícone de instalação na barra de endereço

Ou vá em Menu → "Instalar aplicativo"

📅 Sprints do Projeto
Sprint	Período	Status	Entregas
Sprint 1	Semana 1-2	✅ Concluída	Cadastro, Perfil, Interesses
Sprint 2	Semana 3-4	✅ Concluída	Login, Autenticação JWT, Guards
Sprint 3	Semana 5-6	✅ Concluída	Matchmaking, Sugestões, Cards
Sprint 4	Semana 7-8	🔄 Em desenvolvimento	Perfil, Edição, Configurações
Sprint 5	Semana 9-10	⏳ Pendente	Grupos, Chat, Eventos
Sprint 6	Semana 11-12	⏳ Pendente	Notificações, PWA completo
```

👥 Contribuidores
Nome	Papel	GitHub
Melyssa Vitória da Silva Moya	Desenvolvedora Full Stack	@melyssamoya

📄 Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

📞 Contato
Email: moyamelyssa@gmail.com

GitHub: github.com/guilda-social

🙏 Agradecimentos
FATEC Ipiranga - Pastor Enéas Tognini
Laboratório de Engenharia de Software
Comunidade open source pelas ferramentas incríveis
Desenvolvido com 💛 para conectar jogadores 🎮
