    @Override

    public void closeTab() {

        if (roomData.isHost()) {

            Protocol.closeRoom();

        } else {

            Protocol.leaveRoom();

        }

    }
