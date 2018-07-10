    protected synchronized void DeleteLost() {

        Iterator<Entry<String, WeakReference<PfyshFile>>> i = Files.entrySet().iterator();

        while (i.hasNext()) {

            Entry<String, WeakReference<PfyshFile>> e = i.next();

            if (e.getValue().get() == null) {

                File f = new File(e.getKey());

                f.delete();

                i.remove();

            }

        }

    }
