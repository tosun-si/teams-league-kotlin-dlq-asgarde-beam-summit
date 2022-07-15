package fr.groupbees.domain

import kotlinx.serialization.Serializable

@Serializable
data class TeamBestPasserStats(
    val firstName: String,
    val lastName: String,
    val goalAssists: Int = 0,
    val games: Int = 0
) : java.io.Serializable