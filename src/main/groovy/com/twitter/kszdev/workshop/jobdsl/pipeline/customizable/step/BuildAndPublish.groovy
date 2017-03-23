package com.twitter.kszdev.workshop.jobdsl.pipeline.customizable.step

import static com.twitter.kszdev.workshop.jobdsl.pipeline.customizable.step.publisher.DownstreamTriggerContext.triggerDownstreamJob

class BuildAndPublish extends PipelineStep {

    BuildAndPublish(String projectName) {
        super("${projectName}-build")
    }

    @Override
    List<String> requiredParameters() {
        return ['repository']
    }

    @Override
    protected void build() {
        dslFactory.job(jobName) {
            deliveryPipelineConfiguration 'build'
            scm {
                git parameters.repository
            }
            triggers {
                if (parameters.scmPoll) {
                    scm parameters.scmPoll
                }
            }
            steps {
                gradle {
                    tasks 'clean publish'
                    useWrapper()
                }
            }
            publishers {
                triggerDownstreamJob(delegate, downstreamJob, [VERSION: '$GIT_COMMIT'])
            }
        }
    }
}
