package fr.groupbees.infrastructure.io.mock

import fr.groupbees.domain.TeamStatsRaw
import fr.groupbees.domain_ptransform.TeamStatsInMemoryIOConnector
import org.apache.beam.sdk.transforms.PTransform
import org.apache.beam.sdk.values.PBegin
import org.apache.beam.sdk.values.PCollection
import javax.inject.Inject

class TeamStatsMockIOAdapter @Inject constructor() : TeamStatsInMemoryIOConnector {

    override fun read(): PTransform<PBegin, PCollection<TeamStatsRaw>> {
        return TeamStatsMockReadTransform()
    }
}