    public void read(DataInputStream dis) {

        try {

            header = dis.readUTF();

            int numberRegionScores = sliceInfo.getNumberRecords();

            sortedRegionScores = new RegionScore[numberRegionScores];

            String fileType = sliceInfo.getBinaryType();

            if (USeqUtilities.REGION_SCORE_INT_INT_FLOAT.matcher(fileType).matches()) {

                int start = dis.readInt();

                sortedRegionScores[0] = new RegionScore(start, start + dis.readInt(), dis.readFloat());

                for (int i = 1; i < numberRegionScores; i++) {

                    start = sortedRegionScores[i - 1].start + dis.readInt();

                    sortedRegionScores[i] = new RegionScore(start, start + dis.readInt(), dis.readFloat());

                }

            } else if (USeqUtilities.REGION_SCORE_INT_SHORT_FLOAT.matcher(fileType).matches()) {

                int start = dis.readInt();

                sortedRegionScores[0] = new RegionScore(start, start + dis.readShort() + 32768, dis.readFloat());

                for (int i = 1; i < numberRegionScores; i++) {

                    start = sortedRegionScores[i - 1].start + dis.readInt();

                    sortedRegionScores[i] = new RegionScore(start, start + dis.readShort() + 32768, dis.readFloat());

                }

            } else if (USeqUtilities.REGION_SCORE_SHORT_SHORT_FLOAT.matcher(fileType).matches()) {

                int start = dis.readInt();

                sortedRegionScores[0] = new RegionScore(start, start + dis.readShort() + 32768, dis.readFloat());

                for (int i = 1; i < numberRegionScores; i++) {

                    start = sortedRegionScores[i - 1].start + dis.readShort() + 32768;

                    sortedRegionScores[i] = new RegionScore(start, start + dis.readShort() + 32768, dis.readFloat());

                }

            } else if (USeqUtilities.REGION_SCORE_SHORT_INT_FLOAT.matcher(fileType).matches()) {

                int start = dis.readInt();

                sortedRegionScores[0] = new RegionScore(start, start + dis.readInt(), dis.readFloat());

                for (int i = 1; i < numberRegionScores; i++) {

                    start = sortedRegionScores[i - 1].start + dis.readShort() + 32768;

                    sortedRegionScores[i] = new RegionScore(start, start + dis.readInt(), dis.readFloat());

                }

            } else {

                throw new IOException("Incorrect file type for creating a RegionScore[] -> '" + fileType + "' in " + binaryFile + "\n");

            }

        } catch (IOException e) {

            e.printStackTrace();

            USeqUtilities.safeClose(dis);

        }

    }
