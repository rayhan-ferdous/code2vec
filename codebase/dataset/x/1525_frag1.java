            if ("amount".equals(pColumn)) return fTotal.getAmount();

            if ("amountPaid".equals(pColumn)) return fTotal.getAmountPaid();

            if ("creationDate".equals(pColumn)) return fTotal.getCreationDate();

            if ("dueDate".equals(pColumn)) return fTotal.getDueDate();

            if ("bankAccountID".equals(pColumn)) return new Long(fTotal.getBankAccountID());
