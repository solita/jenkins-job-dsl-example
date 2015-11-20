import utils.HipChat
import utils.Scm
import utils.Job

def build = Job.name('MyProject', 'Nightly', 'Build')
def deploy = Job.name('MyProject', 'Nightly', 'Deploy')

Job.build(this,
[
    name: build,
    description: 'Runs @midnight running all tests including @IntegrationTests annotated',
    gulp: 'gulp build',
    mvn: 'properties:read-project-properties dbmaintain:clearDatabase dbmaintain:updateDatabase clean package -Pdb-ci',
    scmTrigger: '@midnight',
    downstream: deploy,
    coverage: false,
    scm: Scm.git('master'),
    hipchat: HipChat.disabled(['START', 'SUCCESS'])
])

Job.deploy(this,
[
    name: deploy,
    description: '',
    deploy: build,
    host: 'nightly.myproject.domain',
    clearDatabase: true,
    importDatabase: true,
    downstream: '',
    hipchat: HipChat.disabled(['START', 'SUCCESS'])
])