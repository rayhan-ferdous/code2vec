            if ("id".equals(pColumn)) return new Long(fTotal.getId());

            if ("esrAccountNr".equals(pColumn)) return fTotal.getEsrAccountNr();

            if ("referenceNr".equals(pColumn)) return fTotal.getReferenceNr();
