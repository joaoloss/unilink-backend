# Documentação Swagger - Unilink API

## Visão Geral

A API do Unilink está documentada usando **Swagger/OpenAPI 3** através da biblioteca `springdoc-openapi`. Esta documentação fornece uma interface interativa para testar e explorar todos os endpoints da API.

## Como Acessar

### 1. Interface Swagger UI
Após iniciar a aplicação, acesse a interface Swagger UI em:
```
http://localhost:8080/swagger-ui.html
```

### 2. Especificação OpenAPI (JSON)
A especificação OpenAPI em formato JSON está disponível em:
```
http://localhost:8080/api-docs
```

## Configurações

### Configurações no application.properties
```properties
# Swagger/OpenAPI Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.doc-expansion=none
```

### Configuração da API
A configuração principal do OpenAPI está na classe `OpenApiConfig.java` que define:
- Título e descrição da API
- Informações de contato
- Licença
- Servidores disponíveis

## Endpoints Documentados

### 🔐 Autenticação
- **POST** `/api/auth/login` - Realizar login de usuário

### 👥 Usuários
- **GET** `/api/users` - Listar todos os usuários
- **GET** `/api/users/{id}` - Buscar usuário por ID
- **POST** `/api/users` - Criar novo usuário
- **PUT** `/api/users/{id}` - Atualizar usuário
- **DELETE** `/api/users/{id}` - Excluir usuário

### 📋 Projetos
- **GET** `/api/projects` - Listar projetos (com filtros opcionais)
- **GET** `/api/projects/{id}` - Buscar projeto por ID
- **POST** `/api/projects` - Criar novo projeto
- **PUT** `/api/projects/{id}` - Atualizar projeto
- **DELETE** `/api/projects/{id}` - Excluir projeto

### 🏢 Centros
- **GET** `/api/centers` - Listar todos os centros
- **GET** `/api/centers/{id}` - Buscar centro por ID
- **POST** `/api/centers` - Criar novo centro
- **PUT** `/api/centers/{id}` - Atualizar centro
- **DELETE** `/api/centers/{id}` - Excluir centro

### 🏷️ Tags
- **GET** `/api/tags` - Listar todas as tags
- **GET** `/api/tags/{id}` - Buscar tag por ID
- **POST** `/api/tags` - Criar nova tag
- **PUT** `/api/tags/{id}` - Atualizar tag
- **DELETE** `/api/tags/{id}` - Excluir tag

## DTOs Documentados

### UserRequestDTO
```json
{
  "name": "João Silva",
  "email": "joao.silva@email.com",
  "password": "senha123",
  "role": "STUDENT"
}
```

### ProjectRequestDTO
```json
{
  "name": "Sistema de Gestão Escolar",
  "description": "Sistema completo para gerenciamento de escolas",
  "centerId": "uuid-do-centro",
  "ownerId": "uuid-do-proprietario",
  "openForApplications": true,
  "imgUrl": "https://example.com/project-image.jpg",
  "teamSize": 5,
  "tagsToBeAdded": ["uuid-tag1", "uuid-tag2"],
  "tagsToBeRemoved": ["uuid-tag3"]
}
```

### CenterRequestDTO
```json
{
  "name": "Centro de Tecnologia",
  "centerUrl": "https://centro-tecnologia.com"
}
```

### TagRequestDTO
```json
{
  "name": "Java",
  "colorHex": "#FF5733"
}
```

## Códigos de Resposta

### Sucesso
- **200** - Operação realizada com sucesso
- **204** - Operação realizada com sucesso (sem conteúdo)

### Erro
- **400** - Dados inválidos fornecidos
- **401** - Credenciais inválidas
- **404** - Recurso não encontrado

## Como Usar a Interface Swagger

1. **Acesse** `http://localhost:8080/swagger-ui.html`
2. **Explore** os endpoints organizados por tags
3. **Clique** em um endpoint para expandir
4. **Clique** em "Try it out" para testar
5. **Preencha** os parâmetros necessários
6. **Execute** a requisição
7. **Visualize** a resposta

## Benefícios da Documentação Swagger

- ✅ **Interface Interativa** - Teste endpoints diretamente no navegador
- ✅ **Documentação Automática** - Sempre atualizada com o código
- ✅ **Exemplos de Uso** - DTOs com exemplos práticos
- ✅ **Códigos de Resposta** - Documentação completa de respostas
- ✅ **Validação** - Interface valida dados antes do envio
- ✅ **Exportação** - Gera especificação OpenAPI para outras ferramentas

## Desenvolvimento

Para adicionar documentação a novos endpoints:

1. **Importe** as anotações do Swagger:
```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
```

2. **Adicione** a anotação `@Tag` na classe:
```java
@Tag(name = "Nome da Tag", description = "Descrição dos endpoints")
```

3. **Documente** cada método com `@Operation`:
```java
@Operation(
    summary = "Resumo da operação",
    description = "Descrição detalhada"
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Sucesso"),
    @ApiResponse(responseCode = "400", description = "Erro")
})
```

4. **Documente** parâmetros com `@Parameter` e `@Schema`:
```java
@Parameter(description = "Descrição do parâmetro")
@Schema(description = "Descrição do DTO")
```
