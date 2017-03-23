package com.twitter.kszdev.workshop.jobdsl.pipeline.customizable.step

import static com.twitter.kszdev.workshop.jobdsl.pipeline.customizable.step.publisher.DownstreamTriggerContext.triggerDownstreamJob

class AcceptanceTests extends PipelineStep {

    AcceptanceTests(String projectName) {
        super("${projectName}-acceptance-tests")
    }

    @Override
    List<String> requiredParameters() {
        return ['repository']
    }

    @Override
    protected void build() {
        dslFactory.job(jobName) {
            deliveryPipelineConfiguration 'test'
            scm {
                git(parameters.repository, '$VERSION')
            }
            steps {
                gradle {
                    tasks 'clean acceptanceTests'
                    useWrapper()
                }
            }
            publishers {
                triggerDownstreamJob(delegate, downstreamJob)
            }
        }
    }
}
