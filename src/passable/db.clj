(ns passable.db
  (:require [hugsql.core :as hugsql]))

(defn spec [path]
  {:subprotocol "sqlite"
   :subname path})

(hugsql/def-db-fns "passable/db/sql/create_schema.sql")
(hugsql/def-db-fns "passable/db/sql/users.sql" {:quoting :ansi})
(hugsql/def-db-fns "passable/db/sql/groups.sql" {:quoting :ansi})

(defn create-schema [spec]
  (create-users-table spec)
  (create-groups-table spec)
  (create-user-groups-table spec)
  (create-credentials-table spec)
  (create-encrypted-data-table spec)
  (create-group-encrypted-data-table spec)
  (create-credential-encrypted-data-table spec))
