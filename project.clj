(defproject fancy/fancy "0.2.1"
  :description "Fancy printing -- prettier than pretty"
  :url "https://github.com/chbrown/fancy-clojure"
  :license {:name "Eclipse Public License"
            :url "https://www.eclipse.org/legal/epl-v10.html"}
  :pom-addition [:developers [:developer
                              [:name "Christopher Brown"]
                              [:email "io@henrian.com"]]]
  :deploy-repositories [["releases" :clojars]]
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946"]]
  :plugins [[lein-cljsbuild "1.1.7"]]
  :cljsbuild {:builds [{:id "production"
                        :source-paths ["src"]
                        :compiler {:optimizations :simple
                                   :pretty-print false}}]}
  :profiles {:test {:plugins [[lein-cloverage "1.0.10"]]}
             :repl {:dependencies [[org.clojure/tools.namespace "0.3.0-alpha3"]]
                    :source-paths ["dev"]}})
