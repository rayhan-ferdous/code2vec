        if (logger.isDebugEnabled()) {

            logger.debug(str + " [TC]->[SC] " + ret);

        }

        return ret;

    }



    /**

	 * ���ַ��еļ�������ת���ɷ������ġ�

	 * 

	 * @param str

	 *            ԭ�ַ�

	 * @return ת������ַ�

	 */

    public static String simpToTrad(String str) {

        return simpToTrad(str, true);

    }



    /**

	 * ���ַ��еķ�������ת���ɼ������ġ�

	 * 

	 * @param str

	 *            ԭ�ַ�

	 * @return ת������ַ�

	 */

    public static String tradToSimp(String str) {

        return tradToSimp(str, true);
