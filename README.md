# Carmoloc API

API REST para gestão de uma locadora de máquinas e equipamentos para construção civil (betoneiras, compactadores, escoramentos, containers etc.) — controle de clientes, catálogo de equipamentos com rastreio por unidade física, ordens de locação com cálculo automático de preço e disponibilidade, e autenticação/autorização baseada em roles.

Projeto construído do zero como exercício de modelagem de domínio real e boas práticas de back-end em Java/Spring Boot.

## Stack

- **Java 17** + **Spring Boot 4**
- **Spring Data JPA** / Hibernate
- **PostgreSQL** + **Flyway** (migrations versionadas)
- **Spring Security** + **JWT** (access token + refresh token com rotação)
- **Bean Validation**
- **springdoc-openapi** (Swagger UI)
- **JUnit 5**, **Mockito**, **Testcontainers** (testes de integração com Postgres real)
- **Maven**

## Destaques de arquitetura e modelagem

### Domínio modelado a partir de regras de negócio reais

O modelo não é um CRUD genérico — reflete decisões de negócio discutidas e refinadas ao longo do desenvolvimento:

- **Disponibilidade de equipamento** calculada dinamicamente: cada `Equipment` é um tipo de catálogo (ex. "Betoneira 400L"), composto por `EquipmentUnit`s individuais rastreáveis (status `AVAILABLE` / `MAINTENANCE` / `RETIRED`). A quantidade disponível para locação é sempre derivada — nunca um contador fixo desatualizável — combinando unidades operacionais com reservas ativas que se sobrepõem ao período solicitado.
- **Precificação por diária e meia-diária**, com regra de composição configurável por equipamento (`DAILY_ONLY` vs `DAY_AND_HALF`), e snapshot do preço no momento da locação (alterar o preço de catálogo depois nunca afeta pedidos já criados).
- **Máquina de estados da ordem de locação** (`QUOTE → ACTIVE → FINISHED`, com `CANCELLED` acessível a partir dos dois primeiros), onde cada transição é validada explicitamente no service, incluindo checagem de disponibilidade apenas no momento da confirmação — uma cotação nunca trava estoque.

### Controle de concorrência

A confirmação de uma ordem usa **lock pessimista** (`SELECT ... FOR UPDATE`) nas linhas de equipamento envolvidas, em ordem determinística de ID, para eliminar a janela de corrida entre "checar disponibilidade" e "efetivar a reserva" — e evitar deadlock entre confirmações concorrentes envolvendo múltiplos equipamentos.

### Camadas bem definidas

```
Controller → Service (interface + impl) → Repository → Entity
                 ↑
              Mapper (Entity ↔ DTO, sem lógica de negócio)
```

- DTOs de request e response sempre separados — nunca a entidade exposta diretamente na API.
- Exceções de domínio próprias (`InsufficientAvailabilityException`, `EquipmentNotAvailableException` etc.), tratadas centralmente por um `@RestControllerAdvice`.
- `BaseEntity` compartilhando apenas o que é genuinamente uniforme entre entidades (id, auditoria, soft-delete) — sem abstrações genéricas forçadas em service/repository, onde a divergência de regra de negócio entre entidades tornaria a herança um estorvo em vez de ajuda.

### Schema versionado com Flyway

Todo o histórico de evolução do schema está em migrations numeradas e nunca editadas retroativamente — incluindo mudanças de modelagem feitas no meio do desenvolvimento (ex.: migração do controle de quantidade fixa para unidades individuais rastreáveis).

### Autenticação e autorização

- Login com **JWT de curta duração** (access token) + **refresh token opaco persistido em banco**, com **rotação a cada refresh** (mitiga reuso de token vazado).
- Autorização baseada em **roles** (`ADMIN` / `EMPLOYEE`) via `@PreAuthorize`, com tratamento HTTP correto (`401` para não autenticado, `403` para autenticado sem permissão) através de handlers customizados.
- Senhas com hash **BCrypt** — nunca texto puro.

### Testes em múltiplas camadas

- **Unitários** (JUnit + AssertJ) para regras de domínio puras, como o cálculo de diária/meia-diária a partir de datas.
- **Unitários com Mockito** para composição de serviços (ex. `AvailabilityService`), isolando dependências.
- **Integração com Testcontainers**: a query de disponibilidade — a lógica mais crítica do sistema — é validada contra um **PostgreSQL real** (não H2), incluindo migrations do Flyway rodando do zero, garantindo que o comportamento de overlap de datas é correto no banco de produção, não só "no papel".
