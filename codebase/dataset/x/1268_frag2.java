    public void ratStyles(boolean removeLH) {

        int max = -1;

        String cn = "";

        Enumeration<String> style = estilos.keys();

        Hashtable<String, String> rev = new Hashtable<String, String>();

        for (int i = 0; i < statStyleData.size(); i++) {

            String ek = style.nextElement();

            String ev = estilos.get(ek);

            rev.put(ev, ek);

            if (ek.contains("font-size") && max < statStyleData.get(ev)) {

                max = statStyleData.get(ev);

                cn = ev;

            }

        }

        HtmlStyle mostUsed = new HtmlStyle(rev.get(cn));

        StyleItem fs = mostUsed.getStyle("font-size");

        float dividefactor = 1.0F;

        if (fs != null && (fs.unit == null || fs.unit.length() == 0)) {

            dividefactor = fs.number / 0.8f;

        }

        for (Enumeration<String> e = rev.keys(); e.hasMoreElements(); ) {

            String cs = e.nextElement();

            HtmlStyle hs = new HtmlStyle(rev.get(cs));

            if (removeLH) hs.removeStyle("line-height");

            StyleItem si = hs.getStyle("font-size");

            if (si != null) {

                si.number /= dividefactor;

                si.unit = "em";

                rev.put(cs, hs.getStyleContent(StyleItem.st_all));

            }

        }

        estilos = new Hashtable<String, String>();

        for (Enumeration<String> n = rev.keys(); n.hasMoreElements(); ) {

            String styleClassName = n.nextElement();

            String styleDef = rev.get(styleClassName);

            while (estilos.get(styleDef) != null) styleDef += " ";

            estilos.put(styleDef, styleClassName);

        }

    }
