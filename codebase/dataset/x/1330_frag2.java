    public Date getLastModified(String uri) throws ServiceAccessException, AccessDeniedException, ObjectNotFoundException, ObjectLockedException {

        try {

            Map m = parseUri(uri);

            if (representsAbstractFolder(m)) return new Date(0); else return client.getLastModified(uri, m);

        } catch (ZoneMismatchException e) {

            throw new AccessDeniedException(uri, e.getLocalizedMessage(), "read");

        } catch (CCClientException e) {

            throw new ServiceAccessException(service, e.getLocalizedMessage(), e.isWarning());

        } catch (NoAccessException e) {

            throw new AccessDeniedException(uri, e.getLocalizedMessage(), "read");

        } catch (NoSuchObjectException e) {

            throw new ObjectNotFoundException(uri);

        }

    }
