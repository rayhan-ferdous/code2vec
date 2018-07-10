    String getDigestAlgName() {

        String digestAlgOID = this.getDigestAlgOID();

        if (CMSSignedDataGenerator.DIGEST_MD5.equals(digestAlgOID)) {

            return "MD5";

        } else if (CMSSignedDataGenerator.DIGEST_SHA1.equals(digestAlgOID)) {

            return "SHA1";

        } else if (CMSSignedDataGenerator.DIGEST_SHA224.equals(digestAlgOID)) {

            return "SHA224";

        } else {

            return digestAlgOID;

        }

    }
