import groovy.json.JsonSlurper

URL projectsApi = 'https://api.github.com/users/dev-trainings/repos'.toURL()
List projects = new JsonSlurper().parseText(projectsApi.text)

projects.each { project ->
    if (project.name.endsWith('-gradle-project')) {
        job(project.name) {
            scm {
                git(project.git_url)
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
                archiveJunit('build/test-results/test/*.xml') {
                    allowEmptyResults()
                }
            }
        }
    }
}