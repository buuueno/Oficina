# Oficina Mecânica — Parte 2: Design Patterns (GoF)

| Exigência da tarefa | O que aplicamos | Onde |
|---|---|---|
| 3 dos 4 padrões vistos em sala | **Prototype** | `Servico.clonar()` + `ServicoFactory` |
| | **Decorator** | pasta `decorator/` (em volta de `Servico`) |
| | **Observer** | `OrdemServico` + pasta `observer/` |
| Padrões extras do livro GoF | **State** | pasta `state/` (etapas da ordem) |
| | **Strategy** | pasta `strategy/` (formas de pagamento) |

**Por que não Abstract Factory?** Ele serve para criar *famílias* de objetos que precisam combinar entre si (ex.: um kit inteiro de peças de uma montadora). A oficina não tem famílias assim, então o padrão ficaria forçado. Os outros três resolvem problemas reais do nosso sistema.

---

## Como rodar

**VS Code:** a configuração "Oficina - Main" continua em `.vscode/launch.json`. É só dar Run.

**Terminal (PowerShell):**
```powershell
javac -encoding UTF-8 -d bin (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp bin oficina.Main
```

> Se os acentos aparecerem como `?` no terminal, rode `chcp 65001` antes. A saída completa está em `docs/saida-esperada.txt`.

## O que mudou em relação à Parte 1

Os cálculos da Parte 1 continuam iguais (a ordem 1 do Marlon continua dando R$ 428,00).

| Arquivo | Mudança |
|---|---|
| `Servico.java` | implementa `Cloneable` e ganhou o método `clonar()` (**Prototype**) |
| `ServicoFactory.java` | monta cada serviço uma vez como modelo e devolve cópias (**Prototype**) |
| `OrdemServico.java` | o `boolean fechada` virou um objeto `EstadoOrdem` (**State**); ganhou `iniciar()`, `concluir()` e a lista de observadores (**Observer**) |
| `OficinaService.java` | `registrarObservador()`, `iniciarServico()`, `concluirServico()`, `adicionarServico()` aceitando um serviço pronto e `fecharOrdem()` recebendo a forma de pagamento (**Strategy**) |
| `Main.java` | demonstra cada padrão em blocos separados |
| **Novos:** `decorator/`, `state/`, `observer/`, `strategy/` | |

Regra nova: a ordem só pode ser **fechada quando está pronta** (Aberta → Em andamento → Pronta → Fechada).

Diagrama de classes: `docs/diagrama-classes.png`

---

## 1. Prototype

**Ideia do livro:** criar objetos novos *copiando* uma instância-modelo (o protótipo), em vez de montar do zero.

**Problema na oficina:** na Parte 1, a `ServicoFactory` montava o serviço do zero a cada pedido (`new` e depois `adicionarPeca` uma por uma). Agora cada serviço padrão é montado **uma vez** como modelo, e a fábrica sempre entrega uma **cópia**. Assim cada ordem pode ajustar a sua (a revisão da Anna ganhou discos de freio) sem estragar o modelo.

**Analogia:** documento modelo. Você duplica o modelo e edita a cópia; o modelo fica intacto para o próximo.

| Papel no livro | Classe |
|---|---|
| Prototype | `Servico` (método `clonar()`) |
| ConcretePrototype | `TrocaDeOleo`, `Alinhamento`, `RevisaoCompleta` |
| Gerenciador de protótipos | `ServicoFactory` (o `Map` de modelos) |

**Onde olhar:** em `Servico.clonar()`, o `clone()` do Java copia campo a campo e depois criamos uma **lista nova** de peças. No `Main`, a revisão da Anna fica com 3 peças e uma revisão nova saindo da fábrica continua com 2.

## 2. Decorator

**Ideia do livro:** acrescentar responsabilidades a um objeto *em tempo de execução*, como alternativa flexível à herança.

**Problema na oficina:** o cliente pode pedir adicionais (lavagem, leva e traz) em cima de qualquer serviço. Com herança, cada combinação seria uma classe nova (`RevisaoComLavagem`, `TrocaDeOleoComLevaETraz`...).

**Analogia:** lanche com adicionais. O lanche continua sendo um lanche; cada adicional "embrulha" o anterior e soma no preço.

| Papel no livro | Classe |
|---|---|
| Component | `Servico` (classe abstrata da Parte 1) |
| ConcreteComponent | `TrocaDeOleo`, `Alinhamento`, `RevisaoCompleta` |
| Decorator | `ServicoAdicional` (abstrata) |
| ConcreteDecorator | `Lavagem` (+R$ 50), `LevaETraz` (+R$ 40) |

**Onde olhar:** `ServicoAdicional` **é** um `Servico` (`extends`) e **tem** um `Servico` (atributo). Ela repassa as chamadas para o serviço embrulhado, e cada adicional soma o seu valor em `calcularMaoDeObra()`.
```java
Servico revisaoComAdicionais = new LevaETraz(new Lavagem(revisaoDaAnna));
// mão de obra: 470 + 50 + 40 = 560 | peças: 840 | total: 1.400
```

## 3. State (padrão extra do livro)

**Ideia do livro:** permitir que um objeto mude de comportamento quando o seu estado interno muda; parece que o objeto "trocou de classe".

**Problema na oficina:** o que a ordem pode fazer depende da etapa em que ela está. Aberta aceita serviço e pode começar; pronta só pode ser fechada; fechada não aceita mais nada. Sem o padrão, cada método da `OrdemServico` teria um `if/switch` perguntando o status.

**Analogia:** semáforo. O mesmo botão de pedestre faz coisas diferentes conforme a cor atual, e cada cor sabe qual vem depois.

| Papel no livro | Classe |
|---|---|
| Context | `OrdemServico` |
| State | `EstadoOrdem` (classe abstrata: por padrão, tudo é proibido) |
| ConcreteState | `Aberta`, `EmAndamento`, `Pronta`, `Fechada` |

**Onde olhar:** `OrdemServico.iniciar()`, `concluir()` e `fechar()` só repassam para o estado atual: `mudarEstado(estado.iniciar(this))`. O estado devolve o próximo estado (ex.: `Aberta.iniciar()` devolve `new EmAndamento()`) ou lança `OficinaException` se a ação não é permitida.
```
Aberta --iniciar--> EmAndamento --concluir--> Pronta --fechar--> Fechada
```

## 4. Observer

**Ideia do livro:** quando um objeto muda de estado, todos os que dependem dele são avisados automaticamente (relação um-para-muitos).

**Problema na oficina:** quando a ordem muda de etapa, várias partes precisam saber: o cliente (WhatsApp), o painel da recepção e o financeiro. Sem o padrão, a `OrdemServico` teria que conhecer e chamar cada uma delas.

**Analogia:** inscrição num canal do YouTube. O canal avisa todos os inscritos e não sabe o que cada um faz com a notificação.

| Papel no livro | Classe |
|---|---|
| Subject (sujeito) | `OrdemServico` |
| Observer | `ObservadorOrdem` (interface) |
| ConcreteObserver | `NotificacaoWhatsApp`, `PainelRecepcao`, `Financeiro` |

**Onde olhar:** `OrdemServico.mudarEstado()` troca o estado e chama `notificarObservadores()`, que chama `atualizar(this)` em cada um. É aqui que **State e Observer se encontram**. A `OficinaService.abrirOrdem()` registra os observadores em toda ordem nova.

## 5. Strategy (padrão extra do livro)

**Ideia do livro:** definir uma família de algoritmos, encapsular cada um e torná-los intercambiáveis; o algoritmo muda sem mudar quem o usa.

**Problema na oficina:** cada forma de pagamento calcula o valor final de um jeito (Pix tem 5% de desconto, crédito acima de 3x tem 5% de acréscimo). Sem o padrão, o `fecharOrdem()` viraria um `if/else` que cresce a cada forma nova.

**Analogia:** a maquininha de cartão. O caixa só passa o valor; a regra fica dentro da forma de pagamento escolhida.

| Papel no livro | Classe |
|---|---|
| Strategy | `FormaPagamento` (interface) |
| ConcreteStrategy | `PagamentoPix`, `PagamentoDebito`, `PagamentoCredito` |
| Context | `OficinaService` (método `fecharOrdem`) |

**Onde olhar:** `fecharOrdem(numero, forma)` não tem nenhum `if`, só chama `forma.calcularValorFinal(...)`. No `Main`, o mesmo total passa pelas 4 formas num `for` (polimorfismo).

---

## Requisitos da Parte 1 que continuam valendo

- **Factory:** `ServicoFactory` continua centralizando a criação dos serviços; agora ela cria por cópia (Prototype).
- **Injeção de dependência:** repositório no construtor da `OficinaService` (Parte 1); observadores com `registrarObservador()`; cada decorador recebe o `Servico` no construtor; `fecharOrdem()` recebe a `FormaPagamento`.
- **SOLID:** DIP da Parte 1 (`OrdemRepository`) e agora também **OCP (aberto/fechado)**: dá para criar um adicional, uma etapa, um observador ou uma forma de pagamento novos **sem alterar as classes que os usam**.
- **Herança/polimorfismo, coleções e try/catch:** `Servico` e `EstadoOrdem` com suas subclasses, `List`/`Map`, e os 6 blocos de erro no fim do `Main`.

---

## Perguntas prováveis na defesa

**Prototype**
- *O que o `clone()` do Java faz?* Cria um objeto da mesma classe real (`TrocaDeOleo`, `RevisaoCompleta`...) copiando cada campo. Para campos que são objetos, como a lista, ele copia só a **referência**, ou seja, faz uma cópia **rasa**.
- *Então por que criar a lista nova?* Sem isso, o modelo e a cópia dividiriam a mesma lista e a peça adicionada na revisão da Anna apareceria em todas as revisões futuras.
- *E os objetos `Peca`, são compartilhados?* Sim, mas `Peca` não tem setters, ou seja, não muda depois de criada. Compartilhar é seguro, então copiar a lista basta.
- *Por que `implements Cloneable` e o `try/catch`?* `Cloneable` é uma interface marcadora; sem ela o `clone()` lança `CloneNotSupportedException`. O `catch` é obrigatório porque essa exceção é checked, mas nunca acontece.
- *Por que `clonar()` só em `Servico` e não em cada subclasse?* Porque o `clone()` já cria o objeto da classe real e copia também os campos das subclasses (ex.: `taxaDiagnostico` da `RevisaoCompleta`).
- *Isso ainda é Factory?* Sim. A fábrica continua escondendo as classes concretas e centralizando a criação. O Prototype mudou só **como** ela cria: copiando um modelo em vez de montar do zero.

**Decorator**
- *Por que não herança?* Uma subclasse por combinação (explosão de classes), e a combinação ficaria fixa em tempo de compilação.
- *Por que `ServicoAdicional` estende `Servico` **e** tem um `Servico`?* Estende para poder ocupar o lugar de qualquer serviço (a ordem não percebe a diferença e dá para empilhar). Tem um para repassar as chamadas para quem está embrulhado.
- *O decorador não herda uma lista de peças que não usa?* Herda, porque `Servico` é uma classe abstrata com atributos (da Parte 1). Por isso `ServicoAdicional` sobrescreve `getPecas()`, `calcularPecas()` e `adicionarPeca()` para usar as peças do serviço embrulhado; a lista herdada fica vazia. O livro recomenda manter o componente "leve" justamente por isso; se começássemos do zero, `Servico` seria uma interface.
- *A ordem dos decoradores importa?* No valor não (é soma); na descrição sim (muda a ordem do texto).

**State**
- *Por que não um enum com `switch`?* Cada regra nova viraria mais um `case` espalhado em vários métodos da ordem. Com State, as regras de cada etapa ficam juntas na classe da etapa.
- *Quem decide o próximo estado?* O estado atual: `Aberta.iniciar()` devolve `new EmAndamento()`. A ordem só troca e avisa os observadores. Como `mudarEstado()` é privado, ninguém de fora consegue pular etapas.
- *Por que `EstadoOrdem` é classe abstrata e não interface?* Para ter o comportamento padrão num lugar só: toda ação lança "não é possível". Cada estado sobrescreve só o que pode fazer (`Fechada` não sobrescreve nada).
- *Qual a diferença entre State e Strategy?* A estrutura é parecida (a classe guarda um objeto e repassa a ele), mas a intenção muda. No Strategy **quem chama escolhe** a estratégia (o cliente escolhe Pix). No State **o próprio objeto troca** de estado conforme as ações acontecem.
- *Por que o Financeiro usa `instanceof Pronta`?* Ele só precisa saber se a ordem chegou na etapa "Pronta" para preparar a cobrança; é uma checagem de tipo simples.
- *Como adicionar a etapa "Aguardando peça"?* Criar `AguardandoPeca extends EstadoOrdem` e fazer `EmAndamento` poder ir para ela. A `OrdemServico` não muda.

**Observer**
- *Por que não chamar o WhatsApp direto dentro da ordem?* A ordem ficaria acoplada a todas as notificações, e cada novo interessado exigiria alterá-la.
- *Por que tem lista de observadores na `OficinaService` e na `OrdemServico`?* O sujeito é a `OrdemServico`: é ela que guarda e notifica. A `OficinaService` só guarda quem deve ser registrado em cada ordem nova, para não registrar ordem por ordem.
- *É push ou pull?* **Pull.** A ordem passa ela mesma (`this`) e cada observador busca o que precisa (`getEstado()`, `getVeiculo()`).
- *Como adicionar e-mail?* Criar `NotificacaoEmail implements ObservadorOrdem` e registrar com `registrarObservador()`. Nada mais muda.
- *Por que o Financeiro recebe aviso de todas as etapas?* O sujeito avisa todos igualmente; cada observador decide se reage. O Financeiro só age quando a ordem fica pronta.

**Strategy**
- *Por que não um `switch` no `fecharOrdem()`?* Cada forma nova obrigaria a mexer na `OficinaService` (fere o OCP). Com Strategy cada regra fica isolada na sua classe.
- *Diferença entre Strategy e Decorator?* Strategy **troca** o algoritmo inteiro; Decorator **acrescenta** comportamento em volta de um objeto que continua existindo.

**Geral**
- *Por que `double` e não `BigDecimal` para dinheiro?* Simplicidade no trabalho. Num sistema real, `BigDecimal` evita erros de arredondamento.
