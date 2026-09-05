# Apache Camel 4.22 LTS
## Guia Completo de Integração Enterprise em Português do Brasil

**Autor:** Lucas Penco  
**Data:** Setembro de 2026  
**Versão do Camel:** 4.22 LTS (Long Term Support)

---

## Sumário

1. [Introdução ao Apache Camel](#introdução-ao-apache-camel)
2. [O Que Há de Novo na Versão 4.22 LTS](#o-que-ha-de-novo-na-versão-422-lts)
3. [Configuração do Ambiente de Desenvolvimento](#configuração-do-ambiente-de-desenvolvimento)
4. [Conceitos Fundamentais](#conceitos-fundamentais)
5. [Exemplos de Integrações Comuns](#exemplos-de-integrações-comuns)
6. [Padrões de Integração Enterprise (EIPs)](#padrões-de-integração-enterprise-eips)
7. [Configuração e Boas Práticas](#configuração-e-boas-práticas)
8. [Kubernetização com Camel K](#kubernetização-com-camel-k)
9. [Monitoramento e Observabilidade](#monitoramento-e-observabilidade)
10. [Segurança em Produção](#segurança-em-produção)
11. [Referências e Recursos](#referências-e-recursos)

---

## Introdução ao Apache Camel

### O Que é Apache Camel?

Apache Camel é um framework de integração open-source que implementa mais de 80 Padrões de Integração Enterprise (EIPs) do livro clássico de Gregor Hohpe e Bobby Woolf. [cite:8] Com mais de 300 componentes para conexão com bancos de dados, filas de mensagens, APIs e serviços em nuvem, o Camel fornece um framework unificado para lógica de roteamento, transformação e mediação. [cite:8]

### Por Que Usar Camel?

- **Simplicidade**: Rotas declarativas em YAML, XML ou Java
- **Conectividade**: 300+ componentes para sistemas diversos
- **Padrões Comprovados**: Implementação de EIPs testados em produção
- **Cloud-Native**: Suporte nativo a Kubernetes com Camel K
- **Segurança por Padrão**: Recursos de segurança integrados sem configuração extra [cite:16]

### Casos de Uso Tápicos

- Integração entre sistemas legados e modernos
- Processamento de arquivos e transformação de dados
- Orquestração de microsserviços
- Pipelines de dados em tempo real
- APIs REST e GraphQL
- Integração com IA (OpenAI, LangChain4j, Spring AI) [cite:16]

---

## O Que Há de Novo na Versão 4.22 LTS

### Lançamento e Suporte

Apache Camel 4.22 LTS foi lançado em agosto de 2026 e é uma versão de Suporte de Longo Termo (LTS), recebendo correções de bugs e atualizações de segurança por aproximadamente um ano (até agosto de 2027). [cite:16][cite:27] As linhas LTS suportadas atualmente são **4.18.x** e **4.22.x** (4.14.x atingiu o fim da vida útil). [cite:16]

### Principais Novidades

#### 1. Camel TUI (Terminal User Interface)

Uma nova aplicação de terminal para monitorar, gerenciar e desenvolver integrações Camel com mais de 30 abas, incluindo: [cite:16]

- Editor YAML DSL integrado com autocompletar
- Assistente de IA incorporado (F8)
- Abas para rotas, endpoints, consumidores, atividade, erros, histórico, diagramas, saúde, spans OpenTelemetry, profiling JFR, análise de heap, auditoria CVE, catálogo, consultas SQL e muito mais [cite:16]

#### 2. Camel AI Unificado

Novo componente `camel-ai-tool` que substitui `camel-langchain4j-tools` (depreciado) e `camel-spring-ai-tools` (removido): [cite:16]

```java
from("ai-tool:weather?tags=weather&description=Obter clima&parameter.cidade=string")
 .setBody(constant("{\"cidade\": \"São Paulo\", \"temp\": \"22C\"}"));
```

- Funciona com LangChain4j, Spring AI, OpenAI e qualquer agente compatável com MCP [cite:16]
- Servidor MCP incorporado para expor ferramentas a agentes de IA [cite:16]

#### 3. Segurança Aprimorada

- Filtros de desserialização JEP-290
- Listas de permissões dinâmicas de URI para `toD` e `enrich`
- Containment de download para consumidores de armazenamento em nuvem
- Prevenção de path traversal em arquivos tar/zip
- Máscara de credenciais em logs e URIs
- Hardening de autenticação JWT para o servidor HTTP incorporado [cite:16]

#### 4. Melhorias no Splitter EIP

- **Chunking**: Processar itens em lotes de tamanho fixo com `chunkSize`
- **Error threshold**: Parar após N falhas consecutivas com `errorThreshold`
- **Watermark resume**: Retomar do último item processado com sucesso [cite:16]

#### 5. Camel CLI Estável

- Instaladores de uma linha para macOS/Linux e Windows
- `camel self-update` para atualizações automáticas
- `camel doctor` para diagnósticos
- `camel run --openapi-ui` para Swagger UI
- `camel cmd route-diagram` para gerar diagramas de rotas [cite:16]

#### 6. Novos Componentes

- `camel-clickhouse`: Integração com banco de dados ClickHouse
- `camel-duckdb`: Banco de dados analático DuckDB
- `camel-jactl`: Linguagem de scripting Jactl para rotas Camel [cite:16]

#### 7. Observabilidade

- Estatásticas de latência percentil (p50, p95, p99)
- Instrumentação JFR em tempo de execução
- Console de desenvolvimento SQL Trace
- Histograma de heap para diagnóstico de memória [cite:16]

---

## Configuração do Ambiente de Desenvolvimento

### Pré-requisitos

- **Java**: JDK 25 (LTS)
- **Maven**: 3.9+ ou Gradle 8+
- **Opcional**: Docker, Kubernetes (para Camel K)

### Instalação do Camel CLI

#### Método 1: Instalador Automático (Recomendado)

**Linux/macOS:**
```bash
curl -fsSL https://camel.apache.org/install.sh | sh
```

**Windows (PowerShell):**
```powershell
irm https://camel.apache.org/install.ps1 | iex
```

O instalador baixa do Maven Central, verifica checksums SHA-256 e valida que um runtime Java 25 está disponível. A instalação é por usuário e nunca requer `sudo`. [cite:16]

#### Método 2: Maven/Gradle

**Maven (pom.xml):**
```xml
<project>
  <modelVersion>4.0.0</modelVersion>
  
  <parent>
    <groupId>org.apache.camel</groupId>
    <artifactId>camel-parent</artifactId>
    <version>4.22.0</version>
    <relativePath/>
  </parent>
  
  <groupId>com.madrugas</groupId>
  <artifactId>meu-projeto-camel</artifactId>
  <version>1.0.0</version>
  
  <dependencies>
    <dependency>
      <groupId>org.apache.camel</groupId>
      <artifactId>camel-core</artifactId>
    </dependency>
    <dependency>
      <groupId>org.apache.camel</groupId>
      <artifactId>camel-spring-boot-starter</artifactId>
    </dependency>
    <dependency>
      <groupId>org.apache.camel</groupId>
      <artifactId>camel-jackson</artifactId>
    </dependency>
  </dependencies>
  
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.camel</groupId>
        <artifactId>camel-maven-plugin</artifactId>
        <version>4.22.0</version>
      </plugin>
    </plugins>
  </build>
</project>
```

**Gradle (build.gradle):**
```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.5.0'
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.apache.camel:camel-core:4.22.0'
    implementation 'org.apache.camel:camel-spring-boot-starter:4.22.0'
    implementation 'org.apache.camel:camel-jackson:4.22.0'
}
```

### Estrutura de Projeto Tápica

```
meu-projeto-camel/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/madrugas/
│   │   │       ├── Application.java
│   │   │       └── routes/
│   │   │           └── MinhasRotas.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── routes/
│   │           └── minha-rota.yaml
│   └── test/
│       └── java/
│           └── com/madrugas/
│               └── routes/
│                   └── MinhasRotasTest.java
├── pom.xml
└── README.md
```

### Executando com Camel JBang/CLI

```bash
# Executar rota YAML diretamente
camel run minha-rota.yaml

# Modo desenvolvimento com hot reload
camel dev minha-rota.yaml

# Gerar diagrama da rota
camel cmd route-diagram routes/*.yaml

# Gerar topologia de rotas
camel cmd route-topology routes/*.yaml

# Executar com OpenAPI UI
camel run --openapi-ui

# Executar com Java Flight Recorder
camel run --jfr

# Infraestrutura de observabilidade (Prometheus, VictoriaTraces, VictoriaLogs, Perses)
camel infra run observability
```

---

## Conceitos Fundamentais

### CamelContext

O `CamelContext` é o coração de toda aplicação Camel. É o container de runtime que gerencia o ciclo de vida completo da aplicação. [cite:9]

```java
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ExemploContexto {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer:tick?period=5000")
                    .log("Olá, Camel! Hora atual: ${date:now:HH:mm:ss}");
            }
        });
        
        context.start();
        Thread.sleep(30000);
        context.stop();
    }
}
```

### Rotas (Routes)

Uma rota define o fluxo de mensagens de um endpoint de origem para um ou mais endpoints de destino.

**Sintaxe YAML:**
```yaml
- route:
    id: "minha-primeira-rota"
    from:
      uri: "timer:tick"
      parameters:
        period: 5000
    steps:
      - log:
          message: "Mensagem recebida: ${body}"
      - to:
          uri: "log:saida"
```

**Sintaxe Java (RouteBuilder):**
```java
from("timer:tick?period=5000")
    .routeId("minha-primeira-rota")
    .log("Mensagem recebida: ${body}")
    .to("log:saida");
```

**Sintaxe XML:**
```xml
<camelContext xmlns="http://camel.apache.org/schema/spring">
  <route id="minha-primeira-rota">
    <from uri="timer:tick?period=5000"/>
    <log message="Mensagem recebida: ${body}"/>
    <to uri="log:saida"/>
  </route>
</camelContext>
```

### Exchange, Message e Headers

- **Exchange**: Container que envolve a mensagem durante o processamento
- **Message**: Dados sendo transportados (body + headers)
- **Headers**: Metadados da mensagem (chave-valor)

```java
from("direct:inicio")
    .process(exchange -> {
        // Acessar body
        String corpo = exchange.getIn().getBody(String.class);
        
        // Setar header
        exchange.getIn().setHeader("meu-header", "valor");
        
        // Modificar body
        exchange.getIn().setBody(corpo.toUpperCase());
    })
    .log("Body: ${body}, Header: ${header.meu-header}");
```

### Endpoints e Componentes

Endpoints são URIs que representam sistemas externos:

```
componente:configuração?opção1=valor1&opção2=valor2
```

**Exemplos:**
- `file:/data/inbox?include=*.json&delete=true`
- `kafka:topic?brokers=kafka:9092&groupId=meu-grupo`
- `http://api.exemplo.com/endpoint?httpMethod=POST`
- `jdbc:meuBanco?dataSource=#meuDataSource`

### Expressões e Linguagens

Camel suporta múltiplas linguagens de expressão:

**Simple Language (mais comum):**
```java
.simple("${body.nome} tem ${body.idade} anos")
.simple("${header.Content-Type}")
.simple("${date:now:yyyy-MM-dd HH:mm:ss}")
```

**Nova funcionalidade 4.18+:** Operadores Elvis, Ternário e Chain [cite:17]
```java
.simple("${body.nome ?: 'Anônimo'}")  // Elvis: valor ou fallback
.simple("${body.valor > 100 ? 'Alto' : 'Baixo'}")  // Ternário
.simple("${body.texto ~> trim ~> uppercase}")  // Chain: encadeamento
```

**XPath, JSONPath, Groovy, Jactl (novo na 4.22):**
```java
.xpath("/pessoa/nome/text()")
.jsonPath("$.pessoa.nome")
.groovy("return body.nome.toUpperCase()")
.jactl("body.nome.toUpperCase()")  // Novo na 4.22 [cite:16]
```

---

## Exemplos de Integrações Comuns

### 1. Integração com Arquivos

**Leitura e processamento de arquivos:**

```yaml
# file-processor.yaml
- route:
    id: "processador-arquivos"
    from:
      uri: "file:/data/inbox"
      parameters:
        include: ".*\\.json"
        delete: true
        move: ".processed"
    steps:
      - log:
          message: "Processando arquivo: ${header.CamelFileName}"
      - unmarshal:
          json:
            library: Jackson
      - process:
          ref: meuProcessador
      - to:
          uri: "file:/data/outbox"
          parameters:
            fileName: "${date:now:yyyyMMdd-HHmmss}-${header.CamelFileName}"
```

**Java:**
```java
@Component
public class FileRoutes extends RouteBuilder {
    
    @Override
    public void configure() throws Exception {
        // Error handling
        onException(Exception.class)
            .log("Erro ao processar arquivo: ${exception.message}")
            .handled(true)
            .to("file:/data/error?fileName=error-${date:now:yyyyMMdd-HHmmss}.txt");
        
        // Rota principal
        from("file:/data/inbox?include=.*\\.json&delete=true")
            .routeId("processador-arquivos")
            .log("Processando: ${header.CamelFileName}")
            .unmarshal().json()
            .process(new MeuProcessador())
            .to("file:/data/outbox");
    }
    
    public class MeuProcessador implements Processor {
        @Override
        public void process(Exchange exchange) throws Exception {
            Map<String, Object> dados = exchange.getIn().getBody(Map.class);
            dados.put("processadoEm", new Date());
            exchange.getIn().setBody(dados);
        }
    }
}
```

### 2. Integração com Kafka

**Consumidor Kafka:**

```yaml
- route:
    id: "kafka-consumer"
    from:
      uri: "kafka:pedidos"
      parameters:
        brokers: "kafka:9092"
        groupId: "grupo-processador"
        autoOffsetReset: "earliest"
    steps:
      - unmarshal:
          json:
            library: Jackson
      - log:
          message: "Pedido recebido: ${body.id}"
      - to:
          uri: "direct:processar-pedido"
```

**Produtor Kafka:**

```java
from("rest:get:/api/pedidos")
    .routeId("kafka-producer")
    .to("kafka:pedidos?brokers=kafka:9092")
    .setBody(constant("{\"status\": \"recebido\"}"))
    .marshal().json()
    .setHeader(Exchange.CONTENT_TYPE, constant("application/json"));
```

**Configuração SASL (nova opção `saslAuthType` na 4.18+):** [cite:17]
```yaml
from:
  uri: "kafka:pedidos"
  parameters:
    brokers: "kafka-prod:9092"
    saslMechanism: "SCRAM-SHA-256"
    saslAuthType: "SCRAM"  # Deriva automaticamente protocolo e mecanismo
    securityProtocol: "SASL_SSL"
    username: "{{kafka.username}}"
    password: "{{kafka.password}}"
```

### 3. Integração REST/HTTP

**Servidor REST:**

```yaml
- route:
    id: "rest-api"
    from:
      uri: "platform-http:/api/pedidos?httpMethodRestrict=GET,POST"
    steps:
      - choice:
          when:
            - simple: "${header.CamelHttpMethod} == 'GET'"
              steps:
                - to: "direct:buscar-pedidos"
          when:
            - simple: "${header.CamelHttpMethod} == 'POST'"
              steps:
                - unmarshal:
                    json:
                      library: Jackson
                - to: "direct:criar-pedido"
          otherwise:
            - setHeader:
                name: CamelHttpResponseCode
                constant: 405
            - setBody:
                constant: "Método não permitido"
```

**Cliente HTTP:**

```java
from("timer:poll?period=60000")
    .routeId("poll-api-externa")
    .to("https://api.externa.com/dados?bridgeEndpoint=true")
    .unmarshal().json()
    .log("Dados recebidos: ${body}");
```

**Configuração HTTPS com certificado autoassinado (desenvolvimento):** [cite:18]
```properties
camel.ssl.enabled=true
camel.ssl.selfSigned=true
```

### 4. Integração com Banco de Dados

**JDBC:**

```yaml
- route:
    id: "jdbc-insert"
    from:
      uri: "direct:inserir-pedido"
    steps:
      - setBody:
          simple: |
            INSERT INTO pedidos (id, cliente_id, total, status, criado_em)
            VALUES ('${body.id}', '${body.clienteId}', ${body.total}, 'processing', NOW())
      - to:
          uri: "jdbc:meuDataSource"
      - log:
          message: "Inserido ${header.CamelJdbcUpdateCount} registro(s)"
```

**Configuração Spring Boot (application.properties):**
```properties
# DataSource
spring.datasource.url=jdbc:postgresql://localhost:5432/meubanco
spring.datasource.username=usuario
spring.datasource.password=senha
spring.datasource.driver-class-name=org.postgresql.Driver

# Camel
camel.component.jdbc.data-source-ref=meuDataSource
```

**Bean configuration:**
```java
@Configuration
public class DataSourceConfig {
    
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource meuDataSource() {
        return DataSourceBuilder.create().build();
    }
}
```

### 5. Integração com OpenAI (IA)

**Componente `camel-openai` (atualizado na 4.22):** [cite:16][cite:17]

```yaml
- route:
    id: "openai-chat"
    from:
      uri: "direct:perguntar-ia"
    steps:
      - setBody:
          simple: "${body}"
      - to:
          uri: "openai:chat"
          parameters:
            model: "gpt-4o"
            temperature: 0.7
            maxTokens: 1000
      - log:
          message: "Resposta IA: ${body}"
```

**Configuração:**
```properties
camel.component.openai.api-key=${OPENAI_API_KEY}
camel.component.openai.organization-id=org-xxxxx
```

**Novidades 4.22:** [cite:16]
- **Responses API**: `openai:responses` com suporte a entrada texto/imagem, output estruturado
- **áudio**: speech e translation
- **Execução paralela de ferramentas MCP**
- **Orçamento de tokens**: `maxToolCallingRoundTrips` (padrão 10)

**Exemplo RAG (Retrieval-Augmented Generation):** [cite:17]

```java
from("direct:pergunta-rag")
    .routeId("rag-pipeline")
    
    // 1. Gerar embeddings da pergunta
    .to("openai:embeddings?model=text-embedding-3-small")
    
    // 2. Buscar documentos similares no Qdrant/Milvus
    .to("qdrant:search?collection=docs&limit=5")
    
    // 3. Construir prompt com contexto
    .process(exchange -> {
        String pergunta = exchange.getProperty("pergunta", String.class);
        List<String> contextos = exchange.getIn().getBody(List.class);
        
        String prompt = String.format(
            "Use o contexto abaixo para responder:\n\nContexto:\n%s\n\nPergunta: %s",
            String.join("\n", contextos),
            pergunta
        );
        
        exchange.getIn().setBody(prompt);
    })
    
    // 4. Chamar OpenAI com contexto
    .to("openai:chat?model=gpt-4o&temperature=0.3")
    
    // 5. Retornar resposta
    .log("Resposta: ${body}");
```

### 6. Integração com AWS S3

```yaml
- route:
    id: "s3-upload"
    from:
      uri: "file:/uploads?include=.*\\.pdf"
    steps:
      - log:
          message: "Upload S3: ${header.CamelFileName}"
      - to:
          uri: "aws2-s3:meu-bucket"
          parameters:
            operation: "putObject"
            key: "${header.CamelFileName}"
            autoCreateBucket: false
```

**Configuração AWS:**
```properties
camel.component.aws2-s3.access-key=${AWS_ACCESS_KEY}
camel.component.aws2-s3.secret-key=${AWS_SECRET_KEY}
camel.component.aws2-s3.region=sa-east-1
```

**Novidade 4.18:** Streaming direto de arquivos durante upload [cite:17]

### 7. Integração com FTP/SFTP

```yaml
- route:
    id: "ftp-download"
    from:
      uri: "ftp:ftp.exemplo.com"
      parameters:
        username: "{{ftp.user}}"
        password: "{{ftp.password}}"
        directory: "/inbox"
        include: ".*\\.csv"
        delete: true
    steps:
      - unmarshal:
          csv:
            useMaps: true
      - split:
          body:
      - to:
          uri: "direct:processar-linha"
```

**SFTP com MINA (novo componente `camel-mina` na 4.18):** [cite:17]
```yaml
from:
  uri: "mina:ssh://sftp.exemplo.com:22"
  parameters:
    username: "{{sftp.user}}"
    password: "{{sftp.password}}"
```

### 8. Transformação de Dados

**XML para JSON:**
```java
from("direct:xml-para-json")
    .routeId("transformacao-xml-json")
    .unmarshal().jacksonXml()
    .marshal().json()
    .to("kafka:dados-json?brokers=kafka:9092");
```

**CSV para JSON:**
```yaml
- route:
    id: "csv-para-json"
    from:
      uri: "file:/data/csv?include=.*\\.csv"
    steps:
      - unmarshal:
          csv:
            useMaps: true
      - split:
          body:
      - marshal:
          json:
            library: Jackson
      - to:
          uri: "kafka:dados-csv?brokers=kafka:9092"
```

**Enriquecimento de dados:**
```java
from("kafka:eventos-brutos?brokers=kafka:9092")
    .routeId("enriquecimento")
    .unmarshal().json()
    .enrich("http://servico-enriquecimento/enriquecer", (original, resource) -> {
        Map<String, Object> originais = original.getIn().getBody(Map.class);
        Map<String, Object> enriquecidos = resource.getIn().getBody(Map.class);
        
        originais.put("enriquecido", enriquecidos);
        original.getIn().setBody(originais);
        
        return original;
    })
    .marshal().json()
    .to("kafka:eventos-enriquecidos?brokers=kafka:9092");
```

---

## Padrões de Integração Enterprise (EIPs)

### 1. Content-Based Router

Roteia mensagens baseadas no conteúdo:

```yaml
- route:
    id: "router-conteudo"
    from:
      uri: "kafka:pedidos?brokers=kafka:9092"
    steps:
      - choice:
          when:
            - simple: "${body.total} > 1000"
              steps:
                - log: "Pedido de alto valor: ${body.id}"
                - to: "direct:processamento-prioritario"
            
            - simple: "${body.pais} == 'BR'"
              steps:
                - log: "Pedido nacional: ${body.id}"
                - to: "direct:processamento-nacional"
            
            - simple: "${body.pais} != 'BR'"
              steps:
                - log: "Pedido internacional: ${body.id}"
                - to: "direct:processamento-internacional"
          
          otherwise:
            steps:
              - log: "Pedido padrão: ${body.id}"
              - to: "direct:processamento-padrao"
```

### 2. Splitter

Divide mensagens em partes menores:

```yaml
- route:
    id: "splitter-pedidos"
    from:
      uri: "kafka:pedidos-lote?brokers=kafka:9092"
    steps:
      - split:
          body:
          # Novidades 4.22: chunkSize, errorThreshold, watermark [cite:16]
          chunkSize: 10
          errorThreshold: 5
        steps:
          - log: "Processando item: ${body.id}"
          - to: "direct:processar-item"
          - log: "Item processado: ${body.id}"
```

**Java com AggregationStrategy:**
```java
from("direct:agregar-resultados")
    .routeId("splitter-aggregator")
    .split(body(), new MinhasAgregacoes())
        .parallelProcessing()
        .to("direct:processar-paralelo")
    .end()
    .to("kafka:resultados?brokers=kafka:9092");

public class MinhasAgregacoes implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            return newExchange;
        }
        
        List<Object> resultados = oldExchange.getIn().getBody(List.class);
        Object novoResultado = newExchange.getIn().getBody();
        resultados.add(novoResultado);
        
        oldExchange.getIn().setBody(resultados);
        return oldExchange;
    }
}
```

### 3. Multicast

Envia mensagem para múltiplos destinos:

```java
from("direct:multicast-exemplo")
    .routeId("multicast")
    .multicast()
        .parallelProcessing()
        .to("direct:destino1", "direct:destino2", "direct:destino3")
    .end()
    .log("Mensagem enviada para todos os destinos");
```

**Com AggregationStrategy:**
```java
.multicast(new MinhaEstrategiaAgregacao())
    .parallelProcessing()
    .to("http://api1.com", "http://api2.com", "http://api3.com")
.end()
```

### 4. Circuit Breaker

Protege contra falhas em cascata:

```yaml
- route:
    id: "circuit-breaker"
    from:
      uri: "direct:chamar-servico"
    steps:
      - circuitBreaker:
          # Novidade 4.22: durações em expressões Camel (60s, 1m, PT1M) [cite:16]
          resilience4j:
            failureRateThreshold: 50
            waitDurationInOpenState: 60s
            slidingWindowSize: 10
        steps:
          - to: "http://servico-externo.com/api"
        onFallback:
          - log: "Fallback ativado"
          - setBody:
              constant: "{\"status\": \"fallback\"}"
```

**Novidades 4.22:** [cite:16]
- Opções de duração Resilience4j aceitam expressões Camel (`60s`, `1m`, `PT1M`)
- Processamento assáncrono (não-bloqueante) suportado
- Fault Tolerance ganhou contadores de chamadas ao vivo
- `onFallbackViaNetwork()` depreciado

### 5. Dead Letter Channel (DLQ)

```yaml
- onException:
    exception:
      - "java.lang.Exception"
    handled:
      constant:
        expression: "true"
    redeliveryPolicy:
      maximumRedeliveries: 3
      redeliveryDelay: 1000
      backOffMultiplier: 2
      useExponentialBackOff: true
    steps:
      - log:
          message: "Erro: ${exception.message}"
      - setHeader:
          name: error-mensagem
          simple: "${exception.message}"
      - setHeader:
          name: error-stacktrace
          simple: "${exception.stacktrace}"
      - to:
          uri: "kafka:dlq?brokers=kafka:9092"

- route:
    id: "processamento-principal"
    from:
      uri: "kafka:pedidos?brokers=kafka:9092"
    steps:
      - log: "Processando: ${body}"
      - to: "http://servico/processar"
```

### 6. Aggregator

Agrega mensagens relacionadas:

```java
from("kafka:eventos?brokers=kafka:9092")
    .routeId("aggregator")
    .aggregate(header("correlationId"), new MinhaEstrategia())
        .completionSize(10)
        .completionTimeout(5000)
        .to("direct:processar-agregado")
    .end();

public class MinhaEstrategia implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            return newExchange;
        }
        
        // Lógica de agregação
        List<Object> eventos = oldExchange.getIn().getBody(List.class);
        eventos.add(newExchange.getIn().getBody());
        
        oldExchange.getIn().setBody(eventos);
        return oldExchange;
    }
}
```

**Anti-padrão:** Aggregator sem `completionSize` ou `completionTimeout` cresce indefinidamente até OOM. Sempre defina condição de completude. [cite:11]

### 7. Enrich

Enriquece mensagem com dados externos:

```java
from("kafka:pedidos?brokers=kafka:9092")
    .routeId("enrich-pedido")
    .enrich("direct:buscar-cliente", (original, resource) -> {
        Map<String, Object> pedido = original.getIn().getBody(Map.class);
        Map<String, Object> cliente = resource.getIn().getBody(Map.class);
        
        pedido.put("cliente", cliente);
        original.getIn().setBody(pedido);
        
        return original;
    })
    .to("kafka:pedidos-enriquecidos?brokers=kafka:9092");
```

### 8. Publish-Subscribe

```yaml
- route:
    id: "pub-sub"
    from:
      uri: "kafka:eventos?brokers=kafka:9092&topics=eventos.*"
    steps:
      - log: "Evento recebido: ${header.CamelKafkaTopic}"
      - choice:
          when:
            - simple: "${header.CamelKafkaTopic} == 'eventos.pedidos'"
              steps:
                - to: "direct:processar-pedido"
          when:
            - simple: "${header.CamelKafkaTopic} == 'eventos.clientes'"
              steps:
                - to: "direct:processar-cliente"
```

---

## Configuração e Boas Práticas

### Externalização de Configuração

**application.properties:**
```properties
# Endpoints
camel.endpoint.inbox=file:/data/inbox
camel.endpoint.outbox=file:/data/outbox
camel.endpoint.kafka.brokers=kafka:9092

# Credenciais (usar variáveis de ambiente em produção)
camel.kafka.username=${KAFKA_USERNAME}
camel.kafka.password=${KAFKA_PASSWORD}

# Sensiveis (mascarados em logs automaticamente na 4.22) [cite:16]
camel.main.additional-sensitive-keywords=minha-senha,token,secret

# Perfil
camel.main.profile=prod
```

**Uso em rotas:**
```yaml
from:
  uri: "{{camel.endpoint.inbox}}"
  parameters:
    include: ".*\\.json"
```

```java
from("{{camel.endpoint.inbox}}?include=.*\\.json")
    .to("kafka:topic?brokers={{camel.endpoint.kafka.brokers}}");
```

### Organização de Rotas

**Boas práticas:** [cite:7][cite:8]

1. **Mantenha rotas simples**: Decomponha rotas complexas em sub-rotas
2. **Use endpoints `direct:`** para modularização
3. **Separe configuração de lógica de roteamento**
4. **Sempre nomeie rotas e componentes** para rastreabilidade
5. **Limite de ~50 linhas por rota** [cite:8]

**Exemplo de organização:**
```java
@Component
public class RotasPedidos extends RouteBuilder {
    
    @Override
    public void configure() throws Exception {
        // Rota principal
        from("kafka:pedidos?brokers={{kafka.brokers}}")
            .routeId("processamento-pedidos")
            .to("direct:validar-pedido")
            .to("direct:enriquecer-pedido")
            .to("direct:salvar-pedido")
            .to("kafka:pedidos-processados?brokers={{kafka.brokers}}");
        
        // Sub-rotas
        from("direct:validar-pedido")
            .routeId("validacao-pedido")
            .process(new ValidadorPedido());
        
        from("direct:enriquecer-pedido")
            .routeId("enriquecimento-pedido")
            .enrich("direct:buscar-cliente", new EnrichStrategy());
        
        from("direct:salvar-pedido")
            .routeId("salvar-pedido")
            .to("jdbc:meuDataSource");
    }
}
```

### Tratamento de Erros

**Melhor prática:** Use blocos `onException` em vez de try-catch em Processors. [cite:8]

```java
@Override
public void configure() throws Exception {
    // Error handler global
    errorHandler(deadLetterChannel("kafka:dlq?brokers={{kafka.brokers}}")
        .maximumRedeliveries(3)
        .redeliveryDelay(1000)
        .retryAttemptedLogLevel(LoggingLevel.WARN)
        .logStackTrace(true)
        .logRetryStackTrace(true)
        .logExhausted(true)
        .logExhaustedMessageHistory(true));
    
    // Exceções especáficas
    onException(ValidationException.class)
        .handled(true)
        .log("Validação falhou: ${exception.message}")
        .setHeader("erro-tipo", constant("VALIDACAO"))
        .to("kafka:erros-validacao?brokers={{kafka.brokers}}");
    
    onException(SQLException.class)
        .handled(true)
        .maximumRedeliveries(5)
        .redeliveryDelay(2000)
        .backOffMultiplier(2)
        .useExponentialBackOff(true)
        .log("Erro banco de dados: ${exception.message}")
        .to("kafka:erros-banco?brokers={{kafka.brokers}}");
    
    // Rotas
    from("kafka:pedidos?brokers={{kafka.brokers}}")
        .routeId("processamento-pedidos")
        .to("direct:processar");
}
```

**Anti-padrão:** Definir `handled(true)` sem rotear para DLQ ou log significa que erros desaparecem. Sempre logue ou roteie Exchanges falhados. [cite:11]

### Idempotência

Design rotas para lidar com mensagens duplicadas:

```java
from("kafka:pedidos?brokers={{kafka.brokers}}")
    .routeId("processamento-idempotente")
    .idempotentConsumer(header("CamelKafkaMessageId"), 
        jpaIdempotentRepository(JpaRepository.class))
        .to("direct:processar-pedido")
    .end();
```

### Throttling

```java
from("kafka:pedidos?brokers={{kafka.brokers}}")
    .routeId("throttling")
    .throttle(100)  // 100 mensagens por segundo
        .to("direct:processar")
    .end();
```

### Monitoramento

**Habilite JMX e use Camel metrics:** [cite:8]

```properties
# application.properties
camel.springboot.main-run-controller=true
camel.springboot.jmx-enabled=true
management.endpoints.web.exposure.include=health,info,metrics,camelroutes
```

**Adicione Micrometer:**
```xml
<dependency>
    <groupId>org.apache.camel</groupId>
    <artifactId>camel-micrometer</artifactId>
</dependency>
```

### Testes

**Teste rotas rigorosamente com Camel Test:** [cite:8]

```java
@ExtendWith(CamelSpringBootExtension.class)
@SpringBootTest(classes = {Application.class, RotasPedidos.class})
class RotasPedidosTest {
    
    @Autowired
    private ProducerTemplate producerTemplate;
    
    @Autowired
    private MockEndpoint mockEndpoint;
    
    @Test
    void deveProcessarPedido() throws Exception {
        mockEndpoint.reset();
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.expectedBodiesReceived("processado");
        
        producerTemplate.sendBody("direct:processar", "pedido-teste");
        
        mockEndpoint.assertIsSatisfied(5000);
    }
}
```

### Anti-padrões Comuns

**O que NãO fazer:** [cite:11]

1. **Escrever em `exchange.getOut()`:** Cria nova Message e descarta headers. Sempre mutacione `exchange.getIn()`.
2. **Agregação ilimitada em memória:** Use repositórios JDBC ou Infinispan.
3. **Engolir exceções silenciosamente:** Sempre logue ou roteie para DLQ.
4. **Endpoints não reutilizados:** Cache endpoints ou use ProducerTemplate com templates pré-criados.

**Checklist de revisão de código:** [cite:11]
- [ ] Condições de completude em todo aggregator
- [ ] Apenas mutações em `getIn()`
- [ ] Roteamento para DLQ após error handlers
- [ ] Flags de streaming em splits de arquivo/byte array

---

## Kubernetização com Camel K

### O Que é Camel K?

Apache Camel K leva o poder dos padrões de integração do Camel para Kubernetes com uma abordagem serverless e cloud-native. [cite:6] Em vez de implantar servidores de integração monoláticos, você implanta integrações como workloads independentes. Cada integração é construída em um container que pode escalar independentemente baseado na carga. [cite:6]

Camel K suporta múltiplas DSLs incluindo Java, XML, YAML e Groovy. Você pode escrever rotas simples em um único arquivo e implantá-las diretamente no Kubernetes sem construir imagens de container manualmente. O operador Camel K cuida de compilação, containerização e implantação automaticamente. [cite:6]

### Instalação do Camel K

**1. Instalar CLI Camel K:**
```bash
# Download e instalação
curl -L https://downloads.apache.org/camel/camel-k/2.10.1/camel-k-client-2.10.1-linux-amd64.tar.gz | tar xz
sudo mv kamel /usr/local/bin/

# Verificar instalação
kamel version
```

**2. Instalar operador no Kubernetes:**
```bash
# Criar namespace
kubectl create ns camel-k

# Instalar operador
kubectl apply -k github.com/apache/camel-k/install/overlays/kubernetes/descoped?ref=v2.10.1 --server-side

# Verificar status do operador
kubectl get pods -n camel-k
```

**3. Criar IntegrationPlatform:**
```bash
kubectl apply -n camel-k -f - <<'EOF'
apiVersion: camel.apache.org/v1
kind: IntegrationPlatform
metadata:
  name: camel-k
  namespace: camel-k
spec:
  build:
    registry:
      address: registry.exemplo.com
      organization: camel-k
EOF

# Aguardar até estar pronto
kubectl wait --for jsonpath='{.status.phase}'=Ready integrationplatform/camel-k -n camel-k --timeout=60s
kubectl get integrationplatform -A
```

**4. Integração com Knative (opcional para serverless):** [cite:6]
```bash
# Instalar Knative Serving
kubectl apply -f https://github.com/knative/serving/releases/latest/download/serving-crds.yaml
kubectl apply -f https://github.com/knative/serving/releases/latest/download/serving-core.yaml
kubectl apply -f https://github.com/knative-extensions/net-kourier/releases/latest/download/kourier.yaml
kubectl patch configmap/config-network \
  --namespace knative-serving \
  --type merge \
  --patch '{"data":{"ingress-class":"kourier.ingress.networking.knative.dev"}}'

# Instalar Knative Eventing
kubectl apply -f https://github.com/knative/eventing/releases/latest/download/eventing.yaml

# Reiniciar operador se Camel K já estava instalado
kubectl rollout restart deployment/camel-k-operator -n camel-k

# Verificar integração Knative
kubectl get integrationplatform -A -o yaml | grep knative
```

### Primeira Integração no Kubernetes

**Arquivo `file-to-api.yaml`:** [cite:6]
```yaml
# file-to-api.yaml
# Esta é uma Integração Camel K escrita em YAML DSL
- from:
    uri: "file:/data/inbox"
    parameters:
      delete: true
      include: "*.json"
    steps:
      # Log do arquivo sendo processado
      - log:
          message: "Processando arquivo: ${header.CamelFileName}"
      
      # Transformar dados
      - setHeader:
          name: Content-Type
          constant: application/json
      
      # Enviar para API REST
      - to:
          uri: "https://api.exemplo.com/pedidos"
          parameters:
            httpMethod: POST
      
      # Log de conclusão
      - log:
          message: "Completado: ${header.CamelFileName}"
```

**Implantar:**
```bash
# Implantar com EmptyDir montado como diretório de dados
kamel run file-to-api.yaml \
  --trait mount.empty-dirs=inbox-volume:/data

# Verificar status da integração
kamel get

# Ver logs
kamel logs file-to-api
```

### Integração Kafka para Banco de Dados

**Arquivo `kafka-to-database.java`:** [cite:6]
```java
// kafka-to-database.java
import org.apache.camel.builder.RouteBuilder;
import java.util.Map;

public class KafkaToDatabase extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // Tratamento de erros
        onException(Exception.class)
            .log("Erro ao processar pedido: ${exception.message}")
            .handled(true)
            .to("kafka:pedidos-dlq?brokers=kafka:9092");
        
        // Consumir do Kafka
        from("kafka:pedidos?brokers=kafka:9092&groupId=grupo-pedidos")
            .routeId("kafka-para-postgres")
            
            // Parse JSON
            .unmarshal().json()
            
            // Transformar para SQL
            .process(exchange -> {
                Map<String, Object> pedido = exchange.getIn().getBody(Map.class);
                
                String sql = String.format(
                    "INSERT INTO pedidos (pedido_id, cliente_id, total, status) " +
                    "VALUES ('%s', '%s', %s, 'processing')",
                    pedido.get("id"),
                    pedido.get("clienteId"),
                    pedido.get("total")
                );
                
                exchange.getIn().setBody(sql);
            })
            
            // Executar SQL
            .to("jdbc:camel")
            
            // Log sucesso
            .log("Inserido pedido, linhas afetadas: ${header.CamelJdbcUpdateCount}");
    }
}
```

**Implantar com credenciais de banco de dados:** [cite:6]
```bash
# Criar secret para conexão com banco
kubectl create secret generic db-credentials \
  --from-literal=quarkus.datasource.camel.username=usuario \
  --from-literal=quarkus.datasource.camel.password=senha

# Implantar integração
kamel run kafka-to-database.java \
  --property quarkus.datasource.camel.db-kind=postgresql \
  --property quarkus.datasource.camel.jdbc.url=jdbc:postgresql://postgres:5432/meubanco \
  --config secret:db-credentials \
  --dependency camel:jdbc \
  --dependency camel:kafka \
  --dependency mvn:org.postgresql:postgresql:42.7.11

# Monitorar integração
kamel logs kafka-to-database
```

### Content-Based Router no Kubernetes

**Arquivo `content-router.yaml`:** [cite:6]
```yaml
# content-router.yaml
- from:
    uri: "knative:channel/pedidos"
    steps:
      - choice:
          when:
            # Pedidos de alto valor
            - simple: "${body[total]} > 1000"
              steps:
                - log: "Pedido alto valor: ${body[id]}"
                - to: "knative:endpoint/processador-prioritario"
            
            # Pedidos nacionais
            - simple: "${body[pais]} == 'BR'"
              steps:
                - log: "Pedido nacional: ${body[id]}"
                - to: "knative:endpoint/processador-nacional"
            
            # Pedidos internacionais
            - simple: "${body[pais]} != 'BR'"
              steps:
                - log: "Pedido internacional: ${body[id]}"
                - to: "knative:endpoint/processador-internacional"
          
          # Rota padrão
          otherwise:
            steps:
              - log: "Pedido padrão: ${body[id]}"
              - to: "knative:endpoint/processador-padrao"
```

**Implantar:** [cite:6]
```bash
kamel run content-router.yaml \
  --profile knative \
  --trait knative-service.enabled=true \
  --trait knative-service.min-scale=0 \
  --trait knative-service.max-scale=10
```

### Agregação de APIs

**Arquivo `api-aggregator.groovy`:** [cite:6]
```groovy
// api-aggregator.groovy
import org.apache.camel.Exchange
import org.apache.camel.AggregationStrategy

from('timer:trigger?period=60000')
    .routeId('api-aggregator')
    
    // Chamar múltiplas APIs em paralelo e agregar resultados
    .multicast(new AggregationStrategy() {
        Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            if (oldExchange == null) {
                return newExchange
            }
            
            Map<String, Object> agregado = oldExchange.getMessage().getBody(Map)
            Map<String, Object> novosDados = newExchange.getMessage().getBody(Map)
            agregado.putAll(novosDados)
            
            oldExchange.getMessage().setBody(agregado)
            return oldExchange
        }
    })
        .parallelProcessing()
        .to('direct:buscar-usuarios', 'direct:buscar-pedidos', 'direct:buscar-inventario')
    .end()
    
    // Processar dados agregados
    .to('direct:processar-agregado')

// Rotas individuais de API
from('direct:buscar-usuarios')
    .to('https://api.exemplo.com/usuarios?bridgeEndpoint=true')
    .unmarshal().json()
    .process { exchange ->
        exchange.in.body = [usuarios: exchange.in.body]
    }

from('direct:buscar-pedidos')
    .to('https://api.exemplo.com/pedidos?bridgeEndpoint=true')
    .unmarshal().json()
    .process { exchange ->
        exchange.in.body = [pedidos: exchange.in.body]
    }

from('direct:buscar-inventario')
    .to('https://api.exemplo.com/inventario?bridgeEndpoint=true')
    .unmarshal().json()
    .process { exchange ->
        exchange.in.body = [inventario: exchange.in.body]
    }

from('direct:processar-agregado')
    .marshal().json()
    .to('kafka:dados-agregados?brokers=kafka:9092')
    .log('Dados agregados publicados')
```

**Implantar:** [cite:6]
```bash
kamel run api-aggregator.groovy \
  --dependency camel:http \
  --dependency camel:jackson \
  --trait cron.enabled=true \
  --trait cron.schedule="*/5 * * * *"
```

### Tratamento de Erros no Kubernetes

**Arquivo `error-handling.yaml`:** [cite:6]
```yaml
# error-handling.yaml
- onException:
    exception:
      - "java.lang.Exception"
    # Marcar como tratado para acknowledge da mensagem
    handled:
      constant:
        expression: "true"
    # Usar backoff exponencial para retries
    redeliveryPolicy:
      maximumRedeliveries: 3
      redeliveryDelay: 1000
      backOffMultiplier: 2
      useExponentialBackOff: true
    steps:
      - log:
          message: "Erro ao processar mensagem: ${exception.message}"
      - setHeader:
          name: error-mensagem
          simple: "${exception.message}"
      - setHeader:
          name: error-stacktrace
          simple: "${exception.stacktrace}"
      # Enviar para dead letter queue
      - to:
          uri: "kafka:pedidos-dlq?brokers=kafka:9092"

- route:
    id: "processamento-principal"
    from:
      uri: "kafka:pedidos?brokers=kafka:9092"
    steps:
      # Lógica principal de processamento
      - log:
          message: "Processando pedido: ${body}"
      - to:
          uri: "http://servico-pedidos/processar"
      - log:
          message: "Pedido processado com sucesso"
```

### Monitoramento no Kubernetes

**Comandos úteis:** [cite:6]
```bash
# Verificar status das integrações
kamel get

# Ver detalhes da integração
kubectl describe integration kafka-to-database

# Ver logs
kamel logs kafka-to-database

# Ver métricas
kubectl port-forward service/kafka-to-database 8080:8080
curl http://localhost:8080/q/metrics

# Ver health checks
curl http://localhost:8080/q/health
```

**Adicionar métricas customizadas:** [cite:6]
```java
// custom-metrics.java
import org.apache.camel.builder.RouteBuilder;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.concurrent.TimeUnit;

public class CustomMetrics extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        MeterRegistry registry = getContext()
            .getRegistry()
            .lookupByType(MeterRegistry.class)
            .iterator()
            .next();
        
        from("kafka:pedidos?brokers=kafka:9092")
            .routeId("processador-pedidos")
            
            // Capturar tempo de inéio do processamento
            .process(exchange -> {
                exchange.setProperty("start_time", System.currentTimeMillis());
            })
            
            // Incrementar contador
            .process(exchange -> {
                registry.counter("pedidos_processados_total",
                    "status", "recebido").increment();
            })
            
            // Processar pedido
            .to("direct:processar-pedido")
            
            // Registrar tempo de processamento
            .process(exchange -> {
                Long startTime = exchange.getProperty("start_time", Long.class);
                double duration = (System.currentTimeMillis() - startTime) / 1000.0;
                
                registry.timer("duracao_processamento_pedido_segundos")
                    .record(duration, TimeUnit.SECONDS);
            });
    }
}
```

### Boas Práticas Camel K [cite:6]

1. **Mantenha integrações focadas**: Cada integração deve lidar com um fluxo especáfico. Evite criar integrações monoláticas que fazem tudo.

2. **Use formatos de dados apropriados**: JSON para flexibilidade, Avro para evolução de schema, formatos binários para performance.

3. **Implemente tratamento de erros adequado**: Sempre configure error handlers e dead letter channels. Logue erros com contexto suficiente para debug.

4. **Otimize para serverless**: Design integrações para iniciar rapidamente e lidar com mensagens únicas eficientemente. Evite código de inicialização longa.

5. **Monitore performance**: Acompanhe throughput de mensagens, latência de processamento e taxas de erro. Configure alertas para anomalias.

6. **Versione suas integrações**: Use Git para rastrear mudançaas e implemente pipelines CI/CD adequados.

7. **Teste rigorosamente**: Escreva testes unitários para lógica de transformação e testes de integração para fluxos completos.

---

## Monitoramento e Observabilidade

### Novidades 4.22 em Observabilidade [cite:16]

- **Estatásticas de latência percentil**: p50, p95, p99 disponáveis via JMX, dev console, TUI e CLI
- **Instrumentação JFR em runtime**: `camel-jfr` emite eventos JFR durante roteamento de mensagens
- **SQL Trace dev console**: Captura e exibe queries SQL executadas por rotas Camel
- **Heap histogram**: Mostra contagem de instâncias e uso de bytes por classe
- **Decoradores de span Google Cloud**: Contexto de trace mais rico para serviços Google Cloud

### Habilitando Observabilidade

**Spring Boot (application.properties):**
```properties
# Habilitar observabilidade
camel.springboot.observability.enabled=true

# OpenTelemetry
otel.service.name=meu-servico-camel
otel.exporter.otlp.endpoint=http://otel-collector:4317

# Micrometer
management.endpoints.web.exposure.include=health,info,metrics,camelroutes
management.endpoint.health.show-details=always
```

**Dependências:**
```xml
<dependency>
    <groupId>org.apache.camel</groupId>
    <artifactId>camel-opentelemetry2</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.camel</groupId>
    <artifactId>camel-micrometer</artifactId>
</dependency>
```

**Nota:** `camel-opentelemetry` está depreciado. Migre para `camel-opentelemetry2` para estar pronto para o futuro. [cite:17]

### Dashboard Camel (Camel K)

Para monitoramento em tempo real no Kubernetes, use o **Camel Dashboard** com o componente `camel-observability-services`: [cite:5]

```yaml
# deployment.yaml
apiVersion: camel.apache.org/v1
kind: Integration
metadata:
  name: minha-integracao
spec:
  traits:
    observability:
      enabled: true
  sources:
  - content: |
      - from:
          uri: "kafka:pedidos?brokers=kafka:9092"
        steps:
          - log:
              message: "Processando: ${body}"
```

**Acessar dashboard:**
```bash
# Port-forward
kubectl port-forward service/minha-integracao 8080:8080

# Acessar
curl http://localhost:8080/q/metrics
curl http://localhost:8080/q/health
```

### Infraestrutura de Observabilidade Local

**Camel CLI 4.22:** [cite:16]
```bash
# Iniciar stack de observabilidade (Prometheus, VictoriaTraces, VictoriaLogs, Perses)
camel infra run observability

# Executar aplicação com observabilidade automática
camel run --observe minhas-rotas.yaml
```

Zero configuração necessária â€" Camel automaticamente coleta métricas e exporta traces e logs para a stack em execução. [cite:16]

### Métricas Customizadas

```java
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;

public class RotasComMetrics extends RouteBuilder {
    
    private Counter contadorPedidos;
    private Timer timerProcessamento;
    
    @Override
    public void configure() throws Exception {
        MeterRegistry registry = getContext()
            .getRegistry()
            .lookupByType(MeterRegistry.class)
            .iterator()
            .next();
        
        contadorPedidos = registry.counter("pedidos.processados.total");
        timerProcessamento = registry.timer("pedidos.processamento.duracao");
        
        from("kafka:pedidos?brokers={{kafka.brokers}}")
            .routeId("processamento-com-metrics")
            
            .process(exchange -> {
                long inicio = System.currentTimeMillis();
                exchange.setProperty("inicio", inicio);
            })
            
            .to("direct:processar")
            
            .process(exchange -> {
                contadorPedidos.increment();
                
                Long inicio = exchange.getProperty("inicio", Long.class);
                long duracao = System.currentTimeMillis() - inicio;
                timerProcessamento.record(duracao, TimeUnit.MILLISECONDS);
            });
    }
}
```

### Camel TUI para Monitoramento

O novo **Camel TUI** (Terminal User Interface) na 4.22 oferece mais de 30 abas para observação: [cite:16]

```bash
# Executar aplicação
camel run minhas-rotas.yaml

# O TUI é acessado via terminal com atalhos
# F1: Rotas
# F2: Endpoints
# F3: Consumidores
# F4: Atividade
# F5: Erros
# F6: Histórico
# F7: Diagramas
# F8: Assistente IA
# ... e mais 20+ abas
```

---

## Segurança em Produção

### Segurança por Padrão na 4.22 [cite:16]

Camel 4.22 continua o esforça de segurança por padrão:

- **Filtros de desserialização JEP-290**: Previne desserialização de classes não seguras
- **Listas de permissões dinâmicas de URI**: Para `toD` e `enrich`
- **Containment de download**: Para consumidores de armazenamento em nuvem
- **Prevenção de path traversal**: Em arquivos tar/zip
- **Correções de estratégia de filtro de headers**: Em mais componentes
- **Máscara de credenciais mais forte**: Em logs e URIs
- **Hardening de autenticação JWT**: Para servidor HTTP incorporado

O objetivo é que Camel seja seguro para rodar em produção sem precisar lembrar uma lista de verificação de opções de segurança para habilitar. [cite:16]

### Configurações de Segurança

**Palavras-chave sensiveis (mascaramento automático em logs):** [cite:16]
```properties
camel.main.additional-sensitive-keywords=minha-senha,token,api-key,secret,credential
```

**URI allow-lists dinâmicas:**
```java
// Permitir apenas URIs especáficas para toD
from("direct:roteamento-dinamico")
    .toD("http:{{endpoint.dinamico}}?allowlist=http://api1.com,http://api2.com");
```

**Prevenção de path traversal:**
```yaml
# Automático na 4.22 para componentes de arquivo
from:
  uri: "file:/data/uploads"
  parameters:
    # Camel previne automaticamente ../../../etc/passwd
```

### CVE e Vulnerabilidades

**CVEs recentes resolvidos na 4.22:** [cite:25][cite:26]

- **CVE-2026-46587**: Validação inadequada de entrada (resolvido em 4.18.3, 4.21.0, 4.22.0)
- **CVE-2026-40453**: Vulnerabilidade RCE (resolvido em 4.20.0+)

**Verificar CVEs nas dependências:**
```bash
# Camel MCP Server (4.22) [cite:16]
camel plugin get camel_dependency_security_audit

# Ou usar ferramentas como OWASP Dependency-Check
mvn org.owasp:dependency-check-maven:check
```

### Hardening Adicional

**Desabilitar componentes não usados:**
```properties
camel.main.routes-include-pattern=rotas/seguras/*.yaml
```

**Configurar perfil de segurança:**
```properties
camel.main.profile=prod
```

**Limitar URIs dinâmicas:**
```java
// Configurar allow-list no CamelContext
camelContext.setDynamicURIAllowList("http://api.exemplo.com/*,https://seguro.com/*");
```

---

## Referências e Recursos

### Documentação Oficial

- [Apache Camel 4.22 What's New](https://camel.apache.org/blog/2026/08/camel422-whatsnew/) [cite:16]
- [Apache Camel Manual](https://camel.apache.org/manual/)
- [Camel K Documentation](https://camel.apache.org/camel-k/next/)
- [Upgrade Guide 4.x](https://camel.apache.org/manual/camel-4x-upgrade-guide-4_21.html) [cite:21]

### Componentes Novos na 4.22 [cite:16]

- `camel-ai-tool`: Componente unificado de ferramenta IA
- `camel-clickhouse`: Banco de dados ClickHouse
- `camel-duckdb`: Banco de dados analático DuckDB
- `camel-jactl`: Linguagem de scripting Jactl

### Ferramentas

- **Camel CLI**: `curl -fsSL https://camel.apache.org/install.sh | sh`
- **Camel TUI**: Terminal UI com 30+ abas
- **Camel Upgrade Recipes**: Automatiza upgrade entre versões [cite:16]
- **Kaoto**: Designer visual de integrações no VS Code [cite:17]

### Comunidade

- [Apache Camel Blog](https://camel.apache.org/blog/)
- [Camel Integration Quarterly Digests](https://developers.redhat.com/blog/2026/07/23/camel-integration-quarterly-digest-q2-2026/) [cite:2][cite:3][cite:4]
- [GitHub apache/camel](https://github.com/apache/camel)

### Livros e Cursos

- "Enterprise Integration Patterns" por Gregor Hohpe e Bobby Woolf
- "Camel in Action" (2nd Edition)
- Red Hat Training: Integration with Apache Camel

---

## Apêndice: Exemplo Completo de Projeto

### Estrutura Completa

```
meu-projeto-camel-completo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/madrugas/
│   │   │       ├── Application.java
│   │   │       ├── config/
│   │   │       │   ├── DataSourceConfig.java
│   │   │       │   └── SecurityConfig.java
│   │   │       ├── routes/
│   │   │       │   ├── RotasPedidos.java
│   │   │       │   ├── RotasClientes.java
│   │   │       │   └── RotasIntegracao.java
│   │   │       └── processors/
│   │   │           ├── ValidadorPedido.java
│   │   │           └── EnricherCliente.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-prod.properties
│   │       └── routes/
│   │           ├── pedidos.yaml
│   │           └── clientes.yaml
│   └── test/
│       └── java/
│           └── com/madrugas/
│               └── routes/
│                   └── RotasPedidosTest.java
├── kubernetes/
│   ├── deployment.yaml
│   ├── service.yaml
│   ├── configmap.yaml
│   └── secret.yaml
├── pom.xml
├── Dockerfile
├── README.md
└── .gitignore
```

### Application.java

```java
@SpringBootApplication
@EnableAutoConfiguration
public class Application {
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### RotasPedidos.java

```java
@Component
public class RotasPedidos extends RouteBuilder {
    
    @Override
    public void configure() throws Exception {
        // Error handler global
        errorHandler(deadLetterChannel("kafka:dlq?brokers={{kafka.brokers}}")
            .maximumRedeliveries(3)
            .redeliveryDelay(1000)
            .logStackTrace(true));
        
        // Exceções especáficas
        onException(ValidationException.class)
            .handled(true)
            .log("Validação falhou: ${exception.message}")
            .to("kafka:erros-validacao?brokers={{kafka.brokers}}");
        
        // Rota principal
        from("kafka:pedidos?brokers={{kafka.brokers}}&groupId=grupo-processador")
            .routeId("processamento-pedidos")
            
            // Validar
            .to("direct:validar-pedido")
            
            // Enriquecer com dados do cliente
            .to("direct:enriquecer-pedido")
            
            // Salvar no banco
            .to("direct:salvar-pedido")
            
            // Publicar evento
            .to("kafka:pedidos-processados?brokers={{kafka.brokers}}")
            
            // Log
            .log("Pedido processado: ${body.id}");
        
        // Sub-rotas
        from("direct:validar-pedido")
            .routeId("validacao-pedido")
            .process(new ValidadorPedido());
        
        from("direct:enriquecer-pedido")
            .routeId("enriquecimento-pedido")
            .enrich("direct:buscar-cliente", new EnricherCliente());
        
        from("direct:salvar-pedido")
            .routeId("salvar-pedido")
            .to("jdbc:meuDataSource");
    }
}
```

### application.properties

```properties
# Camel
camel.springboot.main-run-controller=true
camel.springboot.jmx-enabled=true
camel.main.profile=prod

# Kafka
kafka.brokers=kafka-prod:9092
kafka.username=${KAFKA_USERNAME}
kafka.password=${KAFKA_PASSWORD}

# Database
spring.datasource.url=jdbc:postgresql://postgres:5432/meubanco
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# Observabilidade
camel.springboot.observability.enabled=true
otel.service.name=meu-servico-camel
otel.exporter.otlp.endpoint=http://otel-collector:4317

# Segurança
camel.main.additional-sensitive-keywords=password,secret,token,api-key
```

### deployment.yaml (Kubernetes)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: meu-servico-camel
  labels:
    app: camel
spec:
  replicas: 3
  selector:
    matchLabels:
      app: camel
  template:
    metadata:
      labels:
        app: camel
    spec:
      containers:
      - name: camel
        image: registry.exemplo.com/meu-servico-camel:1.0.0
        ports:
        - containerPort: 8080
        env:
        - name: KAFKA_USERNAME
          valueFrom:
            secretKeyRef:
              name: kafka-credentials
              key: username
        - name: KAFKA_PASSWORD
          valueFrom:
            secretKeyRef:
              name: kafka-credentials
              key: password
        - name: DB_USERNAME
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: username
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: password
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /q/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /q/health
            port: 8080
          initialDelaySeconds: 10
          periodSeconds: 5
```

---

*Este documento foi criado em Setembro de 2026 e cobre Apache Camel 4.22 LTS. Para atualizações, consulte a documentação oficial e o blog do Apache Camel.* [cite:16][cite:27]