    private String getSignature() {

        if (m_sig == null && m_secret != null) {

            StringBuilder sb = new StringBuilder(m_secret);

            Iterator<Map.Entry<String, String>> i = m_args.entrySet().iterator();

            while (i.hasNext()) {

                Map.Entry<String, String> curr = i.next();

                sb.append(curr.getKey()).append(curr.getValue());

            }

            String sigString = sb.toString();

            m_sig = MD5.compute(sigString);

        }

        return m_sig;

    }
