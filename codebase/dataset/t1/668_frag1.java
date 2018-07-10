            if ("referenceNr".equals(pColumn)) return fTotal.getReferenceNr();

            if ("type".equals(pColumn)) return new Long(fTotal.getType());

            if ("creationDate".equals(pColumn)) return fTotal.getCreationDate();
