        switch(eventHandler.getInterestOps()) {

            case SelectionKey.OP_ACCEPT:

                createListenerThread(eventHandler);

                break;

            case SelectionKey.OP_READ:

                createReaderThread(eventHandler);

                break;

            default:

                if (orb.transportDebugFlag) {

                    dprint(".registerForEvent: default: " + eventHandler);

                }

                throw new RuntimeException("SelectorImpl.registerForEvent: unknown interest ops");
