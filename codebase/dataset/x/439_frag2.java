            if ("boolParam".equals(pColumn)) return new Boolean(fTotal.getBoolParam());

            if ("textParam".equals(pColumn)) return fTotal.getTextParam();

            if ("amountParam".equals(pColumn)) return fTotal.getAmountParam();
