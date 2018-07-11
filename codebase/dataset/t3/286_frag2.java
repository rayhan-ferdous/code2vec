    public static Method[] getStaticMethods(Class cl) {

        Method[] members = cl.getMethods();

        Vector vec = new Vector();

        if (members.length == 0) {

            return null;

        }

        int n = members.length;

        for (int i = 0; i < n; i++) {

            Method memb = members[i];

            if (isStatic(memb)) {

                vec.add(memb);

            }

        }

        if (vec.size() == 0) {

            return null;

        }

        Method[] out = new Method[vec.size()];

        vec.toArray(out);

        return out;

    }
