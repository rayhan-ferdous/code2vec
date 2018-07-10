    public RestServiceResult list(RestServiceResult serviceResult, int nRowStart, int nMaxResults) {

        MaSpellDAO maSpellDAO = new MaSpellDAO();

        List<MaSpell> list = maSpellDAO.findAll(nRowStart, nMaxResults);

        if (list.size() == 0) {

            serviceResult.setError(true);

            serviceResult.setMessage(bundle.getString("spell.list.notFound"));

        } else {

            Object[] array = { list.size() };

            serviceResult.setMessage(MessageFormat.format(bundle.getString("spell.list.success"), array));

            serviceResult.setObjResult(list);

            if ((nRowStart > 0) || (nMaxResults > 0)) serviceResult.setNumResult(maSpellDAO.findAll().size()); else serviceResult.setNumResult(list.size());

        }

        return serviceResult;

    }
