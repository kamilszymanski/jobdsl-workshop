List plugins = [
        'git',
        'job-dsl',
        'parameterized-trigger',
        'delivery-pipeline-plugin',
        'build-pipeline-plugin',
        'gradle',
        'timestamper'
]

def pluginManager = PluginManager.createDefault(Jenkins.instance)
pluginManager.install(plugins, true)