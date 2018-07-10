                        raf.seek(rangePosition);

                        raf.readFully(b, off, lenf);

                    }

                    rangePosition += lenf;

                    return lenf;

                }

            }

            return -1;

        }

    }



    /**

     * An interface to retrieve the signature dictionary for modification.

     */

    public interface SignatureEvent {



        /**

         * Allows modification of the signature dictionary.

         * @param sig the signature dictionary

         */

        public void getSignatureDictionary(PdfDictionary sig);

    }



    private int certificationLevel = NOT_CERTIFIED;



    /**

     * Gets the certified status of this document.

     * @return the certified status

     */

    public int getCertificationLevel() {

        return this.certificationLevel;

    }



    /**

     * Sets the document type to certified instead of simply signed.

     * @param certificationLevel the values can be: <code>NOT_CERTIFIED</code>, <code>CERTIFIED_NO_CHANGES_ALLOWED</code>,

     * <code>CERTIFIED_FORM_FILLING</code> and <code>CERTIFIED_FORM_FILLING_AND_ANNOTATIONS</code>

     */

    public void setCertificationLevel(int certificationLevel) {

        this.certificationLevel = certificationLevel;
