package com.twitter.kszdev.workshop.jobdsl.pipeline.customizable.step

class DeployOnProduction extends PipelineStep {

    DeployOnProduction(String projectName) {
        super("${projectName}-deploy-on-production")
    }

    @Override
    protected void build() {
        dslFactory.job(jobName) {
            deliveryPipelineConfiguration 'production'
            steps {
                shell 'echo "deploying version $VERSION on production"'
            }
        }
    }

}
