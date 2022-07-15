package fr.groupbees.domain_ptransform

import fr.groupbees.domain.TeamStatsRaw
import org.apache.beam.sdk.transforms.PTransform
import org.apache.beam.sdk.values.PBegin
import org.apache.beam.sdk.values.PCollection
import org.apache.beam.sdk.values.PCollectionView
import java.io.Serializable

interface TeamStatsFileIOConnector : Serializable {
    fun readTeamStats(): PTransform<PBegin, PCollection<TeamStatsRaw>>

    fun readTeamSlogans(): PTransform<PBegin, PCollectionView<String>>
}