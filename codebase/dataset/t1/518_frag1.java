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

        if (file.isDirectory()) {

            File[] files = file.listFiles();

            for (int i = 0; i < files.length; ++i) {

                append(builder, files[i], counter);

            }

        } else {

            try {

                counter[1] += builder.addFileRef(file);

                ++counter[0];

                System.out.print('.');

            } catch (DcmParseException e) {

                System.out.println(MessageFormat.format(messages.getString("insertFailed"), new Object[] { file }));

                e.printStackTrace(System.out);

            } catch (IllegalArgumentException e) {

                System.out.println(MessageFormat.format(messages.getString("insertFailed"), new Object[] { file }));

                e.printStackTrace(System.out);

            }

        }

    }



    /**

     *  Description of the Method

     *

     * @exception  IOException  Description of the Exception

     */

    public void compact() throws IOException {

        DirWriter writer = fact.newDirWriter(dirFile, encodeParam());

        long t1 = System.currentTimeMillis();

        long len1 = dirFile.length();

        try {

            writer = writer.compact();

        } finally {

            writer.close();

        }

        long t2 = System.currentTimeMillis();

        long len2 = dirFile.length();

        System.out.println(MessageFormat.format(messages.getString("compactDone"), new Object[] { dirFile, String.valueOf(len1), String.valueOf(len2), String.valueOf((t2 - t1) / 1000f) }));

    }



    /**

     *  Description of the Method

     *

     * @exception  IOException  Description of the Exception

     */

    public void purge() throws IOException {

        DirWriter writer = fact.newDirWriter(dirFile, encodeParam());

        long t1 = System.currentTimeMillis();

        long len1 = dirFile.length();

        int count = 0;

        try {

            count = doPurge(writer);

        } finally {

            writer.close();

        }

        long t2 = System.currentTimeMillis();

        long len2 = dirFile.length();

        System.out.println(MessageFormat.format(messages.getString("purgeDone"), new Object[] { String.valueOf(count), String.valueOf((t2 - t1) / 1000f) }));

    }



    private void addFileIDs(DirWriter w, File file) throws IOException {

        if (file.isDirectory()) {

            File[] files = file.listFiles();

            for (int i = 0; i < files.length; ++i) {

                addFileIDs(w, files[i]);

            }

        } else {

            fileIDs.add(w.toFileIDs(file));

        }

    }



    /**

     *  Description of the Method

     *

     * @param  args             Description of the Parameter

     * @param  off              Description of the Parameter

     * @param  delFiles         Description of the Parameter

     * @exception  IOException  Description of the Exception

     */

    public void remove(String[] args, int off, boolean delFiles) throws IOException {

        long t1 = System.currentTimeMillis();

        int[] counter = new int[2];

        DirWriter w = fact.newDirWriter(dirFile, encodeParam());

        try {

            for (int i = off; i < args.length; ++i) {

                addFileIDs(w, new File(args[i]));

            }

            doRemove(w, counter, delFiles);

        } finally {

            w.close();

        }

        long t2 = System.currentTimeMillis();

        System.out.println(MessageFormat.format(messages.getString("removeDone"), new Object[] { String.valueOf(counter[1]), String.valueOf(counter[0]), String.valueOf((t2 - t1) / 1000f) }));

    }



    private void doRemove(DirWriter w, int[] counter, boolean delFiles) throws IOException {

        for (DirRecord rec = w.getFirstRecord(true); rec != null; rec = rec.getNextSibling(true)) {

            if (patientIDs.contains(rec.getDataset().getString(Tags.PatientID))) {

                if (delFiles) {

                    deleteRefFiles(w, rec, counter);

                }

                counter[1] += w.remove(rec);

            } else if (doRemoveStudy(w, rec, counter, delFiles)) {

                counter[1] += w.remove(rec);

            }

        }

    }



    private boolean doRemoveStudy(DirWriter w, DirRecord parent, int[] counter, boolean delFiles) throws IOException {

        boolean matchAll = true;

        LinkedList toRemove = new LinkedList();

        for (DirRecord rec = parent.getFirstChild(true); rec != null; rec = rec.getNextSibling(true)) {

            if (studyUIDs.contains(rec.getDataset().getString(Tags.StudyInstanceUID))) {

                if (delFiles) {

                    deleteRefFiles(w, rec, counter);

                }

                toRemove.add(rec);

            } else if (doRemoveSeries(w, rec, counter, delFiles)) {

                toRemove.add(rec);

            } else {

                matchAll = false;

            }

        }

        if (matchAll) {

            return true;

        }

        for (Iterator it = toRemove.iterator(); it.hasNext(); ) {

            counter[1] += w.remove((DirRecord) it.next());

        }

        return false;

    }



    private boolean doRemoveSeries(DirWriter w, DirRecord parent, int[] counter, boolean delFiles) throws IOException {

        boolean matchAll = true;

        LinkedList toRemove = new LinkedList();

        for (DirRecord rec = parent.getFirstChild(true); rec != null; rec = rec.getNextSibling(true)) {

            if (seriesUIDs.contains(rec.getDataset().getString(Tags.SeriesInstanceUID))) {

                if (delFiles) {

                    deleteRefFiles(w, rec, counter);

                }

                toRemove.add(rec);

            } else if (doRemoveInstances(w, rec, counter, delFiles)) {

                toRemove.add(rec);

            } else {

                matchAll = false;

            }

        }

        if (matchAll) {

            return true;

        }

        for (Iterator it = toRemove.iterator(); it.hasNext(); ) {

            counter[1] += w.remove((DirRecord) it.next());

        }

        return false;

    }



    private boolean doRemoveInstances(DirWriter w, DirRecord parent, int[] counter, boolean delFiles) throws IOException {

        boolean matchAll = true;

        LinkedList toRemove = new LinkedList();

        for (DirRecord rec = parent.getFirstChild(true); rec != null; rec = rec.getNextSibling(true)) {

            if (sopInstUIDs.contains(rec.getRefSOPInstanceUID()) || matchFileIDs(rec.getRefFileIDs())) {

                if (delFiles) {

                    deleteRefFiles(w, rec, counter);

                }

                toRemove.add(rec);

            } else {

                matchAll = false;

            }

        }

        if (matchAll) {

            return true;

        }

        for (Iterator it = toRemove.iterator(); it.hasNext(); ) {

            counter[1] += w.remove((DirRecord) it.next());

        }

        return false;

    }



    private boolean matchFileIDs(String[] ids) {

        if (ids == null || fileIDs.isEmpty()) {

            return false;

        }

        for (Iterator iter = fileIDs.iterator(); iter.hasNext(); ) {

            if (Arrays.equals((String[]) iter.next(), ids)) {

                return true;

            }

        }

        return false;

    }



    private void deleteRefFiles(DirWriter w, DirRecord rec, int[] counter) throws IOException {

        String[] fileIDs = rec.getRefFileIDs();

        if (fileIDs != null) {

            File f = w.getRefFile(fileIDs);

            if (!f.delete()) {

                System.out.println(MessageFormat.format(messages.getString("deleteFailed"), new Object[] { f }));

            } else {

                ++counter[0];

            }

        }

        for (DirRecord child = rec.getFirstChild(true); child != null; child = child.getNextSibling(true)) {

            deleteRefFiles(w, child, counter);

        }

    }



    private int doPurge(DirWriter w) throws IOException {

        int[] counter = { 0 };

        for (DirRecord rec = w.getFirstRecord(true); rec != null; rec = rec.getNextSibling(true)) {

            if (doPurgeStudy(w, rec, counter)) {

                counter[0] += w.remove(rec);

            }

        }

        return counter[0];

    }



    private boolean doPurgeStudy(DirWriter w, DirRecord parent, int[] counter) throws IOException {

        boolean matchAll = true;

        LinkedList toRemove = new LinkedList();

        for (DirRecord rec = parent.getFirstChild(true); rec != null; rec = rec.getNextSibling(true)) {

            if (doPurgeSeries(w, rec, counter)) {

                toRemove.add(rec);

            } else {

                matchAll = false;

            }

        }

        if (matchAll) {

            return true;

        }

        for (Iterator it = toRemove.iterator(); it.hasNext(); ) {

            counter[0] += w.remove((DirRecord) it.next());

        }

        return false;

    }



    private boolean doPurgeSeries(DirWriter w, DirRecord parent, int[] counter) throws IOException {

        boolean matchAll = true;

        LinkedList toRemove = new LinkedList();

        for (DirRecord rec = parent.getFirstChild(true); rec != null; rec = rec.getNextSibling(true)) {

            if (doPurgeInstances(w, rec, counter)) {

                toRemove.add(rec);

            } else {

                matchAll = false;

            }

        }

        if (matchAll) {

            return true;

        }

        for (Iterator it = toRemove.iterator(); it.hasNext(); ) {

            counter[0] += w.remove((DirRecord) it.next());

        }

        return false;

    }



    private boolean doPurgeInstances(DirWriter w, DirRecord parent, int[] counter) throws IOException {

        boolean matchAll = true;

        LinkedList toRemove = new LinkedList();

        for (DirRecord rec = parent.getFirstChild(true); rec != null; rec = rec.getNextSibling(true)) {

            File file = w.getRefFile(rec.getRefFileIDs());

            if (!file.exists()) {

                toRemove.add(rec);

            } else {

                matchAll = false;

            }

        }

        if (matchAll) {

            return true;

        }

        for (Iterator it = toRemove.iterator(); it.hasNext(); ) {

            counter[0] += w.remove((DirRecord) it.next());

        }

        return false;

    }



    private static Properties loadConfig() {

        InputStream in = DcmDir.class.getResourceAsStream("dcmdir.cfg");

        try {

            Properties retval = new Properties();

            retval.load(in);

            return retval;

        } catch (Exception e) {

            throw new RuntimeException("Could not read dcmdir.cfg", e);

        } finally {

            if (in != null) {

                try {

                    in.close();

                } catch (IOException ignore) {

                }

            }

        }

    }



    private static void exit(String prompt, boolean error) {

        if (prompt != null) {

            System.err.println(prompt);

        }

        if (error) {

            System.err.println(messages.getString("try"));

        }

        System.exit(1);

    }



    private static String replace(String val, String from, String to) {

        return from.equals(val) ? to : val;

    }

}
