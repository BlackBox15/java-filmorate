-- INSERT INTO RATING (RATING)
-- VALUES ('G'),
--        ('PG'),
--        ('PG-13'),
--        ('R'),
--        ('NC-17');
MERGE INTO RATING KEY(ID)
VALUES (1, 'G'),
       (2, 'PG'),
       (3, 'PG-13'),
       (4, 'R'),
       (5, 'NC-17');

MERGE INTO GENRE KEY(ID)
VALUES (1, 'Comedy'),
       (2, 'Drama'),
       (3, 'Thriller'),
       (4, 'Horror'),
       (5, 'Cartoon'),
       (6, 'Musical'),
       (7, 'Western'),
       (8, 'Sci-fi'),
       (9, 'Action'),
       (10, 'Adventure'),
       (11, 'Biography'),
       (12, 'Crime'),
       (13, 'Documentary'),
       (14, 'History'),
       (15, 'Romance'),
       (16, 'Sports')
;
