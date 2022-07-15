package fr.groupbees.infrastructure.io.jsonfile

import fr.groupbees.application.PipelineConf
import fr.groupbees.domain.TeamStatsRaw
import fr.groupbees.domain_ptransform.TeamStatsFileIOConnector
import org.apache.beam.sdk.transforms.PTransform
import org.apache.beam.sdk.values.PBegin
import org.apache.beam.sdk.values.PCollection
import org.apache.beam.sdk.values.PCollectionView
import javax.inject.Inject

class TeamStatsJsonFileIOAdapter @Inject constructor(private val pipelineConf: PipelineConf) :
    TeamStatsFileIOConnector {

    override fun readTeamStats(): PTransform<PBegin, PCollection<TeamStatsRaw>> {
        return TeamStatsJsonFileReadTransform(pipelineConf)
    }

    override fun readTeamSlogans(): PTransform<PBegin, PCollectionView<String>> {
        return TeamSloganJsonFileReadTransform(pipelineConf)
    }
}