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
