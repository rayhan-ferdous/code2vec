        Subsystem sub = getCurrentParentSubsystem(diagram);

        MetaModelCache cache = MDEPlugin.getDefault().getRuntime().getModelCache();

        if (cache == null) return name;

        String newName = name;

        int i = 1;

        ComponentCollection mcmps = cache.getComponents(sub);

        boolean hasName = true;

        while (hasName) {

            hasName = false;

            for (Enumeration e = mcmps.elements(); e.hasMoreElements(); ) {

                Component mcmp = (Component) e.nextElement();

                if (mcmp.getName().equalsIgnoreCase(newName)) {

                    newName = name + (i++);

                    hasName = true;

                    break;

                }

            }

        }

        return newName;
