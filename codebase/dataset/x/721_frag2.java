        public void init() {

            myAgent.restoreBufferedState();

            if (myMovable != null) {

                myMovable.afterMove();

            }

        }
