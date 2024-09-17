# java-filmorate
## Схема базы данных
![Схема базы данных](/src/main/resources/bd_diagram.png)
## Описание таблиц
### Таблица user
В данной таблице хранится основная информация о пользователях.
### Таблица film
В данной таблице хранится основная информация о фильмах.
### Таблица friend
В данной таблице хранится информация о друзьях пользователей.
Запись

| user_id | friend_id | is_confirmed |
|---------|-----------|--------------|
| 1       | 2         | true         | 

означает, что пользовтаель с id=1, отправил запрос на добавление в друзья
пользователю с id=2, и пользователь с id=2 её принял.
### Таблица film_like
В данной таблице хранится информация о лайках, поставленных пользователями фильмам.

Запись

| user_id | film_id |
|---------|---------|
| 1       | 2       |

означает, что пользовтаелю с id=1, понравилься фильм с id=2.

### Таблица genre
В данной таблице хранится информация о жанрах.

### Таблица film_genre
В данной таблице хранится информация о связи фильиов и жанров. 
Один фильм может быть отнесен к нескольким жанрам.

### Таблица rating
В данной таблице хранится информация о рейтингах, которые могут быть поставлены фильмам. 
Фильму может назначаться только один рейтинг.

## Примеры запросов

### Выбор общей информации по всем фильмам

``` SQL
SELECT film.name name, description, release_date, duration, rating.name rating
FROM film
LEFT JOIN rating ON film.rating_id = rating.id
```

### Выбор общей информации фильма по идентификатору

``` SQL
SELECT film.name name, description, release_date, duration, rating.name rating
FROM film
LEFT JOIN rating ON film.rating_id = rating.id
WHERE film.id = 1
```

### Выбор данных 10 самых популярных фильмов, основываясь на количество лайков

``` sql
SELECT
	film.name name,
	top_likes.likes,
	description,
	release_date,
	duration,
	rating.name rating
FROM
	film
INNER JOIN (
	SELECT
		film_id,
		count(user_id) likes
	FROM
		film_like
	GROUP BY
		film_id
	ORDER BY
		count(user_id) desc
	LIMIT 10
) AS top_likes ON
	film.id = top_likes.film_id
LEFT JOIN rating ON
	film.rating_id = rating.id
ORDER BY
	top_likes.likes desc
```

### Выбор общей информации по всем пользователям

``` sql
SELECT id, name, email, login, birthday
FROM "user"
```

### Выбор общей информации пользователя по идентификатору

``` sql
SELECT id, name, email, login, birthday
FROM "user"
WHERE id = 1
```

### Выбор общей информации друзей пользователя

``` sql
SELECT name, email, login, birthday
FROM "user" AS u 
INNER JOIN (
	SELECT
		friend_id 
	FROM
		friend
	WHERE
		user_id = 1 AND is_confirmed ) AS user_friend
		ON user_friend.friend_id = u.id
```

### Выбор общих друзей для двух пользователей

``` sql
SELECT
	friend_user_1.friend_id
FROM
	(
	SELECT
		friend_id
	FROM
		friend
	WHERE
		user_id = 1
		AND is_confirmed
) AS friend_user_1
INNER JOIN (
	SELECT
		friend_id
	FROM
		friend
	WHERE
		user_id = 2
		AND is_confirmed) AS friend_user_2
ON
	friend_user_1.friend_id = friend_user_2.friend_id
```

