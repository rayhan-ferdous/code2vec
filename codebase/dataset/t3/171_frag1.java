    private int findDoc(int docId) {

        int low = 0;

        int high = documentCount - 1;

        while (low <= high) {

            int mid = (low + high) >>> 1;

            int midVal = documentIds[mid];

            if (midVal < docId) low = mid + 1; else if (midVal > docId) high = mid - 1; else return mid;

        }

        return -(low + 1);

    }
