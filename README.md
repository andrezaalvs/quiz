# 📱 Quiz — Aplicativo Android com Firebase

Aplicativo mobile desenvolvido para a plataforma **Android** que permite a execução de quizzes de forma dinâmica, com armazenamento de dados local e em nuvem.

**Trabalho acadêmico** da disciplina **Programação para Dispositivos Móveis (PDM)** — Trabalho 04.

---

## 👥 Autores

- **Andreza Batista Alves**
- **Giovanna Vida Krempel**
- **Keila Almeida Santana**
- **Samira Rodrigues Silva**

---

## 🛠 Tecnologias

| Área | Tecnologias |
|------|-------------|
| **Linguagem** | Kotlin |
| **UI** | Jetpack Compose |
| **Autenticação** | Firebase Authentication |
| **Banco em nuvem** | Firebase Firestore |
| **Banco local** | Room Database |
| **Arquitetura** | MVVM |

---

## ✨ Funcionalidades

- **Autenticação de usuários** — login e cadastro com Firebase Auth  
- **Quizzes por tema** — execução de quizzes com diferentes categorias  
- **Histórico de desempenho** — armazenamento local e em nuvem dos resultados  
- **Modo offline** — uso do app e sincronização quando houver conexão  
- **Ranking global** — lista de melhores pontuações com destaque para o **pódio** (1º, 2º e 3º lugares)  

---

## 📲 Telas

| Tela | Descrição |
|------|------------|
| **Login** | Acesso com e-mail e senha |
| **Registro** | Cadastro de novo usuário |
| **Dashboard** | Menu principal após login |
| **Categorias** | Escolha do tema do quiz |
| **Quiz** | Resolução das perguntas |
| **Histórico** | Lista de resultados anteriores |
| **Ranking** | Ranking global com pódio dos 3 primeiros |

### Capturas de tela

<div align="center">

**Primeira fileira**

| | | | |
|:---:|:---:|:---:|:---:|
| ![Login](telas/WhatsApp%20Image%202026-02-26%20at%2015.01.37%20(1).jpeg) | ![Registro](telas/WhatsApp%20Image%202026-02-26%20at%2015.01.37%20(2).jpeg) | ![Dashboard](telas/WhatsApp%20Image%202026-02-26%20at%2015.01.37%20(3).jpeg) | ![Categorias](telas/WhatsApp%20Image%202026-02-26%20at%2015.01.37%20(4).jpeg) |
| *Login* | *Registro* | *Dashboard* | *Categorias* |

**Segunda fileira**

| | | | |
|:---:|:---:|:---:|:---:|
| ![Quiz](telas/WhatsApp%20Image%202026-02-26%20at%2015.01.37%20(5).jpeg) | ![Tela 6](telas/WhatsApp%20Image%202026-02-26%20at%2015.01.37%20(6).jpeg) | ![Histórico](telas/WhatsApp%20Image%202026-02-26%20at%2015.01.37%20(7).jpeg) | ![Ranking](telas/WhatsApp%20Image%202026-02-26%20at%2015.01.37.jpeg) |
| *Quiz* | *Tela 6* | *Histórico* | *Ranking* |

</div>

---

## 🏗 Estrutura do projeto

```
quiz/
├── app/
│   └── src/main/
│       ├── java/com/example/quiz/
│       │   ├── data/          # Room, DAOs, Repository, Firebase
│       │   ├── model/         # User, Question, QuizResult
│       │   ├── ui/screens/    # Telas em Compose
│       │   ├── viewmodel/    # AuthViewModel, QuizViewModel
│       │   └── MainActivity.kt
│       ├── res/
│       └── AndroidManifest.xml
├── docs/                     # Relatório e Trabalho 04 PDM
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 📋 Pré-requisitos

- **Android Studio** (recomendado: versão recente)
- **JDK 11**
- **Android SDK** — `minSdk 26`, `targetSdk 35`, `compileSdk 35`
- Projeto **Firebase** configurado com:
  - Authentication (e-mail/senha)
  - Firestore
- Arquivo `app/google-services.json` do seu projeto Firebase na pasta `app/`

---

## 🚀 Como executar

1. **Clone o repositório**
   ```bash
   git clone https://github.com/andrezaalvs/quiz.git
   cd quiz
   ```

2. **Configure o Firebase**
   - Crie um projeto em [Firebase Console](https://console.firebase.google.com/)
   - Ative **Authentication** (método E-mail/Senha) e **Firestore**
   - Baixe o `google-services.json` e coloque em `app/`

3. **Abra no Android Studio**
   - File → Open → selecione a pasta `quiz`
   - Aguarde o Gradle sincronizar

4. **Execute**
   - Conecte um dispositivo ou inicie um emulador
   - Run → Run 'app' (ou `Shift+F10`)

---

## 🔧 Dificuldades no desenvolvimento

- **Funcionamento offline** — implementação e sincronização com a nuvem  
- **Sincronização** — integração entre Room e Firebase Firestore  
- **Autenticação e dados** — vínculo entre usuário logado e armazenamento  
- **Ranking** — critérios de desempate e destaque visual do pódio (1º, 2º, 3º)  

---

## 🤖 Uso de LLMs no projeto

Ferramentas baseadas em **Modelos de Linguagem de Grande Escala (LLMs)** foram usadas como apoio em:

- **Design** — criação das telas de login e ranking (gradientes, animações, destaque do pódio)  
- **Lógica** — organização do cálculo do ranking e integração Firebase + Room  
- **Resolução de erros** — dependências e configuração do projeto  

---

## 📄 Documentação acadêmica

- `docs/RELATORIO QUIZ.docx` — relatório do projeto  
- `docs/Trabalho 04 PDM.pdf` — trabalho da disciplina PDM  

---

## 📜 Licença

Projeto desenvolvido com fins acadêmicos. Uso livre para estudo e referência.
