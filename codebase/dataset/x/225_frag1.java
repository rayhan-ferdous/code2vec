    public Hashtable querySummary(User user, String xpath) throws EXistException, PermissionDeniedException {

        RpcConnection con = pool.get();

        try {

            return con.summary(user, xpath);

        } catch (Exception e) {

            handleException(e);

            throw new EXistException(e);

        } finally {

            pool.release(con);

        }

    }
