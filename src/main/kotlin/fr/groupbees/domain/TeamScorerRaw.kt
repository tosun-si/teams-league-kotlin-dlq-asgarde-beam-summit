package fr.groupbees.domain

import kotlinx.serialization.Serializable

@Serializable
data class TeamScorerRaw(
    val scorerFirstName: String,
    val scorerLastName: String,
    val goals: Int,
    val goalAssists: Int,
    val games: Int
) : java.io.Serializable