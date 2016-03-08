(set-env!
 :resource-paths #{"resources"}
 :source-paths #{"src/clj" "src/cljs"}
 :dependencies '[[adzerk/boot-cljs "1.7.228-1"]
                 [adzerk/boot-reload "0.4.5"]
                 [pandeiro/boot-http "0.7.3"]
                 [org.martinklepsch/boot-garden "1.3.0-0"]
                 [ring "1.4.0"]
                 [amalloy/ring-gzip-middleware "0.1.3"]
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [clj-rss "0.2.3"]
                 [clj-time "0.11.0"]
                 [java-jdbc/dsl "0.1.3"]
                 [buddy/buddy-auth "0.9.0"]
                 [org.postgresql/postgresql "9.4.1208"]
                 [org.omcljs/om "1.0.0-alpha28"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.7.228"]
                 [org.clojure/core.async "0.2.371"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [org.clojure/math.numeric-tower "0.0.4"]])

(require
 '[adzerk.boot-cljs :refer [cljs]]
 '[adzerk.boot-reload :refer [reload]]
 '[pandeiro.boot-http :refer [serve]]
 '[org.martinklepsch.boot-garden :refer [garden]])

(deftask css []
  (comp
   (garden :output-to "resources/css/styles.css"
           :styles-var 'chili-dog-night.styles/screen)))

(deftask dev []
  (comp (serve :handler 'chili-dog-night.core/handler :reload true)
        (watch)
        (css)
        (reload :ids #{"resources/js/main"})
        (cljs :ids #{"resources/js/main"} :source-map true :optimizations :none)))

(deftask build []
  (comp
   (cljs :optimizations :advanced)
   (css)
   (aot :namespace '#{chili-dog-night.core})
   (pom :project 'chili-dog-night
        :version "0.1.0")
   (uber)
   (jar :main 'chili-dog-night.core)))
