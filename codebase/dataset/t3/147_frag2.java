    @Test

    public void testParseNoFileExist() {

        File file = new File("xyz.vdr");

        Recording recording = null;

        try {

            recording = recordingParser.parse(file);

            fail();

            assertNull(recording);

        } catch (VDRParserException e) {

            e.printStackTrace();

        }

    }
