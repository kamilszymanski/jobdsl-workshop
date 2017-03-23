job('echo-gradle-project') {
    scm {
        git 'https://github.com/dev-trainings/echo-gradle-project.git'
    }
    steps {
        gradle {
            tasks 'clean publish'
            useWrapper()
        }
    }
    configure {
        (it / quietPeriod).value = 0
    }
}