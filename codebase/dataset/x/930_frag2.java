    public int getMinWorthOverRange(int start, int end) {

        if (isIntervalEmpty(start, end)) {

            return 0;

        }

        int index = indexOfValue(start);

        int minVal = Integer.MAX_VALUE;

        if (index < 0) {

            minVal = 0;

            index = firstIntervalIdx;

            while (endVals[index] <= start) {

                index = getNextIntervalIndex(index);

            }

        }

        while (startVals[index] <= end) {

            if (worthOfInterval.get(index) < minVal) {

                minVal = worthOfInterval.get(index);

            }

            int nextIndex = getNextIntervalIndex(index);

            if (nextIndex < 0) break;

            if ((endVals[index] + 1 < startVals[nextIndex]) && (endVals[index] < end)) {

                if (0 < minVal) {

                    minVal = 0;

                }

            }

            index = nextIndex;

        }

        return minVal;

    }
