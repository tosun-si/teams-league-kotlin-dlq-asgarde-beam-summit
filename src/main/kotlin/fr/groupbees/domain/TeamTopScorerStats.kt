package fr.groupbees.domain

import kotlinx.serialization.Serializable

@Serializable
data class TeamTopScorerStats(
    val firstName: String,
    val lastName: String,
    val goals: Int,
    val games: Int
) : java.io.Serializable