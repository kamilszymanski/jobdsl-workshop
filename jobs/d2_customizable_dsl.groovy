import com.twitter.kszdev.workshop.jobdsl.pipeline.customizable.GradleProjectCustomizablePipelineBuilder
import com.twitter.kszdev.workshop.jobdsl.pipeline.customizable.step.*

def pipelineParameters = [
        repository: 'https://github.com/dev-trainings/echo-gradle-project.git',
        scmPoll: '@midnight'
]
def pipelineSteps = [BuildAndPublish, DeployOnTest, AcceptanceTests, DeployOnProduction]

new GradleProjectCustomizablePipelineBuilder(this, 'project-echo', pipelineParameters)
        .build(pipelineSteps)
