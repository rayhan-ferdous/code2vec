        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("", reader.get(0));

        Assert.assertEquals(1L, reader.getCurrentRecord());

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("", reader.get(0));

        Assert.assertEquals(2L, reader.getCurrentRecord());

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("1", reader.get(0));

        Assert.assertEquals(3L, reader.getCurrentRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }



    @Test
