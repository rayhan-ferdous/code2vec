        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("\"some \\stuff\"", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
