package fr.groupbees.infrastructure.io.bigquery

enum class FailureFields(val value: String) {

    FEATURE_NAME("featureName"),
    PIPELINE_STEP("pipelineStep"),
    JOB_NAME("jobName"),
    INPUT_ELEMENT("inputElement"),
    EXCEPTION_TYPE("exceptionType"),
    STACK_TRACE("stackTrace"),
    COMPONENT_TYPE("componentType"),
    DWH_CREATION_DATE("dwhCreationDate")
}
