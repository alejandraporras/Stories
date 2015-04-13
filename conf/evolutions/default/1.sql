 User schema

# --- !Ups

 CREATE TABLE user (
    username varchar(255) PRIMARY KEY,
    password varchar(255) NOT NULL

 );

 # --- !Downs

 DROP TABLE user;
