        protected SelectableChannel channel;



        protected boolean cleaned;



        protected boolean processed;



        public CanceledEntry(SelectableChannel channel) {

            this.channel = channel;

            processed = false;

            cleaned = false;

        }



        public SelectableChannel getChannel() {
