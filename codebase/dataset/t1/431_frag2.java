        Assert.assertEquals("This field contains a comma, but it doesn't matter as the delimiter is escaped", reader.get(3));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(4, reader.getColumnCount());

        Assert.assertEquals("Chicane, Love on the Run, Knight Rider, This field contains a comma\\, but it doesn't matter as the delimiter is escaped", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
