            dos.writeUTF(header);

            dos.writeInt(sortedPositionScoreTexts[0].position);

            dos.writeFloat(sortedPositionScoreTexts[0].score);

            dos.writeUTF(sortedPositionScoreTexts[0].text);

            if (useShort) {

                int bp = sortedPositionScoreTexts[0].position;

                for (int i = 1; i < sortedPositionScoreTexts.length; i++) {

                    int currentStart = sortedPositionScoreTexts[i].position;

                    int diff = currentStart - bp - 32768;

                    dos.writeShort((short) (diff));

                    dos.writeFloat(sortedPositionScoreTexts[i].score);

                    dos.writeUTF(sortedPositionScoreTexts[i].text);

                    bp = currentStart;

                }

            } else {

                int bp = sortedPositionScoreTexts[0].position;

                for (int i = 1; i < sortedPositionScoreTexts.length; i++) {

                    int currentStart = sortedPositionScoreTexts[i].position;

                    int diff = currentStart - bp;

                    dos.writeInt(diff);

                    dos.writeFloat(sortedPositionScoreTexts[i].score);

                    dos.writeUTF(sortedPositionScoreTexts[i].text);

                    bp = currentStart;

                }

            }

            out.closeEntry();
