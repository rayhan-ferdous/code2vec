        public boolean validate(XMLValidateContext validateContext) throws XMLSignatureException {

            if (validateContext == null) {

                throw new NullPointerException("context cannot be null");

            }

            if (validated) {

                return validationStatus;

            }

            SignatureMethod sm = si.getSignatureMethod();

            Key validationKey = null;

            KeySelectorResult ksResult;

            try {

                ksResult = validateContext.getKeySelector().select(ki, KeySelector.Purpose.VERIFY, sm, validateContext);

                validationKey = ksResult.getKey();

                if (validationKey == null) {

                    throw new XMLSignatureException("the keyselector did " + "not find a validation key");

                }

            } catch (KeySelectorException kse) {

                throw new XMLSignatureException("cannot find validation " + "key", kse);

            }

            try {

                validationStatus = ((DOMSignatureMethod) sm).verify(validationKey, (DOMSignedInfo) si, value, validateContext);

            } catch (Exception e) {

                throw new XMLSignatureException(e);

            }

            validated = true;

            ksr = ksResult;

            return validationStatus;

        }
