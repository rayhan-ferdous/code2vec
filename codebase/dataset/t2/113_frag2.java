        final String[] _ob_names = { "define_properties", "define_properties_with_modes", "define_property", "define_property_with_mode", "delete_all_properties", "delete_properties", "delete_property", "get_all_properties", "get_all_property_names", "get_allowed_properties", "get_allowed_property_types", "get_number_of_properties", "get_properties", "get_property_mode", "get_property_modes", "get_property_value", "is_property_defined", "set_property_mode", "set_property_modes" };

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

                return _OB_op_define_properties(in, handler);

            case 1:

                return _OB_op_define_properties_with_modes(in, handler);

            case 2:

                return _OB_op_define_property(in, handler);

            case 3:

                return _OB_op_define_property_with_mode(in, handler);

            case 4:

                return _OB_op_delete_all_properties(in, handler);

            case 5:

                return _OB_op_delete_properties(in, handler);

            case 6:

                return _OB_op_delete_property(in, handler);

            case 7:

                return _OB_op_get_all_properties(in, handler);

            case 8:
