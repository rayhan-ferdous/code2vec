        } else if (penStyle == WMFConstants.META_PS_DOT) {

            float[] dash = { 1.0f * _scale, 5f * _scale };

            stroke = new BasicStroke(_width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10f * _scale, dash, 0f);

            g2d.setStroke(stroke);

        } else if (penStyle == WMFConstants.META_PS_DASH) {

            float[] dash = { 5f * _scale, 2f * _scale };

            stroke = new BasicStroke(_width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10f * _scale, dash, 0f);

            g2d.setStroke(stroke);

        } else if (penStyle == WMFConstants.META_PS_DASHDOT) {

            float[] dash = { 5f * _scale, 2f * _scale, 1.0f * _scale, 2f * _scale };

            stroke = new BasicStroke(_width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10f * _scale, dash, 0f);

            g2d.setStroke(stroke);

        } else if (penStyle == WMFConstants.META_PS_DASHDOTDOT) {
