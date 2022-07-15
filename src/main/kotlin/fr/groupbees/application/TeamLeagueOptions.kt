package fr.groupbees.application

import org.apache.beam.sdk.options.Description
import org.apache.beam.sdk.options.PipelineOptions

interface TeamLeagueOptions : PipelineOptions {
    @get:Description("Path of the input Json file to read from")
    var inputJsonFile: String

    @get:Description("Path of the slogans file to read from")
    var inputFileSlogans: String

    @get:Description("Path of the file to write to")
    var teamLeagueDataset: String

    @get:Description("Team stats table")
    var teamStatsTable: String

    @get:Description("Job type")
    var jobType: String

    @get:Description("Failure output dataset")
    var failureOutputDataset: String

    @get:Description("Failure output table")
    var failureOutputTable: String

    @get:Description("Feature name for failures")
    var failureFeatureName: String
}