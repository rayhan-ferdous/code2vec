        public void setFrom(MailingList list) {

            if (this.toAddress == null) throw new IllegalStateException("Must call setTo() first");

            String ownerAddress = OwnerAddress.makeOwner(list.getEmail());

            try {

                this.fromAddress = new InternetAddress(ownerAddress, list.getName());

            } catch (UnsupportedEncodingException ex) {

                throw new RuntimeException(ex);

            }

            byte[] token = encryptor.encryptString(this.toAddress.getAddress());

            this.senderEmail = VERPAddress.encodeVERP(list.getEmail(), token);

        }
