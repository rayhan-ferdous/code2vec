    public void stop() {

        try {

            if (process != null) process.destroy();

        } catch (Exception e) {

        }

        this.finished = true;

    }
