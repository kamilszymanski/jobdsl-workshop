package com.twitter.kszdev.workshop.jobdsl.pipeline.customizable.step

import static com.twitter.kszdev.workshop.jobdsl.pipeline.customizable.step.publisher.DownstreamTriggerContext.triggerDownstreamJob

class DeployOnUAT extends PipelineStep {

    DeployOnUAT(String projectName) {
        super("${projectName}-deploy-on-stage")
    }

    @Override
    protected void build() {
        dslFactory.job(jobName) {
            deliveryPipelineConfiguration 'UAT'
            steps {
                shell 'echo "deploying version $VERSION on UAT"'
            }
            publishers {
                triggerDownstreamJob(delegate, downstreamJob)
            }
        }
    }

}
