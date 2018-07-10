    public void nwComponentStateChanged(NwComponent c) {

        if (c instanceof PCNode) {

            if (nameToNode.containsValue(c)) {

                Iterator set = nameToNode.entrySet().iterator();

                while (set != null && set.hasNext()) {

                    Map.Entry e = (Map.Entry) set.next();

                    if (e.getValue().equals(c)) {

                        set.remove();

                        set = null;

                    }

                }

            }

            nameToNode.put(((PCNode) c).getName(), c);

        }

        Iterator iter = nwComponentChangeListeners.iterator();

        while (iter.hasNext()) {

            try {

                ((ChangeListener) iter.next()).stateChanged(new ChangeEvent(c));

            } catch (Throwable t) {

                logger.warning("ChangeListener has thrown exception, ignored.");

                logger.throwing(getClass().getName(), "nwComponentStateChanged", t);

            }

        }

    }
