        if ("orderID".equals(pColumn)) return new Long(vo.getOrderID());

        if ("journalID".equals(pColumn)) return new Long(vo.getJournalID());

        if ("bookDate".equals(pColumn)) return vo.getBookDate();
