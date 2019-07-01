UCDb: classificações e reviews de cursos da UFCG

Back-end do projeto da disciplina Projeto de Software UFCG - 2019.1.

Alunos:
* Igor Santa Ritta Seabra
* Victor Paz de Farias Braga

O back-end foi escrito em Java e é uma API REST que usa o sistema Model-Service-Controller para genenciar o banco de dados e responder aos pedidos. As requisições HTTP são recebidas por um dos Controllers (dependendo da rota), que repassa o pedido ao Service correspondente, que usa um DAO para acessar o banco de dados e trata as respostas de acordo com o que foi pedido. Quase todas as operações de gerenciamento e tratamento de dados são feitas no back-end. Algumas das rotas (de acordo com a especificação do projeto) precisam do filtro de autenticação (JWT) para devolverem a resposta esperada - o perído de validade do token escolhido foi de apenas 5 minutos, pois é suficiente para a demonstração das funcionalidades do sistema (inclusive a de não aceitar mais requisições após o vencimento do token).

Repositório do front-end no GitHub: https://github.com/igorseabra4/psoft-2019.1-projeto-frontEnd
Repositório do back-end no GitHub: https://github.com/igorseabra4/psoft-2019.1-projeto-backEnd

Link para o deployment da aplicação (front-end) no Heroku: https://psoft-igor-victor-front.herokuapp.com/
Link para o deployment do back-end no Heroku: https://projeto-psoft-igor-victor.herokuapp.com/

O back-end permite requisições HTTP de qualquer cliente, desde que sejam conhecidas as rotas e os métodos permitidos para cada uma.
