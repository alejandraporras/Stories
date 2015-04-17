 storiesrated schema

# --- !Ups

CREATE SEQUENCE storiesRated_id_seq;

CREATE TABLE storiesrated (
    id integer NOT NULL DEFAULT nextval('storiesRated_id_seq'),
    author varchar(255) REFERENCES user(username),
    story integer REFERENCES stories(id),

);

# --- !Downs

DROP TABLE storiesrated;
DROP SEQUENCE storiesRated_id_seq;
