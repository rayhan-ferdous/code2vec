    public int getActiveToeCount() {

        Thread[] toes = getToes();

        int count = 0;

        for (int i = 0; i < toes.length; i++) {

            if ((toes[i] instanceof ToeThread) && ((ToeThread) toes[i]).isActive()) {

                count++;

            }

        }

        return count;

    }
