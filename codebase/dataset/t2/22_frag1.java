    public Object getValueAt(int pRow, String pColumn) {

        BookingPeriodVO vo = fTableData[fSortOrder[pRow]];

        if ("id".equals(pColumn)) return new Long(vo.getId());

        if ("periodStart".equals(pColumn)) return vo.getPeriodStart();

        if ("periodEnd".equals(pColumn)) return vo.getPeriodEnd();

        if ("months".equals(pColumn)) return new Long(vo.getMonths());

        if ("closureDate".equals(pColumn)) return vo.getClosureDate();

        if ("validFrom".equals(pColumn)) return vo.getValidFrom();

        if ("validTo".equals(pColumn)) return vo.getValidTo();

        return null;

    }
