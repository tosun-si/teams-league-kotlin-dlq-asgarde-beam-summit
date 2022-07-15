package fr.groupbees.application

import fr.groupbees.injection.DaggerTeamLeagueComponent
import fr.groupbees.injection.TeamLeagueComponent
import org.apache.beam.sdk.Pipeline
import org.apache.beam.sdk.options.PipelineOptionsFactory
import org.slf4j.LoggerFactory

object TeamLeagueApp {
    private val LOGGER = LoggerFactory.getLogger(TeamLeagueApp::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        val options = PipelineOptionsFactory
            .fromArgs(*args)
            .withValidation()
            .`as`(TeamLeagueOptions::class.java)

        val pipeline = Pipeline.create(options)
        val teamLeagueJob: TeamLeagueComponent = DaggerTeamLeagueComponent.builder()
            .withPipelineOptions(options)
            .build()

        teamLeagueJob.composer().compose(pipeline)

        pipeline.run().waitUntilFinish()
        LOGGER.info("End of CDP integration case JOB")
    }
}