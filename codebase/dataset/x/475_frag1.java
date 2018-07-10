            if ("bookText".equals(pColumn)) return fTotal.getBookText();

            if ("bookDate".equals(pColumn)) return fTotal.getBookDate();

            if ("percentage".equals(pColumn)) return new Double(fTotal.getPercentage());
