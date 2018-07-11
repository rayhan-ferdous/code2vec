    protected void realizeTemplateCache(KnownTemplates known) {

        Enumeration e = templateRefs.keys();

        while (e.hasMoreElements()) {

            MergeTemplate template = (MergeTemplate) e.nextElement();

            Vector templateCacheNames = (Vector) templateRefs.get(template);

            int size = templateCacheNames.size();

            Vector result = new Vector();

            for (int i = 0; i < size; i++) {

                MergeTemplate t = known.getTemplate((String) templateCacheNames.elementAt(i));

                if (t != null) {

                    result.addElement(t);

                }

            }

            MergeTemplate[] temps = new MergeTemplate[result.size()];

            result.copyInto(temps);

            template.setTemplates(temps);

        }

    }
