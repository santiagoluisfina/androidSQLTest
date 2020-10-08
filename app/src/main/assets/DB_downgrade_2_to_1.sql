PRAGMA foreign_keys = 0;

CREATE TABLE libros_temp_table AS SELECT *
                                          FROM TBL_LIBROS;

DROP TABLE TBL_LIBROS;

CREATE TABLE TBL_LIBROS (
    ID_LIBRO    INTEGER,
    LIBRO       TEXT,
    ABREVIATURA TEXT,
    PRIMARY KEY (
        ID_LIBRO
    )
);

INSERT INTO TBL_LIBROS (
                           ID_LIBRO,
                           LIBRO,
                           ABREVIATURA
                       )
                       SELECT ID_LIBRO,
                              LIBRO,
                              ABREVIATURA
                         FROM libros_temp_table;

DROP TABLE libros_temp_table;

PRAGMA foreign_keys = 1;


