package fr.groupbees.injection

import dagger.Binds
import dagger.Module
import fr.groupbees.domain_ptransform.FailureIOConnector
import fr.groupbees.domain_ptransform.TeamStatsDatabaseIOConnector
import fr.groupbees.domain_ptransform.TeamStatsFileIOConnector
import fr.groupbees.domain_ptransform.TeamStatsInMemoryIOConnector
import fr.groupbees.infrastructure.io.bigquery.FailureBigqueryIOAdapter
import fr.groupbees.infrastructure.io.bigquery.TeamStatsBigQueryIOAdapter
import fr.groupbees.infrastructure.io.cloudlogging.FailureCloudLoggingIOAdapter
import fr.groupbees.infrastructure.io.jsonfile.TeamStatsJsonFileIOAdapter
import fr.groupbees.infrastructure.io.mock.TeamStatsMockIOAdapter
import javax.inject.Named

@Module
interface IOConnectorModule {

    @Binds
    fun provideTeamStatsDatabaseIOConnector(bigQueryIOAdapter: TeamStatsBigQueryIOAdapter): TeamStatsDatabaseIOConnector

    @Binds
    fun provideTeamStatsFileIOConnector(fileIOAdapter: TeamStatsJsonFileIOAdapter): TeamStatsFileIOConnector

    @Binds
    fun provideTeamStatsInMemoryIOConnector(inMemoryIOAdapter: TeamStatsMockIOAdapter): TeamStatsInMemoryIOConnector

    @Binds
    @Named(FailureIOConnector.FAILURE_LOG_SINK_NAME)
    fun provideFailureLogIOConnector(failureLogAdapter: FailureCloudLoggingIOAdapter): FailureIOConnector

    @Binds
    @Named(FailureIOConnector.FAILURE_DATABASE_SINK_NAME)
    fun provideFailureDatabaseIOConnector(failureBigqueryIOAdapter: FailureBigqueryIOAdapter): FailureIOConnector
}