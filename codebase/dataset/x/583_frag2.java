    private void reloadCategories(String password) throws XmlRpcException, IOException {

        Vector<Object> params = new Vector<Object>();

        params.addElement(getBlogId());

        params.addElement(userName);

        params.addElement(password);

        Object output = execute("metaWeblog.getCategories", params);

        List<IBlogCategory> cat = handleFetchCategoryOutput(output, params);

        setCategories(cat);

        saveCategories();

    }
