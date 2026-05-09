# DecideTogether

**Plataforma de videoconferência para tomada de decisões em grupo**

> Converse por vídeo e realize votações interativas em tempo real — tudo em um único app Android nativo.

## Preview

Link para acesso:([DecideTogheter](https://decidetalk-m78vmgoz.manus.space))
<img width="175" height="236" alt="Captura de tela 2026-05-09 144332" src="https://github.com/user-attachments/assets/c9fbfacf-99ab-4952-be35-3d2248df1100" />

---

## Visão Geral

O **DecideTogether** é um aplicativo Android nativo desenvolvido em **Kotlin**, que combina videoconferência (via SDK do Jitsi Meet) com um sistema de votação em tempo real. Ideal para reuniões onde decisões precisam ser tomadas de forma democrática e transparente.

---

## Funcionalidades

| Funcionalidade | Descrição |
|---|---|
| Criar Sala | Cria uma sala de videoconferência com nome personalizado |
| Entrar em Sala | Entra em uma sala existente pelo nome |
| Videoconferência | Chamada de vídeo em tempo real via Jitsi Meet |
| Nova Votação | Cria votações com pergunta e múltiplas opções |
| Votação em Tempo Real | Participantes votam e veem os resultados instantaneamente |
| Timer de Votação | Encerramento automático após 30s, 60s ou 2min |
| Histórico de Decisões | Registro de todas as votações encerradas na sessão |

---

## Telas do Aplicativo

```
Splash Screen → Home → Criar/Entrar em Sala → Videoconferência + Votação
```

1. **Splash Screen** — Logo animado com transição automática
2. **Home** — Botões "Criar Sala" e "Entrar em Sala"
3. **Criar/Entrar em Sala** — Formulário com nome do usuário e nome da sala
4. **Videoconferência** — Jitsi Meet com overlay de votação e histórico

---

## Tecnologias Utilizadas

| Componente | Tecnologia |
|---|---|
| Linguagem | Kotlin 1.9.23 |
| Plataforma | Android nativo (minSdk 29 / Android 10+) |
| Build System | Gradle 8.6 com KTS |
| Videoconferência | Jitsi Meet SDK 9.2.2 |
| UI | Views XML + ViewBinding |
| Arquitetura | MVVM (preparado para expansão) |
| Navegação | Intent-based |
| Animações | ObjectAnimator + ViewPropertyAnimator |

---

## Pré-requisitos

- **Android Studio** Hedgehog (2023.1.1) ou superior
- **JDK 17**
- **Android SDK** com API 34 instalado
- **Emulador** ou dispositivo físico com Android 10+ (API 29+)
- Conexão com a internet (necessária para o Jitsi Meet)

---

## Como Abrir no Android Studio

1. Clone ou extraia o projeto:
   ```bash
   git clone https://github.com/seu-usuario/DecideTogether.git
   # ou extraia o ZIP baixado
   ```

2. Abra o **Android Studio**

3. Selecione **"Open"** (ou **File → Open...**)

4. Navegue até a pasta `DecideTogether/` e clique em **"OK"**

5. Aguarde o **Gradle sync** concluir (pode levar alguns minutos na primeira vez)

6. Se solicitado, instale os SDKs ou ferramentas ausentes

---

## Como Executar

### No Emulador

1. No Android Studio, abra o **AVD Manager** (Device Manager)
2. Crie um dispositivo virtual com Android 10+ (API 29+)
3. Inicie o emulador
4. Clique no botão **▶ Run** (ou `Shift+F10`)

### Em Dispositivo Físico

1. Habilite o **Modo Desenvolvedor** no dispositivo:
   - Configurações → Sobre o telefone → toque 7x em "Número da versão"
2. Ative a **Depuração USB**:
   - Configurações → Opções do desenvolvedor → Depuração USB
3. Conecte o dispositivo via USB
4. Clique no botão **▶ Run** no Android Studio

---

## Como Gerar o APK

### APK de Debug (para testes)

```bash
# Via terminal na raiz do projeto
./gradlew assembleDebug
```

O APK será gerado em:
```
app/build/outputs/apk/debug/app-debug.apk
```

### APK de Release (para distribuição)

1. No Android Studio: **Build → Generate Signed Bundle/APK**
2. Selecione **APK**
3. Crie ou selecione um **Keystore** de assinatura
4. Selecione o build type **release**
5. Clique em **Finish**

Ou via terminal (após configurar o keystore no `build.gradle.kts`):
```bash
./gradlew assembleRelease
```

---

## Estrutura do Projeto

```
DecideTogether/
├── app/
│   ├── src/main/
│   │   ├── java/com/decidetogether/
│   │   │   ├── DecideTogetherApp.kt          # Application class
│   │   │   ├── adapter/
│   │   │   │   ├── VoteOptionAdapter.kt       # Adapter das opções de votação
│   │   │   │   └── DecisionHistoryAdapter.kt  # Adapter do histórico
│   │   │   ├── model/
│   │   │   │   ├── Vote.kt                    # Modelo de votação
│   │   │   │   ├── VoteOption.kt              # Modelo de opção
│   │   │   │   └── DecisionRecord.kt          # Modelo de histórico
│   │   │   ├── ui/
│   │   │   │   ├── splash/SplashActivity.kt   # Tela 1: Splash
│   │   │   │   ├── home/HomeActivity.kt       # Tela 2: Home
│   │   │   │   ├── room/RoomActivity.kt       # Tela 3: Criar/Entrar
│   │   │   │   ├── conference/
│   │   │   │   │   └── ConferenceActivity.kt  # Tela 4: Videoconferência
│   │   │   │   └── voting/
│   │   │   │       └── CreateVoteDialog.kt    # Dialog de nova votação
│   │   │   └── util/
│   │   │       └── Constants.kt               # Constantes globais
│   │   ├── res/
│   │   │   ├── layout/                        # Layouts XML das telas
│   │   │   ├── drawable/                      # Ícones e backgrounds vetoriais
│   │   │   ├── values/                        # Strings, cores, temas, dimens
│   │   │   ├── anim/                          # Animações de transição
│   │   │   └── font/                          # Fonte Inter (adicionar manualmente)
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   ├── libs.versions.toml                     # Versões centralizadas (TOML)
│   └── wrapper/gradle-wrapper.properties
├── build.gradle.kts
├── settings.gradle.kts
├── .gitignore
└── README.md
```

---

## Configuração do Jitsi Meet

O app usa o servidor público **meet.jit.si** por padrão. Para usar um servidor privado:

1. Abra `app/src/main/java/com/decidetogether/util/Constants.kt`
2. Altere a constante `JITSI_SERVER_URL`:
   ```kotlin
   const val JITSI_SERVER_URL = "https://seu-servidor-jitsi.com"
   ```

### Adicionar Fonte Inter (Opcional)

Para usar a fonte Inter conforme o design:

1. Baixe os arquivos em [fonts.google.com/specimen/Inter](https://fonts.google.com/specimen/Inter)
2. Coloque os arquivos `.ttf` em `app/src/main/res/font/`:
   - `inter_regular.ttf`
   - `inter_medium.ttf`
   - `inter_bold.ttf`

---

## Exportar para GitHub

```bash
cd DecideTogether
git init
git add .
git commit -m "feat: projeto inicial DecideTogether"
git branch -M main
git remote add origin https://github.com/seu-usuario/DecideTogether.git
git push -u origin main
```

---

## Compatibilidade

| Requisito | Valor |
|---|---|
| Android mínimo | Android 10 (API 29) |
| Android alvo | Android 14 (API 34) |
| Arquiteturas | arm64-v8a, armeabi-v7a, x86_64 |
| Orientação | Portrait (retrato) |

---

## Licença

Este projeto é de uso livre para fins educacionais e pessoais.

---

*Desenvolvido com Kotlin e Android Studio.*
