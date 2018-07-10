        } catch (IOException e) {

            logVisitor.warning("refreshBundles error", e);

            throw e;

        } catch (Exception e) {

            logVisitor.warning("refreshBundles error", e);

            throw new IOException(e.getMessage());

        }

    }
