        if (extCount + currentUnit > ext.length || extCount + currentUnit < 0) return size + "";

        float commaPower = (float) java.lang.Math.pow(10, commaDigits);

        return (Math.round((size) * commaPower) / commaPower) + " " + ext[extCount + currentUnit];

    }
