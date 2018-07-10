            Vector<EpgMatch> autoAddItems = store.getEpgMatchList();

            for (int x = 0; x < autoAddItems.size(); x++) {

                EpgMatch item = (EpgMatch) autoAddItems.get(x);

                item.remMatchListName(name);

            }

            store.saveEpgAutoList(null);

        }

        StringBuffer out = new StringBuffer(256);

        out.append("HTTP/1.0 302 Moved Temporarily\n");

        out.append("Location: /servlet/" + urlData.getServletClass() + "?action=05\n\n");

        return out.toString().getBytes();

    }
