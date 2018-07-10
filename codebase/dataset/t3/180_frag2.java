                                                                        e = 99;

                                                                        break;

                                                                    }

                                                                }

                                                            }

                                                        }

                                                    }

                                                }

                                            }

                                            MP4Atom stsc = stbl.lookup(MP4Atom.typeToInt("stsc"), 0);

                                            if (stsc != null) {

                                                log.debug("Sample to chunk atom found");

                                                audioSamplesToChunks = stsc.getRecords();

                                                log.debug("Record count: {}", audioSamplesToChunks.size());

                                                MP4Atom.Record rec = audioSamplesToChunks.firstElement();

                                                log.debug("Record data: Description index={} Samples per chunk={}", rec.getSampleDescriptionIndex(), rec.getSamplesPerChunk());

                                            }

                                            MP4Atom stsz = stbl.lookup(MP4Atom.typeToInt("stsz"), 0);

                                            if (stsz != null) {

                                                log.debug("Sample size atom found");

                                                audioSamples = stsz.getSamples();

                                                log.debug("Sample size: {}", stsz.getSampleSize());

                                                log.debug("Sample count: {}", audioSamples.size());
