    public static TreeNodeAuthority getInstance() {

        if (authority == null) {

            authority = new ColletTreeAuthority();

        }

        return authority;

    }
