                ds.writeDataset(getTransformerHandler(tf, xslt, prompt), dict);

                ++no;

            } else {

                no = query(prefix + count + '.', no, level + 1, dr.getFirstChildBy(null, keys, ignoreCase), tf, xslt);

            }

            ++count;

        }

        return no;

    }



    private DcmEncodeParam encodeParam() {

        return new DcmEncodeParam(ByteOrder.LITTLE_ENDIAN, true, false, false, skipGroupLen, undefSeqLen, undefItemLen);

    }



    /**

     *  Description of the Method

     *

     * @param  args             Description of the Parameter

     * @param  off              Description of the Parameter

     * @exception  IOException  Description of the Exception

     */

    public void create(String[] args, int off) throws IOException {

        if (uid == null || uid.length() == 0) {

            uid = UIDGenerator.getInstance().createUID();

        }

        File rootDir = dirFile.getParentFile();

        if (rootDir != null && !rootDir.exists()) {

            rootDir.mkdirs();

        }

        DirWriter writer = fact.newDirWriter(dirFile, uid, id, readMeFile, readMeCharset, encodeParam());

        try {

            build(writer, args, off);

        } finally {

            writer.close();

        }

    }



    /**

     *  Description of the Method

     *

     * @param  args             Description of the Parameter

     * @param  off              Description of the Parameter

     * @exception  IOException  Description of the Exception

     */

    public void append(String[] args, int off) throws IOException {

        DirWriter writer = fact.newDirWriter(dirFile, encodeParam());

        try {

            build(writer, args, off);

        } finally {

            writer.close();

        }

    }



    private void addDirBuilderPrefElem(HashMap map, String key) {

        if (!key.startsWith("dir.")) {

            return;

        }

        int pos2 = key.lastIndexOf('.');

        String type = key.substring(4, pos2).replace('_', ' ');

        Dataset ds = (Dataset) map.get(type);

        if (ds == null) {

            map.put(type, ds = dof.newDataset());

        }

        int tag = Tags.forName(key.substring(pos2 + 1));

        ds.putXX(tag, VRMap.DEFAULT.lookup(tag));

    }



    private DirBuilderPref getDirBuilderPref() {

        HashMap map = new HashMap();

        for (Enumeration en = cfg.keys(); en.hasMoreElements(); ) {

            addDirBuilderPrefElem(map, (String) en.nextElement());

        }

        DirBuilderPref pref = fact.newDirBuilderPref();

        for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {

            Map.Entry entry = (Map.Entry) it.next();

            pref.setFilterForRecordType((String) entry.getKey(), (Dataset) entry.getValue());

        }

        return pref;

    }



    private void build(DirWriter w, String[] args, int off) throws IOException {

        DirBuilderPref pref = getDirBuilderPref();

        long t1 = System.currentTimeMillis();

        int[] counter = new int[2];

        DirBuilder builder = fact.newDirBuilder(w, pref);

        for (int i = off; i < args.length; ++i) {

            append(builder, new File(args[i]), counter);

        }

        long t2 = System.currentTimeMillis();

        System.out.println(MessageFormat.format(messages.getString("insertDone"), new Object[] { String.valueOf(counter[1]), String.valueOf(counter[0]), String.valueOf((t2 - t1) / 1000f) }));

    }



    /**

     *  Description of the Method

     *

     * @param  builder          Description of the Parameter

     * @param  file             Description of the Parameter

     * @param  counter          Description of the Parameter

     * @exception  IOException  Description of the Exception

     */

    public void append(DirBuilder builder, File file, int[] counter) throws IOException {
