            if ("ledgerDebit".equals(pColumn)) return new Long(fTotal.getLedgerDebit());

            if ("amountDebit".equals(pColumn)) return fTotal.getAmountDebit();

            if ("currencyDebitID".equals(pColumn)) return new Short(fTotal.getCurrencyDebitID());

            if ("ledgerCredit".equals(pColumn)) return new Long(fTotal.getLedgerCredit());
