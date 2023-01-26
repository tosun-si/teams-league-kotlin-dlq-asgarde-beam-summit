package fr.groupbees.infrastructure.io.bigquery

import com.google.api.services.bigquery.model.TableRow
import fr.groupbees.asgarde.Failure
import fr.groupbees.domain_ptransform.FailureIOConnector
import fr.groupbees.infrastructure.io.FailureConf
import fr.groupbees.infrastructure.io.bigquery.FailureFields.*
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO
import org.apache.beam.sdk.transforms.PTransform
import org.apache.beam.sdk.values.PCollection
import org.apache.beam.sdk.values.POutput
import org.joda.time.Instant
import javax.inject.Inject

class FailureBigqueryIOAdapter @Inject constructor(private val failureConf: FailureConf) : FailureIOConnector {

    override fun write(): PTransform<PCollection<Failure>, out POutput> {
        return BigQueryIO.write<Failure>()
            .withMethod(BigQueryIO.Write.Method.FILE_LOADS)
            .to("${failureConf.outputDataset}.${failureConf.outputTable}")
            .withFormatFunction { failure -> toFailureTableRow(failureConf, failure) }
            .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
            .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
    }

    companion object {
        private const val COMPONENT_TYPE_VALUE = "DATAFLOW"

        private fun toFailureTableRow(failureConf: FailureConf, failure: Failure): TableRow {
            val creationDate = Instant.now().toString()
            return TableRow()
                .set(FEATURE_NAME.value, failureConf.featureName)
                .set(PIPELINE_STEP.value, failure.pipelineStep)
                .set(JOB_NAME.value, failureConf.jobName)
                .set(INPUT_ELEMENT.value, failure.inputElement)
                .set(EXCEPTION_TYPE.value, failure.exception.javaClass.simpleName)
                .set(STACK_TRACE.value, failure.exception.stackTraceToString())
                .set(COMPONENT_TYPE.value, COMPONENT_TYPE_VALUE)
                .set(DWH_CREATION_DATE.value, creationDate)
        }
    }
}