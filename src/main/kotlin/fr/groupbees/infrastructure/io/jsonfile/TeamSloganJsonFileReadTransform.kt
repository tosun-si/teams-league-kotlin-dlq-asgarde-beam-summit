package fr.groupbees.infrastructure.io.jsonfile

import fr.groupbees.application.PipelineConf
import org.apache.beam.sdk.io.TextIO
import org.apache.beam.sdk.transforms.PTransform
import org.apache.beam.sdk.transforms.View
import org.apache.beam.sdk.values.PBegin
import org.apache.beam.sdk.values.PCollectionView

class TeamSloganJsonFileReadTransform(private val pipelineConf: PipelineConf) :
    PTransform<PBegin, PCollectionView<String>>() {

    override fun expand(input: PBegin): PCollectionView<String> {
        return input
            .apply("Read slogans", TextIO.read().from(pipelineConf.inputFileSlogans))
            .apply("Create as collection view", View.asSingleton())
    }
}