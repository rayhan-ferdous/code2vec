    public void run() {

        try {

            convert();

        } catch (Exception exc) {

            if (Constants.debug) {

                System.out.println("Conversion failed! " + exc.getMessage());

                exc.printStackTrace();

            }

        }

    }
