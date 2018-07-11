    private void _setYRange(double min, double max) {

        if (min > max) {

            min = -1.0;

            max = 1.0;

        } else if (min == max) {

            min -= 0.1;

            max += 0.1;

        }

        double largest = Math.max(Math.abs(min), Math.abs(max));

        _yExp = (int) Math.floor(Math.log(largest) * _log10scale);

        if (_yExp > 1 || _yExp < -1) {

            double ys = 1.0 / Math.pow(10.0, (double) _yExp);

            _ytickMin = min * ys;

            _ytickMax = max * ys;

        } else {

            _ytickMin = min;

            _ytickMax = max;

            _yExp = 0;

        }

        _yMin = min;

        _yMax = max;

    }
