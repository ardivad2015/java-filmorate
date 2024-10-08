# java-filmorate
## Схема базы данных
![Схема базы данных](/src/main/resources/bd_diagram.png)
## Описание таблиц
### Таблица users
В данной таблице хранится основная информация о пользователях.
### Таблица films
В данной таблице хранится основная информация о фильмах.
### Таблица friends
В данной таблице хранится информация о друзьях пользователей.
Запись

| user_id | friend_id |
|---------|-----------|
| 1       | 2         |

означает, что пользователь с id=1, отправил запрос на добавление в друзья
пользователю с id=2.
### Таблица films_likes
В данной таблице хранится информация о лайках, поставленных пользователями фильмам.

Запись

| user_id | film_id |
|---------|---------|
| 1       | 2       |

означает, что пользователю с id=1, понравился фильм с id=2.

### Таблица genres
В данной таблице хранится информация о жанрах.

### Таблица films_genres
В данной таблице хранится информация о связи фильмов и жанров. 
Один фильм может быть отнесен к нескольким жанрам.

### Таблица film_ratings
В данной таблице хранится информация о рейтингах, которые могут быть поставлены фильмам. 
Фильму может назначаться только один рейтинг.

## Примеры запросов

### Выбор общей информации по всем фильмам

``` SQL
SELECT film_id, film_name, description, release_date, duration, rating_id
FROM films
```

### Выбор общей информации фильма по идентификатору

``` SQL
SELECT film_name, description, release_date, duration, rating_id
FROM films
WHERE film_id = 1
```

### Выбор данных 10 самых популярных фильмов, основываясь на количество лайков

``` sql
SELECT
    films.film_id,
	film_name,
	top_likes.likes,
	description,
	release_date,
	duration,
	rating_id
FROM
	films
INNER JOIN (
	SELECT
		film_id,
		count(user_id) likes
	FROM
		films_likes
	GROUP BY
		film_id
	ORDER BY
		count(user_id) desc
	LIMIT 10
) AS top_likes ON
	films.film_id = top_likes.film_id
ORDER BY
	top_likes.likes desc
```

### Выбор общей информации по всем пользователям

``` sql
SELECT user_id, user_name, email, login, birthday
FROM users
```

### Выбор общей информации пользователя по идентификатору

``` sql
SELECT user_id, user_name, email, login, birthday
FROM users
WHERE user_id = 1
```

### Выбор общей информации друзей пользователя

``` sql
SELECT user_name, email, login, birthday
FROM users AS u 
INNER JOIN (
	SELECT
		friend_id 
	FROM
		friends
	WHERE
		user_id = 1) AS user_friend
		ON user_friend.friend_id = u.user_id
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
		friends
	WHERE
		user_id = 1
) AS friend_user_1
INNER JOIN (
	SELECT
		friend_id
	FROM
		friends
	WHERE
		user_id = 2) AS friend_user_2
ON
	friend_user_1.friend_id = friend_user_2.friend_id
```

