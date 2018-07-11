    public RestServiceResult search(RestServiceResult serviceResult, String sTestName) {

        List<CoTest> list = new CoTestDAO().findByTestName(sTestName);

        if (list.size() == 0) {

            serviceResult.setError(true);

            serviceResult.setMessage(bundle.getString("test.search.notFound"));

        } else {

            Object[] arrayParam = { list.size() };

            serviceResult.setMessage(MessageFormat.format(bundle.getString("test.search.success"), arrayParam));

            serviceResult.setObjResult(list);

        }

        return serviceResult;

    }



    /**

	 * Realiza la busqueda de una secuencia

	 * 

	 * @param serviceResult

	 *            El {@link RestServiceResult} que contendr�n el resultado de la

	 *            operaci�n.

	 * @param nTestId

	 *            C�digo de la prueba

	 * @return El {@link RestServiceResult} contiene el resultado de la

	 *         operaci�n.

	 */

    public RestServiceResult search(RestServiceResult serviceResult, Long nTestId) {

        CoTest coTest = new CoTestDAO().findById(nTestId);

        if (coTest == null) {

            serviceResult.setError(true);

            serviceResult.setMessage(bundle.getString("test.search.notFound"));

        } else {

            List<CoTest> list = new ArrayList<CoTest>();

            EntityManagerHelper.refresh(coTest);

            list.add(coTest);

            Object[] arrayParam = { list.size() };

            serviceResult.setMessage(MessageFormat.format(bundle.getString("test.search.success"), arrayParam));

            serviceResult.setObjResult(list);

        }

        return serviceResult;

    }



    /**

	 * Obtiene la lista de pruebas

	 * 

	 * @param result

	 *            El {@link RestServiceResult} que contendr�n los mensajes

	 *            localizados y estado SQL .

	 * @return El {@link RestServiceResult} que contiene el resultado de la

	 *         operaci�n.

	 */

    public RestServiceResult list(RestServiceResult result) {

        return list(result, 0, 0);

    }



    /**

	 * Obtiene la lista de pruebas

	 * 

	 * @param serviceResult

	 *            El {@link RestServiceResult} que contendr�n los mensajes

	 *            localizados y estado SQL .

	 * @return El {@link RestServiceResult} que contiene el resultado de la

	 *         operaci�n.

	 */

    public RestServiceResult list(RestServiceResult serviceResult, int nRowStart, int nMaxResults) {

        CoTestDAO coTestDAO = new CoTestDAO();

        List<CoTest> list = coTestDAO.findAll(nRowStart, nMaxResults);

        if (list.size() == 0) {

            serviceResult.setError(true);

            serviceResult.setMessage(bundle.getString("test.list.notFound"));

        } else {

            Object[] array = { list.size() };

            serviceResult.setMessage(MessageFormat.format(bundle.getString("test.list.success"), array));

            serviceResult.setObjResult(list);

            if ((nRowStart > 0) || (nMaxResults > 0)) serviceResult.setNumResult(coTestDAO.findAll().size()); else serviceResult.setNumResult(list.size());

        }

        return serviceResult;

    }



    /**

	 * Obtiene la lista de pruebas clonables

	 * 

	 * @param result

	 *            El {@link RestServiceResult} que contendr�n los mensajes

	 *            localizados y estado SQL .

	 * @return El {@link RestServiceResult} que contiene el resultado de la

	 *         operaci�n.

	 */

    public RestServiceResult listClone(RestServiceResult result) {
