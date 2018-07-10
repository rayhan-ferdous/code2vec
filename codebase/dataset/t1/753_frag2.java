            ASN1EncodableVector v = new ASN1EncodableVector();

            v.add(new DERObjectIdentifier(ID_CONTENT_TYPE));

            v.add(new DERSet(new DERObjectIdentifier(ID_PKCS7_DATA)));

            attribute.add(new DERSequence(v));

            v = new ASN1EncodableVector();

            v.add(new DERObjectIdentifier(ID_SIGNING_TIME));

            v.add(new DERSet(new DERUTCTime(signingTime.getTime())));

            attribute.add(new DERSequence(v));

            v = new ASN1EncodableVector();

            v.add(new DERObjectIdentifier(ID_MESSAGE_DIGEST));

            v.add(new DERSet(new DEROctetString(secondDigest)));

            attribute.add(new DERSequence(v));

            if (!this.crls.isEmpty()) {

                v = new ASN1EncodableVector();

                v.add(new DERObjectIdentifier(ID_ADBE_REVOCATION));

                ASN1EncodableVector v2 = new ASN1EncodableVector();

                for (Iterator i = this.crls.iterator(); i.hasNext(); ) {

                    ASN1InputStream t = new ASN1InputStream(new ByteArrayInputStream((((X509CRL) i.next()).getEncoded())));

                    v2.add(t.readObject());

                }

                v.add(new DERSet(new DERSequence(new DERTaggedObject(true, 0, new DERSequence(v2)))));

                attribute.add(new DERSequence(v));

            }

            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
