package fr.groupbees.domain_ptransform

import fr.groupbees.domain.TeamStatsRaw
import org.apache.beam.sdk.transforms.PTransform
import org.apache.beam.sdk.values.PBegin
import org.apache.beam.sdk.values.PCollection
import java.io.Serializable

interface TeamStatsInMemoryIOConnector : Serializable {
    fun read(): PTransform<PBegin, PCollection<TeamStatsRaw>>
}