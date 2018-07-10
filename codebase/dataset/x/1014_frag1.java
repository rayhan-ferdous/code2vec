    public void disconnectStore() {

        Store tmpStore = getStore();

        setStore(null);

        try {

            tmpStore.close();

        } catch (MessagingException ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(null, "Error while disconnecting");

        }

    }
