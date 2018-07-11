        void buildTree() {

            int numSymbols = freqs.length;

            int[] heap = new int[numSymbols];

            int heapLen = 0;

            int maxCode = 0;

            for (int n = 0; n < numSymbols; n++) {

                int freq = freqs[n];

                if (freq != 0) {

                    int pos = heapLen++;

                    int ppos;

                    while (pos > 0 && freqs[heap[ppos = (pos - 1) / 2]] > freq) {

                        heap[pos] = heap[ppos];

                        pos = ppos;

                    }

                    heap[pos] = n;

                    maxCode = n;

                }

            }

            while (heapLen < 2) {

                int node = maxCode < 2 ? ++maxCode : 0;

                heap[heapLen++] = node;

            }

            numCodes = Math.max(maxCode + 1, minNumCodes);

            int numLeafs = heapLen;

            int[] childs = new int[4 * heapLen - 2];

            int[] values = new int[2 * heapLen - 1];

            int numNodes = numLeafs;

            for (int i = 0; i < heapLen; i++) {

                int node = heap[i];

                childs[2 * i] = node;

                childs[2 * i + 1] = -1;

                values[i] = freqs[node] << 8;

                heap[i] = i;

            }

            do {

                int first = heap[0];

                int last = heap[--heapLen];

                int ppos = 0;

                int path = 1;

                while (path < heapLen) {

                    if (path + 1 < heapLen && values[heap[path]] > values[heap[path + 1]]) path++;

                    heap[ppos] = heap[path];

                    ppos = path;

                    path = path * 2 + 1;

                }

                int lastVal = values[last];

                while ((path = ppos) > 0 && values[heap[ppos = (path - 1) / 2]] > lastVal) heap[path] = heap[ppos];

                heap[path] = last;

                int second = heap[0];

                last = numNodes++;

                childs[2 * last] = first;

                childs[2 * last + 1] = second;

                int mindepth = Math.min(values[first] & 0xff, values[second] & 0xff);

                values[last] = lastVal = values[first] + values[second] - mindepth + 1;

                ppos = 0;

                path = 1;

                while (path < heapLen) {

                    if (path + 1 < heapLen && values[heap[path]] > values[heap[path + 1]]) path++;

                    heap[ppos] = heap[path];

                    ppos = path;

                    path = ppos * 2 + 1;

                }

                while ((path = ppos) > 0 && values[heap[ppos = (path - 1) / 2]] > lastVal) heap[path] = heap[ppos];

                heap[path] = last;

            } while (heapLen > 1);

            if (heap[0] != childs.length / 2 - 1) throw new RuntimeException("Weird!");

            buildLength(childs);

        }
