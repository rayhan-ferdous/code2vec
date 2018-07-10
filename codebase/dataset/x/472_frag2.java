        private int idx;



        private int count;



        private final int type;



        /**

     * Construct a new HashIterator with the supplied type.

     * @param type {@link #KEYS}, {@link #VALUES}, or {@link #ENTRIES}

     */

        HashIterator(int type) {

            this.type = type;

            count = elements;

            idx = 0;

        }



        /**

     * Returns true if the Iterator has more elements.

     * @return true if there are more elements

     * @throws ConcurrentModificationException if the HashMap was modified

     */

        public boolean hasNext() {
