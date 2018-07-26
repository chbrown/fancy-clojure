(defproject fancy/fancy "0.2.1"
  :description "Fancy printing -- prettier than pretty"
  :url "https://github.com/chbrown/fancy-clojure"
  :license {:name "Eclipse Public License"
            :url "https://www.eclipse.org/legal/epl-v10.html"}
  :pom-addition [:developers [:developer
                              [:name "Christopher Brown"]
                              [:email "io@henrian.com"]]]
  :deploy-repositories [["releases" :clojars]]
  :plugins [[lein-cljsbuild "1.1.7"]]
  :cljsbuild {:builds [{:id "production"
                        :source-paths ["src"]
                        :compiler {:optimizations :simple
                                   :pretty-print false}}
                       {:id "test"
                        :source-paths ["src" "test"]
                        :compiler {:output-dir "target/test"
                                   :output-to "target/test/main.js"
                                   :main fancy.doo-runner
                                   :optimizations :whitespace}}]}
  :profiles {:dev  {:dependencies [[org.clojure/clojure "1.9.0"]
                                   [org.clojure/clojurescript "1.10.339"]]}
             :test {:plugins [[lein-doo "0.1.10"]
                              [lein-cloverage "1.0.10"]]
                    :doo {:paths {:rhino "lein run -m org.mozilla.javascript.tools.shell.Main"}}}
             :repl {:dependencies [[org.clojure/tools.namespace "0.3.0-alpha3"]]
                    :source-paths ["dev"]}})
