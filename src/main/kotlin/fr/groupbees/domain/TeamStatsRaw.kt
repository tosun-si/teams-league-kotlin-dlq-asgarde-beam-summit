package fr.groupbees.domain

import fr.groupbees.domain.exception.TeamStatsRawValidatorException
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TeamStatsRaw(
    val teamName: String,
    val teamScore: Int,
    val scorers: List<TeamScorerRaw>
) : java.io.Serializable {

    fun validateFields(): TeamStatsRaw {
        if (Objects.isNull(teamName) || teamName == "") {
            throw TeamStatsRawValidatorException(TEAM_EMPTY_ERROR_MESSAGE)
        }
        return this
    }

    companion object {
        const val TEAM_EMPTY_ERROR_MESSAGE = "Team name cannot be null or empty"
    }
}