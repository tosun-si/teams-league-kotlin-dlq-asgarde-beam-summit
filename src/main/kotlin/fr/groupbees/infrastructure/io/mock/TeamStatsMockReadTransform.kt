package fr.groupbees.infrastructure.io.mock

import fr.groupbees.domain.TeamScorerRaw
import fr.groupbees.domain.TeamStatsRaw
import org.apache.beam.sdk.transforms.Create
import org.apache.beam.sdk.transforms.PTransform
import org.apache.beam.sdk.values.PBegin
import org.apache.beam.sdk.values.PCollection
import javax.inject.Inject

class TeamStatsMockReadTransform @Inject constructor() : PTransform<PBegin, PCollection<TeamStatsRaw>>() {

    override fun expand(input: PBegin): PCollection<TeamStatsRaw> {
        val psgScorers = listOf(
            TeamScorerRaw("Kylian", "Mbappe", 15, 6, 13),
            TeamScorerRaw("Da Silva", "Neymar", 11, 7, 12),
            TeamScorerRaw("Angel", "Di Maria", 7, 8, 13),
            TeamScorerRaw("Lionel", "Messi", 12, 8, 13),
            TeamScorerRaw("Marco", "Verrati", 3, 10, 13)
        )
        val realScorers = listOf(
            TeamScorerRaw("Karim", "Benzema", 14, 7, 13),
            TeamScorerRaw("Junior", "Vinicius", 9, 6, 12),
            TeamScorerRaw("Luca", "Modric", 5, 9, 11),
            TeamScorerRaw("Silva", "Rodrygo", 7, 5, 13),
            TeamScorerRaw("Marco", "Asensio", 6, 3, 13)
        )
        val teamStats = listOf(
            TeamStatsRaw("", 30, psgScorers),
            TeamStatsRaw("Real", 25, realScorers)
        )

        return input.apply("Create in memory team stats", Create.of(teamStats))
    }
}