        Assert.assertEquals(3L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("2", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
