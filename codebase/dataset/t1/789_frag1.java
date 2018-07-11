        if ("customerID".equals(pColumn)) return new Long(vo.getCustomerID());

        if ("amount".equals(pColumn)) return vo.getAmount();

        if ("amountPaid".equals(pColumn)) return vo.getAmountPaid();
