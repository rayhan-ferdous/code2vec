    private Record getRecordFromRawMarc(String marcRecordStr) {

        MarcStreamReader reader;

        int tries = 0;

        boolean tryAgain = false;

        do {

            try {

                tries++;

                tryAgain = false;

                reader = new MarcStreamReader(new ByteArrayInputStream(marcRecordStr.getBytes("UTF8")));

                if (reader.hasNext()) {

                    Record record = reader.next();

                    if (verbose) {

                        System.out.println(record.toString());

                    }

                    return (record);

                }

            } catch (MarcException me) {

                if (tries == 1) {

                    tryAgain = true;

                    marcRecordStr = normalizeUnicode(marcRecordStr);

                } else {

                    me.printStackTrace();

                }

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();

            }

        } while (tryAgain);

        return (null);

    }
