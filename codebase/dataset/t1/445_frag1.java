                assertEquals(m.getReturnType(), java.util.Vector.class);

                assertEquals(m.getModifiers(), 41);

            }

            {

                final Method m = javax.media.pm.PackageManager.class.getMethod("setProtocolPrefixList", new Class[] { java.util.Vector.class });

                assertEquals(m.getReturnType(), void.class);

                assertEquals(m.getModifiers(), 41);

            }

            {

                final Method m = javax.media.pm.PackageManager.class.getMethod("commitProtocolPrefixList", new Class[] {});

                assertEquals(m.getReturnType(), void.class);

                assertEquals(m.getModifiers(), 41);

            }

            {

                final Method m = javax.media.pm.PackageManager.class.getMethod("getContentPrefixList", new Class[] {});

                assertEquals(m.getReturnType(), java.util.Vector.class);

                assertEquals(m.getModifiers(), 41);

            }

            {

                final Method m = javax.media.pm.PackageManager.class.getMethod("setContentPrefixList", new Class[] { java.util.Vector.class });
