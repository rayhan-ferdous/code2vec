            if ("itemDescription".equals(pColumn)) return fTotal.getItemDescription();

            if ("itemSerialNr".equals(pColumn)) return fTotal.getItemSerialNr();

            if ("itemCount".equals(pColumn)) return new Long(fTotal.getItemCount());
