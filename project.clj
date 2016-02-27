(defproject passable "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"local" ~(str (.toURI (java.io.File. "~/.m2/repository")))}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.cli "0.3.3"]
                 [org.xerial/sqlite-jdbc "3.8.11.2"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [com.layerware/hugsql "0.4.3"]
                 [caesium "0.6.0-SNAPSHOT"]] ;; my fork of caesium is installed locally
  :main ^:skip-aot passable.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
