import groovy.xml.MarkupBuilder

def writer = new StringWriter()
def xml = new MarkupBuilder(writer)

xml.project {
    scm {
        userRemoteConfigs {
            'hudson.plugins.git.UserRemoteConfig' {
                url 'https://github.com/dev-trainings/echo-gradle-project.git'
            }
        }
    }
    builders {
        'hudson.plugins.gradle.Gradle' {
            tasks 'clean publish'
            useWrapper true
        }
    }
}

println writer.toString()
