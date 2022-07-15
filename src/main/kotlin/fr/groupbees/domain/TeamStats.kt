package fr.groupbees.domain

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TeamStats(
    val teamName: String,
    val teamScore: Int,
    val teamTotalGoals: Int,
    val teamSlogan: String = "",
    val topScorerStats: TeamTopScorerStats,
    val bestPasserStats: TeamBestPasserStats
) : java.io.Serializable {

    fun addSloganToStats(allSlogans: Map<String, String>): TeamStats {
        val slogan = Optional
            .ofNullable(allSlogans[teamName])
            .orElseThrow { IllegalArgumentException("No slogan for team : $teamName") }

        return this.copy(
            teamSlogan = slogan,
        )
    }

    companion object {
        fun computeTeamStats(teamStatsRaw: TeamStatsRaw): TeamStats {
            val teamScorers: List<TeamScorerRaw> = teamStatsRaw.scorers

            val topScorer = teamScorers.stream()
                .max(Comparator.comparing(TeamScorerRaw::goals))
                .orElseThrow { NoSuchElementException() }

            val bestPasser = teamScorers.stream()
                .max(Comparator.comparing(TeamScorerRaw::goalAssists))
                .orElseThrow { NoSuchElementException() }

            val teamTotalGoals = teamScorers.stream()
                .mapToInt(TeamScorerRaw::goals)
                .sum()

            val topScorerStats = TeamTopScorerStats(
                firstName = topScorer.scorerFirstName,
                lastName = topScorer.scorerLastName,
                goals = topScorer.goals,
                games = topScorer.games
            )

            val bestPasserStats = TeamBestPasserStats(
                firstName = bestPasser.scorerFirstName,
                lastName = bestPasser.scorerLastName,
                goalAssists = bestPasser.goalAssists,
                games = bestPasser.games
            )

            return TeamStats(
                teamName = teamStatsRaw.teamName,
                teamScore = teamStatsRaw.teamScore,
                teamTotalGoals = teamTotalGoals,
                topScorerStats = topScorerStats,
                bestPasserStats = bestPasserStats
            )
        }
    }
}
