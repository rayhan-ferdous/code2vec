            PrintWriter fw = new PrintWriter(new FileWriter(OutputDir + filename));

            fw.print("Id,Job_Id");

            for (int j = 0; j < Headers.size(); j++) {

                fw.print(",");

                fw.print(Headers.get(j));

            }

            fw.println();

            for (int i = 0; i < NumRows; i++) {

                fw.print("" + i + "," + Job_Ids.get(i));

                for (int j = 0; j < DataTable.size(); j++) {

                    fw.print(",");

                    fw.print(DataTable.get(j).get(i));

                }

                fw.println();

            }

            fw.flush();

            fw.close();

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return nResCollected;
