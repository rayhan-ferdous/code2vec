                                public void run() {

                                    ArrayList trackStates = getStateSocrates();

                                    ArrayList stateList = (ArrayList) trackStates.get(channelMemory);

                                    int itemToMoveDestination = 0;

                                    try {

                                        itemToMoveDestination = Integer.parseInt(getId());

                                    } catch (Throwable t) {

                                        setId(getToolTipText());

                                        return;

                                    }

                                    if (itemToMoveDestination < 0 || itemToMoveDestination >= stateList.size()) {

                                        setId(getToolTipText());

                                        return;

                                    }

                                    setToolTipText(getId());

                                    SourceManagerItem.StateItem stateItemToMove = (SourceManagerItem.StateItem) stateList.remove(itemToMoveSource);

                                    stateList.add(itemToMoveDestination, stateItemToMove);

                                    setStateSocrates(trackStates);

                                    setStatePlato(trackStates);

                                    PropertiesAccess.updateView();

                                }
