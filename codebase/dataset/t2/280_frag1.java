            workingDOS.writeUTF(header);

            workingDOS.writeInt(sortedPositionScoreTexts[0].position);

            workingDOS.writeFloat(sortedPositionScoreTexts[0].score);

            workingDOS.writeUTF(sortedPositionScoreTexts[0].text);

            if (useShort) {

                int bp = sortedPositionScoreTexts[0].position;

                for (int i = 1; i < sortedPositionScoreTexts.length; i++) {

                    int currentStart = sortedPositionScoreTexts[i].position;

                    int diff = currentStart - bp - 32768;

                    workingDOS.writeShort((short) (diff));

                    workingDOS.writeFloat(sortedPositionScoreTexts[i].score);

                    workingDOS.writeUTF(sortedPositionScoreTexts[i].text);

                    bp = currentStart;

                }

            } else {

                int bp = sortedPositionScoreTexts[0].position;

                for (int i = 1; i < sortedPositionScoreTexts.length; i++) {

                    int currentStart = sortedPositionScoreTexts[i].position;

                    int diff = currentStart - bp;

                    workingDOS.writeInt(diff);

                    workingDOS.writeFloat(sortedPositionScoreTexts[i].score);

                    workingDOS.writeUTF(sortedPositionScoreTexts[i].text);

                    bp = currentStart;

                }

            }

        } catch (Exception e) {
