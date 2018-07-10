    public void performQueries(Collection queries, OperationObserver callback) {

        List finalQueries = new ArrayList(queries.size());

        Iterator it = queries.iterator();

        while (it.hasNext()) {

            Query query = (Query) it.next();

            query = filterThroughDelegateDeprecated(query);

            if (query != null) {

                finalQueries.add(query);

            }

        }

        if (!finalQueries.isEmpty()) {

            getParentDataDomain().performQueries(queries, callback);

        }

    }
