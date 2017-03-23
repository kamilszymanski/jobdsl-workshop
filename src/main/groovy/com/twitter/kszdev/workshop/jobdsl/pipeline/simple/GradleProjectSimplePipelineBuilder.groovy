package com.twitter.kszdev.workshop.jobdsl.pipeline.simple

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.View
import javaposse.jobdsl.dsl.helpers.publisher.PublisherContext

class GradleProjectSimplePipelineBuilder {

    DslFactory dslFactory
    String projectName
    String scmPoll
    String repository

    void build(DslFactory dslFactory) {
        this.dslFactory = dslFactory
        assemblePipeline()
        createPipelineView()
    }

    private void assemblePipeline() {
        createBuildJob()
        createDeployOnTestJob()
        createAcceptanceTestsJob()
        createDeployOnProductionJob()
    }

    private Job createBuildJob() {
        dslFactory.job("${projectName}-build") {
            scm {
                git repository
            }
            triggers {
                scm scmPoll
            }
            steps {
                gradle {
                    tasks 'clean publish'
                    useWrapper()
                }
            }
            publishers {
                triggerDownstreamJob(delegate, "${projectName}-deploy-on-test", [VERSION: '$GIT_COMMIT'])
            }
        }
    }

    private Job createDeployOnTestJob() {
        dslFactory.job("${projectName}-deploy-on-test") {
            steps {
                shell 'echo "deploying version $VERSION on test"'
            }
            publishers {
                triggerDownstreamJob(delegate, "${projectName}-acceptance-tests")
            }
        }
    }

    private Job createAcceptanceTestsJob() {
        dslFactory.job("${projectName}-acceptance-tests") {
            scm {
                git(repository, '$VERSION')
            }
            steps {
                gradle {
                    tasks 'clean acceptanceTests'
                    useWrapper()
                }
            }
            publishers {
                triggerDownstreamJob(delegate, "${projectName}-deploy-on-production")
            }
        }
    }

    private Job createDeployOnProductionJob() {
        dslFactory.job("${projectName}-deploy-on-production") {
            steps {
                shell 'echo "deploying version $VERSION on production"'
            }
        }
    }

    private View createPipelineView() {
        dslFactory.deliveryPipelineView(projectName) {
            allowPipelineStart()
            enableManualTriggers()
            pipelines {
                component(projectName, "${projectName}-build")
            }
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
}
