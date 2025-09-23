# Gerenciador De Contas Bancaria Com Spring

Sistema de gerenciamento de contas bancárias, desenvolvido como parte do curso de Programação de Computadores 2 no IFSP.

O projeto implementa funcionalidades de CRUD (Criar, Ler, Atualizar, Deletar) para contas, além de serviços para filtrar, ordenar e agrupar contas.

## Tecnologias Utilizadas

* **Backend:** Java (JDK 21+), Spring Boot (v3.5.5), Spring Web
* **Banco de Dados:** MySQL
* **Build Tool:** Gradle
* **Frontend:** HTML / CSS / JavaScript

---

## ⚙️ Instalação e Execução

Siga estes passos para configurar e executar a aplicação.

### Passo 1: Descompactar o Projeto e Preparar o Banco de Dados

1.  **Descompacte o arquivo `.zip`** do projeto em uma pasta de sua preferência.
2.  **Localize o arquivo de script SQL** que acompanha o projeto (ex: `banco_digital.sql`) ele fica localizado em: `src\main\java\app\resources\db\banco_digital.sql`.
3.  **Execute o Script no MySQL:** Use o cliente de linha de comando do MySQL ou uma ferramenta gráfica (MySQL Workbench, DBeaver) para executar o arquivo `.sql`. Isso criará o banco de dados (`banco_digital`) e todas as tabelas necessárias.
    * *Exemplo via linha de comando (após conectar no mysql):*
        ```sql
        source /caminho/completo/para/o/banco_digital.sql;
        ```

### Passo 2: Configurar a Conexão com o Banco de Dados

A aplicação precisa das suas credenciais para se conectar ao banco de dados que você acabou de criar.

1.  Abra o projeto em um editor de código (como o VS Code).
2.  Navegue e abra o arquivo de `conexao.java`: `src\main\java\app\resources\dao\Conexao.java`.
3.  Edite as seguintes linhas com as informações do seu banco de dados:

    ```java
        private static final String URL = "jdbc:mysql://servidor:porta/banco_de_dados";
        private static final String USER = "user";
        private static final String PASSWORD = "password";
    ```

### Passo 3: Construir e Executar o Projeto com Gradle

Com o banco de dados pronto, podemos iniciar o servidor.

1.  **Abra um Terminal:** Abra um terminal (PowerShell, CMD, Git Bash, etc.) **dentro da pasta raiz do projeto** (a pasta que contém o arquivo `gradlew`).

2.  **(Apenas para Linux/macOS)** Dê permissão de execução ao script do Gradle:
    ```bash
    chmod +x gradlew
    ```

3.  **Construir o Projeto (Build):** Este comando utiliza o "Gradle Wrapper" para baixar todas as dependências da internet (pode demorar alguns minutos na primeira vez) e compilar o código.

    ```bash
    # No Windows
    .\gradlew build

    # No Linux ou macOS
    ./gradlew build
    ```
    Aguarde até que a mensagem **`BUILD SUCCESSFUL`** apareça.

4.  **Executar a Aplicação:** Após o build, este comando iniciará o servidor da aplicação Spring Boot.

    ```bash
    # No Windows
    .\gradlew bootRun

    # No Linux ou macOS
    ./gradlew bootRun
    ```
    Aguarde até que o console mostre a mensagem `Tomcat started on port(s): 8080 (http)`. Isso significa que o backend está no ar.

---

## 🚀 Como Acessar e Testar a Aplicação

Com o servidor backend rodando, você pode acessar e testar a API diretamente ou interagir com ela através da interface frontend.

### Acessando a API Backend

A API estará disponível em `http://localhost:8080`. Você pode usar uma ferramenta como o **Hoppscotch** ou **Postman** para enviar requisições e testar os endpoints.

* **Exemplo para listar contas:**
    * `GET` `http://localhost:8080/api/contas/corrente`

* **Exemplo para filtrar contas:**
    * `GET` `http://localhost:8080/api/services/filter/FiltroBySaldoMaiorQ?saldoMin=1000`

* **Verifique outras rotas nas classes de controle em:**
    * `src\main\java\app\controllers`

### Acessando a Interface Frontend

Existem duas maneiras de executar o frontend, dependendo da sua necessidade.

#### Método 1: Live Server

1.  **Instale a Extensão:** No VS Code, instale a extensão **"Live Server"**.
2.  **Execute o Servidor:** Na pasta do projeto, encontre o diretório `view/`. Clique com o botão direito no arquivo `index.html` (ou no seu arquivo HTML principal) e selecione **"Open with Live Server"**.
3.  **Acesse no Navegador:** O Live Server abrirá seu navegador em uma URL própria, geralmente `http://localhost:5500` ou `http://127.0.0.1:5500`.

#### Método 2: Servido pelo Backend

Neste método, o próprio servidor Spring Boot entrega os arquivos do frontend.

1.  **Copie os Arquivos:** Copie todo o conteúdo da pasta `src/view/` para a pasta `src/main/resources/static` do projeto backend.
2.  **Acesse no Navegador:** Com o backend rodando (`./gradlew bootRun`), abra o navegador e acesse a URL principal da aplicação: **`http://localhost:8080`**.

---

### ⚠️ Configuração de CORS (Obrigatório para o Método 1)

Para que o **Método 1 (Live Server)** funcione, o backend (rodando na porta `8080`) precisa dar permissão para que o frontend (rodando na porta `5500`) possa fazer requisições a ele.

1.  **Abra o arquivo de configuração de CORS** do projeto, localizado em `app/config/WebConfig.java`.
2.  **Encontre a linha `.allowedOrigins(...)`**.
3.  **Adicione a URL do Live Server** à lista de origens permitidas.

    ```java
    // Dentro de WebConfig.java
    
    @Configuration
    public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/api/**")
                // Adicione a URL do seu Live Server aqui
                .allowedOrigins("http://localhost:5500", "http://127.0.0.1:5500") // Altere essa linha caso nenhuma das urls seja a sua
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
        }
    }
    ```
4.  Reinicie a aplicação backend para que a nova configuração de CORS seja aplicada.