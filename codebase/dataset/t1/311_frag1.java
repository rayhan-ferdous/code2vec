        Assert.assertEquals(1L, reader.getCurrentRecord());

        Assert.assertEquals(2, reader.getColumnCount());

        Assert.assertEquals("1\r", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
