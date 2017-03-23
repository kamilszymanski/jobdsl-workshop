package com.twitter.kszdev.workshop.jobdsl.pipeline.customizable

import groovy.transform.InheritConstructors
import com.twitter.kszdev.workshop.jobdsl.pipeline.customizable.step.PipelineStep

import static com.twitter.kszdev.workshop.jobdsl.pipeline.customizable.view.ViewFactory.createDeliveryPipelineView

@InheritConstructors
class GradleProjectCustomizablePipelineBuilder extends PipelineAssembler {

    void build(List<PipelineStep> pipelineSteps) {
        List<String> jobs = assemble(pipelineSteps)
        createDeliveryPipelineView(dslFactory, projectName, jobs[0])
    }

}
