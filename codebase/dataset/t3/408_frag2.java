    public RestServiceResult list(RestServiceResult serviceResult, int nRowStart, int nMaxResults) {

        ToExercise2GroupDAO ToExercise2GroupDAO = new ToExercise2GroupDAO();

        List<ToExercise2Group> list = ToExercise2GroupDAO.findAll(nRowStart, nMaxResults);

        if (list.size() == 0) {

            serviceResult.setError(true);

            serviceResult.setMessage(bundle.getString("exercise2group.list.notFound"));

        } else {

            Object[] array = { list.size() };

            serviceResult.setMessage(MessageFormat.format(bundle.getString("exercise2group.list.success"), array));

            serviceResult.setObjResult(list);

            if ((nRowStart > 0) || (nMaxResults > 0)) serviceResult.setNumResult(ToExercise2GroupDAO.findAll().size()); else serviceResult.setNumResult(list.size());

        }

        return serviceResult;

    }
