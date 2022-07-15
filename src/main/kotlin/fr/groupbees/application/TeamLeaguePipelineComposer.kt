package fr.groupbees.application

import fr.groupbees.domain_ptransform.*
import org.apache.beam.sdk.Pipeline
import javax.inject.Inject
import javax.inject.Named

class TeamLeaguePipelineComposer @Inject constructor(
    private val databaseIOConnector: TeamStatsDatabaseIOConnector,
    private val fileIOConnector: TeamStatsFileIOConnector,
    private val inMemoryIOConnector: TeamStatsInMemoryIOConnector,
    @Named(FailureIOConnector.FAILURE_LOG_SINK_NAME) private val failureLogIOConnector: FailureIOConnector,
    @Named(FailureIOConnector.FAILURE_DATABASE_SINK_NAME) private val failureDatabaseIOConnector: FailureIOConnector
) {
    fun compose(pipeline: Pipeline): Pipeline {
        val slogansSideInput = pipeline
            .apply("Read slogans", fileIOConnector.readTeamSlogans())

        val resultTeamStats = pipeline
            .apply("Read team stats", inMemoryIOConnector.read())
            .apply("Team stats domain transform", TeamStatsTransform(slogansSideInput))

        resultTeamStats
            .output()
            .apply("Write to database", databaseIOConnector.write())

        resultTeamStats
            .failures()
            .apply(FailureIOConnector.LOG_FAILURES_STACKDRIVER, failureLogIOConnector.write())

        resultTeamStats
            .failures()
            .apply(FailureIOConnector.WRITE_FAILURES_DATABASE, failureDatabaseIOConnector.write())

        return pipeline
    }
}