        if ("discharge".equals(pColumn)) return vo.getDischarge();

        if ("bankAccountID".equals(pColumn)) return new Long(vo.getBankAccountID());

        if ("ahvCode".equals(pColumn)) return new Long(vo.getAhvCode());
