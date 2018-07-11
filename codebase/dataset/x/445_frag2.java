            if ("trxID".equals(pColumn)) return new Long(fTotal.getTrxID());

            if ("typeID".equals(pColumn)) return new Long(fTotal.getTypeID());

            if ("typeName".equals(pColumn)) return fTotal.getTypeName();
