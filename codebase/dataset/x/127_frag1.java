    public void deleteAnnotations(List nf) {

        Iterator iter = nf.iterator();

        Pattern p = Pattern.compile("^[ \t]*@.+");

        Matcher m = null;

        boolean b;

        while (iter.hasNext()) {

            String checkel = (String) iter.next();

            m = p.matcher(checkel);

            b = m.matches();

            if (b) {

                iter.remove();

            }

        }

    }
