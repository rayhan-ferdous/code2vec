                break;

            case 4:

                pd.setLogarithmic(((Boolean) aValue).booleanValue());

        }

    }



    public void addTableModelListener(TableModelListener l) {

        if (listeners == null) {

            listeners = new Vector();

        }

        listeners.add(l);
