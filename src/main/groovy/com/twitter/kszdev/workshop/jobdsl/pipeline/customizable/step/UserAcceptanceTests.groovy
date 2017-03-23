package com.twitter.kszdev.workshop.jobdsl.pipeline.customizable.step

import static com.twitter.kszdev.workshop.jobdsl.pipeline.customizable.step.publisher.DownstreamTriggerContext.manuallyTriggerDownstreamJob

class UserAcceptanceTests extends PipelineStep {

    UserAcceptanceTests(String projectName) {
        super("${projectName}-user-acceptance-tests")
    }

    @Override
    protected void build() {
        dslFactory.job(jobName) {
            deliveryPipelineConfiguration 'UAT'
            publishers {
                manuallyTriggerDownstreamJob(delegate, downstreamJob)
            }
        }
    }

}
