    public static boolean deleteUser(User userToDelete) {

        if (userToDelete != null) {

            try {

                String jsonuser = new Gson().toJson(userToDelete);

                printUser(userToDelete);

                HttpClient client = new HttpClient();

                PostMethod method = new PostMethod(BASEURL + "UserHandler/deleteUser");

                method.addParameter("user", jsonuser);

                int returnCode = client.executeMethod(method);

                System.out.println(method.getResponseBodyAsString());

                return true;

            } catch (IllegalArgumentException e) {

                e.printStackTrace();

            } catch (HttpException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

        return false;

    }
