            for (int n = 0; n < gamesPerRound; n++) {

                event.addMatch(schedule[i][n]);

            }

            GameDayArrivedEvent tmp = new GameDayArrivedEvent(this, day);

            tmp.addSchedulerGameDayEvent(event);

            GameController.getInstance().getScenario().getScheduler().addEvent(tmp);
