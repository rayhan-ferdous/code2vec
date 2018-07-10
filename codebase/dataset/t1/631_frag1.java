    public static Integer generateRandomSmallInt(boolean unique) {

        Integer randomInteger = null;

        if (unique) {

            do {

                randomInteger = generator.nextInt();

            } while (set.contains(randomInteger));

            set.add(randomInteger);

        } else {

            randomInteger = generator.nextInt();

        }

        return randomInteger;

    }
