package utils

class HipChat {

    static Closure all = {
        return disabled([])
    }
    
    static Closure disabled = {
        disabledStatuses ->
        return {
            rooms('Project HipChat Room')
            notifyAborted(!disabledStatuses.contains('ABORTED'))
            notifyBackToNormal(!disabledStatuses.contains('TO_NORMAL'))
            notifyBuildStart(!disabledStatuses.contains('START'))
            notifyFailure(!disabledStatuses.contains('FAILURE'))
            notifyNotBuilt(!disabledStatuses.contains('NOT_BUILT'))
            notifySuccess(!disabledStatuses.contains('SUCCESS'))
            notifyUnstable(!disabledStatuses.contains('UNSTABLE'))
        } 
    }
}