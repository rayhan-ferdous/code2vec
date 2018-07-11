        parameters.add(new Parameter("method", METHOD_GET_SIZES));

        parameters.add(new Parameter("api_key", apiKey));

        parameters.add(new Parameter("photo_id", photoId));

        parameters.add(new Parameter("api_sig", AuthUtilities.getSignature(sharedSecret, parameters)));

        Response response = transport.get(transport.getPath(), parameters);
