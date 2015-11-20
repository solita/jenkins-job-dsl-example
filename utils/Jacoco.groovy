package utils

class Jacoco {

    static Closure defaults = {
        return {
            changeBuildStatus(true)
            execPattern('**/**.exec')
            classPattern('**/classes')
            sourcePattern('**/src/main/java')
            inclusionPattern('')
            exclusionPattern('')
            minimumInstructionCoverage('75')
            minimumBranchCoverage('68')
            minimumComplexityCoverage('66')
            minimumLineCoverage('74')
            minimumMethodCoverage('68')
            minimumClassCoverage('87')
            maximumInstructionCoverage('76')
            maximumBranchCoverage('69')
            maximumComplexityCoverage('67')
            maximumLineCoverage('75')
            maximumMethodCoverage('69')
            maximumClassCoverage('88')
        }
    }
}