            baos = reader.getDataStreamUsingID(identifier);

            long dsNoCdxTime = (System.currentTimeMillis() - start);

            System.out.println("Without CDX: " + dsNoCdxTime + " ms");

            baos.close();

        } catch (ARCException e) {

            e.printStackTrace();

        } catch (IOException e) {
