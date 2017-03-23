package com.twitter.kszdev.workshop.jobdsl

import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.dsl.MemoryJobManagement
import spock.lang.Specification
import spock.lang.Unroll

class DslScriptsSyntaxSpec extends Specification {

    MemoryJobManagement jobManagement = new MemoryJobManagement()
    DslScriptLoader scriptLoader = new DslScriptLoader(jobManagement)

    @Unroll
    def 'should find no syntactic errors in #dsl DSL script'() {
        when:
            scriptLoader.runScript(dsl.text)
        then:
            noExceptionThrown()
        where:
            dsl << dslScripts
    }

    private List getDslScripts() {
        List dslScripts = []
        new File('jobs').eachFileRecurse {
            if (it.name.endsWith('.groovy')) {
                dslScripts << it
            }
        }
        return dslScripts
    }

}
