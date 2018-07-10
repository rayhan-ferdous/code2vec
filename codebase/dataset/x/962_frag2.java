    public static void alleleCounts() {

        Pattern tab = Pattern.compile("\t");

        try {

            BufferedWriter bw = new BufferedWriter(new FileWriter("C:/Projects/NAM/hapmap/v2snps/allelecounts.txt"));

            bw.write("chromosome\tallele\tcount");

            for (int chr = 1; chr <= 10; chr++) {

                HashMap<String, Integer> alleleMap = new HashMap<String, Integer>();

                String filename = "C:/Projects/NAM/hapmap/v2snps/Chr" + chr + "_nohets_zeroone_cnv.txt";

                BufferedReader br = new BufferedReader(new FileReader(filename));

                String input;

                String[] data;

                br.readLine();

                while ((input = br.readLine()) != null) {

                    data = tab.split(input);

                    String allele = data[1];

                    Integer alleleCount = alleleMap.get(allele);

                    if (alleleCount == null) alleleCount = 0;

                    alleleMap.put(allele, alleleCount + 1);

                }

                br.close();

                Set<String> keys = alleleMap.keySet();

                TreeSet<String> sortedKeys = new TreeSet<String>(keys);

                bw.newLine();

                for (String key : sortedKeys) {

                    StringBuffer sb = new StringBuffer();

                    sb.append(chr);

                    sb.append("\t").append(key);

                    sb.append("\t").append(alleleMap.get(key));

                    bw.write(sb.toString());

                    bw.newLine();

                    System.out.println(sb.toString());

                }

            }

            bw.close();

        } catch (IOException e) {

            e.printStackTrace();

            System.exit(-1);

        }

    }
