        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("\"double\\\\\\\\double backslash\"", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
