        Assert.assertEquals(',', reader.getDelimiter());

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(3, reader.getColumnCount());

        Assert.assertEquals("\"bob said, \"\"Hey!\"\"\",2, 3 ", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        Assert.assertEquals("", reader.getRawRecord());

        reader.close();

    }
