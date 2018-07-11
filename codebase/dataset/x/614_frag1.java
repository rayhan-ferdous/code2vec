            result = new byte[encr.length + salt.length];

            System.arraycopy(encr, 0, result, 0, encr.length);

            System.arraycopy(salt, 0, result, encr.length, salt.length);

            return result;
