package com.twitter.kszdev.workshop.jobdsl.pipeline.customizable.step.publisher

import javaposse.jobdsl.dsl.helpers.publisher.PublisherContext

class DownstreamTriggerContext {

    private static final String SUCCESSFUL_BUILD = 'SUCCESS'

    static void triggerDownstreamJob(PublisherContext publisherContext, String downstreamJob, Map downstreamParameters = [:]) {
        publisherContext.downstreamParameterized {
            trigger(downstreamJob) {
                condition SUCCESSFUL_BUILD
                parameters {
                    currentBuild()
                    if (downstreamParameters) {
                        predefinedProps downstreamParameters
                    }
                }
            }
        }
    }

    static void manuallyTriggerDownstreamJob(PublisherContext publisherContext, String downstreamJob, Map downstreamParameters = [:]) {
        publisherContext.buildPipelineTrigger(downstreamJob) {
            parameters {
                currentBuild()
                if (downstreamParameters) {
                    predefinedProps downstreamParameters
                }
            }
        }
    }

}
