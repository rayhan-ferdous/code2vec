        List<ToPublication> list = toPublicationDAO.findAll(nRowStart, nMaxResults);

        if (list.size() == 0) {

            serviceResult.setError(true);

            serviceResult.setMessage(bundle.getString("publication.list.notFound"));

        } else {

            Object[] array = { list.size() };

            serviceResult.setMessage(MessageFormat.format(bundle.getString("publication.list.success"), array));

            serviceResult.setObjResult(list);

            if ((nRowStart > 0) || (nMaxResults > 0)) serviceResult.setNumResult(toPublicationDAO.findAll().size()); else serviceResult.setNumResult(list.size());

        }

        return serviceResult;
