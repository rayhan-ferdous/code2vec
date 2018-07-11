    protected void initializeCipher(Cipher cipher, int mode) throws Exception {

        AlgorithmParameterSpec paramSpec = Cipher.getMaxAllowedParameterSpec(cipher.getAlgorithm());

        if (paramSpec instanceof PBEParameterSpec) {

            paramSpec = new PBEParameterSpec(SecurityConstants.SALT, SecurityConstants.ITERATION_COUNT);

            cipher.init(mode, secretKey, paramSpec);

        } else if (paramSpec instanceof IvParameterSpec) {

            paramSpec = new IvParameterSpec(SecurityConstants.SALT);

            cipher.init(mode, secretKey, paramSpec);

        } else {

            cipher.init(mode, secretKey, (AlgorithmParameterSpec) null);

        }

    }
