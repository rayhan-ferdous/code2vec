        Assert.assertEquals("", reader.get(1));

        Assert.assertEquals(',', reader.getDelimiter());

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(2, reader.getColumnCount());

        Assert.assertEquals(",", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        Assert.assertEquals("", reader.getRawRecord());

        reader.close();

    }
