package utils

class Scm {

    static Closure git = {
        branchName ->
        return {
            git {
                remote {
                    url('git@github.com:solita/jenkins-job-dsl-example.git')
                }
                branch(branchName)
                localBranch(branchName)
                configure { node ->
                    node / userRemoteConfigs / 'hudson.plugins.git.UserRemoteConfig' / credentialsId('xxxx')
                    // Dont trigger our builds if only Jenkins Job DSL configuration changes
                    node / 'extensions' / 'hudson.plugins.git.extensions.impl.PathRestriction' / excludedRegions(".*jenkins.*")
                }
            }
        }
    }  
}