package com.twitter.kszdev.workshop.jobdsl

import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.dsl.GeneratedItems
import javaposse.jobdsl.dsl.MemoryJobManagement
import spock.lang.Specification
import spock.lang.Unroll

class PipelineSpec extends Specification {

    MemoryJobManagement jobManagement = new MemoryJobManagement()
    DslScriptLoader scriptLoader = new DslScriptLoader(jobManagement)
    String dsl = new File('jobs/c1_pipeline.groovy').text

    def 'should generate all jobs belonging to a pipeline'() {
        given:
            Set expectedJobs = [
                    'project-echo-build',
                    'project-echo-deploy-on-test',
                    'project-echo-acceptance-tests',
                    'project-echo-deploy-on-production']
        when:
            GeneratedItems generatedItems = scriptLoader.runScript(dsl)
        then:
            jobNamesOf(generatedItems) == expectedJobs
    }

    private Set jobNamesOf(GeneratedItems generatedItems) {
        return generatedItems.jobs*.jobName
    }

    @Unroll
    def 'should trigger #downstream after #upstream runs successfully'() {
        when:
            scriptLoader.runScript(dsl)
        then:
            downstream == downstreamJobTriggeredBy(upstream)
        where:
            upstream                        | downstream
            'project-echo-build'            | 'project-echo-deploy-on-test'
            'project-echo-deploy-on-test'   | 'project-echo-acceptance-tests'
            'project-echo-acceptance-tests' | 'project-echo-deploy-on-production'
    }

    private String downstreamJobTriggeredBy(upstreamJob) {
        String xml = jobManagement.savedConfigs[upstreamJob]
        def jobConfig = new XmlSlurper().parseText(xml)
        return jobConfig
                .publishers
                .'hudson.plugins.parameterizedtrigger.BuildTrigger'
                .configs
                .'hudson.plugins.parameterizedtrigger.BuildTriggerConfig'
                .projects
    }

    def 'build step should define VERSION property available to downstream jobs'() {
        when:
            String upstreamJobName = 'project-echo-build'
            scriptLoader.runScript(dsl)
        then:
            downstreamJobBuildParameters(upstreamJobName).contains 'VERSION=$GIT_COMMIT'
    }

    List<String> downstreamJobBuildParameters(String upstreamJob) {
        String xml = jobManagement.savedConfigs[upstreamJob]
        def jobConfig = new XmlSlurper().parseText(xml)
        String params = jobConfig
                            .publishers
                            .'hudson.plugins.parameterizedtrigger.BuildTrigger'
                            .configs
                            .'hudson.plugins.parameterizedtrigger.BuildTriggerConfig'
                            .configs
                            .'hudson.plugins.parameterizedtrigger.PredefinedBuildParameters'
                            .properties
        return params.split('\n')
    }
}
