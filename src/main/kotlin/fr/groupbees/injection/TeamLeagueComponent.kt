package fr.groupbees.injection

import dagger.BindsInstance
import dagger.Component
import fr.groupbees.application.TeamLeagueOptions
import fr.groupbees.application.TeamLeaguePipelineComposer
import javax.inject.Singleton

@Singleton
@Component(modules = [TeamLeagueModule::class])
interface TeamLeagueComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun withPipelineOptions(options: TeamLeagueOptions): Builder

        fun build(): TeamLeagueComponent
    }

    fun composer(): TeamLeaguePipelineComposer
}