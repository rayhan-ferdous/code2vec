    public void reorganise() {

        HashMap temp = new HashMap();

        Iterator i = sub_spaces.entrySet().iterator();

        while (i.hasNext()) {

            Map.Entry me = (Map.Entry) i.next();

            temp.put(me.getKey(), me.getValue());

        }

        sub_spaces = temp;

        super.reorganise();

    }
