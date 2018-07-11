    private int query(String prefix, int no, int level, DirRecord dr, SAXTransformerFactory tf, Templates xslt) throws IOException, TransformerConfigurationException {

        int count = 1;

        for (; dr != null; dr = dr.getNextSiblingBy(null, keys, ignoreCase)) {

            if (level >= qrLevel) {

                Dataset ds = dr.getDataset();

                String prompt = POS_FORMAT.format(ds.getItemOffset()) + prefix + count + " [" + dr.getType() + "] #" + no;

                ds.writeDataset(getTransformerHandler(tf, xslt, prompt), dict);

                ++no;

            } else {

                no = query(prefix + count + '.', no, level + 1, dr.getFirstChildBy(null, keys, ignoreCase), tf, xslt);

            }

            ++count;

        }

        return no;

    }
