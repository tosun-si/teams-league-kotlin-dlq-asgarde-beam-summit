package fr.groupbees.application

data class PipelineConf(
    val inputJsonFile: String,
    val inputFileSlogans: String,
    val teamLeagueDataset: String,
    val teamStatsTable: String
) : java.io.Serializable