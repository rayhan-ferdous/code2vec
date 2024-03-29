    public boolean updateProduct(Product productToUpdate) {

        if (productToUpdate != null) {

            try {

                String jsonproduct = new Gson().toJson(productToUpdate);

                HttpClient client = new HttpClient();

                PostMethod method = new PostMethod(BASEURL + "ProductDAO/updateProduct");

                method.addParameter("product", jsonproduct);

                int returnCode = client.executeMethod(method);

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
