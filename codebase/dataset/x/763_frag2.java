    public void initAL() {

        try {

            AL.create();

        } catch (LWJGLException e) {

            e.printStackTrace();

        }

        AL10.alDopplerFactor(0.02f);

    }
