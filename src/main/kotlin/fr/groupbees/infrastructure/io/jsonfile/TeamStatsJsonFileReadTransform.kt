package fr.groupbees.infrastructure.io.jsonfile

import fr.groupbees.application.PipelineConf
import fr.groupbees.domain.TeamStatsRaw
import fr.groupbees.domain_ptransform.JsonUtil.deserialize
import org.apache.beam.sdk.io.TextIO
import org.apache.beam.sdk.transforms.MapElements
import org.apache.beam.sdk.transforms.PTransform
import org.apache.beam.sdk.transforms.SerializableFunction
import org.apache.beam.sdk.values.PBegin
import org.apache.beam.sdk.values.PCollection
import org.apache.beam.sdk.values.TypeDescriptor.of

class TeamStatsJsonFileReadTransform(private val pipelineConf: PipelineConf) :
    PTransform<PBegin, PCollection<TeamStatsRaw>>() {

    override fun expand(input: PBegin): PCollection<TeamStatsRaw> {
        return input
            .apply("Read Json file", TextIO.read().from(pipelineConf.inputJsonFile))
            .apply(
                "Deserialize",
                MapElements.into(of(TeamStatsRaw::class.java)).via(SerializableFunction { deserializeToTeamStats(it) })
            )
    }

    private fun deserializeToTeamStats(teamStatsAsString: String): TeamStatsRaw {
        return deserialize(teamStatsAsString, TeamStatsRaw::class.java)
    }
}