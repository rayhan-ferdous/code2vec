            if ("periodStart".equals(pColumn)) return fTotal.getPeriodStart();

            if ("periodEnd".equals(pColumn)) return fTotal.getPeriodEnd();

            if ("months".equals(pColumn)) return new Long(fTotal.getMonths());
