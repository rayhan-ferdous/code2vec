    private byte[] decipherCode(byte[] cipheredCode) {

        DataStore privateDS = new DataStore();

        try {

            privateDS.put(RequestDecipher.DS_CIPHERED_CODE, cipheredCode);

            new RequestDecipher(privateDS, this).init();

        } catch (TimeOutException e) {

            if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "SelfProtectedAgent: " + getName() + ". Error requesting the code deciphering: " + e);

        }

        return (byte[]) privateDS.remove(RequestDecipher.DS_DECIPHERED_CODE);

    }



    class RequestDecipher extends RequestInitiator {



        public RequestDecipher(DataStore ds, Agent agent) {

            super(agent);

            _ds = ds;

            _agent = agent;

        }



        @Override

        protected void handleFailureRequest(ACLMessage failure) {

            if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "SelfProtectedAgent: RequestDecipher: " + getName() + ". Error deciphering the agent code: " + failure.getContent());

        }



        @Override

        protected void handleInformRequest(ACLMessage inform) {

            Predicate predicate = null;

            byte[] data;

            try {

                predicate = (Predicate) _agent.getContentManager().extractContent(inform);

            } catch (Exception e) {

                if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "SelfProtectedAgent: RequestDecipher: " + getName() + ": " + ERR_EXTRACT_CONTENT + e);

            }

            if (predicate != null) {

                if (predicate instanceof InformDecipheredDataPredicate) {

                    data = ((InformDecipheredDataPredicate) predicate).getDecipheredData();

                    if (data != null) {

                        _ds.put(DS_DECIPHERED_CODE, data);

                    } else {

                        if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "SelfProtectedAgent: RequestDecipher: " + getName() + ": " + ERR_NULL_CONTENT);

                    }

                } else {

                    if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "SelfProtectedAgent: RequestDecipher: " + getName() + ": " + ERR_INCORRECT_ACTION);

                }

            } else {

                if (_logger.isLoggable(Logger.SEVERE)) _logger.log(Logger.SEVERE, "SelfProtectedAgent: RequestDecipher: " + getName() + ": " + ERR_NULL_ACTION);

            }

        }



        /**

		 * Method to request the code deciphering.

		 */

        protected ACLMessage prepareInitiation(ACLMessage request) {
