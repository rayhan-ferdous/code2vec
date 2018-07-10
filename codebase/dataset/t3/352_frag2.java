    public RestServiceResult list(RestServiceResult serviceResult, int nRowStart, int nMaxResults) {

        ToForumPostDAO toForumPostDAO = new ToForumPostDAO();

        List<ToForumPost> list = toForumPostDAO.findAll(nRowStart, nMaxResults);

        if (list.size() == 0) {

            serviceResult.setNumResult(0);

            serviceResult.setMessage(bundle.getString("forumPost.list.notFound"));

        } else {

            Object[] array = { list.size() };

            serviceResult.setMessage(MessageFormat.format(bundle.getString("forumPost.list.success"), array));

            if ((nRowStart > 0) || (nMaxResults > 0)) serviceResult.setNumResult(toForumPostDAO.findAll().size()); else serviceResult.setNumResult(list.size());

        }

        serviceResult.setObjResult(list);

        return serviceResult;

    }
