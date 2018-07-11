        final String[] _ob_names = { "_get_MyChannel", "_get_MyID", "_get_MyOperator", "_get_pull_consumers", "_get_push_consumers", "add_filter", "destroy", "get_all_filters", "get_filter", "get_proxy_consumer", "get_qos", "obtain_notification_pull_consumer", "obtain_notification_push_consumer", "obtain_pull_consumer", "obtain_push_consumer", "offer_change", "remove_all_filters", "remove_filter", "set_qos", "validate_qos" };

        int _ob_left = 0;

        int _ob_right = _ob_names.length;

        int _ob_index = -1;

        while (_ob_left < _ob_right) {

            int _ob_m = (_ob_left + _ob_right) / 2;

            int _ob_res = _ob_names[_ob_m].compareTo(opName);

            if (_ob_res == 0) {

                _ob_index = _ob_m;

                break;

            } else if (_ob_res > 0) _ob_right = _ob_m; else _ob_left = _ob_m + 1;

        }

        switch(_ob_index) {

            case 0:

                return _OB_att_get_MyChannel(in, handler);

            case 1:

                return _OB_att_get_MyID(in, handler);

            case 2:

                return _OB_att_get_MyOperator(in, handler);

            case 3:

                return _OB_att_get_pull_consumers(in, handler);

            case 4:

                return _OB_att_get_push_consumers(in, handler);

            case 5:

                return _OB_op_add_filter(in, handler);

            case 6:

                return _OB_op_destroy(in, handler);

            case 7:

                return _OB_op_get_all_filters(in, handler);

            case 8:
