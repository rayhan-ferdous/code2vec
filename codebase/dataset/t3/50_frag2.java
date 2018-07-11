                        Enumeration<InteractionDetectionMethod.InteractionDetectionMethodTerm> e = MIVocabTree.getInteractionDetectionMethodRoot().depthFirstEnumeration();

                        while (e.hasMoreElements()) {

                            InteractionDetectionMethod.InteractionDetectionMethodTerm idmTerm = e.nextElement();

                            if (cvTerm.getTermId().equalsIgnoreCase(termId)) {

                                experimentBuilder.setInteractionDetectionMethod(idmTerm);

                                break;

                            }

                        }

                        responseString = getInteractionDetectionMethodResponse(experiment, request, index);

                    }

                    sendResponse(response, responseString);

                    return null;

                }
