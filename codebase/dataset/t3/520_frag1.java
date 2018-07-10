    public Object getValueAt(int pRow, String pColumn) {

        BankAccountVO vo = fTableData[fSortOrder[pRow]];

        if ("id".equals(pColumn)) return new Long(vo.getId());

        if ("codeAn".equals(pColumn)) return vo.getCodeAn();

        if ("type".equals(pColumn)) return vo.getType();

        if ("swiftBic".equals(pColumn)) return vo.getSwiftBic();

        if ("clearingNr".equals(pColumn)) return vo.getClearingNr();

        if ("accountNumber".equals(pColumn)) return vo.getAccountNumber();

        if ("ibanNumber".equals(pColumn)) return vo.getIbanNumber();

        if ("dtaID".equals(pColumn)) return new Long(vo.getDtaID());

        if ("currencyID".equals(pColumn)) return new Long(vo.getCurrencyID());

        if ("contactID".equals(pColumn)) return new Long(vo.getContactID());

        if ("mailAddressID".equals(pColumn)) return new Long(vo.getMailAddressID());

        if ("modified".equals(pColumn)) return vo.getModified();

        if ("validFrom".equals(pColumn)) return vo.getValidFrom();

        if ("validTo".equals(pColumn)) return vo.getValidTo();

        return null;

    }
