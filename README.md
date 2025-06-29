readme_content = """
# 📚 LibraryAPI

📋 **Descrição do Projeto**  
A **LibraryAPI** é uma aplicação backend desenvolvida em **Java 21** com **Spring Boot**, voltada para o gerenciamento de uma biblioteca digital. A aplicação permite o cadastro, pesquisa e controle de livros, autores, usuários e autenticação OAuth2, seguindo boas práticas de arquitetura RESTful.

A aplicação é modular e organizada em domínios claros que representam os principais recursos do sistema:

- 👤 **Usuário**: Cadastro de usuários com autenticação segura.
- ✍️ **Autor**: Gerenciamento de autores literários.
- 📘 **Livro**: Controle completo de acervo bibliográfico com filtros dinâmicos de pesquisa.
- 🔐 **OAuth2 Client**: Gerenciamento de credenciais de clientes para autenticação via Authorization Server.

---

## ⚙️ Funcionalidades

### 👤 Módulo de Usuários
- Cadastro de novos usuários com criptografia de senha.
- Associação de papéis (roles) para controle de acesso.

### ✍️ Módulo de Autores
- Cadastro e edição de autores com dados pessoais.
- Pesquisa por nome, nacionalidade e filtros combinados.
- Exclusão segura (não é possível excluir autores com livros cadastrados).

### 📘 Módulo de Livros
- Cadastro de livros com associação a autores.
- Pesquisa avançada com filtros (ISBN, título, autor, gênero, ano).
- Atualização e exclusão com validações.
- Controle de preço e data de publicação.

### 🔐 Módulo OAuth2 Clients
- Cadastro de clients para autenticação via OAuth2.
- Redirecionamento configurável e escopo customizado.
- Codificação automática do `client_secret`.

---

## 🧪 Tecnologias e Ferramentas

- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Security + OAuth2**
- **PostgreSQL**
- **Swagger/OpenAPI (documentação automática)**
- **MapStruct (mapeamento DTO ↔ Entity)**
- **Lombok**
- **JUnit & Mockito (testes automatizados)**
- **Hypersistence Utils (para tipos avançados no JPA)**
- **Thymeleaf** (para login HTML)
- **Maven** (gerenciador de dependências)

---

## 📁 Estrutura do Projeto

\`\`\`
LibraryAPI/
│
├── model/               → Entidades JPA (Livro, Autor, Usuario, Client)
├── controller/          → Endpoints REST com segurança e documentação Swagger
├── service/             → Lógica de negócio e transações
├── repository/          → Interfaces JPA e Specifications
├── validator/           → Regras de validação customizadas
├── dto/                 → Objetos de transferência com validações
├── mappers/             → MapStruct para conversão DTO ↔ Entity
├── config/              → Configurações de segurança, banco e Swagger
└── security/            → Implementações customizadas de autenticação
\`\`\`

---

## 🚀 Como Executar o Projeto

### ✅ Pré-requisitos
- Java 21
- PostgreSQL em execução
- Maven instalado

### 📝 Passos

\`\`\`bash
# Clone o repositório
git clone https://github.com/seu-usuario/LibraryAPI.git

# Acesse o diretório do projeto
cd LibraryAPI

# Configure o banco no arquivo src/main/resources/application.yml

# Execute as migrações do banco
mvn flyway:migrate

# Inicie a aplicação
mvn spring-boot:run
\`\`\`

### 🔎 Acesse a documentação da API:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🤝 Contribuições

Contribuições são bem-vindas!  
Abra uma issue ou envie um pull request com sugestões, melhorias ou correções.

---

## 📄 Licença

Este projeto está licenciado sob a **MIT License**.  
Consulte o arquivo `LICENSE` para mais detalhes.
"""
