    public void closeWriters() {

        try {

            Iterator<DataOutputStream> it = chromOut.values().iterator();

            while (it.hasNext()) it.next().close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
