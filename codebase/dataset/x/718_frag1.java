        tempD[1] = value2Temp;

        D.set(tempD);

        float value1 = plane1.getTheValue();

        float value2 = plane2.getTheValue();

        float aveValue = (value1 + value2) / 2;

        Color3f theColor = ColorTable.getColor(aveValue);

        Plane2d plane = new Plane2d(A, B, C, D, theColor, false, 0, 0, 0.0f, true);
