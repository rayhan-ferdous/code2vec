            if ("user".equals(pColumn)) return fTotal.getUser();

            if ("obj".equals(pColumn)) return fTotal.getObj();

            if ("privilege".equals(pColumn)) return fTotal.getPrivilege();

            if ("type".equals(pColumn)) return fTotal.getType();

            if ("hasPrivilege".equals(pColumn)) return new Boolean(fTotal.isHasPrivilege());
