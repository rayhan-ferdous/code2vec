                int appidInt = appid != null ? Integer.parseInt(appid) : 0;

                if (!TextUtils.isEmpty(frequency)) {

                    cv.clear();

                    cv.put(Words.WORD, word);

                    cv.put(Words.FREQUENCY, frequencyInt);
