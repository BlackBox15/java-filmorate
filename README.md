# java-filmorate
Template repository for Filmorate project.

![Filmorate (1)](https://github.com/BlackBox15/java-filmorate/assets/99198001/0f5ab172-8af2-4b38-9af6-1fa235249483)

Я вынес лист друзей в таблицу friends, в которую входят ID пользователя и его друзей.
Подтверждение дружбы можно обнаружить при условии принадлежности friend_id (17) к user_id (4) и поиска обратной принадлежности friend_id (4) к user_id (17).
Соответственно, если поиск даёт пересечение - значит, заявка на дружбу была принята.

user_id  |  friend_id
---------|------------
3        |    5
4        |    8 
3        |    13
4        |    17
17       |    4

