        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals(" 1", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
