After completing all assignments from `assignments` directory checkout next `workshop_part_<N>` branch

# Running Jenkins

`export JENKINS_HOME=~/.jenkins_workshop` (`set JENKINS_HOME=~/.jenkins_workshop` on Windows) to set Jenkins working directory (defaults to `~/.jenkins`)

`java -jar jenkins.war` to run Jenkins

# Repository structure

+ assignments - workshop assignments
+ jobs - Job DSL scripts
+ scripts - useful Jenkins and shell scripts
+ snippets - Groovy code snippets (not related to Job DSL)
+ src
    + main - builders, factories and other custom helper methods and classes used in DSL scripts 
    + test - tests

# References

[API Viewer](https://jenkinsci.github.io/job-dsl-plugin/)

[DSL Playground application](https://job-dsl.herokuapp.com/)

[Job-DSL wiki](https://github.com/jenkinsci/job-dsl-plugin/wiki)

[Job-DSL sources](https://github.com/jenkinsci/job-dsl-plugin)

[Discussion group](https://groups.google.com/forum/#!forum/job-dsl-plugin)