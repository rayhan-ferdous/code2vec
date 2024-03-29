    public RestServiceResult search(RestServiceResult serviceResult, Long nActivityId) {

        CoActivity coActivity = new CoActivityDAO().findById(nActivityId);

        if (coActivity == null) {

            serviceResult.setError(true);

            serviceResult.setMessage(bundle.getString("activity.search.notFound"));

        } else {

            List<CoActivity> list = new ArrayList<CoActivity>();

            EntityManagerHelper.refresh(coActivity);

            list.add(coActivity);

            Object[] arrayParam = { list.size() };

            serviceResult.setMessage(MessageFormat.format(bundle.getString("activity.search.success"), arrayParam));

            serviceResult.setObjResult(list);

        }

        return serviceResult;

    }
