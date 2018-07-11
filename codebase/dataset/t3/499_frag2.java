    public synchronized String getAnimalName() {

        if (animal_name_cache != null) return animal_name_cache;

        try {

            ResponseAPDU response = sendCommandAPDU(ANIMAL_CLA, ANIMAL_NAME);

            if (response.sw() == SW_NO_ERROR) {

                if (response.data() != null) {

                    animal_name_cache = new String(response.data());

                    return new String(response.data());

                } else {

                    animal_name_cache = "";

                    return "";

                }

            } else System.err.println("Cannot read animal-name message from the animal");

        } catch (CardTerminalException e) {

            System.err.println("Unable to communicate with the animal (card)");

        }

        return null;

    }
