                                                                                    e = 99;

                                                                                    break;

                                                                                }

                                                                            }

                                                                        }

                                                                    }

                                                                }

                                                            }

                                                        }

                                                    }

                                                    log.debug("{}", ToStringBuilder.reflectionToString(avc1));

                                                }

                                                MP4Atom stsc = stbl.lookup(MP4Atom.typeToInt("stsc"), 0);

                                                if (stsc != null) {

                                                    log.debug("Sample to chunk atom found");

                                                    videoSamplesToChunks = stsc.getRecords();

                                                    log.debug("Record count: {}", videoSamplesToChunks.size());

                                                    MP4Atom.Record rec = videoSamplesToChunks.firstElement();

                                                    log.debug("Record data: Description index={} Samples per chunk={}", rec.getSampleDescriptionIndex(), rec.getSamplesPerChunk());

                                                }

                                                MP4Atom stsz = stbl.lookup(MP4Atom.typeToInt("stsz"), 0);

                                                if (stsz != null) {

                                                    log.debug("Sample size atom found");

                                                    videoSamples = stsz.getSamples();

                                                    log.debug("Sample size: {}", stsz.getSampleSize());
