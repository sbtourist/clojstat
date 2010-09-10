# ClojStat - JStat GC logs viewer

ClojStat is a simple graphical viewer of [JStat](http://download.oracle.com/javase/6/docs/technotes/tools/share/jstat.html) garbage collector logs, based on Clojure, Compojure and JQuery Visualization.   
It currently has a limited set of features and comes with a very simple web interface, but if you need to quickly make sense of JStat logs by plotting them, it just works!

## Installation

Download and install [Leiningen](http://github.com/technomancy/leiningen), then just checkout ClojStat from its git repository.

## Usage

From ClojStat directory, just run Leiningen REPL:

    $> lein repl

Then load ClojStat sources and run its server application:

    $> (use '[com.blogspot.sbtourist.clojure.jstat.clj-runner])
    $> (run-server 8080)

Now point your browser at _http://localhost:8080/post_ and post your JStat data through the form by inserting the following data:

* JStat file: path on your filesystem of your JStat logs file.
* JStat stats: comma-separated list of JStat parameters (as reported in the header), i.e.: E,O,GCT
* Samples interval: JStat samples interval to take, defining plot granularity.

Please note that JStat header names depend on the JStat version, and how you run it.

The server can be stopped with:

    $> (stop-server)

Enjoy!