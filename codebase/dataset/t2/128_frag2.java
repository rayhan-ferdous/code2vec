        public void decrypt(byte[] buffer) {

            int nLen = buffer.length;

            long lTemp;

            for (int nI = 0; nI < nLen; nI += 8) {

                lTemp = byteArrayToLong(buffer, nI);

                lTemp = decryptBlockCBC(lTemp);

                longToByteArray(lTemp, buffer, nI);

            }

        }
