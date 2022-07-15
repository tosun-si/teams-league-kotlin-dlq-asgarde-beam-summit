package fr.groupbees.domain_ptransform

import com.fasterxml.jackson.core.type.TypeReference
import fr.groupbees.asgarde.*
import fr.groupbees.domain.TeamStats
import fr.groupbees.domain.TeamStatsRaw
import org.apache.beam.sdk.transforms.DoFn
import org.apache.beam.sdk.transforms.PTransform
import org.apache.beam.sdk.transforms.WithFailures.Result
import org.apache.beam.sdk.values.PCollection
import org.apache.beam.sdk.values.PCollectionView
import org.slf4j.LoggerFactory

class TeamStatsTransform(private val slogansSideInput: PCollectionView<String>) :
    PTransform<PCollection<TeamStatsRaw>, Result<PCollection<TeamStats>, Failure>>() {

    override fun expand(input: PCollection<TeamStatsRaw>): Result<PCollection<TeamStats>, Failure> {
        return CollectionComposer.of(input)
            .map("Validate fields") { it.validateFields() }
            .mapFn(
                name = "Compute team stats",
                startBundleAction = { LOGGER.info("####################Start bundle compute stats") },
                transform = { TeamStats.computeTeamStats(it) })
            .mapFnWithContext<TeamStats, TeamStats>(
                name = "Add team slogan",
                setupAction = { LOGGER.info("####################Start add slogan") },
                sideInputs = listOf(slogansSideInput),
                transform = { addSloganToStats(it, slogansSideInput) }
            )
            .result
    }

    fun addSloganToStats(
        context: DoFn<TeamStats, TeamStats>.ProcessContext,
        slogansSideInput: PCollectionView<String>
    ): TeamStats {
        val slogans: String = context.sideInput(slogansSideInput)

        val ref = object : TypeReference<Map<String, String>>() {}
        val slogansAsMap = JsonUtil.deserializeToMap(slogans, ref)
        val teamStats: TeamStats = context.element()

        return teamStats.addSloganToStats(slogansAsMap)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TeamStatsTransform::class.java)
    }
}