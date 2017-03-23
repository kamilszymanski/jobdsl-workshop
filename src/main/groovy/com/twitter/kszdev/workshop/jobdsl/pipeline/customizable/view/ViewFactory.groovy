package com.twitter.kszdev.workshop.jobdsl.pipeline.customizable.view

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.View

class ViewFactory {

    static View createDeliveryPipelineView(DslFactory dslFactory, String projectName, String initialJob) {
        dslFactory.deliveryPipelineView(projectName) {
            allowPipelineStart()
            enableManualTriggers()
            pipelines {
                component(projectName, initialJob)
            }
        }
    }

}
