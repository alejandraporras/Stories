 comments schema

# --- !Ups

CREATE SEQUENCE comments_id_seq;

CREATE TABLE comments (
    id integer NOT NULL DEFAULT nextval('comments_id_seq'),
    author varchar(255) REFERENCES user(username),
    story integer REFERENCES stories(id),
    text varchar (255),
    data Date
);

# --- !Downs

DROP TABLE comments;
DROP SEQUENCE comments_id_seq;
