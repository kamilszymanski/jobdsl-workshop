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
        downstreamParameterized {
            trigger('project-echo-deploy-on-test') {
                condition 'SUCCESS'
                parameters {
                    predefinedProp('VERSION', '$GIT_COMMIT')
                }
            }
        }
    }
}

job('project-echo-deploy-on-test') {
    steps {
        shell 'echo deploying project v. $VERSION on test'
    }
    publishers {
        downstreamParameterized {
            trigger('project-echo-acceptance-tests') {
                condition 'SUCCESS'
                parameters {
                    currentBuild()
                }
            }
        }
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
        downstreamParameterized {
            trigger('project-echo-deploy-on-production') {
                condition 'SUCCESS'
                parameters {
                    currentBuild()
                }
            }
        }
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