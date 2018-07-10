        if ("recipient".equals(pColumn)) return vo.getRecipient();

        if ("beneficiary".equals(pColumn)) return vo.getBeneficiary();

        if ("message4x35".equals(pColumn)) return vo.getMessage4x35();

        if ("orderer".equals(pColumn)) return vo.getOrderer();

        return null;
