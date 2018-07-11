    public List getNamespacesPrefixes() throws DatabaseException {

        if (namespacesPrefixes == null) {

            namespacesPrefixes = new ArrayList();

            StoredMap m = getNamespaces();

            Iterator i = m.keySet().iterator();

            while (i.hasNext()) {

                namespacesPrefixes.add(i.next());

            }

            StoredIterator.close(i);

        }

        return namespacesPrefixes;

    }
