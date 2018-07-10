                        Enumeration<ParticipantIdentificationMethod.ParticipantIdentificationMethodTerm> e = MIVocabTree.getParticipantIDMethodRoot().depthFirstEnumeration();

                        while (e.hasMoreElements()) {

                            ParticipantIdentificationMethod.ParticipantIdentificationMethodTerm pidmTerm = e.nextElement();

                            if (cvTerm.getTermId().equalsIgnoreCase(termId)) {

                                experimentBuilder.setParticipantIDMethod(pidmTerm.shallowClone());

                                break;

                            }

                        }

                        responseString = getParticipantIDMethodResponse(experiment, request, index);

                    }

                    sendResponse(response, responseString);

                    return null;

                } else if (cvTerm instanceof FeatureDetectionMethod.FeatureDetectionMethodTerm) {
