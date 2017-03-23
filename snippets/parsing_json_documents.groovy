import groovy.json.JsonSlurper

String metadata = """
                    [
                      {
                        "name": "jobdsl-config-server",
                        "owner": {
                          "login": "dev-trainings"
                        }
                      },
                      {
                        "name": "jobdsl-service-discovery",
                        "owner": {
                          "login": "dev-trainings"
                        }
                      }
                    ]
                    """

def json = new JsonSlurper().parseText(metadata)

json.each { repo ->
    println "${repo.name} repository owner: ${repo.owner.login}"
}