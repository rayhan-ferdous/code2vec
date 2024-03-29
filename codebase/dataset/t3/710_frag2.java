    public void test46() throws Exception {

        String data = "Ch\\icane, Love on the Run, Kn\\ight R\\ider, Th\\is f\\ield conta\\ins an \\i\\, but \\it doesn't matter as \\it \\is escapedi" + "Samuel Barber, Adag\\io for Str\\ings, Class\\ical, Th\\is f\\ield conta\\ins a comma \\, but \\it doesn't matter as \\it \\is escaped";

        CsvReader reader = CsvReader.parse(data);

        reader.setUseTextQualifier(false);

        reader.setEscapeMode(CsvReader.ESCAPE_MODE_BACKSLASH);

        reader.setRecordDelimiter('i');

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("Chicane", reader.get(0));

        Assert.assertEquals("Love on the Run", reader.get(1));

        Assert.assertEquals("Knight Rider", reader.get(2));

        Assert.assertEquals("This field contains an i, but it doesn't matter as it is escaped", reader.get(3));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(4, reader.getColumnCount());

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("Samuel Barber", reader.get(0));

        Assert.assertEquals("Adagio for Strings", reader.get(1));

        Assert.assertEquals("Classical", reader.get(2));

        Assert.assertEquals("This field contains a comma , but it doesn't matter as it is escaped", reader.get(3));

        Assert.assertEquals(1L, reader.getCurrentRecord());

        Assert.assertEquals(4, reader.getColumnCount());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
