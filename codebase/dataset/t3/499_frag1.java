    public synchronized int getClock() {

        try {

            ResponseAPDU response = sendCommandAPDU(ANIMAL_CLA, ANIMAL_CLOCK);

            if (response.sw() == SW_NO_ERROR) {

                if (response.data() != null) return getInt(response.data()); else return 0;

            } else System.err.println("Cannot read clock message from the animal");

        } catch (CardTerminalException e) {

            System.err.println("Unable to communicate with the animal (card)");

        }

        return 0;

    }
