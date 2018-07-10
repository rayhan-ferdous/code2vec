    @SuppressWarnings("unchecked")

    public List<SyndEntry> getBlogs(String countryCode, Integer numberOfDays) {

        if (countryCode == null) {

            countryCode = "";

        }

        if (updateBlogList(countryCode)) {

            doUpdateOfBlogList(countryCode);

        }

        if (blogTable.get(countryCode) != null && blogTable.get(countryCode).blogList != null) {

            if (numberOfDays == null) {

                return blogTable.get(countryCode).blogList;

            }

            List<SyndEntry> list = blogTable.get(countryCode).blogList;

            SyndEntry[] ary = list.toArray(new SyndEntry[0]);

            ArrayList<SyndEntry> filteredList = new ArrayList<SyndEntry>();

            Calendar cal = Calendar.getInstance();

            cal.add(Calendar.HOUR, numberOfDays * -24);

            Date filterDate = cal.getTime();

            for (int i = 0; i < ary.length; i++) {

                if (ary[i].getPublishedDate().after(filterDate)) {

                    filteredList.add(ary[i]);

                } else {

                    break;

                }

            }

            return filteredList;

        } else {

            return null;

        }

    }
