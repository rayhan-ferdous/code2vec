        Constructor ctors[] = c.getConstructors();

        Arrays.sort(ctors, new Comparator() {



            public int compare(Object x, Object y) {

                return x.toString().compareTo(y.toString());

            }

        });

        System.out.println(c + " has " + ctors.length + " visible constructors");

        for (int i = 0; i < ctors.length; ++i) System.out.println("   " + i + ": " + ctors[i]);

        Constructor declaredCtors[] = c.getDeclaredConstructors();
