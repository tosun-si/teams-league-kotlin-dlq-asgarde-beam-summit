package fr.groupbees.infrastructure.io

import java.io.Serializable

class FailureConf(
    val featureName: String,
    val jobName: String,
    val outputDataset: String,
    val outputTable: String
) : Serializable