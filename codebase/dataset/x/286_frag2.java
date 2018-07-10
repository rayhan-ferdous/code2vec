        try {

            if (mode == Cipher.ENCRYPT_MODE) cipher.init(true, iv); else cipher.init(false, iv);

            byte[] result = new byte[cipher.getOutputSize(original.length)];

            int bytesSoFar = cipher.processBytes(original, 0, original.length, result, 0);

            cipher.doFinal(result, bytesSoFar);

            return result;

        } catch (Exception e) {

            return null;
