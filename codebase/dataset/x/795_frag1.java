            return null;

        }

        if (object instanceof RecTeam) {

            RecTeam o = (RecTeam) object;

            RecTeamPK id = o.getRecTeamPK();

            if (id == null) {

                return "";

            }

            String delim = "#";

            String escape = "~";

            String personID = String.valueOf(id.getPersonID());
