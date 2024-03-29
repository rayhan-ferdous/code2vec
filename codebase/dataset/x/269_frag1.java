    public void processEvents() {

        try {

            Event e = new Event("AppleScript");

            e.addField("script", FieldType.STRING, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);

            e.addField("Target", hostname);

            Event[] events = new Event[1];

            events[0] = e;

            eheap.registerForEvents(events, new EHListener());

            while (true) {

                if (latest != null) {

                    synchronized (eheap) {

                        if (verifyPermission(latest)) {

                            execute(latest.getPostValueString("script"));

                        }

                        latest = null;

                    }

                } else {

                    Thread.sleep(100);

                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
