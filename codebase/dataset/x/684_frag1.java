                    } catch (NotFound ex) {

                        out = rh.createExceptionReply();

                        NotFoundHelper.write(out, ex);

                    } catch (CannotProceed ex) {

                        out = rh.createExceptionReply();

                        CannotProceedHelper.write(out, ex);

                    } catch (InvalidName ex) {

                        out = rh.createExceptionReply();

                        InvalidNameHelper.write(out, ex);

                    }

                    break;

                }
