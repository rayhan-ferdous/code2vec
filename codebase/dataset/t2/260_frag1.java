    public static Closure foldClosure3(final Value p1, final Value p2, final Value p3, final Closure c) {

        assert p1 != null : "null p1";

        assert p2 != null : "null p2";

        assert p3 != null : "null p3";

        assert c != null : "null c";

        c.elements[c.next--] = p3;

        c.elements[c.next--] = p2;

        c.elements[c.next--] = p1;

        return c;

    }
