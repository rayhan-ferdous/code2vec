    public boolean updateIndexDFile(Channel channel, HashMap hmParams) throws Exception {

        try {

            String strFilePath = channel.getDefineFilePath();

            String strFileName = channel.getDefineFileName();

            ChannelHtmlParser chp = null;

            chp = new ChannelHtmlParser(strFilePath + strFileName, strFilePath);

            String strContent = new String(chp.UpdateIndexDfileParams(hmParams).getBytes("ISO-8859-1"), "GBK");

            Function.writeTextFile(strContent, strFilePath + "index_d.html", true);

        } catch (ParserException ex) {

            log.error(ex);

        } catch (IOException ex) {

            log.error(ex);

        }

        return true;

    }
