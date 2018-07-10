        for (int i = 0; i < maxID; i++) {

            bestWeekList[i] = users[i];

        }

        for (int i = maxID; --i >= 0; ) {

            for (int j = 0; j < i; j++) {

                if (bestWeekList[j].getProfitForWeek() > bestWeekList[j + 1].getProfitForWeek()) {

                    User u = bestWeekList[j];

                    bestWeekList[j] = bestWeekList[j + 1];

                    bestWeekList[j + 1] = u;

                }

            }

        }

    }
