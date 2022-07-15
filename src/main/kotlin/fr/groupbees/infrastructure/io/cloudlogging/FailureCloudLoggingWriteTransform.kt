package fr.groupbees.infrastructure.io.cloudlogging

import fr.groupbees.asgarde.Failure
import org.apache.beam.sdk.transforms.MapElements
import org.apache.beam.sdk.transforms.PTransform
import org.apache.beam.sdk.transforms.SerializableFunction
import org.apache.beam.sdk.values.PCollection
import org.apache.beam.sdk.values.PDone
import org.apache.beam.sdk.values.TypeDescriptors
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.LoggerFactory

class FailureCloudLoggingWriteTransform : PTransform<PCollection<Failure>, PDone>() {

    override fun expand(input: PCollection<Failure>): PDone {
        input.apply(STEP_MAP_FAILURE_JSON_STRING, LogTransform())
        return PDone.`in`(input.pipeline)
    }

    internal class LogTransform : PTransform<PCollection<Failure>, PCollection<String>>() {
        override fun expand(input: PCollection<Failure>): PCollection<String> {
            return input
                .apply(
                    STEP_MAP_FAILURE_JSON_STRING, MapElements
                        .into(TypeDescriptors.strings())
                        .via(SerializableFunction { failure -> toFailureLogInfo(failure) })
                )
                .apply(
                    STEP_LOG_FAILURE, MapElements
                        .into(TypeDescriptors.strings())
                        .via(SerializableFunction { failureAsString: String -> logFailure(failureAsString) })
                )
        }

        /**
         * Logs the given failure string object.
         */
        private fun logFailure(failureAsString: String): String {
            LOGGER.error(failureAsString)
            return failureAsString
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(FailureCloudLoggingWriteTransform::class.java)
        private const val STEP_MAP_FAILURE_JSON_STRING = "Map Failure to Json String"
        private const val STEP_LOG_FAILURE = "Logs Failure to stackDriver"

        private fun toFailureLogInfo(failure: Failure): String {
            val inputElementInfo = "InputElement : " + failure.inputElement
            val stackTraceInfo = "StackTrace : " + ExceptionUtils.getStackTrace(failure.exception)

            return "$inputElementInfo \n $stackTraceInfo"
        }
    }
}