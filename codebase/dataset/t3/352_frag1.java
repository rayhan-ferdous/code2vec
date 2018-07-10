    public RestServiceResult list(RestServiceResult result, int nRowStart, int nMaxResults) {

        ToAssistanceDAO assistanceDAO = new ToAssistanceDAO();

        List<ToAssistance> list = assistanceDAO.findAll(nRowStart, nMaxResults);

        if (list.size() == 0) {

            result.setError(true);

            result.setMessage(bundle.getString("assistance.list.notFound"));

        } else {

            Object[] array = { list.size() };

            result.setMessage(MessageFormat.format(bundle.getString("assistance.list.success"), array));

            result.setObjResult(list);

            if ((nRowStart > 0) || (nMaxResults > 0)) result.setNumResult(assistanceDAO.findAll().size()); else result.setNumResult(list.size());

        }

        return result;

    }
