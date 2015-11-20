import utils.HipChat
import utils.Scm
import utils.Jacoco
import utils.Job

def build = Job.name('MyProject', 'Dev', 'Build')
def uiUnitTests = Job.name('MyProject', 'Dev', 'UI Unit Tests')
def e2eTests = Job.name('MyProject', 'Dev', 'E2E Tests')
def deploy = Job.name('MyProject', 'Dev', 'Deploy');

Job.build(this,
[
    name: build,
    description: 'Runs after every SCM change running all tests except @IntegrationTests annotated, triggers UI Unit Tests on success',
    gulp: 'gulp build --profile="dev"',
    mvn: 'properties:read-project-properties dbmaintain:clearDatabase dbmaintain:updateDatabase clean package -Pdev-ad,db-ci -DexcludedGroups="fi.myproject.IntegrationTests"',
    scmTrigger: 'H/5 * * * *',
    downstream: uiUnitTests,
    jacoco: Jacoco.defaults(),
    scm: Scm.git('master'),
    hipchat: HipChat.disabled(['START', 'SUCCESS'])
])

Job.uiUnitTests(this,
[
    name: uiUnitTests,
    description: 'Triggered by Dev - Build, triggers Dev - Deploy on success',
    downstream: deploy,
    scm: Scm.git('master'),
    hipchat: HipChat.disabled(['START', 'SUCCESS'])
])

Job.e2eTests(this,
[
    name: e2eTests,
    description: 'Triggered by Dev - Deploy',
    host: 'dev.myproject.domain',
    scm: Scm.git('master'),
    hipchat: HipChat.disabled(['START', 'SUCCESS'])
])

Job.deploy(this,
[
    name: deploy,
    description: '',
    deploy: build,
    host: 'dev.myproject.domain',
    clearDatabase: true,
    downstream: e2eTests,
    hipchat: HipChat.disabled(['START', 'SUCCESS'])
])