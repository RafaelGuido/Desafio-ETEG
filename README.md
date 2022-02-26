# Desafio Técnico - Empresa ETEG

### Descrição do projeto :seedling:

Desenvolver uma aplicação web para fazer uma gestão básica de estoque de uma locadora de filmes.

### Entidades(+atributos básicos) :pushpin:

- Filme (nome, gênero, diretor, quantidade).
- Usuário (nome, sexo, CPF).
- Locação (filme, usuario, data de devolução, etc).
- Histórico de locação: Registros das locações de filmes.

### Funcionalidades :sparkles:
- CRUD usuário:
- Regra 1: validar consistência dos dados, como CPF e data de nascimento.
- Regra 2: validar obrigatoriedade dos dados, como nome e CPF.
- Regra 3: só serão aceitos cadastros de usuários com mais de 18 anos.
- CRUD filme:
- Regra 1: um usuário pode alugar no máximo 5 filmes por vez.
- Regra 2: o filme deve estar disponível em estoque.
- Renovar locação: dado uma locação, renová-la para um número X de dias.
- Regra 1: Limitar o número de renovações em no máximo duas ocorrências.
- Consultar histórico de locação.

### Observações :rotating_light:

- Não utilizar Spring-Data-REST, queremos ver a forma como o candidato estrutura o RestController.
- Apenas reforçando: os requisitos e regras citadas acima são apenas uma proposta. Elas podem ser alteradas e novas regras podem ser adicionadas.
- É um diferencial o desenvolvimento de testes automatizados.
- Utilizar banco de dados relacional e JPA.

### Pré-requisitos :thumbsup:

- JDK 8 e Maven
- IntelliJ ou Eclipse

### Foi utilizado :point_down:

- Gitmoji Plus (plugin)

### Instalação da aplicação :point_down:

- IntelliJ/Eclipse: Importar como projeto Maven.
    
### Iniciar aplicação :point_down:

- Rodar a classe DesafioetegApplication
