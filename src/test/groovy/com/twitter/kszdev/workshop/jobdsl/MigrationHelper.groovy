package com.twitter.kszdev.workshop.jobdsl

import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.dsl.MemoryJobManagement
import org.custommonkey.xmlunit.Diff
import org.custommonkey.xmlunit.XMLUnit
import org.junit.ComparisonFailure
import spock.lang.Requires
import spock.lang.Specification

class MigrationHelper extends Specification {

    MemoryJobManagement jobManagement = new MemoryJobManagement()
    DslScriptLoader scriptLoader = new DslScriptLoader(jobManagement)

    @Requires({ Boolean.valueOf(env['dslMigrationTests']) })
    def 'generated configuration should match expected one'() {
        given:
            String jobName = 'echo-gradle-project'
            String dsl = new File('jobs/a1_single_job.groovy').text
            String expectedConfig = this.class.getResource('/project_echo_job.xml').text

        when:
            scriptLoader.runScript(dsl)

        then:
            generatedJobConfigMatches(jobName, expectedConfig)
    }

    void generatedJobConfigMatches(String job, String expectedConfig) {
        String actualConfig = jobManagement.savedConfigs[job]
        Diff diff = compareXml(expectedConfig, actualConfig)
        if (!diff.similar()) {
            throw new ComparisonFailure('Generated configuration does not match expected one', expectedConfig, actualConfig)
        }
    }

    Diff compareXml(String expected, String actual) {
        XMLUnit xmlUnit = new XMLUnit()
        xmlUnit.ignoreWhitespace = true
        xmlUnit.normalizeWhitespace = true
        return xmlUnit.compareXML(expected, actual)
    }

}
