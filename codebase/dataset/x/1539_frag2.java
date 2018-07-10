    public static Collection<IndexCommit> listCommits(Directory dir) throws IOException {

        final String[] files = dir.listAll();

        Collection<IndexCommit> commits = new ArrayList<IndexCommit>();

        SegmentInfos latest = new SegmentInfos();

        latest.read(dir);

        final long currentGen = latest.getGeneration();

        commits.add(new ReaderCommit(latest, dir));

        for (int i = 0; i < files.length; i++) {

            final String fileName = files[i];

            if (fileName.startsWith(IndexFileNames.SEGMENTS) && !fileName.equals(IndexFileNames.SEGMENTS_GEN) && SegmentInfos.generationFromSegmentsFileName(fileName) < currentGen) {

                SegmentInfos sis = new SegmentInfos();

                try {

                    sis.read(dir, fileName);

                } catch (FileNotFoundException fnfe) {

                    sis = null;

                }

                if (sis != null) commits.add(new ReaderCommit(sis, dir));

            }

        }

        return commits;

    }
