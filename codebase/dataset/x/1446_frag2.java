                    logger.debug("   DROP target = " + drop_target);

                    Point position = drop_target.relativePosition(x, y);

                    logger.debug("   DROP rel-pos = " + position);

                    HashMap drop_event = new HashMap(event);

                    drop_event.put(XComponent.ACTION, new Integer(XComponent.POINTER_DROP));
