package fr.groupbees.domain_ptransform

import fr.groupbees.asgarde.Failure
import fr.groupbees.domain.TeamStats
import fr.groupbees.domain.TeamStatsRaw
import fr.groupbees.domain.exception.TeamStatsRawValidatorException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.beam.sdk.Pipeline
import org.apache.beam.sdk.testing.PAssert
import org.apache.beam.sdk.testing.TestPipeline
import org.apache.beam.sdk.testing.ValidatesRunner
import org.apache.beam.sdk.transforms.Create
import org.apache.beam.sdk.transforms.MapElements
import org.apache.beam.sdk.transforms.SerializableFunction
import org.apache.beam.sdk.transforms.View
import org.apache.beam.sdk.values.PCollection
import org.apache.beam.sdk.values.PCollectionView
import org.apache.beam.sdk.values.TypeDescriptors.strings
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import java.io.Serializable

private inline fun <reified T> deserializeFromResourcePath(resourcePath: String): List<T> {
    val elementAsString = ResourceUtil.toStringContent(resourcePath)
    return Json.decodeFromString(elementAsString)
}

class TeamStatsTransformTest : Serializable {

    @Transient
    private val pipeline: TestPipeline = TestPipeline.create()

    @Rule
    fun pipeline(): TestPipeline = pipeline

    @Test
    @Category(ValidatesRunner::class)
    fun givenInputTeamsStatsRawWithoutErrorWhenTransformToStatsDomainThenExpectedOutputInResult() {
        // Given.
        val inputTeamsStatsRaw = deserializeFromResourcePath<TeamStatsRaw>(
            "files/input/domain/ptransform/input_teams_stats_raw_without_error.json"
        )

        val input = pipeline.apply("Read team stats Raw", Create.of(inputTeamsStatsRaw))

        // When.
        val resultTransform = input
            .apply("Transform to team stats", TeamStatsTransform(getSlogansSideInput(pipeline)))

        val output: PCollection<String> = resultTransform
            .output()
            .apply(
                "Map to Json String",
                MapElements.into(strings()).via(SerializableFunction { JsonUtil.serialize(it) })
            )
            .apply(
                "Log Output team stats",
                MapElements.into(strings()).via(SerializableFunction { logStringElement(it) })
            )

        val expectedTeamsStats = deserializeFromResourcePath<TeamStats>(
            "files/expected/domain/ptransform/expected_teams_stats_without_error.json"
        ).map { Json.encodeToString(it) }

        // Then.
        PAssert.that(output).containsInAnyOrder(expectedTeamsStats)
        PAssert.that(resultTransform.failures()).empty()
        pipeline.run().waitUntilFinish()
    }

    @Test
    @Category(ValidatesRunner::class)
    fun givenInputTeamsStatsRawWithOneErrorAndOneGoodInputWhenTransformToStatsDomainThenOneExpectedFailureAndOneGoodOutput() {
        // Given.
        val inputTeamsStatsRaw = deserializeFromResourcePath<TeamStatsRaw>(
            "files/input/domain/ptransform/input_teams_stats_raw_with_one_error_one_good_output.json"
        )
        val input = pipeline.apply("Read team stats Raw", Create.of(inputTeamsStatsRaw))

        // When.
        val resultTransform = input
            .apply("Transform to team stats", TeamStatsTransform(getSlogansSideInput(pipeline)))

        val output: PCollection<String> = resultTransform
            .output()
            .apply(
                "Map to Json String",
                MapElements.into(strings()).via(SerializableFunction { JsonUtil.serialize(it) })
            )
            .apply(
                "Log Output team stats",
                MapElements.into(strings()).via(SerializableFunction { logStringElement(it) })
            )

        val expectedTeamsStats = deserializeFromResourcePath<TeamStats>(
            "files/expected/domain/ptransform/expected_teams_stats_with_one_error_one_good_output.json"
        ).map { Json.encodeToString(it) }

        val teamStatsRawWithErrorField = inputTeamsStatsRaw
            .find { t: TeamStatsRaw -> t.teamScore == PSG_SCORE }

        val expectedFailure = Failure.from(
            "Validate fields",
            teamStatsRawWithErrorField,
            TeamStatsRawValidatorException(TeamStatsRaw.TEAM_EMPTY_ERROR_MESSAGE)
        ).toString()

        val resultFailuresAsString = resultTransform.failures()
            .apply("Failure as string", MapElements.into(strings()).via(SerializableFunction { it.toString() }))
            .apply("Log Failure", MapElements.into(strings()).via(SerializableFunction { logStringElement(it) }))

        // Then.
        PAssert.that(output).containsInAnyOrder(expectedTeamsStats)
        PAssert.that(resultFailuresAsString).containsInAnyOrder(listOf(expectedFailure))
        pipeline.run().waitUntilFinish()
    }

    private fun getSlogansSideInput(pipeline: Pipeline): PCollectionView<String> {
        return pipeline
            .apply("String side input", Create.of(SLOGANS))
            .apply("Create as collection view", View.asSingleton())
    }

    private fun logStringElement(element: String): String {
        println(element)
        return element
    }

    companion object {
        private const val PSG_SCORE = 30
        private const val SLOGANS = "{\"PSG\": \"Paris est magique\",\"Real\": \"Hala Madrid\"}"
    }
}