-- :name create-users-table
-- :command :execute
-- :result :raw
create table if not exists users
( id integer primary key autoincrement not null
, name text unique not null
, public_key blob not null );

-- :name create-groups-table
-- :command :execute
-- :result :raw
create table if not exists groups
( id integer primary key autoincrement not null
, name text unique not null );

-- :name create-user-groups-table
-- :command :execute
-- :result :raw
-- :doc It goes without saying that secret_key is encrypted by issuer for user_id
create table if not exists user_groups
( user_id integer not null
, group_id integer not null
, issuer int not null
, secret_key blob not null
, foreign key (user_id) references users(id)
, foreign key (group_id) references groups(id)
, foreign key (issuer) references users(id) );

-- :name create-credentials-table
-- :command :execute
-- :result :raw
create table if not exists credentials
( id integer primary key autoincrement not null
, name text unique not null );

-- :name create-encrypted-data-table
-- :command :execute
-- :result :raw
create table if not exists encrypted_data
( id integer primary key autoincrement not null
, data blob not null )

-- :name create-group-encrypted-data-table
-- :command :execute
-- :result :raw
create table if not exists group_encrypted_data
( group_id integer not null
, encrypted_data_id integer not null
, foreign key (group_id) references groups(id)
, foreign key (encrypted_data_id) references encrypted_data(id) );

-- :name create-credential-encrypted-data-table
-- :command :execute
-- :result :raw
create table if not exists credential_encrypted_data
( credential_id integer not null
, encrypted_data_id integer not null
, foreign key (credential_id) references credentials(id)
, foreign key (encrypted_data_id) references encrypted_data(id) );
