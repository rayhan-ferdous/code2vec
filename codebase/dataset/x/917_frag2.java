        String rm = replace(cfg.getProperty("readme"), "<none>", null);

        if (rm != null) {

            this.readMeFile = new File(rm);

            this.readMeCharset = replace(cfg.getProperty("readme-charset"), "<none>", null);

        }

        this.id = replace(cfg.getProperty("fs-id", ""), "<none>", "");

        this.uid = replace(cfg.getProperty("fs-uid", ""), "<auto>", "");

        this.maxlen = new Integer(cfg.getProperty("maxlen", "79"));

        this.vallen = new Integer(cfg.getProperty("maxlen", "64"));

        this.skipGroupLen = !"<yes>".equals(cfg.getProperty("grouplen"));

        this.undefSeqLen = !"<yes>".equals(cfg.getProperty("seqlen"));

        this.undefItemLen = !"<yes>".equals(cfg.getProperty("itemlen"));

        this.onlyInUse = "<yes>".equals(cfg.getProperty("onlyInUse"));

        this.ignoreCase = "<yes>".equals(cfg.getProperty("ignoreCase"));

        for (Enumeration it = cfg.keys(); it.hasMoreElements(); ) {

            String key = (String) it.nextElement();

            if (key.startsWith("key.")) {

                try {

                    keys.putXX(Tags.forName(key.substring(4)), cfg.getProperty(key));

                } catch (Exception e) {

                    throw new IllegalArgumentException("Illegal key - " + key + "=" + cfg.getProperty(key));

                }

            }

        }

        String qrl = keys.getString(Tags.QueryRetrieveLevel, QRLEVEL[1]);

        this.qrLevel = Arrays.asList(QRLEVEL).indexOf(qrl);

        if (qrLevel == -1) {

            throw new IllegalArgumentException("Illegal Query Retrieve Level - " + qrl);

        }

        keys.remove(Tags.QueryRetrieveLevel);

    }



    private TransformerHandler getTransformerHandler(SAXTransformerFactory tf, Templates tpl, String dsprompt) throws TransformerConfigurationException, IOException {

        TransformerHandler th = tf.newTransformerHandler(tpl);

        th.setResult(new StreamResult(System.out));

        Transformer t = th.getTransformer();

        t.setParameter("maxlen", maxlen);

        t.setParameter("vallen", vallen);

        t.setParameter("vallen", vallen);

        t.setParameter("dsprompt", dsprompt);

        return th;

    }



    /**

     *  Description of the Method

     *

     * @exception  IOException                        Description of the Exception

     * @exception  TransformerConfigurationException  Description of the Exception

     */

    public void list() throws IOException, TransformerConfigurationException {

        SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();

        Templates xslt = loadDcmDirXSL(tf);

        DirReader reader = fact.newDirReader(dirFile);

        reader.getFileSetInfo().writeFile2(getTransformerHandler(tf, xslt, FILE_SET_INFO), dict, null, 128, null);

        try {

            list("", reader.getFirstRecord(onlyInUse), tf, xslt);

        } finally {

            reader.close();

        }

    }



    private static Templates loadDcmDirXSL(SAXTransformerFactory tf) throws IOException, TransformerConfigurationException {

        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        InputStream in = cl.getResourceAsStream("Dcm2Xml2.xsl");

        return tf.newTemplates(new StreamSource(in));

    }



    private static final DecimalFormat POS_FORMAT = new DecimalFormat("0000 DIRECTORY RECORD - ");



    private void list(String prefix, DirRecord first, SAXTransformerFactory tf, Templates xslt) throws IOException, TransformerConfigurationException {

        int count = 1;

        for (DirRecord rec = first; rec != null; rec = rec.getNextSibling(onlyInUse)) {

            Dataset ds = rec.getDataset();

            String prompt = POS_FORMAT.format(ds.getItemOffset()) + prefix + count + " [" + rec.getType() + "]";

            ds.writeDataset2(getTransformerHandler(tf, xslt, prompt), dict, null, 128, null);

            list(prefix + count + '.', rec.getFirstChild(onlyInUse), tf, xslt);

            ++count;

        }

    }



    /**

     *  Description of the Method

     *

     * @exception  IOException                        Description of the Exception

     * @exception  TransformerConfigurationException  Description of the Exception

     */

    public void query() throws IOException, TransformerConfigurationException {

        SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();

        Templates xslt = loadDcmDirXSL(tf);

        DirReader reader = fact.newDirReader(dirFile);

        try {

            query("", 1, 0, reader.getFirstRecordBy(null, keys, ignoreCase), tf, xslt);

        } finally {

            reader.close();

        }

    }



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
