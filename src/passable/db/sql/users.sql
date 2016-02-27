-- :name add-user :! :n
insert into users
( name
, public_key )
values
( :name
, :public-key );

-- :name get-user :? :1
select
  name
, public_key as "public-key"
from users
where name = :name;
