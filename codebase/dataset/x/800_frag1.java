        public TestClient(String monitorURL, String jobId, int maxMessageNumber, SubscribeTest cbInterface) {

            this.cbInterface = cbInterface;

            this.monitorURL = monitorURL;

            this.jobId = jobId;

            this.maxMessageNumber = maxMessageNumber;

            this.subscribe();

        }
