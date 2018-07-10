    public String getAxisDomain() {

        String retval;

        if (!_ad._just_got_data) {

            _status = ERROR;

            retval = null;

        } else if (_status == NORMAL) {

            retval = _ad._domain[0];

        } else {

            retval = null;

        }

        return retval;

    }
