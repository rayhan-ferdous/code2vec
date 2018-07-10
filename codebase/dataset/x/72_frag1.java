    public synchronized void contactUpdated(Person original, Person updated) {

        if (contactUpdated) return;

        phoneBookRequest.operation = ClientDataRequest.Operation.UPDATE;

        phoneBookRequest.original = original;

        phoneBookRequest.updated = updated;

        try {

            SealedObject sealedPhoneBookRequest = new SealedObject(phoneBookRequest, outCipher);

            objectOut.writeObject(sealedPhoneBookRequest);

            objectOut.flush();

            objectOut.reset();

        } catch (IOException e) {

            Debug.error("Error writing updated contact to server");

            Debug.error(e.toString());

            e.printStackTrace();

        } catch (IllegalBlockSizeException e) {

            Debug.error("Illegal block size exception!");

            Debug.error(e.toString());

            e.printStackTrace();

        }

        phoneBookRequest.original = null;

        phoneBookRequest.updated = null;

    }
