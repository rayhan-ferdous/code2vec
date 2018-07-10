                        if (inRange(startdate, enddate, date)) {

                            String content = URLExplorer(exploreurl);

                            ticker.addNews(new NewsEvent(title, content, date.getTime()));

                        } else if (date.before(startdate)) {

                            reallydone = true;

                            break;
