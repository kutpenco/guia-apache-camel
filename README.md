# Camel Orders

Exemplo funcional de integração Apache Camel baseado no `guia-apache-camel.md`.
A aplicação expõe uma API REST para criar, consultar e listar pedidos, usando
rotas nomeadas, `direct:` para modularização, validação, error handler com
redelivery/DLQ e métricas do Actuator. O armazenamento padrão é em memória para
permitir execução local sem dependências externas; Postgres, Kafka e OpenTelemetry
estão disponíveis no Compose para evoluir a integração.

## Executar localmente

Requisitos: JDK 17+, Maven 3.9+.

```bash
cp .env.example .env
mvn spring-boot:run
curl -X POST http://localhost:8080/api/orders \
  -H 'Content-Type: application/json' \
  -d '{"id":"p-1","customerId":"c-1","total":125.50,"country":"BR"}'
curl http://localhost:8080/api/orders
```

## Testes e container

```bash
mvn test
docker compose up --build
docker compose --profile observability up
```

O perfil `observability` inicia o coletor OTLP. Credenciais reais nunca devem
ser commitadas: use `.env` (ignorado pelo Git) ou Secrets do orquestrador.
