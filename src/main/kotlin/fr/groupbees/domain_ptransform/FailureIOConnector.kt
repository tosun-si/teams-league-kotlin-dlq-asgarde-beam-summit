package fr.groupbees.domain_ptransform

import fr.groupbees.asgarde.Failure
import org.apache.beam.sdk.transforms.PTransform
import org.apache.beam.sdk.values.PCollection
import org.apache.beam.sdk.values.POutput
import java.io.Serializable

interface FailureIOConnector : Serializable {
    fun write(): PTransform<PCollection<Failure>, out POutput>

    companion object {
        const val FAILURE_LOG_SINK_NAME = "failure_log_sink"
        const val FAILURE_DATABASE_SINK_NAME = "failure_database_sink"
        const val LOG_FAILURES_STACKDRIVER = "Log failures to Cloud Logging"
        const val WRITE_FAILURES_DATABASE = "Write failures to database"
    }
}