job('echo-gradle-project') {
    quietPeriod(0)
    scm {
        git 'https://github.com/dev-trainings/echo-gradle-project.git'
    }
    triggers {
        scm '@daily'
    }
    wrappers {
        timestamps()
    }
    steps {
        gradle {
            tasks 'clean publish'
            useWrapper()
        }
    }
    publishers {
        archiveJunit('build/test-results/test/*.xml')
    }
}