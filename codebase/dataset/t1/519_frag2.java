        } catch (IOException e) {

            logVisitor.warning("resolveBundles error", e);

            throw e;

        } catch (Exception e) {

            logVisitor.warning("resolveBundles error", e);

            throw new IOException(e.getMessage());

        }

    }
