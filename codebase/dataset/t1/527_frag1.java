        if ("salaryPayment".equals(pColumn)) return vo.getSalaryPayment();

        if ("referenceNumber".equals(pColumn)) return vo.getReferenceNumber();

        if ("debitAccountID".equals(pColumn)) return new Long(vo.getDebitAccountID());
