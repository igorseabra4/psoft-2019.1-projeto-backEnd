UCDb: classificações e reviews de cursos da UFCG

Back-end do projeto da disciplina Projeto de Software UFCG - 2019.1.

Alunos:
* Igor Santa Ritta Seabra
* Victor Paz de Farias Braga

O back-end foi escrito em Java e é uma API REST que usa o sistema Model-Service-Controller para genenciar o banco de dados e responder aos pedidos. As requisições HTTP são recebidas por um dos Controllers (dependendo da rota), que repassa o pedido ao Service correspondente, que usa um DAO para acessar o banco de dados e trata as respostas de acordo com o que foi pedido. Quase todas as operações de gerenciamento e tratamento de dados são feitas no back-end. Algumas das rotas (de acordo com a especificação do projeto) precisam do filtro de autenticação (JWT) para devolverem a resposta esperada - o perído de validade do token escolhido foi de apenas 5 minutos, pois é suficiente para a demonstração das funcionalidades do sistema (inclusive a de não aceitar mais requisições após o vencimento do token).

As seguintes rotas estão presentes na API:

Rotas que não necessitam autenticação:
* POST	/api/v1/auth			Cria um novo usuário no sistema. No corpo deve ser passado um JSON com os atributos: firstName, lastName, email, password. Retorna o usuário criado.
* POST	/api/v1/auth/login		Realiza login do usuário no sistema. No corpo deve ser passado um JSON com os atributos: email, password. Retorna o token, nome do usuário e ID.
* GET	/api/v1/auth/withID		Retorna o nome do usuário com ID passada no parâmetro 'id'.
* GET	/api/v1/courses			Retorna uma lista com todas as disciplinas (ID e nome).
* GET	/api/v1/courses/substring	Retorna uma lista com todas as disciplinas (ID e nome) que são da ID ou contém no nome o valor passado no parâmetro 'str'.

Rotas que necessitam autenticação (JWT Bearer):
* GET		/api/v1/courses/rank			Retorna uma lista com todas as disciplinas ordenadas por número decrescente de likes, depois por nota, depois pela quantidade de comentários.
* GET		/api/v1/courses/profile/{id}		Retorna o perfil da disciplina com o ID especificado no caminho, com ID, nome, lista de IDs de usuários que curtiram, quantidade de curtidas, mapa de IDs de usuários para as notas que deram, nota média e lista de IDs de comentários.
* PUT		/api/v1/courses/profile/{id}/like	Dá um like na disciplina com o ID especificado no caminho pelo usuário com ID especificada no corpo, caso esse usuário não já o tenha feito.
* DELETE	/api/v1/courses/profile/{id}/like	Remove o like na disciplina com o ID especificado no caminho do usuário com ID especificada no corpo, caso haja like desse usuário na disciplina.
* PUT		/api/v1/courses/profile/{id}/grade	Dá uma nota para a disciplina com o ID especificado no caminho. A nota e ID do usuário que deu devem ser especificadas no corpo. Substitui a nota anterior caso o usuário já tenha dado nota.
* GET		/api/v1/courses/profile/{id}/comments	Retorna a lista de comentários que foram dados à disciplina, com ID do comentário, ID do usuário que comentou, nome do usuário que comentou, o comentário em si, ID do comentário pai caso seja resposta a outro (-1 caso não), objeto data com a data e hora que o comentário foi feito e um valor booleano que indica se o comentário já foi deletado (nesse caso, o campo do comentário em si estará vazio).
* PUT		/api/v1/courses/profile/{id}/comment	Adiciona um comentário à disciplina com o ID especificado no caminho. No corpo devem haver ID do usuário, nome do usuário, o comentário em si e a ID de um comentário pai caso seja resposta a outro (-1 caso não).
* DELETE	/api/v1/courses/profile/{id}/comment	Marca um comentário da disciplina com o ID especificado no caminho como deletado. No corpo devem haver ID do usuário e do comentário a ser apagado.

As seguintes rotas foram criadas apenas para teste das funcionalidades da API. Não há implementação para acessá-las no front-end do projeto, e elas seriam removidas ou limitadas a um tipo específico de usuário em uma aplicação real. Elas necessitam autenticação (JWT Bearer):
* GET	/api/v1/courses/comments			Retorna a lista de todos os comentários existentes na API, sem remover o conteúdo de comentários deletados.
* PUT	/api/v1/courses/resetcomments			Marca todos os comentários previamente marcados como deletados de volta como ativos.
* PUT	/api/v1/courses/profile/{id}/resetcomments	Marca todos os comentários de uma disciplina específica previamente marcados como deletados de volta como ativos.

Repositório do front-end no GitHub: https://github.com/igorseabra4/psoft-2019.1-projeto-frontEnd
Repositório do back-end no GitHub: https://github.com/igorseabra4/psoft-2019.1-projeto-backEnd

Link para o deployment da aplicação (front-end) no Heroku: https://psoft-igor-victor-front.herokuapp.com/
Link para o deployment do back-end no Heroku: https://projeto-psoft-igor-victor.herokuapp.com/

O back-end permite requisições HTTP de qualquer cliente, desde que sejam conhecidas as rotas e os métodos permitidos para cada uma.
