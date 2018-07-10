    public void read(DataInputStream dis) {

        try {

            header = dis.readUTF();

            int numberRegionTexts = sliceInfo.getNumberRecords();

            sortedRegionTexts = new RegionText[numberRegionTexts];

            String fileType = sliceInfo.getBinaryType();

            if (USeqUtilities.REGION_TEXT_INT_INT_TEXT.matcher(fileType).matches()) {

                int start = dis.readInt();

                sortedRegionTexts[0] = new RegionText(start, start + dis.readInt(), dis.readUTF());

                for (int i = 1; i < numberRegionTexts; i++) {

                    start = sortedRegionTexts[i - 1].start + dis.readInt();

                    sortedRegionTexts[i] = new RegionText(start, start + dis.readInt(), dis.readUTF());

                }

            } else if (USeqUtilities.REGION_TEXT_INT_SHORT_TEXT.matcher(fileType).matches()) {

                int start = dis.readInt();

                sortedRegionTexts[0] = new RegionText(start, start + dis.readShort() + 32768, dis.readUTF());

                for (int i = 1; i < numberRegionTexts; i++) {

                    start = sortedRegionTexts[i - 1].start + dis.readInt();

                    sortedRegionTexts[i] = new RegionText(start, start + dis.readShort() + 32768, dis.readUTF());

                }

            } else if (USeqUtilities.REGION_TEXT_SHORT_SHORT_TEXT.matcher(fileType).matches()) {

                int start = dis.readInt();

                sortedRegionTexts[0] = new RegionText(start, start + dis.readShort() + 32768, dis.readUTF());

                for (int i = 1; i < numberRegionTexts; i++) {

                    start = sortedRegionTexts[i - 1].start + dis.readShort() + 32768;

                    sortedRegionTexts[i] = new RegionText(start, start + dis.readShort() + 32768, dis.readUTF());

                }

            } else if (USeqUtilities.REGION_TEXT_SHORT_INT_TEXT.matcher(fileType).matches()) {

                int start = dis.readInt();

                sortedRegionTexts[0] = new RegionText(start, start + dis.readInt(), dis.readUTF());

                for (int i = 1; i < numberRegionTexts; i++) {

                    start = sortedRegionTexts[i - 1].start + dis.readShort() + 32768;

                    sortedRegionTexts[i] = new RegionText(start, start + dis.readInt(), dis.readUTF());

                }

            } else {

                throw new IOException("Incorrect file type for creating a RegionText[] -> '" + fileType + "' in " + binaryFile + "\n");

            }

        } catch (IOException e) {

            e.printStackTrace();

            USeqUtilities.safeClose(dis);

        }

    }
