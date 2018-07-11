    public RestServiceResult search(RestServiceResult serviceResult, String sThemesName) {

        List<ToThemes> list = new ToThemesDAO().findByTitle(sThemesName);

        if (list.size() == 0) {

            serviceResult.setError(true);

            serviceResult.setMessage(bundle.getString("theme.search.notFound"));

        } else {

            Object[] arrayParam = { list.size() };

            serviceResult.setMessage(MessageFormat.format(bundle.getString("theme.search.success"), arrayParam));

            serviceResult.setObjResult(list);

        }

        return serviceResult;

    }



    /**

	 * Realiza la busqueda de un tema

	 * 

	 * @param serviceResult

	 *            El {@link RestServiceResult} que contendr�n el resultado de la

	 *            operaci�n.

	 * @param nThemesId

	 *            C�digo del tema

	 * @return El {@link RestServiceResult} contiene el resultado de la

	 *         operaci�n.

	 */

    public RestServiceResult search(RestServiceResult serviceResult, Long nThemesId) {

        ToThemes toThemes = new ToThemesDAO().findById(nThemesId);

        if (toThemes == null) {

            serviceResult.setError(true);

            serviceResult.setMessage(bundle.getString("theme.search.notFound"));

        } else {

            List<ToThemes> list = new ArrayList<ToThemes>();

            EntityManagerHelper.refresh(toThemes);

            list.add(toThemes);

            Object[] arrayParam = { list.size() };

            serviceResult.setMessage(MessageFormat.format(bundle.getString("theme.search.success"), arrayParam));

            serviceResult.setObjResult(list);

        }

        return serviceResult;

    }



    /**

	 * Obtiene la lista de temas

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

	 * Obtiene la lista de temas

	 * 

	 * @param serviceResult

	 *            El {@link RestServiceResult} que contendr�n los mensajes

	 *            localizados y estado SQL .

	 * @return El {@link RestServiceResult} que contiene el resultado de la

	 *         operaci�n.

	 */

    public RestServiceResult list(RestServiceResult serviceResult, int nRowStart, int nMaxResults) {

        ToThemesDAO toThemesDAO = new ToThemesDAO();

        List<ToThemes> list = toThemesDAO.findAll(nRowStart, nMaxResults);

        if (list.size() == 0) {

            serviceResult.setError(true);

            serviceResult.setMessage(bundle.getString("theme.list.notFound"));

        } else {

            Object[] array = { list.size() };

            serviceResult.setMessage(MessageFormat.format(bundle.getString("theme.list.success"), array));

            serviceResult.setObjResult(list);

            if ((nRowStart > 0) || (nMaxResults > 0)) serviceResult.setNumResult(toThemesDAO.findAll().size()); else serviceResult.setNumResult(list.size());

        }

        return serviceResult;

    }



    /**

	 * Obtiene la lista de temas

	 * 

	 * @param result

	 *            El {@link RestServiceResult} que contendr�n los mensajes

	 *            localizados y estado SQL .

	 * @return El {@link RestServiceResult} que contiene el resultado de la

	 *         operaci�n.

	 */

    public RestServiceResult listThemesForCourse(RestServiceResult result, Long nCourse) {
