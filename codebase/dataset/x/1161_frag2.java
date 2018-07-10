    public void setState(int state) {

        if (log.isDebugEnabled()) log.debug("cv " + number() + " set state from " + _state + " to " + state);

        int oldstate = _state;

        _state = state;

        switch(state) {

            case UNKNOWN:

                setColor(COLOR_UNKNOWN);

                break;

            case EDITED:

                setColor(COLOR_EDITED);

                break;

            case READ:

                setColor(COLOR_READ);

                break;

            case STORED:

                setColor(COLOR_STORED);

                break;

            case FROMFILE:

                setColor(COLOR_FROMFILE);

                break;

            case SAME:

                setColor(COLOR_SAME);

                break;

            case DIFF:

                setColor(COLOR_DIFF);

                break;

            default:

                log.error("Inconsistent state: " + _state);

        }

        if (oldstate != state) prop.firePropertyChange("State", Integer.valueOf(oldstate), Integer.valueOf(state));

    }
