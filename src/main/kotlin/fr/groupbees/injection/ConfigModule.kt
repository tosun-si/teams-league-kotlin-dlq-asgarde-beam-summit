package fr.groupbees.injection

import dagger.Module
import dagger.Provides
import fr.groupbees.application.PipelineConf
import fr.groupbees.application.TeamLeagueOptions
import fr.groupbees.infrastructure.io.FailureConf

@Module
internal object ConfigModule {

    @Provides
    fun providePipelineConf(options: TeamLeagueOptions): PipelineConf {
        return PipelineConf(
            inputJsonFile = options.inputJsonFile,
            inputFileSlogans = options.inputFileSlogans,
            teamLeagueDataset = options.teamLeagueDataset,
            teamStatsTable = options.teamStatsTable
        )
    }

    @Provides
    fun provideFailureConf(options: TeamLeagueOptions): FailureConf {
        return FailureConf(
            featureName = options.failureFeatureName,
            jobName = options.jobType,
            outputDataset = options.failureOutputDataset,
            outputTable = options.failureOutputTable
        )
    }
}