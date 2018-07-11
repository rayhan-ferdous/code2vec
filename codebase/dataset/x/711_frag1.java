        } catch (Exception e) {

            AIOUtils.log("", e);

        }

        return logFile;

    }



    public static void log(String msg) {

        if (logfileWriter != null) {

            logfileWriter.append(new SimpleDateFormat("HH:mm:ss").format(new Date()) + "," + msg + "\n");

            if (logfileWriter.checkError()) {

                throw new RuntimeException("error occur when writing log file.");
