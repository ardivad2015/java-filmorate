CREATE TABLE IF NOT EXISTS users (
	user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	user_name VARCHAR(150) NOT NULL,
	email VARCHAR(50) NOT NULL,
	login VARCHAR(50) NOT NULL,
	birthday TIMESTAMP NOT NULL
);

 CREATE TABLE  IF NOT EXISTS film_ratings (
            rating_id INTEGER NOT NULL,
            rating_name VARCHAR(50) NOT NULL,
            CONSTRAINT film_ratings_pk PRIMARY KEY (rating_id)
  );

 CREATE TABLE IF NOT EXISTS films (
            film_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
            film_name VARCHAR(150) NOT NULL,
            description VARCHAR(200) NOT NULL,
            duration INTEGER NOT NULL,
            release_date TIMESTAMP NOT NULL,
            rating_id INTEGER NOT NULL,
           CONSTRAINT films_fk FOREIGN KEY (rating_id) REFERENCES film_ratings(rating_id) ON DELETE RESTRICT ON UPDATE RESTRICT,
           CONSTRAINT check_release_date CHECK (release_date>='1895-12-28')
  );

  CREATE TABLE IF NOT EXISTS friends (
  	user_id BIGINT NOT NULL,
  	friend_id BIGINT NOT NULL,
  	CONSTRAINT friends_pk PRIMARY KEY (user_id,friend_id),
  	CONSTRAINT friends_users_fk_1 FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  	CONSTRAINT friends_users_fk_2 FOREIGN KEY (friend_id) REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE RESTRICT
  );

  CREATE TABLE IF NOT EXISTS genres (
  	  genre_id INTEGER NOT NULL,
      genre_name VARCHAR(50) NOT NULL,
      CONSTRAINT genres_pk PRIMARY KEY (genre_id)
  );

  CREATE TABLE IF NOT EXISTS films_genres (
  	film_id BIGINT NOT NULL,
  	genre_id INTEGER NOT NULL,
  	CONSTRAINT  films_genres_pk PRIMARY KEY (film_id,genre_id),
  	CONSTRAINT films_genres_fk_1 FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  	CONSTRAINT films_genres_fk_2 FOREIGN KEY (genre_id) REFERENCES genres(genre_id) ON DELETE RESTRICT ON UPDATE RESTRICT
  );

    CREATE TABLE IF NOT EXISTS films_likes (
    	film_id BIGINT NOT NULL,
    	user_id BIGINT NOT NULL,
    	CONSTRAINT  films_likes_pk PRIMARY KEY (film_id,user_id),
    	CONSTRAINT films_likes_fk_1 FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    	CONSTRAINT films_likes_fk_2 FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE RESTRICT
    );