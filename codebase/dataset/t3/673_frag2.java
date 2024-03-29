    public static String convertFrameID23To24(String identifier) {

        if (identifier.length() < 4) {

            return null;

        }

        if (ID3v23Frames.getInstanceOf().getIdToValueMap().containsKey(identifier)) {

            if (ID3v24Frames.getInstanceOf().getIdToValueMap().containsKey(identifier)) {

                return identifier;

            } else {

                return ID3Frames.convertv23Tov24.get(identifier.substring(0, 4));

            }

        }

        return null;

    }
