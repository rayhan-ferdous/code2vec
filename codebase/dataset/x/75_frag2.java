                if (b > 1 && b < cname.length()) {

                    cname = cname.substring(b);

                }

            }

            int i = cname.lastIndexOf('.');

            if (i != -1) {

                sm.checkPackageAccess(name.substring(0, i));
