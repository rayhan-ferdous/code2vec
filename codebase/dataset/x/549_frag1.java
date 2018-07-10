    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "resolveObject", args = { java.lang.Object.class })

    public void test_resolveObjectLjava_lang_Object() throws Exception {

        Integer original = new Integer(10);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(original);

        oos.flush();

        oos.close();

        byte[] bytes = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

        ObjectInputStreamWithResolveObject ois = new ObjectInputStreamWithResolveObject(bais);

        Integer actual = (Integer) ois.readObject();

        ois.close();

        assertEquals(ObjectInputStreamWithResolveObject.intObj, actual);

    }
