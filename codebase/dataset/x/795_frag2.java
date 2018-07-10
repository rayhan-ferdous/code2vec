            if (id == null) {

                return "";

            }

            String delim = "#";

            String escape = "~";

            Object trainingIDObj = id.getTrainingID();

            String trainingID = trainingIDObj == null ? "" : String.valueOf(trainingIDObj);

            trainingID = trainingID.replace(escape, escape + escape);
