    public void testForSkip() {

        StringBuilder builder = new StringBuilder();

        String string = "123456";

        for (int i = 0; i < string.length(); i += 2) builder.append(string.charAt(i));

        assertEquals("135", builder.toString());

    }
