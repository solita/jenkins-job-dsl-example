import utils.HipChat
import utils.Scm
import utils.Job

def build = Job.name('MyProject', 'Test', 'Build')
def uiUnitTests = Job.name('MyProject', 'Test', 'UI Unit Tests')
def deploy = Job.name('MyProject', 'Test', 'Deploy')

Job.build(this, 
[
    name: build,
    description: 'Runs manually running all tests including @IntegrationTests annotated',
    gulp: 'gulp build',
    mvn: 'properties:read-project-properties dbmaintain:clearDatabase dbmaintain:updateDatabase clean package -Pdb-ci',
    scmTrigger: '',
    downstream: uiUnitTests,
    scm: Scm.git('tags/v1.4.3'),
    hipchat: HipChat.all()
])

Job.uiUnitTests(this, 
[
    name: uiUnitTests,
    description: 'Triggered by Test - Build, triggers Test - Deploy on success',
    downstream: deploy,
    scm: Scm.git('tags/v1.4.3'),
    hipchat: HipChat.all()
])

Job.deploy(this,
[
    name: deploy,
    description: '',
    deploy: build,
    host: 'test.myproject.domain',
    clearDatabase: false,
    downstream: '',
    hipchat: HipChat.all()
])