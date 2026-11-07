# Texas Logistics API

API REST de logística para gerenciar motoristas (drivers), cargas (shipments)
e atribuições de carga a motoristas (assignments).

## Stack

- Java 21
- Spring Boot 4.0.6 (Web MVC, Data JPA, Validation)
- PostgreSQL (porta 5433 local; aplicação na porta 8080)
- Maven Wrapper (`./mvnw`)
- Testes: JUnit 5 + Mockito + AssertJ
- Documentação: Swagger/OpenAPI (springdoc), em `/swagger-ui/index.html`

## Estrutura

Pacote raiz: `com.jose.texaslogistics`

```
driver/      Driver, DriverStatus (AVAILABLE, BUSY, INACTIVE), Controller,
             Service, Repository, Request/Response DTOs, exceções
shipment/    Shipment, ShipmentStatus (PENDING, IN_TRANSIT, DELIVERED,
             CANCELED), Controller, Service, Repository, DTOs, exceções
assignment/  Assignment, Controller, Service, Repository, DTOs, exceções
exception/   GlobalExceptionHandler (único, trata todos os módulos)
```

Fluxo padrão de uma requisição:
Controller -> Service -> Repository -> PostgreSQL, e de volta como DTO.

## Convenções do código

- Injeção de dependência **por construtor** (sem `@Autowired` em campo).
- Entidades JPA nunca saem na API: sempre `RequestDTO` na entrada e
  `ResponseDTO` na saída. A conversão entidade -> DTO é feita no construtor
  do ResponseDTO.
- Regras de negócio ficam no **Service**, nunca no Controller.
- Erros de negócio são exceções próprias (ex: `DriverNotFoundException`)
  tratadas no `GlobalExceptionHandler`, que devolve JSON padronizado
  (`timestamp`, `status`, `error`, `message`).
- Operações que alteram mais de uma entidade usam `@Transactional`
  (ex: `AssignmentService.createAssignment`).
- Listagens usam paginação (`Pageable`) e filtro opcional por status.
- Cada classe fica **no pacote que o caminho da pasta indica**. A declaração
  `package` dentro do arquivo deve sempre bater com a pasta.

## Regras de negócio importantes

- Criar assignment: o driver deve existir e não pode estar `INACTIVE` nem
  `BUSY`; o shipment deve existir e estar `PENDING`. Ao criar, o driver vira
  `BUSY` e o shipment vira `IN_TRANSIT`.
- Concluir assignment: o shipment deve estar `IN_TRANSIT`. Ao concluir, o
  shipment vira `DELIVERED` e o driver volta a `AVAILABLE`.
- Excluir driver com assignments vinculadas retorna **409 Conflict**.

## Testes

- Testes unitários de Service com `@ExtendWith(MockitoExtension.class)`,
  `@Mock` nos repositories e `@InjectMocks` no service.
- Padrão **AAA** (Arrange, Act, Assert), com nomes de teste descritivos.
- Em teste unitário não há banco: objetos criados com `new` têm `id` nulo.
  Não assertar mensagens que dependem desse id.
- Antes de considerar qualquer tarefa concluída, rodar `./mvnw clean test`.

## Comandos úteis

```
./mvnw clean test          # compila e roda todos os testes
./mvnw spring-boot:run     # sobe a API em http://localhost:8080
git status
git diff
```

## Sobre mim (o desenvolvedor)

Sou iniciante em Java backend. Este projeto é para **estudo** e para montar um
**portfólio** para vagas de desenvolvedor Java júnior nos EUA. Quero entender
o que está acontecendo, não apenas receber código pronto.

## Como me ajudar

**Didática (prioridade máxima)**
- Antes de alterar código, explique o que vai fazer e por quê.
- Depois de alterar, explique cada mudança: o que o código faz, com quais
  classes ele se conecta e qual problema resolve.
- Ao usar uma anotação ou conceito novo (`@Transactional`, `@Mock`,
  `@RestControllerAdvice`, etc.), explique o que é, com um exemplo simples.
- Mostre o fluxo completo quando fizer sentido.
- Em funcionalidades novas e conceitos que quero aprender, **me deixe tentar
  escrever primeiro** e depois revise meu código, apontando o que um
  desenvolvedor sênior melhoraria.
- Em tarefas mecânicas e repetitivas (ex: anotações Swagger), pode fazer
  direto, mas resuma o que foi feito.

**Ritmo**
- Prefira etapas maiores e com sentido, em vez de micro-passos.
- Não repita o que já foi feito nem recapitule a cada resposta.
- Não altere código que já funciona sem motivo; se sugerir, justifique.

**Idioma**
- Código, nomes, comentários, mensagens de commit e README: **em inglês**
  (quero praticar inglês técnico para entrevistas).
- Explicações para mim: **em português**.

**Git**
- Commits pequenos e focados, no formato Conventional Commits
  (`feat:`, `fix:`, `test:`, `docs:`, `refactor:`).
- Antes de commitar, mostre o `git diff` e explique o que está sendo enviado.
- Não faça `git push` sem eu pedir.

## Roadmap (próximos passos, em ordem de prioridade)

1. Terminar a documentação Swagger de Shipments e Assignments
   (Drivers já está feito).
2. Padronizar o erro com um `ErrorResponse` e documentá-lo no Swagger.
3. Corrigir avisos do log: serialização de `PageImpl`, `open-in-view`,
   aviso do springdoc.
4. Testes: `ShipmentServiceTest`, testes de controller com MockMvc, e testes
   de integração (`@DataJpaTest` / Testcontainers).
5. Docker + docker-compose com PostgreSQL.
6. README em inglês (o que é o projeto, como rodar, exemplos de endpoints).
7. Spring Security + JWT (fazer junto comigo, passo a passo, com explicação).
8. Melhorias de qualidade: `@Column(nullable = false)` nas entidades,
   auditoria (`@CreatedDate` / `@LastModifiedDate`), CORS.
