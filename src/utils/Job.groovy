package utils

class Job {

    static String name(def prefix, def name, def suffix) {
        return "${prefix} - ${name} - ${suffix}"
    }

    static void build(def script, def profile) {
      
        script.mavenJob(profile.name) {
            description(profile.description)
            blockOnUpstreamProjects()
            blockOn('*Build*') {
                blockLevel('GLOBAL')
            }
            jdk('Java 8')
            scm(profile.scm)
            triggers {
                if (profile.scmTrigger)
                    scm(profile.scmTrigger)
            }
            preBuildSteps {
                shell("""
                  cd frontend
                  bower install
                  npm install
                  ${profile.gulp}
                """.stripIndent())
            }
            goals(profile.mvn)
            rootPOM('backend/pom.xml')
            publishers {
                archiveArtifacts('backend/target/*.jar')
                if (profile.downstream)
                    downstream (profile.downstream, 'SUCCESS')
                if (profile.jacoco)
                    jacocoCodeCoverage(profile.jacoco)
                hipChat(profile.hipchat)
            }
        }
    }

    static void uiUnitTests(def script, def profile) {
      
        script.job(profile.name) {
            description(profile.description)
            blockOnUpstreamProjects()
            scm(profile.scm)
            wrappers {
                xvfb('Xvfb') {
                    screen('1024x768x24')
                }
            }
            steps {
                shell("""
                    cd frontend
                    bower install
                    npm install
                    gulp test-unit
                """.stripIndent())
            }
            publishers {
                archiveJunit('frontend/test-unit/test-results.xml')
                downstream(profile.downstream, 'SUCCESS')
                hipChat(profile.hipchat)
            }
        }
    }

    static void e2eTests(def script, def profile) {
      
        script.job(profile.name) {
            description(profile.description)
            blockOnUpstreamProjects()
            scm(profile.scm)
            wrappers {
                xvfb('Xvfb') {
                    screen('1024x768x24')
                }
            }
            steps {
                shell("""
                    #!/bin/bash
                    cd frontend
                    bower install
                    npm install
                    gulp test-e2e --testBaseUrl https://${profile.host}
                """.stripIndent())
            }
            publishers {
                archiveArtifacts {
                    pattern('frontend/test-e2e/screenshots/*')
                    allowEmpty(true)
                }
                archiveJunit('frontend/test-e2e/test-results.xml')
                hipChat(profile.hipchat)
            }
        }
    }

    static void deploy(def script, def profile) {
      
        script.job(profile.name) {
            description(profile.description)
            steps {
              shell("Deploy artifacts here and stop server")
              if (profile.clearDatabase)
                  shell("dbmaintain clear database")
              if (profile.importDatabase)
                  shell("Import some test data")
              shell("Database migration and update application")
            }
            publishers {
                if (profile.downstream)
                    downstream(profile.downstream, 'SUCCESS')
                hipChat(profile.hipchat)
            }
        }
    }
}
