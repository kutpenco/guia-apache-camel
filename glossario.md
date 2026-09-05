# Glossário Apache Camel e do projeto

Este glossário reúne os principais termos usados no
[`guia-apache-camel.md`](./guia-apache-camel.md), no código da aplicação
Camel Orders e na infraestrutura Docker.

## Apache Camel

**Java 25** — Versão LTS do JDK usada pelo projeto para compilação, testes e
execução local e nos containers Docker.

**Apache Camel** — Framework open source para integração de sistemas. Conecta
APIs, bancos de dados, arquivos, filas e serviços cloud por meio de rotas,
componentes e padrões de integração.

**Camel 4.22 LTS** — Linha de longo suporte descrita no guia. LTS significa
*Long-Term Support*, ou suporte de longo prazo.

**CamelContext** — Container principal de execução do Camel. Gerencia ciclo de
vida, componentes, endpoints, rotas e configurações.

**Camel K** — Distribuição e operador Kubernetes para executar integrações
Camel diretamente no Kubernetes.

**Camel CLI** — Interface de linha de comando para executar, desenvolver,
diagnosticar e gerar diagramas de integrações Camel.

**Camel JBang** — Forma de executar código ou rotas Camel com JBang, sem a
necessidade de criar previamente um projeto Maven completo.

**Camel TUI** — Interface de terminal para monitorar e operar rotas, endpoints,
consumidores, erros, métricas e outros recursos Camel.

## Mensagens e execução

**Exchange** — Envelope de processamento que transporta uma mensagem,
propriedades, headers e informações de erro durante uma rota.

**Message** — Conteúdo transportado por um `Exchange`. Possui *body* e
*headers*.

**Body** — Corpo principal da mensagem, como JSON, texto, XML ou um objeto Java.

**Header** — Metadado associado à mensagem, como método HTTP, tipo de conteúdo,
identificador de correlação ou nome de arquivo.

**Property** — Valor associado ao `Exchange` durante todo o processamento,
independentemente da troca interna de mensagens.

**Endpoint** — URI que identifica a origem ou o destino de uma mensagem, por
exemplo `direct:processar`, `kafka:pedidos` ou `file:/data/inbox`.

**Component** — Implementação que fornece um tipo de endpoint e sua integração
com uma tecnologia, como HTTP, Kafka, JDBC, File ou S3.

**Producer** — Parte que envia uma mensagem para um endpoint.

**Consumer** — Parte que recebe mensagens de um endpoint de entrada.

**ProducerTemplate** — API Java usada para enviar mensagens para endpoints
Camel a partir de código ou testes.

**Processor** — Código Java que inspeciona ou transforma um `Exchange`.

**RouteBuilder** — Classe base usada para declarar rotas Camel em Java.

**Route** — Fluxo de processamento que começa em um endpoint de entrada e
passa por etapas até um ou mais destinos.

**Route ID** — Identificador único e legível de uma rota, usado em logs,
monitoramento e testes.

## DSLs e linguagens

**DSL (Domain-Specific Language)** — Linguagem específica de domínio usada
para expressar integrações. O projeto e o guia usam DSLs em Java, YAML e XML.

**Java DSL** — Declaração programática de rotas usando classes como
`RouteBuilder`, por exemplo:

```java
from("direct:entrada")
    .routeId("exemplo")
    .log("Mensagem: ${body}")
    .to("direct:saida");
```

**YAML DSL** — Representação declarativa de rotas em YAML, adequada para
configuração, automação e execução pelo Camel CLI/Camel K.

**XML DSL** — Representação declarativa de rotas em XML, comum em configurações
Spring e integrações legadas.

**Simple Language** — Linguagem de expressões do Camel para acessar body,
headers, propriedades, datas e condições, como `${body}` e `${header.id}`.

**JSONPath** — Linguagem para consultar campos em documentos JSON.

**XPath** — Linguagem para consultar nós e valores em documentos XML.

**Jactl** — Linguagem de scripting que pode ser usada em rotas Camel.

## Enterprise Integration Patterns (EIPs)

**EIP** — *Enterprise Integration Pattern*, padrão reutilizável para resolver
problemas recorrentes de integração entre sistemas.

**Content-Based Router** — Encaminha uma mensagem para destinos diferentes
conforme seu conteúdo, headers ou propriedades.

**Splitter** — Divide uma mensagem composta em várias mensagens menores para
processamento individual.

**Multicast** — Envia a mesma mensagem para vários destinos, em sequência ou
paralelamente.

**Circuit Breaker** — Interrompe temporariamente chamadas para um serviço que
está falhando, evitando sobrecarga e permitindo recuperação.

**Aggregator** — Combina várias mensagens relacionadas em uma única mensagem
com base em uma chave de correlação e uma estratégia de agregação.

**Enricher** — Enriquece uma mensagem com dados obtidos de outra fonte ou rota.

**Publish-Subscribe** — Publica um evento para múltiplos consumidores
interessados, sem acoplá-los diretamente ao produtor.

**Dead Letter Channel (DLC)** — Estratégia de tratamento de erros que redireciona
mensagens que não puderam ser processadas para um destino de falha.

**DLQ (Dead Letter Queue)** — Fila usada para armazenar mensagens rejeitadas ou
que excederam o número de tentativas.

**Redelivery** — Nova tentativa de processamento de uma mensagem após uma falha.

**Retry** — Política de repetição que define quantidade, intervalo e estratégia
das novas tentativas.

**Idempotência** — Propriedade que garante que processar a mesma mensagem mais
de uma vez produz o mesmo efeito final.

**Throttling** — Limitação controlada da taxa de mensagens ou chamadas para
proteger recursos e serviços downstream.

**Enrichment** — Processo de adicionar informações externas ao corpo ou aos
headers da mensagem.

## Integrações e componentes

**REST** — Estilo de comunicação HTTP usado pela API de pedidos.

**HTTP** — Protocolo usado para expor APIs e chamar serviços externos.

**Kafka** — Plataforma distribuída de eventos e mensageria usada para publicar
e consumir mensagens em tópicos.

**Broker** — Servidor que recebe, armazena e entrega mensagens; no Compose,
o Kafka atua como broker.

**Topic** — Canal nomeado do Kafka onde eventos são publicados e consumidos.

**Consumer group** — Grupo de consumidores Kafka que divide o processamento de
partições entre suas instâncias.

**JDBC** — API Java para conexão e execução de operações em bancos relacionais.

**PostgreSQL** — Banco relacional disponibilizado no Docker Compose.

**Data source** — Configuração reutilizável de conexões com um banco de dados.

**File component** — Componente Camel para consumir, produzir e mover arquivos.

**OpenAI** — Serviço de inteligência artificial apresentado no guia como
exemplo de integração.

**S3** — Serviço de armazenamento de objetos da AWS e também padrão de API
suportado por vários provedores.

**FTP/SFTP** — Protocolos usados para transferência de arquivos; SFTP adiciona
o transporte seguro sobre SSH.

## Aplicação Camel Orders

**Order** — Modelo de pedido da aplicação, com `id`, `customerId`, `total`,
`country` e `status`.

**OrderValidator** — Processor que valida campos obrigatórios e o valor
positivo do pedido.

**OrderRepository** — Serviço que armazena pedidos em memória usando uma
estrutura concorrente.

**PROCESSING** — Status atribuído a um pedido após validação e persistência.

**Actuator** — Módulo do Spring Boot que expõe endpoints operacionais, como
health checks e métricas.

**Health check** — Verificação que indica se a aplicação está disponível e
saudável, neste projeto em `/actuator/health`.

**Métrica** — Medição operacional de uma aplicação, como contagem, duração ou
taxa de erros.

## Configuração, observabilidade e segurança

**Spring Boot** — Framework usado para inicializar a aplicação Java, injetar
dependências e integrar o Camel ao ambiente web.

**Maven** — Ferramenta de build e gerenciamento de dependências Java usada pelo
projeto.

**Profile** — Conjunto de configurações ativadas por ambiente, como `dev` e
`prod`.

**Variável de ambiente** — Valor externo usado para configurar portas,
credenciais e endpoints sem alterar o código.

**OpenTelemetry (OTel)** — Padrão e conjunto de ferramentas para coletar
traces, métricas e logs de aplicações distribuídas.

**OTLP** — Protocolo usado para enviar telemetria ao coletor OpenTelemetry.

**Micrometer** — Fachada de métricas usada pelo Spring Boot e integrada ao
ecossistema de observabilidade.

**JFR (Java Flight Recorder)** — Ferramenta da JVM para registrar eventos de
execução, desempenho e diagnóstico.

**Prometheus** — Sistema de coleta e consulta de métricas baseado em séries
temporais.

**Tracing** — Rastreamento do caminho de uma requisição por vários serviços.

**Span** — Unidade individual de trabalho dentro de um trace distribuído.

**Mascaramento de credenciais** — Ocultação de senhas, tokens e chaves em logs
e URIs.

**CVE** — Identificador público de vulnerabilidades de segurança conhecidas.

**Hardening** — Aplicação de configurações e controles para reduzir a
superfície de ataque.

## Containers e Kubernetes

**Dockerfile** — Arquivo com instruções para construir a imagem da aplicação.

**Docker Compose** — Ferramenta para definir e executar a aplicação e seus
serviços dependentes em conjunto.

**Imagem** — Artefato imutável usado como base para criar containers.

**Container** — Instância isolada de um serviço executando a partir de uma
imagem.

**Volume** — Armazenamento persistente ou compartilhado entre containers.

**Kubernetes** — Plataforma para orquestrar containers em escala.

**Deployment** — Recurso Kubernetes que descreve réplicas e atualização de
pods.

**Service** — Recurso Kubernetes que fornece uma forma estável de acessar pods.

**ConfigMap** — Recurso Kubernetes para configurações não sensíveis.

**Secret** — Recurso Kubernetes para credenciais e valores sensíveis.

**Knative** — Plataforma Kubernetes para workloads orientados a eventos e
serverless.

## Testes

**JUnit 5** — Framework de testes usado para verificar o comportamento da
aplicação.

**CamelSpringBootTest** — Suporte de teste que inicializa o contexto Camel
integrado ao Spring Boot.

**Teste unitário** — Teste focado em uma unidade isolada, como um Processor.

**Teste de rota** — Teste que exercita o fluxo de uma ou mais rotas Camel.

**Teste de integração** — Teste que valida a colaboração entre componentes,
contexto, rotas e serviços reais ou simulados.

**Mock** — Substituto controlado de um componente real usado para verificar
mensagens, chamadas e resultados durante testes.
