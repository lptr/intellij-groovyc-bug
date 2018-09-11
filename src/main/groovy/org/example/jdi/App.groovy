package org.example.jdi

import com.sun.jdi.VMDisconnectedException

class App {
    static void main(String... args) {
        println "Hello from Groovy ${GroovySystem.version}, I can see ${VMDisconnectedException.name}!"
    }
}
