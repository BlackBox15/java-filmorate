-- PUBLIC.RATING definition

-- Drop table

-- DROP TABLE PUBLIC.RATING;
-- DROP TABLE PUBLIC.GENRE;
-- DROP TABLE PUBLIC.FRIENDSHIP;

CREATE TABLE IF NOT EXISTS PUBLIC.GENRE (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    GENRE VARCHAR_IGNORECASE(5)
);

CREATE TABLE IF NOT EXISTS PUBLIC.RATING (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    RATING VARCHAR_IGNORECASE(20)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILM (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    NAME VARCHAR_IGNORECASE(100),
    DESCRIPTION VARCHAR_IGNORECASE,
    RELEASE_DATE DATE,
    DURATION INTEGER,
    RATING_ID INTEGER UNIQUE,
    FOREIGN KEY (RATING_ID) REFERENCES PUBLIC.RATING(ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.USERS (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    EMAIL VARCHAR_IGNORECASE(255),
    LOGIN VARCHAR_IGNORECASE,
    NAME VARCHAR_IGNORECASE,
    BIRTHDAY DATE
);

CREATE TABLE IF NOT EXISTS PUBLIC.FRIENDSHIP (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    USER_ID INTEGER NOT NULL,
    FRIEND_ID INTEGER,
    FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS(ID),
    FOREIGN KEY (FRIEND_ID) REFERENCES PUBLIC.USERS(ID)
);


