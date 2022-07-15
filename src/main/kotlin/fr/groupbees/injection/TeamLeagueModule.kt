package fr.groupbees.injection

import dagger.Module

@Module(includes = [IOConnectorModule::class, ConfigModule::class])
class TeamLeagueModule 