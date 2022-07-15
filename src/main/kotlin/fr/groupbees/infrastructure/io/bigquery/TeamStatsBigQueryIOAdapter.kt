package fr.groupbees.infrastructure.io.bigquery

import com.google.api.services.bigquery.model.TableRow
import fr.groupbees.application.PipelineConf
import fr.groupbees.domain.TeamStats
import fr.groupbees.domain_ptransform.TeamStatsDatabaseIOConnector
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO
import org.joda.time.Instant
import javax.inject.Inject

class TeamStatsBigQueryIOAdapter @Inject constructor(private val pipelineConf: PipelineConf) :
    TeamStatsDatabaseIOConnector {

    override fun write(): BigQueryIO.Write<TeamStats> {
        return BigQueryIO.write<TeamStats>()
            .withMethod(BigQueryIO.Write.Method.FILE_LOADS)
            .to("${pipelineConf.teamLeagueDataset}.${pipelineConf.teamStatsTable}")
            .withFormatFunction { teamStats: TeamStats -> toTeamStatsTableRow(teamStats) }
            .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
            .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
    }

    companion object {

        private fun toTeamStatsTableRow(teamStats: TeamStats): TableRow {
            val topScorerStats = teamStats.topScorerStats
            val bestPasser = teamStats.bestPasserStats

            val topScorerStatsRow = TableRow()
                .set("firstName", topScorerStats.firstName)
                .set("lastName", topScorerStats.lastName)
                .set("goals", topScorerStats.goals)
                .set("games", topScorerStats.games)

            val bestPasserStatsRow = TableRow()
                .set("firstName", bestPasser.firstName)
                .set("lastName", bestPasser.lastName)
                .set("goalAssists", bestPasser.goalAssists)
                .set("games", bestPasser.games)

            return TableRow()
                .set("teamName", teamStats.teamName)
                .set("teamScore", teamStats.teamScore)
                .set("teamTotalGoals", teamStats.teamTotalGoals)
                .set("teamSlogan", teamStats.teamSlogan)
                .set("topScorerStats", topScorerStatsRow)
                .set("bestPasserStats", bestPasserStatsRow)
                .set("ingestionDate", Instant().toString())
        }
    }
}