            MessageDigest md = MessageDigest.getInstance("SHA-1");

            byte[] hash = new byte[40];

            md.update(s.getBytes("iso-8859-1"), 0, s.length());

            hash = md.digest();

            return toHex(hash);
