    private String getTypeValue(XMLElement output_element) {

        Vector elements = output_element.getChildren();

        String value = null;

        for (int i = 0; i < elements.size() && value == null; i++) {

            XMLElement element = (XMLElement) elements.elementAt(i);

            if (element.getType().equals("value")) {

                value = element.getCharData();

            }

        }

        return value;

    }



    /** Deals with a handful of tag-value pairs: total_score/score, amino-acid, 

   *  non-canonical_splice_site, cigar, symbol, problem and ResultTags (output type=tag)

   *  <output>

   *     <type>tag</type>

   *     <value>comment: incomplete CDS</value>

   *  </output>

   */

    private boolean setTagValue(SeqFeatureI sf, XMLElement output_element, String type) {
