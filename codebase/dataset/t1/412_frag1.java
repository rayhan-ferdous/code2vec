    private void readNoteEffect(TGNoteEffect effect) {

        int header = readHeader(3);

        if (((header & EFFECT_BEND) != 0)) {

            effect.setBend(readBendEffect());

        }

        if (((header & EFFECT_TREMOLO_BAR) != 0)) {

            effect.setTremoloBar(readTremoloBarEffect());

        }

        if (((header & EFFECT_HARMONIC) != 0)) {

            effect.setHarmonic(readHarmonicEffect());

        }

        if (((header & EFFECT_GRACE) != 0)) {

            effect.setGrace(readGraceEffect());

        }

        if (((header & EFFECT_TRILL) != 0)) {

            effect.setTrill(readTrillEffect());

        }

        if (((header & EFFECT_TREMOLO_PICKING) != 0)) {

            effect.setTremoloPicking(readTremoloPickingEffect());

        }

        effect.setVibrato(((header & EFFECT_VIBRATO) != 0));

        effect.setDeadNote(((header & EFFECT_DEAD) != 0));

        effect.setSlide(((header & EFFECT_SLIDE) != 0));

        effect.setHammer(((header & EFFECT_HAMMER) != 0));

        effect.setGhostNote(((header & EFFECT_GHOST) != 0));

        effect.setAccentuatedNote(((header & EFFECT_ACCENTUATED) != 0));

        effect.setHeavyAccentuatedNote(((header & EFFECT_HEAVY_ACCENTUATED) != 0));

        effect.setPalmMute(((header & EFFECT_PALM_MUTE) != 0));

        effect.setStaccato(((header & EFFECT_STACCATO) != 0));

        effect.setTapping(((header & EFFECT_TAPPING) != 0));

        effect.setSlapping(((header & EFFECT_SLAPPING) != 0));

        effect.setPopping(((header & EFFECT_POPPING) != 0));

        effect.setFadeIn(((header & EFFECT_FADE_IN) != 0));

    }
