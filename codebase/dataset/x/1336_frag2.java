        private SortedSet<Header> readHeader(FontInputStream is) throws IOException {

            SortedSet<Header> records = new TreeSet<Header>(Header.COMPARATOR_BY_OFFSET);

            this.sfntVersion = is.readFixed();

            this.numTables = is.readUShort();

            this.searchRange = is.readUShort();

            this.entrySelector = is.readUShort();

            this.rangeShift = is.readUShort();

            for (int tableNumber = 0; tableNumber < this.numTables; tableNumber++) {

                Header table = new Header(is.readULongAsInt(), is.readULong(), is.readULongAsInt(), is.readULongAsInt());

                records.add(table);

            }

            return records;

        }
