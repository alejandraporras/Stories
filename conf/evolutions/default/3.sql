 Stories schema

# --- !Ups

CREATE SEQUENCE story_id_seq;

CREATE TABLE story (
    id integer NOT NULL DEFAULT nextval('story_id_seq'),
    title varchar(255),
    author varchar(255),
    text varchar(255),
    points Integer,
    data Date
);

# --- !Downs

DROP TABLE story;
DROP SEQUENCE story_id_seq;
