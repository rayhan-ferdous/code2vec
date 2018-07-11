        if (!Mixed.makeFilename(given_digest).equals(calculatedDigest)) {

            logger.warning("Warning: public key of sharer didn't match its digest:\n" + "given digest :'" + given_digest + "'\n" + "pubkey       :'" + _pubkey.trim() + "'\n" + "calc. digest :'" + calculatedDigest + "'");

            return null;

        }

        sharer = new Identity(_sharer.substring(0, _sharer.indexOf("@")), _pubkey);

        identities.getNeutrals().add(sharer);
