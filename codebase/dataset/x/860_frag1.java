                                    System.arraycopy(tempbcCache, insertPos + 1, tempbcCache, insertPos + 2, n - insertPos - 1);

                                    tempbcCache[insertPos + 1] = entry;

                                }

                                n++;

                            }

                            tempMap.remove(key);

                        }

                        bcCount = 0;

                        bcStats.clear();
