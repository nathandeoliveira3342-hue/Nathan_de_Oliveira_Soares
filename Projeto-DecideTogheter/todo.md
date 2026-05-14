# DecideTogether - TODO

## Funcionalidades Principais

### Landing Page & Navegação
- [x] Landing page com apresentação do app
- [x] Botão "Criar Sala" na landing page
- [x] Botão "Entrar em Sala" na landing page
- [x] Navegação entre páginas (Home → Room → Conference)

### Autenticação & Salas
- [x] Tela de criação de sala com formulário (nome do usuário + nome da sala)
- [x] Tela de entrada em sala com formulário (nome do usuário + nome da sala)
- [x] Validação de campos (não vazio, comprimento mínimo)
- [x] Armazenamento de dados de sala no banco de dados

### Videoconferência
- [x] Integração com Jitsi Meet External API (Web SDK)
- [x] Renderização do iframe do Jitsi Meet
- [x] Configuração de sala prefixada no Jitsi
- [x] Controles de câmera e microfone
- [x] Detecção de participantes conectados

### Sistema de Votação
- [x] Criar votação com pergunta e múltiplas opções (2-6 opções)
- [x] Dialog/modal para criação de nova votação
- [x] Armazenamento de votações no banco de dados
- [x] Registro de votos dos participantes
- [x] Cálculo de percentuais em tempo real

### Overlay de Votação
- [x] Exibir overlay de votação sobre o vídeo
- [x] Mostrar pergunta da votação no overlay
- [x] Listar opções de votação com barras de progresso
- [x] Animar barras de progresso em tempo real
- [x] Destacar visualmente a opção vencedora (troféu/cor especial)
- [x] Mostrar timer regressivo durante a votação

### Timer & Encerramento
- [x] Toggle para ativar/desativar timer
- [x] Opções de timer: 30s, 60s, 2min
- [x] Timer regressivo visual
- [x] Encerramento automático da votação ao fim do tempo
- [x] Botão para encerrar votação manualmente

### Histórico de Decisões
- [x] Exibir painel de histórico de decisões
- [x] Listar todas as votações encerradas da sessão
- [x] Mostrar pergunta, opção vencedora e percentual
- [x] Mostrar horário de encerramento de cada votação

### Design & Responsividade
- [x] Dark mode completo com paleta roxa e ciano
- [x] CSS variables para cores (roxa, ciano, backgrounds)
- [x] Layout responsivo para desktop
- [x] Layout responsivo para tablet
- [x] Layout responsivo para mobile
- [x] Animações fluidas de transição entre telas
- [x] Animações das barras de progresso

### Backend & Banco de Dados
- [x] Tabela de salas (rooms)
- [x] Tabela de votações (votes)
- [x] Tabela de opções de votação (vote_options)
- [x] Tabela de votos dos usuários (user_votes)
- [x] Tabela de histórico de decisões (decision_records)
- [x] Procedures tRPC para criar sala
- [x] Procedures tRPC para entrar em sala
- [x] Procedures tRPC para criar votação
- [x] Procedures tRPC para registrar voto
- [x] Procedures tRPC para encerrar votação
- [x] Procedures tRPC para obter histórico

### Testes
- [x] Testes unitários para procedures tRPC
- [x] Testes de validação de formulários
- [x] Testes de cálculo de percentuais

## Status

✅ **Todas as funcionalidades implementadas e testadas com sucesso!**

O site DecideTogether está pronto para ser publicado e acessado permanentemente.
