    private static String getDetails(StreamSource source) {

        StringBuilder sb = new StringBuilder();

        if (source.getPublicId() != null) {

            sb.append("PublicId='");

            sb.append(source.getPublicId());

            sb.append("'  ");

        }

        if (source.getSystemId() != null) {

            sb.append("SystemId='");

            sb.append(source.getSystemId());

            sb.append("'  ");

        }

        return sb.toString();

    }
