import com.twitter.kszdev.workshop.jobdsl.pipeline.simple.GradleProjectSimplePipelineBuilder

def projectProperties = [
        projectName: 'project-echo',
        repository: 'https://github.com/dev-trainings/echo-gradle-project.git',
        scmPoll: '@midnight'
]

new GradleProjectSimplePipelineBuilder(projectProperties)
        .build(this)
