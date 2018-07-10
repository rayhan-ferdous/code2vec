        List<ToNotebook> list = toNotebookDAO.findAll(nRowStart, nMaxResults);

        if (list.size() == 0) {

            serviceResult.setError(true);

            serviceResult.setMessage(bundle.getString("notebook.list.notFound"));

        } else {

            Object[] array = { list.size() };

            serviceResult.setMessage(MessageFormat.format(bundle.getString("notebook.list.success"), array));

            serviceResult.setObjResult(list);

            if ((nRowStart > 0) || (nMaxResults > 0)) serviceResult.setNumResult(toNotebookDAO.findAll().size()); else serviceResult.setNumResult(list.size());

        }

        return serviceResult;
