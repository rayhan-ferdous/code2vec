    public static Chunker tempAndReal(final Chunker preprocessor, final Chunker mainChunker) {

        return new Chunker() {



            @Override

            public Collection<Chunk> chunk(TextWithChunks chunkText) {

                TextWithChunks tempText = new TextWithChunks(chunkText);

                tempText.addAll(preprocessor.chunk(tempText));

                return mainChunker.chunk(tempText);

            }

        };

    }
