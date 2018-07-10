        if (p_tag == TAG_IOIQltyInd) {

            _IOIQltyInd = new String(v_value);

            return NORMAL;

        }

        if (p_tag == TAG_IOINaturalFlag) {

            _IOINaturalFlag = new String(v_value);

            return NORMAL;

        }

        if (p_tag == TAG_NoIOIQualifiers) {

            _NoIOIQualifiers = new String(v_value);

            return START_GROUP;

        }

        if (p_tag == TAG_Text) {

            _Text = new String(v_value);

            return NORMAL;

        }

        if (p_tag == TAG_EncodedTextLen) {

            _EncodedTextLen = new String(v_value);

            return DATA_TYPE;

        }

        if (p_tag == TAG_EncodedText) {

            _EncodedText = new String(v_value);

            return NORMAL;

        }

        if (p_tag == TAG_TransactTime) {

            _TransactTime = new String(v_value);

            return NORMAL;

        }

        if (p_tag == TAG_URLLink) {

            _URLLink = new String(v_value);

            return NORMAL;

        }

        if (p_tag == TAG_NoRoutingIDs) {

            _NoRoutingIDs = new String(v_value);

            return START_GROUP;

        }

        if (p_tag == TAG_SpreadToBenchmark) {

            _SpreadToBenchmark = new String(v_value);

            return NORMAL;

        }

        if (p_tag == TAG_Benchmark) {

            _Benchmark = new String(v_value);

            return NORMAL;

        }
