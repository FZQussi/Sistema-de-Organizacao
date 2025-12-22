# 🚗 Sistema de Organização e Gestão de Estacionamento

Aplicação de **terminal (CLI)** desenvolvida em **Java**, com **Maven**, para gestão completa de um estacionamento, incluindo veículos, pagamentos, funcionários, turnos e salários, com persistência em ficheiros, logs e testes automatizados.

---

## 📌 Visão Geral

Este projeto implementa um **Sistema de Gestão de Estacionamento** com múltiplas funcionalidades administrativas e operacionais, permitindo:

* Controlo de entradas e saídas de veículos
* Cálculo automático de pagamentos
* Gestão de funcionários (operadores e gerentes)
* Controlo de turnos, horários e atrasos
* Cálculo mensal de salários
* Persistência de dados em ficheiros
* Registo de logs
* Testes unitários com JUnit

A aplicação foi desenhada com foco em **organização, modularidade, separação de responsabilidades e boas práticas em Java**.

---

## 🛠️ Tecnologias Utilizadas

* **Java (JDK 17+)**
* **Maven**
* **Log4j 2** – Logging da aplicação
* **Gson** – Persistência em JSON
* **jBCrypt** – Encriptação segura de passwords
* **JUnit 5** – Testes unitários
* **Aplicação CLI (Terminal)**

---

## 📂 Estrutura do Projeto

```text
com.example
├── app
│   └── Main.java
│
├── model
│   ├── Carro.java
│   ├── Estacionamento.java
│   ├── MovimentoEstacionamento.java
│   ├── Utilizador.java
│   └── Operador.java
│
├── service
│   ├── AuthService.java
│   ├── UserService.java
│   ├── TurnoService.java
│   ├── PaymentService.java
│   ├── GestaoEstacionamento.java
│   ├── GestaoPagamentos.java
│   └── RegistarEntradaService.java
│
├── utils
│   ├── FileUtils.java
│   ├── PasswordUtils.java
│   ├── MovimentosUtils.java
│   ├── CalculadoraTempo.java
│   └── ConsoleUtils.java
│
└── resources
    └── log4j2.xml
```

---

## 🚘 Funcionalidades

### Gestão de Estacionamento

* Registo de **entrada e saída de veículos**
* Controlo de **capacidade máxima**
* Cálculo automático do **tempo estacionado**
* Cálculo de **preço por permanência**
* Registo de movimentos em ficheiro

### Pagamentos e Lucros

* Registo de pagamentos por veículo
* Persistência em ficheiro (`pagamentos.txt`)
* Base para análise de **lucros do estacionamento**

### Gestão de Funcionários

* Criação, edição e remoção de utilizadores
* Perfis distintos:

  * **Operador**
  * **Gerente**
* Passwords encriptadas com **BCrypt**
* Persistência em ficheiro JSON

### Turnos e Horários

* Registo automático de:

  * Entrada prevista vs. real
  * Saída prevista vs. real
* Cálculo de atrasos
* Registo detalhado de turnos

### Cálculo de Salários

* Cálculo mensal automático
* Considera:

  * Horas trabalhadas
  * Atrasos
  * Salário por hora
* Registo do pagamento mensal

---

## 💾 Persistência de Dados

Os dados são guardados localmente na pasta:

```text
SistemaOrganizacaoData/
```

Contendo:

* `users.json` – Utilizadores
* `turnos.txt` – Turnos e horários
* `pagamentos.txt` – Pagamentos e salários
* `movimentos.txt` – Entradas e saídas
* `nacionalidades.json` – Lista base de nacionalidades

A estrutura é criada automaticamente no arranque da aplicação.

---

## 📜 Logging

A aplicação utiliza **Log4j 2** para:

* Informação operacional
* Avisos
* Erros
* Debug técnico

Facilita **auditoria, manutenção e diagnóstico de erros**.

---

## 🧪 Testes

* Testes unitários com **JUnit 5**
* Foco em regras de negócio e serviços
* Execução via Maven:

```bash
mvn test
```

---

## ▶️ Executar o Projeto

### Pré-requisitos

* Java JDK 17 ou superior
* Maven

### Passos

```bash
git clone <url-do-repositorio>
cd nome-do-projeto
mvn clean install
mvn exec:java
```

Ou executar diretamente a classe `Main`.

---

## 📈 Melhorias Futuras

* Interface gráfica (JavaFX)
* Base de dados relacional (MySQL/PostgreSQL)
* Relatórios financeiros
* Sistema de permissões avançado
* Exportação de dados (CSV / PDF)

---

## 👤 Autor

Projeto desenvolvido para fins **académicos e de aprendizagem**, com foco em:

* Programação Orientada a Objetos
* Arquitetura em camadas
* Persistência de dados
* Boas práticas em Java

---

