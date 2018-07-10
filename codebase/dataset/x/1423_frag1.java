            if ("amount".equals(pColumn)) return fTotal.getAmount();

            if ("currencyPayID".equals(pColumn)) return new Short(fTotal.getCurrencyPayID());

            if ("countryID".equals(pColumn)) return new Long(fTotal.getCountryID());
