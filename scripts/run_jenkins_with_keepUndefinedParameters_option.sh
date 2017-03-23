# without hudson.model.ParametersAction.keepUndefinedParameters=true parameters that are not declared explicitly in downstream jobs are being dropped
# on the other hand when passing undeclared parameters is enabled malicious users are able to trigger builds, passing arbitrary environment variables to modify the behavior of those builds
# instead of allowing all undefined parameters to be passed you can allow only for some specific parameters with hudson.model.ParametersAction.safeParameters property
# you can read more on those vulnerabilities at: https://jenkins.io/blog/2016/05/11/security-update/ and https://wiki.jenkins-ci.org/display/SECURITY/Jenkins+Security+Advisory+2016-05-11
java -Dhudson.model.ParametersAction.keepUndefinedParameters=true -jar jenkins.war