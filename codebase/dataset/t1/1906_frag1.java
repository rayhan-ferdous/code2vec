        Assert.assertEquals("\"2\n\",Toni", reader.getRawRecord());

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("3", reader.get(0));

        Assert.assertEquals("Brian", reader.get(1));

        Assert.assertEquals(1L, reader.getCurrentRecord());

        Assert.assertEquals(2, reader.getColumnCount());

        Assert.assertEquals("\"3\",Brian", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
