        if ("shippingDate".equals(pColumn)) return vo.getShippingDate();

        if ("price".equals(pColumn)) return vo.getPrice();

        if ("currencyID".equals(pColumn)) return new Short(vo.getCurrencyID());
