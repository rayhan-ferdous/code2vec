                int halfbyte = (hash[i] >>> 4) & 0x0F;

                int two_halfs = 0;

                do {

                    if ((0 <= halfbyte) && (halfbyte <= 9)) {

                        buf.append((char) ('0' + halfbyte));

                    } else {

                        buf.append((char) ('a' + (halfbyte - 10)));

                    }

                    halfbyte = hash[i] & 0x0F;

                } while (two_halfs++ < 1);

            }

            return buf.toString();

        } catch (Exception e) {
