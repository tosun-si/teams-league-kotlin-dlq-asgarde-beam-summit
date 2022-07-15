package fr.groupbees.infrastructure.io.cloudlogging

import fr.groupbees.asgarde.Failure
import fr.groupbees.domain_ptransform.FailureIOConnector
import org.apache.beam.sdk.transforms.PTransform
import org.apache.beam.sdk.values.PCollection
import org.apache.beam.sdk.values.POutput
import javax.inject.Inject

class FailureCloudLoggingIOAdapter @Inject constructor() : FailureIOConnector {

    override fun write(): PTransform<PCollection<Failure>, out POutput> {
        return FailureCloudLoggingWriteTransform()
    }
}