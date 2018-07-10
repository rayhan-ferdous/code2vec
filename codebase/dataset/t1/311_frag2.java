        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(2, reader.getColumnCount());

        Assert.assertEquals(generateString('a', 75) + "," + generateString('b', 75), reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
