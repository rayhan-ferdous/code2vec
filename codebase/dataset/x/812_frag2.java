        if ("tstamp".equals(pColumn)) return vo.getTstamp();

        if ("listType".equals(pColumn)) return new Long(vo.getListType());

        if ("listSubType".equals(pColumn)) return new Long(vo.getListSubType());
