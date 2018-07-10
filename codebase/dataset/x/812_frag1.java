        if ("amount".equals(pColumn)) return vo.getAmount();

        if ("currencyID".equals(pColumn)) return new Short(vo.getCurrencyID());

        if ("exchangeRate".equals(pColumn)) return new Double(vo.getExchangeRate());
