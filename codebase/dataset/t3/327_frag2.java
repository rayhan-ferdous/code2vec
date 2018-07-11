            public int size() {

                if (size == -1 || sizeModCount != PrefixSearchTreeMap.this.modCount) {

                    size = 0;

                    sizeModCount = PrefixSearchTreeMap.this.modCount;

                    Iterator i = iterator();

                    while (i.hasNext()) {

                        size++;

                        i.next();

                    }

                }

                return size;

            }
