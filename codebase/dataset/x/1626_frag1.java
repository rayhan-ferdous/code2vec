        if ("schemaname".equals(pColumn)) return vo.getSchemaname();

        if ("tablename".equals(pColumn)) return vo.getTablename();

        if ("tableowner".equals(pColumn)) return vo.getTableowner();

        if ("tablespace".equals(pColumn)) return vo.getTablespace();

        if ("hasindexes".equals(pColumn)) return new Boolean(vo.getHasindexes());
