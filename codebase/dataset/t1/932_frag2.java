    protected void buildFreqTable(FreqList freqList, StringEntry strEntry, StringFreqData wordFreq, ArrayList wordList, StringFreqData pairFreq, ArrayList pairList) {

        String tok = null;

        String curWord = null;

        String lastWord = null;

        String wordPair = null;

        Adler32 ad = new Adler32();

        StringData curSD = null;

        StringData lastSD = null;

        StringData pairSD = null;

        TreeMap foundList = null;

        StringEntry se = null;

        Object obj = null;

        String lastTok = " ";

        long dig;

        ArrayList tokenList = new ArrayList();

        StaticUtils.tokenizeText(strEntry.getSrcText(), tokenList);

        for (int i = 0; i < tokenList.size(); i++) {

            tok = ((Token) tokenList.get(i)).text;

            ad.reset();

            lastWord = curWord;

            curWord = tok.toLowerCase();

            wordPair = lastWord + curWord;

            ad.update(curWord.getBytes());

            curSD = new StringData(ad.getValue(), tok);

            wordList.add(curSD);

            if (freqList == null) wordFreq.sub(curSD.getDigest(), tok); else wordFreq.add(curSD.getDigest(), tok);

            dig = curSD.getDigest();

            if (lastSD != null) dig += (lastSD.getDigest() << 32);

            pairSD = new StringData(dig, lastTok + tok);

            pairList.add(pairSD);

            if (freqList == null) pairFreq.sub(pairSD.getDigest(), wordPair); else pairFreq.add(pairSD.getDigest(), wordPair);

            curSD.setLow(pairSD.getDigest());

            if (lastSD != null) lastSD.setHigh(pairSD.getDigest());

            if (freqList != null) {

                foundList = find(curWord);

                if (foundList != null) {

                    while (foundList.size() > 0) {

                        obj = foundList.firstKey();

                        se = (StringEntry) foundList.remove(obj);

                        if (se != strEntry) freqList.add(se);

                    }

                }

            }

            lastTok = tok;

            lastSD = curSD;

        }

        if (curWord == null) return;

        ad.reset();

        ad.update(curWord.getBytes());

        pairSD = new StringData(ad.getValue(), tok);

        curSD.setHigh(pairSD.getDigest());

        pairList.add(pairSD);

        if (freqList == null) pairFreq.sub(curSD.getDigest(), tok); else pairFreq.add(curSD.getDigest(), tok);

    }
