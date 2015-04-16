 Stories schema

# --- !Ups

CREATE SEQUENCE story_id_seq;

CREATE TABLE stories (
    id integer NOT NULL DEFAULT nextval('story_id_seq'),
    title varchar(255),
    author varchar(255) REFERENCES user(username),
    text varchar(5000),
    points Integer,
    data Date
);

# --- !Downs

DROP TABLE stories;
DROP SEQUENCE story_id_seq;
