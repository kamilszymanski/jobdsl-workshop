package com.twitter.kszdev.workshop.jobdsl.pipeline.customizable

import javaposse.jobdsl.dsl.DslFactory
import com.twitter.kszdev.workshop.jobdsl.pipeline.customizable.step.PipelineStep

class PipelineAssembler {

    protected final DslFactory dslFactory
    protected final String projectName
    protected final Map parameters

    protected PipelineAssembler(DslFactory dslFactory, String projectName, Map parameters) {
        this.dslFactory = dslFactory
        this.projectName = projectName
        this.parameters = parameters
    }

    protected List<String> assemble(List<Class<? extends PipelineStep>> pipelineSteps) {
        List<PipelineStep> jobs = pipelineSteps.reverse().inject([]) { acc, step ->
            String downstreamJob = acc.empty ? null : acc.last().jobName
            acc << createJob(step, downstreamJob)
        }
        jobs.each {
            it.create()
        }
        return jobs.reverse()*.jobName
    }

    protected PipelineStep createJob(Class<? extends PipelineStep> step, String downstreamJob) {
        PipelineStep job = step.newInstance([projectName].toArray())
        job.dslFactory = dslFactory
        job.parameters = parameters
        job.downstreamJob = downstreamJob
        return job
    }

}
