        sb.append("  -initpose x y z t \n     OR -initpose getpose p	<initial pose>\n");

        sb.append("  -noobst		<disable obstacle avoidance>\n");

        sb.append("  -cfg f		<use environment config file f>\n");

        sb.append("  -test              <run test sequence>\n");

        sb.append("  -reddy				<instantiate cramer instead of pioneer>\n");

        sb.append("  -speak                     <enable speech production>\n");

        sb.append("  -outdev <n>                <use device n for audio out>\n");

        sb.append("  -sleepdec <n>              <set sleepDec to n>\n");

        return sb.toString();
