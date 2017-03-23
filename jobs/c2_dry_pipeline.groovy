import javaposse.jobdsl.dsl.helpers.publisher.PublisherContext

job('project-echo-build') {
    scm {
        git 'https://github.com/dev-trainings/echo-gradle-project.git'
    }
    triggers {
        scm '@midnight'
    }
    steps {
        gradle {
            tasks 'clean publish'
            useWrapper()
        }
    }
    publishers {
        triggerDownstreamJob(delegate, 'project-echo-deploy-on-test', [VERSION: '$GIT_COMMIT'])
    }
}

job('project-echo-deploy-on-test') {
    steps {
        shell 'echo deploying project v. $VERSION on test'
    }
    publishers {
        triggerDownstreamJob(delegate, 'project-echo-acceptance-tests')
    }
}

job('project-echo-acceptance-tests') {
    scm {
        git('https://github.com/dev-trainings/echo-gradle-project.git', '$VERSION')
    }
    steps {
        gradle {
            tasks 'clean acceptanceTests'
            useWrapper()
        }
    }
    publishers {
        triggerDownstreamJob(delegate, 'project-echo-deploy-on-production')
    }
}

job('project-echo-deploy-on-production') {
    steps {
        shell 'echo deploying project v. $VERSION on production'
    }
}

deliveryPipelineView('project-echo') {
    allowPipelineStart()
    enableManualTriggers()
    pipelines {
        component('Project Echo', 'project-echo-build')
    }
}

void triggerDownstreamJob(PublisherContext publisherContext, String downstreamJob, Map downstreamParameters = [:]) {
    publisherContext.downstreamParameterized {
        trigger(downstreamJob) {
            condition 'SUCCESS'
            parameters {
                currentBuild()
                if (downstreamParameters) {
                    predefinedProps downstreamParameters
                }
            }
        }
    }
}