package com.twitter.kszdev.workshop.jobdsl.pipeline.customizable.step

import static com.twitter.kszdev.workshop.jobdsl.pipeline.customizable.step.publisher.DownstreamTriggerContext.triggerDownstreamJob

class DeployOnTest extends PipelineStep {

    DeployOnTest(String projectName) {
        super( "${projectName}-deploy-on-test")
    }

    @Override
    protected void build() {
        dslFactory.job(jobName) {
            deliveryPipelineConfiguration 'test'
            steps {
                shell 'echo "deploying version $VERSION on test"'
            }
            publishers {
                triggerDownstreamJob(delegate, downstreamJob)
            }
        }
    }

}
