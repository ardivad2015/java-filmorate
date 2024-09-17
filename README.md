# java-filmorate
## Схема базы данных
![Схема базы данных](/src/main/resources/bd_diagram.png)
## Примеры запросов
### Выбор данных фильма по идентификатору
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
		user_like
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

