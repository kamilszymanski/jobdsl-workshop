package com.twitter.kszdev.workshop.jobdsl.pipeline.customizable.step

import groovy.transform.PackageScope
import javaposse.jobdsl.dsl.DslFactory

abstract class PipelineStep {

    final String jobName

    protected DslFactory dslFactory
    protected Map parameters
    protected String downstreamJob

    protected PipelineStep(String jobName) {
        this.jobName = jobName
    }

    protected abstract void build()

    List<String> requiredParameters() {
        return []
    }

    void create() {
        checkRequiredParametersPresence()
        build()
    }

    private void checkRequiredParametersPresence() {
        requiredParameters().each {
            if (parameters[it] == null) {
                throw new UndefinedRequiredParameterException(jobName, it)
            }
        }
    }

    static class UndefinedRequiredParameterException extends RuntimeException {

        @PackageScope
        UndefinedRequiredParameterException(String jobName, String parameter) {
            super("Undefined parameter '$parameter' required by $jobName")
        }

    }

}
