-- :name add-group :! :n
insert into groups (name) values (:name);

-- :name add-user-group :! :n
insert into user_groups
( user_id
, group_id
, issuer
, secret_key )
select
  u.id
, g.id
, i.id
, :secret-key
from users as u
join groups as g
  on g.name = :group-name
join users as i
  on i.name = :issuer-name
where u.name = :user-name;

-- :name get-user-group-key :? :1
select
  ug.secret_key as "secret-key"
, i.public_key as "public-key"
, i.name as issuer
from groups as g
join user_groups as ug
  on ug.group_id = g.id
join users as u
  on ug.user_id = u.id
join users as i
  on ug.issuer = u.id
where g.name = :group-name
  and u.name = :user-name;
