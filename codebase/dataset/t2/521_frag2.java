    private static String toHexString(byte[] bytes) {

        StringBuilder sb = new StringBuilder(bytes.length * 3);

        for (int b : bytes) {

            b &= 0xff;

            sb.append(HEXDIGITS[b >> 4]);

            sb.append(HEXDIGITS[b & 15]);

            sb.append(' ');

        }

        return sb.toString();

    }



    /**

     * Decorador para preservar a cadeia de confian�a durante a verifica��o e

     * auxiliar na cria��o de contextos SSL.

     */

    private static class TrustManagerDecorator implements X509TrustManager {
