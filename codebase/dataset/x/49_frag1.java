                        Globals.setSleepModeStatus(false);

                    }

                    break;

                case CHAT_ROOM:

                    if (information.length < 2) {

                        return;

                    }

                    if (TabOrganizer.getRoomPanel() != null) {

                        TabOrganizer.getRoomPanel().chat(information[0], information[1], ChatStyles.USER);
